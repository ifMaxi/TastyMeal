package com.maxidev.tastymeal.di

import android.content.Context
import androidx.room.Room
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.dao.BookmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val DATA_BASE_NAME = "tasty_meal_db"

    @Provides
    @Singleton
    fun providesRoomDataBase(
        @ApplicationContext context: Context
    ): TastyMealDataBase {

        return Room.databaseBuilder(
            context = context,
            klass = TastyMealDataBase::class.java,
            name = DATA_BASE_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun providesBookmarkDao(dataBase: TastyMealDataBase): BookmarkDao {

        return dataBase.bookmarkDao()
    }
}