1. 项目简介：这是一个用于演示Jetpack Compose重组优化策略的Demo项目。
2. 文章链接：。
3. 运行说明：同步完依赖直接运行项目即可。若要查看Compose编译报告，请命令行运行./gradlew assembleRelease ，Compose编译报告目录在app/build/compose_compiler。
4. 示例目录：
 ○ LambdaParameterActivity：演示了如何通过方法引用或remember来稳定Lambda参数，避免不必要的重组。对应文章章节【2.1.1】。
 ○ KeyUsageScreen：演示了如何通过键(Key)使用稳定Lambda参数优化LazyColumn，避免不必要的重组。对应文章章节【2.1.2】。
 ○ StateHoistingScreen：演示了如何通过状态下沉，缩小重组作用域。对应文章章节【2.1.3】。
 ○ PhaseOptimizationScreen：演示了如何通过Modify的Lambda方式来优化重组阶段，避免不必要的重组。对应文章章节【2.2.1】。
 ○ DerivedStateScreen：演示了如何通过derivedStateOf合并高频状态更新ss。对应文章章节【2.3.2】。