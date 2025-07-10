
import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.domain.usecase.CategoriesUseCase
import com.maxidev.tastymeal.domain.usecase.RandomMealUseCase
import com.maxidev.tastymeal.domain.usecase.SearchMealUseCase
import com.maxidev.tastymeal.presentation.home.HomeViewModel
import com.maxidev.tastymeal.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    lateinit var categoriesUseCase: CategoriesUseCase

    @RelaxedMockK
    lateinit var randomMealUseCase: RandomMealUseCase

    @RelaxedMockK
    lateinit var searchMealUseCase: SearchMealUseCase

    private lateinit var viewModel: HomeViewModel

    private val mockCategory = CategoriesMeal(idMeal = "1", strName = "Dessert", strThumb = "url")
    private val mockMeal = MinimalMeal(idMeal = "1", strMeal = "Test Meal", strMealThumb = "url", strCategory = "Dessert")
    private val mockCategories = listOf(mockCategory)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        every { categoriesUseCase() } returns flow { emit(mockCategories) }
        coEvery { randomMealUseCase() } returns listOf(mockMeal)
        every { searchMealUseCase.invoke(any()) } returns flow { emit(PagingData.from(listOf(mockMeal))) }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is initialized, then all states are property set up`() = runBlocking {
        viewModel = HomeViewModel(
            categoriesUseCase = categoriesUseCase,
            randomMealUseCase = randomMealUseCase,
            searchMealUseCase = searchMealUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 1) { categoriesUseCase() }
        coVerify(exactly = 1) { randomMealUseCase() }
        verify(exactly = 1) { searchMealUseCase(any()) }

        assert(!viewModel.isRefreshing.value)
        assert(viewModel.categoriesFlow.value.isNotEmpty())
    }

    @Test
    fun `when randomMeal is successful, then randomFlow emits Success state`() = runTest {
        viewModel = HomeViewModel(
            categoriesUseCase = categoriesUseCase,
            randomMealUseCase = randomMealUseCase,
            searchMealUseCase = searchMealUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val randomState = viewModel.randomFlow.value

        assert(randomState is Resource.Success)
        assert((randomState as Resource.Success).data == mockMeal)
    }

    @Test
    fun `when randomMeal throws HttpException, then randomFlow emits Error state`() = runTest {
        val httpException = mockk<HttpException>()

        every { httpException.message() } returns "Http Error"
        coEvery { randomMealUseCase().first() } throws httpException

        viewModel = HomeViewModel(
            categoriesUseCase = categoriesUseCase,
            randomMealUseCase = randomMealUseCase,
            searchMealUseCase = searchMealUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val randomState = viewModel.randomFlow.value
        assert(randomState is Resource.Error)
        assert((randomState as Resource.Error).message == "HTTP Error")
    }

    @Test
    fun `when randomMeal throws IOException, then randomFlow emits Error state`() = runTest {
        val ioException = IOException("Network error")
        coEvery { randomMealUseCase() } throws ioException

        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val randomState = viewModel.randomFlow.value
        assert(randomState is Resource.Error)
        assert((randomState as Resource.Error).message == "java.io.IOException: Network error")
    }

    @Test
    fun `when categoriesUseCase throws error, then categoriesFlow emits empty list`() = runTest {
        every { categoriesUseCase.invoke() } returns flow { throw RuntimeException("Error") }

        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.categoriesFlow.value.isEmpty())
    }

    @Test
    fun `when refreshAll is called, then isRefreshing is updated correctly`() = runTest {
        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)

        viewModel.refreshAll()

        assert(viewModel.isRefreshing.value)

        testDispatcher.scheduler.advanceTimeBy(1500)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(!viewModel.isRefreshing.value)

        coVerify(exactly = 2) { randomMealUseCase() }
        verify(exactly = 2) { searchMealUseCase(any()) }
    }

    @Test
    fun `when randomMeal is called, then Loading state is emitted first`() = runTest {
        val slowFlow = flow {
            delay(1000)
            emit(mockMeal)
        }
        coEvery { randomMealUseCase().first() } returns slowFlow.first()

        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)

        assert(viewModel.randomFlow.value is Resource.Loading)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.randomFlow.value is Resource.Success)
    }

    @Test
    fun `when mealByLetter is called, then searchByLetterState is updated`() = runTest {
        val pagingData = PagingData.from(listOf(mockMeal))
        every { searchMealUseCase(any()) } returns flow { emit(pagingData) }

        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val searchFlow = viewModel.searchByLetterState.value
        assert(searchFlow != emptyFlow<PagingData<MinimalMeal>>())

        verify { searchMealUseCase(any()) }
    }

    @Test
    fun `when ViewModel is initialized, then mealByLetter is called with valid letter`() {
        val capturedLetter = slot<String>()
        every { searchMealUseCase.invoke(capture(capturedLetter)) } returns flow { emit(PagingData.empty()) }

        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)

        val usedLetter = capturedLetter.captured
        assert(usedLetter.length == 1)
        assert(usedLetter[0] in 'a'..'z')
        assert(usedLetter != "ñ")
    }

    @Test
    fun `when refreshAll is called, then methods are called in correct order`() = runTest {
        viewModel = HomeViewModel(categoriesUseCase, randomMealUseCase, searchMealUseCase)
        clearMocks(randomMealUseCase, searchMealUseCase)

        viewModel.refreshAll()
        testDispatcher.scheduler.advanceTimeBy(1500)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerifyOrder {
            randomMealUseCase()
            searchMealUseCase(any())
        }
    }
}