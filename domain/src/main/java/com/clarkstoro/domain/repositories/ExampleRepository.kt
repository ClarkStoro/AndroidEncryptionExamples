package com.clarkstoro.domain.repositories

import com.clarkstoro.domain.models.ExampleModel

interface ExampleRepository { suspend fun getAllItems(): List<ExampleModel> }