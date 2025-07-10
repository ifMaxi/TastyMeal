import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.dao.CategoriesDao
import com.maxidev.tastymeal.data.local.entity.CategoriesEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesDaoTest {

    private lateinit var categoriesDao: CategoriesDao
    private lateinit var dataBase: TastyMealDataBase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        dataBase = Room.inMemoryDatabaseBuilder(context, TastyMealDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        categoriesDao = dataBase.categoriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataBase.close()
    }

    private val itemOne = CategoriesEntity(idMeal = "CategoryOne", strName = "Fish", strThumb = "Image")
    private val itemTwo = CategoriesEntity(idMeal = "CategoryTwo", strName = "Beef", strThumb = "Image")

    private suspend fun addOneCategory() {
        categoriesDao.upsertAll(listOf(itemOne))
    }

    private suspend fun addTwoCategories() {
        categoriesDao.upsertAll(listOf(itemOne, itemTwo))
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertOneCategoryIntoDB() = runBlocking {
        addOneCategory()

        val allCategories = categoriesDao.getCategories().first()

        assertEquals(allCategories[0], itemOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCategories_returnsAllCategories() = runBlocking {
        addTwoCategories()

        val allCategories = categoriesDao.getCategories().first()

        assertEquals(allCategories[0], itemOne)
        assertEquals(allCategories[1], itemTwo)
    }
}