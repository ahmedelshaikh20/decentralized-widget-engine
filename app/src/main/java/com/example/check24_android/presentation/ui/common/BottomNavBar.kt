package com.example.check24_android.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.check24_android.presentation.navigation.Dashboard
import com.example.check24_android.presentation.navigation.Flights
import com.example.check24_android.presentation.navigation.Insurance
import com.example.check24_android.presentation.ui.theme.AppCardDefaults
import com.example.check24_android.presentation.ui.theme.BottomNavDefaults
import com.example.check24_android.presentation.ui.theme.BrandWhite
import com.example.check24_android.presentation.ui.theme.Check24androidTheme
import com.example.check24_android.presentation.ui.theme.NavSelected
import com.example.check24_android.presentation.ui.theme.NavUnselected

sealed class BottomNavItem(
  val title: String,
  val icon: ImageVector,
  val destination: Any
) {
  object HomeItem : BottomNavItem("Home", Icons.Filled.Home, Dashboard)
  object FlightsItem : BottomNavItem("Flights", Icons.Filled.PlayArrow, Flights)
  object InsuranceItem : BottomNavItem("Insurance", Icons.Filled.DateRange, Insurance)

}


  // Observe current route to set selected state
//  val navBackStackEntry by navController.currentBackStackEntryAsState()
//  val currentRoute = navBackStackEntry?.destination?.route

  @Composable
  fun Check24BottomBar(
    currentRoute: String,
    onNavigate: (Any) -> Unit
  ) {
    val items = listOf(
      BottomNavItem.HomeItem,
      BottomNavItem.FlightsItem,
      BottomNavItem.InsuranceItem
      )

    NavigationBar(
      containerColor = BrandWhite,
      tonalElevation = AppCardDefaults.elevation,
    ) {
      items.forEach { item ->
        val isSelected = currentRoute == item.destination

        NavigationBarItem(
          label = {
            Text(
              text = item.title,
              style = MaterialTheme.typography.labelLarge,
              color = if (isSelected) NavSelected else NavUnselected
            )
          },
          icon = {
            Icon(
              imageVector = item.icon,
              contentDescription = item.title,
              modifier = Modifier.size(BottomNavDefaults.iconSize), // 24.dp
              tint = if (isSelected) NavSelected else NavUnselected
            )
          },
          selected = isSelected,
          onClick = { onNavigate(item.destination) },

          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = NavSelected,
            selectedTextColor = NavSelected,
            indicatorColor = Color.Transparent,
            unselectedIconColor = NavUnselected,
            unselectedTextColor = NavUnselected
          )
        )
      }
    }
  }


@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
  Check24androidTheme {
    Check24BottomBar(
      currentRoute = "home",
      onNavigate = {}
    )
  }
}
