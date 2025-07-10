import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.dao.RecipeDao
import com.maxidev.tastymeal.data.local.entity.RecipeEntity
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
class RecipeDaoTest {

    private lateinit var recipeDao: RecipeDao
    private lateinit var dataBase: TastyMealDataBase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        dataBase = Room.inMemoryDatabaseBuilder(context, TastyMealDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        recipeDao = dataBase.recipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataBase.close()
    }

    private val recipeOne = RecipeEntity(
        id = 1, title = "Recipe 1", portions = "20", preparationTime = "20 min", cookingTime = "60 min",
        source = "Link.", image = Uri.EMPTY, cameraImage = Uri.EMPTY,
        instructions = "Lorem impsum dolor sit amet.", ingredientsAndMeasures = "Lorem ing/measure.",
        notes = "Lorem notes."
    )
    private val recipeTwo = RecipeEntity(
        id = 2, title = "Recipe 2", portions = "7", preparationTime = "40 min", cookingTime = "120 min",
        source = "Links.", image = Uri.EMPTY, cameraImage = Uri.EMPTY,
        instructions = "Lorem impsum dolor sit amet. Lorem bla bla.", ingredientsAndMeasures = "Lorem ing/measure. Bla bla.",
        notes = "Lorem notes. Bla bla."
    )

    private suspend fun addOneRecipe() {
        recipeDao.upsertRecipe(recipeOne)
    }

    private suspend fun addTwoRecipes() {
        recipeDao.upsertRecipe(recipeOne)
        recipeDao.upsertRecipe(recipeTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneRecipe()

        val allRecipes = recipeDao.getAllRecipes().first()

        assertEquals(allRecipes[0], recipeOne)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllRecipes_returnsAllRecipesFromDB() = runBlocking {
        addTwoRecipes()

        val allRecipes = recipeDao.getAllRecipes().first()

        assertEquals(allRecipes[0], recipeOne)
        assertEquals(allRecipes[1], recipeTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateRecipes_updatesRecipesInDB() = runBlocking {
        addTwoRecipes()

        recipeDao.upsertRecipe(recipeOne)
        recipeDao.upsertRecipe(recipeTwo)

        val allRecipes = recipeDao.getAllRecipes().first()

        assertEquals(allRecipes[0], recipeOne)
        assertEquals(allRecipes[1], recipeTwo)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteRecipes_deletesAllRecipesInDB() = runBlocking {
        addTwoRecipes()

        recipeDao.deleteRecipe(recipeOne)
        recipeDao.deleteRecipe(recipeTwo)

        val allRecipes = recipeDao.getAllRecipes().first()

        assertTrue(allRecipes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetRecipeById_returnsRecipeById() = runBlocking {
        addOneRecipe()

        val recipe = recipeDao.getRecipeById(1)

        assertEquals(recipe.first(), recipeOne)
    }
}