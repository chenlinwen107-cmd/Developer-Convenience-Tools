# DCT — Developer Convenience Tools（第 1 阶段）

Android 优先的 AI 编程工作台。理念：**AI 负责理解与协助，Termux 负责本地执行，用户拥有最终控制权。**

第 1 阶段只包含：工程配置、Material 3 主题、导航骨架、六个页面占位、domain 接口与 Mock 实现、手动 DI。
**不包含**真实 AI API、GitHub OAuth、Termux 命令执行、终端模拟器。所有数据均为 Mock，终端输出统一带 `[模拟]` 前缀并有横幅标注。

## 技术版本

| 项 | 版本 |
|---|---|
| Android Gradle Plugin | 8.13.0 |
| Gradle（wrapper 配置） | 8.13 |
| Kotlin | 2.3.0（Compose 编译器插件 `org.jetbrains.kotlin.plugin.compose` 与 Kotlin 同版本） |
| Compose BOM | 2025.09.00 |
| Navigation Compose / Lifecycle | 2.9.4 / 2.9.4 |
| Activity Compose / Core KTX | 1.11.0 / 1.17.0 |
| compileSdk / targetSdk / minSdk | 36 / 36 / 26 |
| JDK | 17+ |

版本集中在 `gradle/libs.versions.toml`。

**兼容依据（官方）**
- Kotlin 官方兼容表：Kotlin 2.3.0 完全支持 Gradle 7.6.3–9.0.0、AGP 8.2.2–8.13.0。本工程的 Gradle 8.13 与 AGP 8.13.0 均在范围内。
- AGP 8.13 官方说明：最低 Gradle 8.13、JDK 17、SDK Build Tools 35.0.0、最高支持 API 36.1。
- 此前使用的 Kotlin 2.2.20 + AGP 8.13.2 不在 Kotlin 官方表内（2.2.20 仅列到 AGP 8.11.1），已更换。AGP 8.13.2 虽新增 R8 对 Kotlin 2.3 的支持，但超出 Kotlin 表中 2.3.0 对应的 AGP 上限，因此保守选 8.13.0；本工程未启用代码压缩，不受影响。
- Compose BOM 2025.09.00、Lifecycle 2.9.4、Activity Compose 1.11.0、Navigation Compose 2.9.4 确认存在于公开仓库的依赖声明中；Core KTX 1.17.0 的存在性及各库之间的相互兼容性均未经官方表确认。**这些依赖版本未经实际构建验证。**

## 构建

1. Android Studio（支持 AGP 8.13 的版本）打开本目录，等待 Gradle Sync，运行 `app`。
2. 命令行：本包含 `gradlew` / `gradlew.bat`（简化启动脚本），但**不含 `gradle/wrapper/gradle-wrapper.jar`**（生成环境无法下载二进制）。首次请在已装 Gradle 8.13+ 的环境执行一次：
   `gradle wrapper --gradle-version 8.13`
   这会生成官方 jar 并覆盖脚本，之后即可 `./gradlew assembleDebug`。没有 jar 时 `./gradlew` 会给出明确错误提示而不是静默失败。
3. 在 Termux 构建需要自行配置 JDK 17 与 Android SDK（含 platform 36、build-tools 35+），本工程不提供该环境。

## 目录结构

```
app/src/main/java/com/dct/app/
├── DctApplication.kt / MainActivity.kt
├── di/AppContainer.kt            手动 DI，替换真实实现只改这里
├── domain/model, domain/repository   数据模型与四个 Repository 接口
├── data/mock                     四个 Mock 实现
├── core/ui/theme, core/ui/components 主题 tokens、通用组件
├── navigation                    Destination、DctApp（底栏/侧栏自适应）、NavHost
└── feature/{home,project,assistant,terminal,github,settings}
                                  每个功能：Screen + ViewModel + UiState
```

## 后续集成接口

- `ProjectRepository`：本地项目与 Git 状态。
- `GitHubRepository`：仓库、提交、差异；真实实现需 OAuth 与安全存储 Token。
- `AiAssistantRepository`：对话与修改建议。`approve` 在真实实现中必须在用户审查 diff 后才应用。
- `CommandExecutionRepository`：命令提交与输出。真实实现通过 Termux 执行层，页面本身不执行命令；高风险命令需确认。

在 `AppContainer` 中把 Mock 换成真实实现即可，ViewModel 与页面无需改动。

## 安全原则

AI 修改先展示差异再由用户决定；推送与高风险命令需明确确认；API Key / GitHub Token 不得明文写入文件或日志，不得暴露给模型。
