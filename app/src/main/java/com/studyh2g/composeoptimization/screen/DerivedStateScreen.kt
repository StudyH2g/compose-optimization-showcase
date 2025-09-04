package com.studyh2g.composeoptimization.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * derivedStateOf 优化演示屏幕 - 展示合并高频状态更新的效果
 *
 * 此屏幕包含两个对比组件：
 * 1. 左侧：未使用 derivedStateOf（高频状态变更导致频繁重组）
 * 2. 右侧：使用 derivedStateOf（状态变更被合并，减少重组次数）
 *
 * 通过Layout Inspector可观察：
 * - 左侧：滚动时按钮组件频繁重组
 * - 右侧：滚动时仅当条件变化时才重组
 */
@Composable
fun DerivedStateScreen(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        // 未优化版本：高频状态变更导致频繁重组
        WithOutDerivedStateOf(
            modifier = Modifier.weight(1f)
        )

        // 添加间距分隔两个列表
        Spacer(modifier = Modifier.weight(0.1f))

        // 优化版本：使用 derivedStateOf 合并状态变更
        WithDerivedStateOf(
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 未优化版本：高频状态变更导致频繁重组
 *
 * 问题分析：
 * 1. listState.firstVisibleItemIndex 在滚动时频繁变化
 * 2. 每次变化都会触发 showButton 重新计算
 * 3. 即使计算结果不变（如连续滚动时始终显示按钮），也会触发重组
 *
 * Layout Inspector验证：
 * - 每次滚动一个项目时，按钮组件的重组计数都会增加
 */
@SuppressLint("FrequentlyChangingValue")
@Composable
fun WithOutDerivedStateOf(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    Row {
        LazyColumn(state = listState) {
            items(list, key = { it }) { item ->
                Text("Item $item", modifier = modifier.height(150.dp))
            }
        }

        // 问题点：直接使用高频变化的状态值
        val showButton = listState.firstVisibleItemIndex > 0

        if (showButton) {
            Button(onClick = {}) {
                Text("Without Derived")
            }
        }
    }
}

/**
 * 优化版本：使用 derivedStateOf 合并高频状态变更
 *
 * 优化机制：
 * 1. derivedStateOf 将高频变化的索引值转换为稳定的布尔值
 * 2. 只有当布尔值实际变化时（true ↔ false）才会触发重组
 * 3. 中间状态变更被自动合并，避免不必要的重组
 *
 * Layout Inspector验证：
 * - 滚动过程中按钮重组计数不变
 * - 仅当列表第一项完全移出/移入视图时重组计数增加
 */
@Composable
fun WithDerivedStateOf(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    Row {
        // 优化点：使用 derivedStateOf 包装高频状态
        val showButton by remember {
            derivedStateOf {
                // 仅当条件实际变化时触发重组
                listState.firstVisibleItemIndex > 0
            }
        }

        if (showButton) {
            Button(onClick = {}) {
                Text("With Derived")
            }
        }

        LazyColumn(state = listState) {
            items(list, key = { it }) { item ->
                Text("Item $item", modifier = modifier.height(150.dp))
            }
        }
    }
}