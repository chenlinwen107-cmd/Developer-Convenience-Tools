# DCT UI 全量改造（候选补丁，未编译）

基线：`ui-redesign-source`，提交 `88c4061`。改动前已核对仓库 blob SHA 与本地基线一致。
本包只含改动文件，目录从 `source/dct/` 起，解压到仓库根目录即可覆盖。**没有提交或推送任何内容。**

## 对 GPT 第一阶段补丁的处理
- `Theme.kt`、`Tokens.kt`：与我第一阶段文件逐字相同，继承并保留。
- `Components.kt`：采纳 GPT 的两处修正（`DctScreen` 不再新增 `subtitle` 参数；`LogBlock`/`DiffView` 保留横向滚动），不采纳其 diff 行宽处理（行底色只到文字宽度）。改为：所有行取“最宽行与可视宽度的较大者”，整行底色铺满。
- 其余组件、导航、图标、页面、资源为新增或重写。

## 改动文件（共 16 个）
**主题与组件**
- `core/ui/theme/Theme.kt`：浅/深色板、`DctColors`（`MaterialTheme.dct`）、状态色、代码与 diff 色。
- `core/ui/theme/Tokens.kt`：`DctDimens`、`DctTypography`。
- `core/ui/components/Components.kt`：重写。保留全部原函数名与参数；新增 `DctPanel`、`DctDivider`、`SectionLabel`、`ListRow`、`ComingSoonRow`、`KeyValueRow`、`CodeTag`、`DctButton`、`ActionTile`。
- `core/ui/icons/DctIcons.kt`（新增）：5 个自绘线性图标，无新依赖。

**导航**
- `navigation/DctApp.kt`：底栏半透明 + 顶部细边框，无模糊；横屏/平板用带 “DCT” 字标的侧栏。
- `navigation/Destinations.kt`：只替换图标引用。路由、顺序、`TopLevelDestinations` 不变。

**页面**（只改布局与样式，ViewModel 与调用方式不变）
- 工作台：两个真实入口大按钮，未实现入口改为“即将推出”行，项目与任务为分隔行面板。
- 项目详情：键值信息面板 + 操作入口分组。
- AI 助手：用户消息靠右气泡，AI 消息无气泡；修改建议带状态点；批准/拒绝使用统一按钮。
- 终端：命令输入使用等宽字体；每条任务以 `$ 命令` 开头，保留“模拟”标签、退出码、耗时、失败提示。
- GitHub：仓库、提交（哈希标签）、差异、操作入口分组。
- 设置：分组列表（AI / 终端 / 显示）。

**资源**
- `res/values/themes.xml`、`res/values/colors.xml`、`res/values-night/themes.xml`、`res/values-night/colors.xml`：启动窗口背景与 Compose 背景色一致（浅 `#FFFFFF` / 深 `#0B0F14`）。

## 未改动（已与基线逐文件比对）
`domain/`、`data/mock/`、`di/`、所有 ViewModel、`DctNavHost.kt`、`MainActivity.kt`、`DctApplication.kt`、Manifest、全部 Gradle 文件与版本目录、README、`main` 分支、原始 ZIP、`.github` 工作流。无新增依赖（所有 import 均来自已声明的 Compose、Lifecycle、Navigation、Activity）。

## 行为变化
- 所有文字说明与“模拟/Mock”标注保留；原先灰掉的按钮改为不可点击的“即将推出”行（外观变化，仍不可操作）。
- `DctScreen` 标题栏固定、不再随列表滚动；内容区新增 `imePadding`，键盘弹出时输入框不被遮挡。
- 日志与 diff 保持横向滚动。
- 主题 XML 父主题由深色 Material 改为浅色（夜间用深色变体）。

## 已完成的检查
- 27 个 Kotlin 文件：包名与目录一致、括号配对、项目内 import 全部可解析、无未使用 import、无未导入的引用。
- 浅/深两套逐项计算 WCAG 对比度，文字类组合 >= 4.5:1：正文、状态标签、横幅、终端、diff 增/删/块头、底栏选中/未选中、侧栏选中、用户气泡、代码标签、主按钮卡片说明文字。

## 未验证（务必实测）
- **没有编译。** 沙箱无网络与 Gradle，不能说构建通过。请构建 `:app:assembleDebug`。
- 依赖较新的 Material 3 API（`HorizontalDivider`/`VerticalDivider`、`surfaceContainer*`、`Surface(onClick)`），对应 BOM 2025.09.00；以及 Compose 的 `BoxWithConstraints` + `IntrinsicSize.Max` 组合（diff 行宽）。
- 自绘图标的实际观感、深浅色切换、窄屏换行、平板与横屏侧栏、键盘弹出时的布局均未在设备上看过。
- `imePadding` 与底部导航栏同时存在时的间距需实测。

## 已知问题与局限
- 底栏“半透明”几乎看不出：内容位于导航栏上方（由 Scaffold 间距保证不被遮挡），没有内容滚动到栏下方，所以只是接近不透明的白/黑底加细边框；真正的玻璃效果需要内容延伸到栏下并加模糊，当前不引入依赖。
- GitHub 页没有显示“分支”：`GitHubRepo` 模型没有分支字段，不虚构数据；需扩展领域模型后再做。
- 首页、GitHub 页的列表放在单个 `item` 内，不再逐项懒加载；Mock 数据量很小没有影响，接入真实长列表时需改回逐项。
- 界面文字仍是硬编码中文；设置页的主题切换只是占位，主题跟随系统。
- 与本次无关、仍待处理：现有 `android-build.yml` 构建的是 ZIP 解压出的旧代码，不会构建 `source/dct`；`source/dct` 缺少 `gradle-wrapper.jar`。
