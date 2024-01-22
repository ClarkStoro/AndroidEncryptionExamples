package com.clarkstoro.data.network

import com.clarkstoro.data.datasources.ExampleDatasource
import com.clarkstoro.data.local_db.AppDatabase
import com.clarkstoro.data.local_db.entities.ExampleDaoModel
import com.clarkstoro.data.utils.MockDataProvider
import com.clarkstoro.domain.models.ExampleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExampleDatasourceImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : ExampleDatasource {
    private val exampleDao by lazy { appDatabase.exampleDao() }

    override suspend fun getAllItems(): List<ExampleModel> = MockDataProvider.getItemsList()
    override suspend fun addExampleItem() {
        withContext(Dispatchers.IO) {
            exampleDao.insertExampleDaoModel(ExampleDaoModel())
        }
    }
}