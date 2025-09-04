package com.studyh2g.composeoptimization.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * 阶段转移优化演示页面 - 展示如何将计算从组合阶段转移到布局/绘制阶段
 *
 * 此页面包含两个对比组件：
 * 1. 上方：使用非Lambda方式的Modifier.offset（在组合阶段计算）
 * 2. 下方：使用Lambda方式的Modifier.offset（在布局阶段计算）
 *
 * 核心优化点：通过使用Lambda版本的Modifier API，将计算延迟到布局/绘制阶段，
 * 从而避免不必要的组合阶段重组
 */
@Composable
fun PhaseOptimizationScreen(modifier: Modifier = Modifier) {
    // 垂直布局展示两个对比组件
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        // 非Lambda方式（组合阶段计算）
        WithoutLambda()

        // Lambda方式（布局阶段计算）
        WithLambda()
    }
}

/**
 * 使用非Lambda方式的Modifier.offset - 在组合阶段计算偏移量
 *
 * 这种实现方式会导致：
 * 1. 每次偏移量变化时，整个组件都会重组
 * 2. 偏移量计算在组合阶段完成，增加了组合阶段的开销
 */
@Composable
private fun WithoutLambda() {
    // 状态：存储当前X轴偏移量
    var offsetX by remember { mutableIntStateOf(0) }

    // 带边框的列容器
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 文本组件 - 使用非Lambda偏移量
        Text(
            text = "Without Lambda",
            // 问题点：使用offset(x.dp)会在组合阶段计算偏移量
            // 当offsetX变化时，整个Text组件会重组
            modifier = Modifier.offset(x = offsetX.dp, y = 0.dp)
        )

        // 移动按钮 - 每次点击增加100偏移量
        Button(onClick = { offsetX += 100 }) {
            Text("Without Move")
        }
    }
}

/**
 * 使用Lambda方式的Modifier.offset - 在布局阶段计算偏移量
 *
 * 这种实现方式优化点：
 * 1. 偏移量计算延迟到布局阶段，避免组合阶段重组
 * 2. 仅实际需要绘制的组件在布局阶段更新，性能更优
 */
@Composable
private fun WithLambda() {
    // 状态：存储当前X轴偏移量
    var offsetX by remember { mutableIntStateOf(0) }

    // 带边框的列容器
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 文本组件 - 使用Lambda偏移量
        Text(
            text = "With Lambda",
            // 优化点：使用offset { IntOffset }的Lambda形式
            // 偏移量计算延迟到布局阶段，组合阶段不会重组
            modifier = Modifier.offset {
                IntOffset(offsetX, 0)
            }
        )

        // 移动按钮 - 每次点击增加100偏移量
        Button(onClick = { offsetX += 100 }) {
            Text("Lambda Move")
        }
    }
}