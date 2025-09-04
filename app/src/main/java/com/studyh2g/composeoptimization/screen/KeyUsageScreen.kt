package com.studyh2g.composeoptimization.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * 键(Key)使用演示页面 - 展示在LazyColumn中使用key对重组性能的影响
 *
 * 此页面包含两个对比列表：
 * 1. 左侧：不使用key的列表（所有项在添加新项时都会重组）
 * 2. 右侧：使用key的列表（仅新项重组，现有项跳过重组）
 */
@Composable
fun KeyUsageScreen(modifier: Modifier = Modifier) {
    // 水平布局展示两个对比列表
    Row(modifier = modifier) {
        // 左侧：不使用key的列表
        AddListElement(
            desc = "Without Key",
            hasKey = false,
            modifier = Modifier.weight(1f) // 平均分配空间
        )

        // 添加间距分隔两个列表
        Spacer(modifier = Modifier.weight(0.1f))

        // 右侧：使用key的列表
        AddListElement(
            desc = "With Key",
            hasKey = true,
            modifier = Modifier.weight(1f) // 平均分配空间
        )
    }
}

/**
 * 可重用的列表组件，展示键(Key)对重组的影响
 *
 * @param desc 列表描述文本（显示在顶部）
 * @param hasKey 是否在LazyColumn中使用键(Key)
 * @param modifier 修饰符
 */
@Composable
private fun AddListElement(
    desc: String,
    hasKey: Boolean,
    modifier: Modifier = Modifier,
) {
    // 状态管理：存储当前列表项
    var items by remember { mutableStateOf(listOf(1, 2, 3)) }

    // 垂直布局：标题 + 列表 + 按钮
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        // 列表标题（描述当前列表类型）
        Text(
            text = desc,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 核心：LazyColumn根据hasKey决定是否使用key
        LazyColumn {
            if (hasKey) {
                // 使用key优化：Compose能准确追踪项变化，仅更新变化项
                items(
                    items = items,
                    key = { it } // 使用项值作为唯一键
                ) { item ->
                    Text("Item $item")
                }
            } else {
                // 未使用key：添加新项时所有现有项都会重组
                items(items) { item ->
                    Text("Item $item")
                }
            }
        }

        // 添加新项的按钮
        Button(
            onClick = {
                // 在列表开头添加新项（模拟最差重组场景）
                items = listOf(items.size + 1) + items
            },
        ) {
            Text("Add item at top")
        }
    }
}