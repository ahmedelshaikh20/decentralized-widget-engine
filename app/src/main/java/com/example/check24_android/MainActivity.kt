package com.example.check24_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.check24_android.presentation.navigation.AppNavigation
import com.example.check24_android.presentation.screens.dashboard.DashboardScreen
import com.example.check24_android.presentation.ui.theme.BrandBlueDark
import com.example.check24_android.presentation.ui.theme.BrandWhite
import com.example.check24_android.presentation.ui.theme.Check24androidTheme
import com.example.check24_android.presentation.usercontext.UserContextRepo
import com.example.check24_android.ui.components.Check24BottomBar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
  // This only for PoC
  private val userContextRepo: UserContextRepo by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      handleUserSwitch(intent)
        enableEdgeToEdge()
        setContent {
          val navController = rememberNavController()
            Check24androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                  topBar ={
                    AppTopBar()
                  },
                  bottomBar = {

                    Check24BottomBar(
                      currentRoute = "home",
                      onNavigate = { newRoute ->
                              navController.navigate(newRoute)
                      }
                    )
                  }

                ) { innerPadding ->
                  AppNavigation(
                    navController = navController,
                    modifier = Modifier
                      .padding(innerPadding)
                      .padding(16.dp)
                  )
                }
            }
        }
    }

  // only for Poc
  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleUserSwitch(intent)
  }

  // only for Poc
  private fun handleUserSwitch(intent: Intent) {

    val uri = intent.data ?: return
    val userId = uri.getQueryParameter("userId")?.toIntOrNull() ?: return
    val userName = uri.getQueryParameter("userName") ?: "User$userId"
    Log.d("DEEPLINK", "uri=$uri userId=$userId userName=$userName")

    lifecycleScope.launch {
      userContextRepo.switchUser(userId, userName)
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
