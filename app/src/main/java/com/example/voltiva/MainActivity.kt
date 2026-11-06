package com.example.voltiva

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.voltiva.ui.theme.VoltivaTheme

/** First screen: the list of charging stations. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val stations = loadStations(this)

        setContent {
            VoltivaTheme {
                StationListScreen(
                    stations = stations,
                    onStationClick = { station -> openStationDetail(station) },
                )
            }
        }
    }

    private fun openStationDetail(station: Station) {
        val intent = Intent(this, StationDetailActivity::class.java)
        intent.putExtra(StationDetailActivity.EXTRA_STATION_ID, station.id)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationListScreen(
    stations: List<Station>,
    onStationClick: (Station) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
    ) { innerPadding ->
        // One column on a phone in portrait, more columns on wider screens.
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(stations, key = { it.id }) { station ->
                StationCard(station = station, onClick = { onStationClick(station) })
            }
        }
    }
}

@Composable
fun StationCard(station: Station, onClick: () -> Unit) {
    val connectorText = station.connectors.map { stringResource(connectorLabel(it)) }.joinToString(", ")

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(station.name, style = MaterialTheme.typography.titleMedium)
            Text(station.city)
            Text(connectorText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationListScreenPreview() {
    val station = Station(
        id = "bb-2", name = "Nabijacka Europa", address = "Na Troskach, Banska Bystrica", city = "Banska Bystrica",
        latitude = 48.7378, longitude = 19.1521, powerKw = 150,
        connectors = listOf("CCS", "TYPE_2"), open24h = true,
    )
    VoltivaTheme {
        StationListScreen(stations = listOf(station), onStationClick = {})
    }
}
