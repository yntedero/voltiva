package com.example.voltiva

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.voltiva.ui.theme.VoltivaTheme

/** Second screen: everything about one station, plus a button that opens it in a maps app. */
class StationDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val stationId = intent.getStringExtra(EXTRA_STATION_ID)
        val station = loadStations(this).find { it.id == stationId }
        if (station == null) {
            finish()
            return
        }

        setContent {
            VoltivaTheme {
                StationDetailScreen(
                    station = station,
                    onBackClick = { finish() },
                    onOpenInMapsClick = { openInMaps(station) },
                )
            }
        }
    }

    private fun openInMaps(station: Station) {
        val uri = "geo:0,0?q=${station.latitude},${station.longitude}(${Uri.encode(station.name)})".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_maps_app, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_STATION_ID = "station_id"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(
    station: Station,
    onBackClick: () -> Unit,
    onOpenInMapsClick: () -> Unit,
) {
    val connectorText = station.connectors.map { stringResource(connectorLabel(it)) }.joinToString(", ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(station.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_ev_station),
                contentDescription = stringResource(R.string.station_image),
            )
            Text(station.address)
            Text(stringResource(R.string.power_kw, station.powerKw))
            Text(stringResource(R.string.connectors, connectorText))
            Text(stringResource(if (station.open24h) R.string.open_24h_yes else R.string.open_24h_no))
            Button(onClick = onOpenInMapsClick) {
                Text(stringResource(R.string.open_in_maps))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationDetailScreenPreview() {
    val station = Station(
        id = "bb-9", name = "Nabijacka Kremnicka", address = "Kremnicka, Banska Bystrica", city = "Banska Bystrica",
        latitude = 48.7230, longitude = 19.1200, powerKw = 150,
        connectors = listOf("CCS", "CHADEMO", "TYPE_2"), open24h = true,
    )
    VoltivaTheme {
        StationDetailScreen(station = station, onBackClick = {}, onOpenInMapsClick = {})
    }
}
