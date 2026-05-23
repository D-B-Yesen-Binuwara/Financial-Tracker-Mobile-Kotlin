package com.spendly.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spendly.financetracker.ui.FinanceTrackerApp
import com.spendly.financetracker.ui.theme.FinanceTrackerTheme
import com.spendly.financetracker.ui.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val factory = remember { FinanceViewModel.Factory(applicationContext) }
            val viewModel: FinanceViewModel = viewModel(factory = factory)

            FinanceTrackerTheme {
                FinanceTrackerApp(viewModel = viewModel)
            }
        }
    }
}
