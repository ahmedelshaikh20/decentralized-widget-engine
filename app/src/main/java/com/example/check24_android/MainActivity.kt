package com.example.check24_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.screens.dashboard.DashboardScreen
import com.example.check24_android.presentation.ui.theme.BrandBlueDark
import com.example.check24_android.presentation.ui.theme.BrandWhite
import com.example.check24_android.presentation.ui.theme.Check24androidTheme
import com.example.check24_android.ui.components.Check24BottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Check24androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                  topBar ={
                    AppTopBar()
                  },
                  bottomBar = {
                    Check24BottomBar(
                      currentRoute = "home",
                      onNavigate = { newRoute ->
                      }
                    )
                  }

                ) { innerPadding ->
                  DashboardScreen(modifier = Modifier.padding(innerPadding).padding(16.dp))
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
  CenterAlignedTopAppBar(
    title = {
      Text(
        text = "CHECK24 Home",
        style = MaterialTheme.typography.titleLarge,
        color = BrandWhite
      )
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = BrandBlueDark,
      titleContentColor = BrandWhite,
      actionIconContentColor = BrandWhite,
      navigationIconContentColor = BrandWhite
    )
  )
}
