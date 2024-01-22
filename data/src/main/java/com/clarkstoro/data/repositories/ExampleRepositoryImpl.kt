package com.clarkstoro.data.repositories

import com.clarkstoro.data.datasources.ExampleDatasource
import com.clarkstoro.domain.models.ExampleModel
import com.clarkstoro.domain.repositories.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val exampleDatasource: ExampleDatasource
) : ExampleRepository {
    override suspend fun getAllItems(): List<ExampleModel> = exampleDatasource.getAllItems()
}