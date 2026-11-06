package com.example.voltiva

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** One charging station, as stored in assets/stations.json. */
@Serializable
data class Station(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val powerKw: Int,
    val connectors: List<String>,
    val open24h: Boolean,
)

/** Reads all stations from the bundled assets/stations.json file. */
fun loadStations(context: Context): List<Station> {
    val json = context.assets.open("stations.json").bufferedReader().use { it.readText() }
    return Json.decodeFromString<List<Station>>(json)
}

/** The string resource with the readable name of a connector value. */
fun connectorLabel(connector: String): Int = when (connector) {
    "TYPE_2" -> R.string.connector_type_2
    "CCS" -> R.string.connector_ccs
    "CHADEMO" -> R.string.connector_chademo
    else -> error("Unknown connector: $connector")
}
