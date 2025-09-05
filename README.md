# Jetpack Compose 重组优化 Demo

这是一个用于演示 Jetpack Compose 重组优化策略的 Demo 项目，配套文章《Jetpack Compose重组优化：xxx》。

## 核心原理

Compose 的重组（Recomposition）是其响应式编程模型的核心。理解编译器何时及为何会跳过重组是进行性能优化的关键。
**[详细的重组跳过决策流程图](./docs/compose-recomposition-skip-flowchart.pdf)** 直观地阐释了这一机制。

## 运行说明

1.  克隆项目并同步 Gradle 依赖。
2.  直接运行 `app` 模块即可。
3.  应用内每个示例界面均可独立运行和观察效果。**建议结合 Android Studio 的 Layout Inspector（重组计数功能）** 来直观对比优化前后的差异。

## 生成编译报告

若要查看模块的 Compose 编译器报告（用于分析稳定性、可跳过性等），请在项目根目录运行以下命令：
```bash
./gradlew assembleRelease
```

报告生成目录：app/build/compose_compiler/ , 包含：

| 文件类型 | 说明 |
| :--- | :--- |
| [`*-classes.txt`]	| 类稳定性报告（哪些类被识别为稳定）|
| [`*-composables.txt`]	| 可组合项的可重启性和可跳过性报告 |
| [`*-composables.csv`]	| 可组合项报告的 CSV 版本 |
| [`*-module.json`]	| 编译阶段优化指标汇总 |

## 示例目录
| 示例名称                                                                                                       | 对应文章章节 | 优化策略说明 |
|:-----------------------------------------------------------------------------------------------------------| :--- | :--- |
| [`LambdaParameterActivity`](app/src/main/java/com/studyh2g/composeoptimization/LambdaParameterActivity.kt) | 2.1.1 | 演示了如何通过**方法引用**或 **`remember`** 来稳定 Lambda 参数，避免因回调函数实例变化导致的不必要重组。 |
| [`KeyUsageScreen`](app/src/main/java/com/studyh2g/composeoptimization/screen/KeyUsageScreen.kt)            | 2.1.2 | 演示了如何在 `LazyColumn` 中为项添加**唯一键 (`key`)**，帮助 Compose 精准识别项的变化，重用组件实例，避免大规模重组。 |
| [`StateHoistingScreen`](app/src/main/java/com/studyh2g/composeoptimization/screen/StateHoistingScreen.kt)  | 2.1.3 | 演示了如何通过**状态下沉**，将状态移至其作用的最小范围，从而最大限度地缩小重组范围。 |
| [`PhaseOptimizationScreen`](app/src/main/java/com/studyh2g/composeoptimization/screen/PhaseOptimizationScreen.kt) | 2.2.1 | 演示了如何使用 Modifier（如 `offset`）的 **Lambda 重载**，将计算从组合阶段推迟到布局阶段，优化性能。 |
| [`DerivedStateScreen`](app/src/main/java/com/studyh2g/composeoptimization/screen/DerivedStateScreen.kt)   | 2.3.2 | 演示了如何使用 **`derivedStateOf`** 合并多个高频状态更新为一个，避免因无关状态变化导致的冗余重组。 |