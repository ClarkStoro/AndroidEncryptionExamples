package com.clarkstoro.data.utils

import com.clarkstoro.domain.models.ExampleModel

object MockDataProvider {
    fun getItemsList(): List<ExampleModel> = listOf(
        ExampleModel("Abruzzo", "L'Aquila"),
        ExampleModel("Basilicata", "Potenza"),
        ExampleModel("Calabria", "Catanzaro"),
        ExampleModel("Campania", "Napoli"),
        ExampleModel("Emilia-Romagna", "Bologna"),
        ExampleModel("Friuli-Venezia Giulia", "Trieste"),
        ExampleModel("Lazio", "Roma"),
        ExampleModel("Liguria", "Genova"),
        ExampleModel("Lombardia", "Milano"),
        ExampleModel("Marche", "Ancona"),
        ExampleModel("Molise", "Campobasso"),
        ExampleModel("Piemonte", "Torino"),
        ExampleModel("Puglia", "Bari"),
        ExampleModel("Sardegna", "Cagliari"),
        ExampleModel("Sicilia", "Palermo"),
        ExampleModel("Toscana", "Firenze"),
        ExampleModel("Trentino-Alto Adige", "Trento"),
        ExampleModel("Umbria", "Perugia"),
        ExampleModel("Valle d'Aosta", "Aosta"),
        ExampleModel("Veneto", "Venezia"),
    )
}