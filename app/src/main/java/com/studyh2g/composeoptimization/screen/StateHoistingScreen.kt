package com.studyh2g.composeoptimization.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * 状态下沉优化演示屏幕 - 展示状态下沉对重组性能的影响
 *
 * 此屏幕包含两个对比组件：
 * 1. 上方：未优化的列表项（状态在高层组件）
 * 2. 下方：优化后的列表项（状态下沉到子组件）
 *
 * 通过Layout Inspector可观察：
 * - 未优化版本：点击按钮时整个Column作用域重组（标题会被调用检查）
 * - 优化版本：点击按钮时仅按钮组件重组，父组件完全跳过重组
 */
@Composable
fun StateHoistingScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // 未优化：状态在父组件导致整个作用域重组
        UnoptimizedListItem(title = "Unoptimized")

        // 优化：状态下沉后父组件满足跳过条件
        OptimizedListItem(title = "Optimized")
    }
}

/**
 * 未优化版本：状态保存在高层组件导致重组作用域过大
 *
 * 重组机制：
 * 1. 状态变更触发整个Column作用域重组
 * 2. 标题组件参数未变 → 跳过内容重组执行
 * 3. 但标题组件仍会被调用进行检查 → 重组计数不变但增加检查开销
 *
 * Layout Inspector验证：
 * - 点击按钮时：
 *   - 标题组件：重组计数不变（跳过执行）
 *   - 按钮组件：重组计数增加（正常变化）
 */
@Composable
fun UnoptimizedListItem(title: String) {
    var isClicked by remember { mutableStateOf(false) }

    Column {
        // 虽参数未变，但每次重组都会被检查（重组计数+1）
        Text(text = title)

        Button(onClick = { isClicked = !isClicked }) {
            Text(if (isClicked) "Clicked" else "Click Me")
        }
    }
}

/**
 * 优化版本：通过状态下沉精确控制重组作用域
 *
 * 优化机制：
 * 1. 状态移至实际使用的最小范围（按钮组件内部）
 * 2. 父组件满足跳过重组条件：
 *    - 输入参数未变（title不变）
 *    - 调用点稳定（ClickableButtonWithState位置不变）
 * 3. 状态变更仅影响按钮组件内部重组
 *
 * Layout Inspector验证：
 * - 点击按钮时：
 *   - 按钮组件：重组计数增加
 *   - 标题组件：重组计数不变（完全跳过执行）
 *   - 父组件：重组计数不变
 */
@Composable
fun OptimizedListItem(title: String) {
    Column {
        // 完全跳过执行：
        // 1. 参数未变 → 跳过内容重组
        // 2. 调用点稳定 → 跳过函数调用
        Text(text = title)

        // 独立重组作用域：
        // 状态变化仅影响此组件内部
        ClickableButtonWithState()
    }
}

/**
 * 状态隔离组件：承载下沉的状态
 *
 * 优势：
 * 1. 状态变更仅触发本组件重组
 * 2. 重组作用域限制在最小范围
 * 3. 避免父组件冗余的执行和检查
 */
@Composable
private fun ClickableButtonWithState() {
    var isClicked by remember { mutableStateOf(false) }

    Button(onClick = { isClicked = !isClicked }) {
        Text(if (isClicked) "Clicked" else "Click Me")
    }
}