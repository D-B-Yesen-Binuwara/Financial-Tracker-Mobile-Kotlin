package com.spendly.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spendly.financetracker.ui.viewmodel.AppTab

typealias OnTabSelected = (AppTab) -> Unit

@Composable
fun AppBottomNavigation(currentTab: AppTab, onTabSelected: OnTabSelected) {
    val items = listOf(
        AppTab.HOME,
        AppTab.TRANSACTIONS,
        AppTab.GOALS,
        AppTab.ANALYTICS,
        AppTab.PROFILE
    )

    NavigationBar {
        items.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { BottomNavIcon(tab = tab, selected = currentTab == tab) },
                label = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                )
            )
        }
    }
}

@Composable
private fun BottomNavIcon(tab: AppTab, selected: Boolean) {
    val label = when (tab) {
        AppTab.HOME -> "H"
        AppTab.TRANSACTIONS -> "T"
        AppTab.GOALS -> "G"
        AppTab.ANALYTICS -> "A"
        AppTab.PROFILE -> "P"
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
