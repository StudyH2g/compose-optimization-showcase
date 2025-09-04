package com.studyh2g.composeoptimization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyh2g.composeoptimization.constant.Routes
import com.studyh2g.composeoptimization.service.LambdaParameterViewModel
import com.studyh2g.composeoptimization.ui.theme.ComposeOptimizationShowcaseTheme

// ===============================================================
// 关键说明：为什么使用单独的Activity展示Lambda参数优化
// ===============================================================
// 1. 重组作用域隔离：
//    - 在Activity的顶层作用域中，每个RgbSelector有更明确的独立重组边界
//    - Compose运行时能更精确地追踪状态变化，实现预期的优化效果
//
// 2. 与可组合函数的区别：
//    当在同一个可组合函数中使用多个RgbSelector时：
//    a. 它们共享同一个父作用域(Column)
//    b. 状态变化可能导致整个作用域重组
//    c. 无法完美展示"只有被点击的按钮重组"的优化效果
//
// 3. 本Activity的展示优势：
//    - 清晰展示三种参数传递方式对重组的影响
//    - 非Lambda参数：整个组件重组
//    - Lambda参数：只有被点击的按钮重组
//    - remember Lambda：只有被点击的按钮重组
// ===============================================================
class LambdaParameterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeOptimizationShowcaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    GlobalTopAppBar(
                        currentRoute = Routes.LAMBDA_PARAMETER,
                        onBackClick = { finish() },
                        onHomeClick = { finish() }
                    )
                }) { innerPadding ->
                    val viewModel: LambdaParameterViewModel by viewModels()
                    val changeColorRemember = remember<(Color) -> Unit> {
                        {
                            viewModel.changeColorRemember(it)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // =================================================================
                        // 1. 非Lambda参数传递方式
                        // =================================================================
                        // 问题：每次父组件重组都会创建新的lambda实例
                        // 结果：整个RgbSelector组件都会重组（包括未点击的按钮）
                        // 原因：lambda捕获了viewModel实例，被视为不稳定参数
                        RgbSelector(
                            desc = "非Lambda参数",
                            color = viewModel.color,
                            onColorClick = {
                                viewModel.changeColor(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // =================================================================
                        // 2. Lambda参数（方法引用）传递方式
                        // =================================================================
                        // 优化：使用viewModel::changeColorLambda方法引用
                        // 优势：方法引用是单例对象，不会在每次重组时创建新实例
                        // 结果：只有被点击的按钮会重组
                        RgbSelector(
                            desc = "Lambda参数",
                            color = viewModel.colorLambda,
                            onColorClick = viewModel::changeColorLambda
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // =================================================================
                        // 3. remember Lambda传递方式
                        // =================================================================
                        // 优化：使用remember记忆lambda表达式
                        // 优势：跨重组复用同一个lambda实例
                        // 结果：只有被点击的按钮会重组
                        RgbSelector(
                            desc = "使用remember",
                            color = viewModel.colorRemember, onColorClick = changeColorRemember
                        )
                    }
                }
            }
        }
    }
}

/**
 * 自定义颜色选择器组件
 *
 * @param desc 组件描述，显示在顶部
 * @param color 当前显示的颜色
 * @param onColorClick 颜色点击回调函数
 * @param modifier 修饰符
 *
 * 重组优化说明：
 * 1. 当onColorClick参数是稳定的（相同实例）时，Compose可以跳过未变化的按钮重组
 * 2. 如果onColorClick每次重组都创建新实例，会导致整个组件重组
 */
@Composable
private fun RgbSelector(
    desc: String,
    color: Color,
    onColorClick: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            desc,
            style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    onColorClick(Color.Red)
                }) {
                Text(text = "Red")
            }
            Button(onClick = {
                onColorClick(Color.Green)
            }) {
                Text(text = "Green")
            }
            Button(onClick = {
                onColorClick(Color.Blue)
            }) {
                Text(text = "Blue")
            }
        }
    }
}
