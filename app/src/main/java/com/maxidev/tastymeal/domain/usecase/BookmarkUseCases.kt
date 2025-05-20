package com.maxidev.tastymeal.domain.usecase

import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllBookmarksUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(): Flow<List<Meal>> =
        repository.getAllBookmarks()
}

class GetBookmarkByIdUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(id: String): Flow<Meal> =
        repository.getBookmarkById(id)
}

class IsBookmarkedUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(id: String): Flow<Boolean> =
        repository.isBookmark(id)
}

class SaveBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(meal: Meal) =
        repository.upsertBookmark(meal)
}

class DeleteBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(meal: Meal) =
        repository.deleteBookmark(meal)
}

class ClearAllBookmarksUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke() =
        repository.clearAll()
}