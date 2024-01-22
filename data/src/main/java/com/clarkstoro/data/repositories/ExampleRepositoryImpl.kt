package com.clarkstoro.data.repositories

import com.clarkstoro.domain.models.ExampleModel
import com.clarkstoro.domain.repositories.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(

) : ExampleRepository {
    override suspend fun getAllItems(): List<ExampleModel> = emptyList()
}