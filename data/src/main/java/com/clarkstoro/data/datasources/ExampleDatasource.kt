package com.clarkstoro.data.datasources

import com.clarkstoro.domain.models.ExampleModel

interface ExampleDatasource {
    suspend fun getAllItems(): List<ExampleModel>
    suspend fun addExampleItem()
}