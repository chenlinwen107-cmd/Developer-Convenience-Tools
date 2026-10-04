package com.dct.app.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.ComingSoonRow
import com.dct.app.core.ui.components.DctDivider
import com.dct.app.core.ui.components.DctPanel
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.ListRow
import com.dct.app.core.ui.components.SectionLabel
import com.dct.app.core.ui.components.StatusChip
import com.dct.app.core.ui.theme.DctSpacing

/** 第一阶段为静态占位页，无需 ViewModel；加入真实设置项时再引入。 */
@Composable
fun SettingsScreen() {
    DctScreen("设置") {
        item {
            SettingsGroup("AI") {
                ComingSoonRow("AI 提供商与模型", description = "配置提供商与模型。")
                DctDivider()
                ComingSoonRow(
                    "API Key 管理",
                    description = "后续将使用安全存储，不会明文写入项目文件或日志，也不会暴露给模型。",
                )
            }
        }
        item {
            SettingsGroup("终端") {
                ListRow(
                    title = "Termux 连接",
                    subtitle = "这里将说明所需权限与连接方式。第一阶段不会与 Termux 通信。",
                    trailing = { StatusChip("未连接（尚未实现）", ChipTone.NEUTRAL) },
                )
            }
        }
        item {
            SettingsGroup("显示") {
                ComingSoonRow("主题 / 编辑器 / 终端显示", description = "当前主题跟随系统深浅色。")
            }
        }
    }
}

@Composable
private fun SettingsGroup(label: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
        SectionLabel(label)
        DctPanel(content = content)
    }
}
