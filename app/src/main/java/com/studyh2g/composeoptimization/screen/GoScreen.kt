package com.studyh2g.composeoptimization.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studyh2g.composeoptimization.constant.Routes

@Composable
fun GoScreen(navController: NavController, goLambda: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationButton(
            text = "Lambda参数优化",
            destination = Routes.LAMBDA_PARAMETER,
            navController = navController,
            goLambda = goLambda
        )
        Spacer(Modifier.height(16.dp))
        NavigationButton(
            text = "derivedStateOf 优化",
            destination = Routes.DERIVED_STATE,
            navController = navController
        )
        Spacer(Modifier.height(16.dp))
        NavigationButton(
            text = "Key使用优化",
            destination = Routes.KEY_USAGE,
            navController = navController
        )
        Spacer(Modifier.height(16.dp))
        NavigationButton(
            text = "阶段转移优化",
            destination = Routes.PHASE_OPTIMIZATION,
            navController = navController
        )
        Spacer(Modifier.height(16.dp))
        NavigationButton(
            text = "状态下沉优化",
            destination = Routes.STATE_HOISTING,
            navController = navController
        )
    }
}

@Composable
fun NavigationButton(
    text: String,
    destination: String,
    navController: NavController,
    goLambda: (() -> Unit)? = null,
) {
    Button(
        onClick = {
            if (goLambda == null) {
                navController.navigate(destination)
            } else {
                goLambda()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}