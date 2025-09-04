package com.studyh2g.composeoptimization

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studyh2g.composeoptimization.constant.Routes
import com.studyh2g.composeoptimization.screen.DerivedStateScreen
import com.studyh2g.composeoptimization.screen.GoScreen
import com.studyh2g.composeoptimization.screen.KeyUsageScreen
import com.studyh2g.composeoptimization.screen.PhaseOptimizationScreen
import com.studyh2g.composeoptimization.screen.StateHoistingScreen
import com.studyh2g.composeoptimization.ui.theme.ComposeOptimizationShowcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeOptimizationShowcaseTheme {
                // 全局状态管理
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // 全局TopAppBar
                        GlobalTopAppBar(
                            currentRoute = currentRoute,
                            onBackClick = { navController.popBackStack() },
                            onHomeClick = { navController.navigate(Routes.GO) { popUpTo(Routes.GO) } }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.GO,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // ==============================================
                        // 导航到Lambda参数优化专用Activity
                        // 原因：在独立Activity中能更精确展示重组优化效果
                        // ==============================================
                        composable(Routes.GO) { GoScreen(navController, goLambda = { startActivity(
                            Intent(this@MainActivity, LambdaParameterActivity::class.java)
                        )}) }
                        composable(Routes.DERIVED_STATE) { DerivedStateScreen() }
                        composable(Routes.KEY_USAGE) { KeyUsageScreen() }
                        composable(Routes.PHASE_OPTIMIZATION) { PhaseOptimizationScreen() }
                        composable(Routes.STATE_HOISTING) { StateHoistingScreen() }
                    }
                }
            }
        }
    }
}

// 全局顶部导航栏
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalTopAppBar(
    currentRoute: String?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    Routes.GO -> "Compose重组优化概览"
                    Routes.LAMBDA_PARAMETER -> "Lambda参数优化"
                    Routes.DERIVED_STATE -> "derivedStateOf 优化"
                    Routes.KEY_USAGE -> "Key使用优化"
                    Routes.PHASE_OPTIMIZATION -> "阶段转移优化"
                    Routes.STATE_HOISTING -> "状态下沉优化"
                    else -> "性能优化演示"
                }
            )
        },
        navigationIcon = {
            if (currentRoute != null && currentRoute != Routes.GO) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            } else {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "首页"
                    )
                }
            }
        },
    )
}