import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.dao.BookmarkDao
import com.maxidev.tastymeal.data.local.entity.BookMarkEntity
import com.maxidev.tastymeal.data.local.entity.MealEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkDaoTest {

    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var dataBase: TastyMealDataBase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        dataBase = Room.inMemoryDatabaseBuilder(context, TastyMealDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        bookmarkDao = dataBase.bookmarkDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataBase.close()
    }

    private val entityOne = MealEntity(
        idMeal = "abc1", strMeal = "Beef Asado", strMealThumb = "Image", strCategory = "Beef",
        strInstructions = "Lorem impsum dolor sit amet.", strTags = "", strYouTube = "",
        strSource = "", strIngredients = listOf("Beef", "Onion", "Garlic"),
        strMeasure = listOf("200g", "1", "5"), bookmark = BookMarkEntity(bookmarked = false)
    )

    private val entityTwo = MealEntity(
        idMeal = "abc2", strMeal = "Salad", strMealThumb = "Image", strCategory = "Vegetables",
        strInstructions = "Lorem impsum dolor sit amet. Lorem bla bla", strTags = "", strYouTube = "",
        strSource = "", strIngredients = listOf("Salad", "Onion", "Garlic"),
        strMeasure = listOf("20", "55", "33"), bookmark = BookMarkEntity(bookmarked = true)
    )

    private suspend fun addOneMeal() {
        bookmarkDao.upsertBookmark(entityOne)
    }

    private suspend fun addTwoMeals() {
        bookmarkDao.upsertBookmark(entityOne)
        bookmarkDao.upsertBookmark(entityTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsMealsIntoDB() = runBlocking {
        addOneMeal()

        val allMeals = bookmarkDao.getBookmark().first()

        assertEquals(allMeals[0], entityOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllMeals_returnsAllMealsFromDB() = runBlocking {
        addTwoMeals()

        val allMeals = bookmarkDao.getBookmark().first()

        assertEquals(allMeals[0], entityOne)
        assertEquals(allMeals[1], entityTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateMeals_updatesMealsInDB() = runBlocking {
        addTwoMeals()

        bookmarkDao.upsertBookmark(entityOne)
        bookmarkDao.upsertBookmark(entityTwo)

        val allMeals = bookmarkDao.getBookmark().first()

        assertEquals(allMeals[0], entityOne)
        assertEquals(allMeals[1], entityTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteMeals_deletesAllMealsInDB() = runBlocking {
        addTwoMeals()

        bookmarkDao.deleteBookmark(entityOne)
        bookmarkDao.deleteBookmark(entityTwo)

        val allMeals = bookmarkDao.getBookmark().first()

        assertTrue(allMeals.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetBookmarkById_returnBookmarkById() = runBlocking {
        addOneMeal()

        val recipe = bookmarkDao.getBookmarkById("abc1")

        assertEquals(recipe.first(), entityOne)
    }
}