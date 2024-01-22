package com.clarkstoro.domain.usecases

import com.clarkstoro.domain.base.Result
import com.clarkstoro.domain.models.ExampleModel
import com.clarkstoro.domain.repositories.ExampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExampleUseCase @Inject constructor(private val repository: ExampleRepository) {
    suspend operator fun invoke(): Flow<Result<List<ExampleModel>>> = flow {
        try {
            val result = repository.getAllItems()
            emit(Result.Success(result))
        } catch (e: Exception) { emit(Result.Failure(e)) }
    }
}