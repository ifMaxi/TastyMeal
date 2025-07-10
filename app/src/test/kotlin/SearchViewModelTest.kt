
import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.domain.usecase.SearchMealUseCase
import com.maxidev.tastymeal.presentation.search.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    lateinit var searchMealUseCase: SearchMealUseCase

    private lateinit var viewModel: SearchViewModel
    private val mockMeal = MinimalMeal(idMeal = "1", strMeal = "Test Meal", strMealThumb = "url", strCategory = "Dessert")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        every { searchMealUseCase.invoke(any()) } returns flow { emit(PagingData.from(listOf(mockMeal))) }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when searchMeals is called, then searchState is updated`() = runTest {
        val pagingData = PagingData.from(listOf(mockMeal))
        every { searchMealUseCase(any()) } returns flow { emit(pagingData) }

        viewModel = SearchViewModel(searchMealUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val searchFlow = viewModel.searchState.value.search
        assert(searchFlow != emptyFlow<PagingData<MinimalMeal>>())

        verify { searchMealUseCase(any()) }
    }
}