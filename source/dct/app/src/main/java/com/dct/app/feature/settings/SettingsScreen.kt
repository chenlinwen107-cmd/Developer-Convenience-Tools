package com.dct.app.feature.settings

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.dct.app.core.ui.components.ChipTone
import com.dct.app.core.ui.components.DctScreen
import com.dct.app.core.ui.components.PhaseNote
import com.dct.app.core.ui.components.SectionCard
import com.dct.app.core.ui.components.StatusChip

/** 第一阶段为静态占位页，无需 ViewModel；加入真实设置项时再引入。 */
@Composable
fun SettingsScreen() {
    DctScreen("设置") {
        item {
            SectionCard("AI 提供商与模型") {
                FilledTonalButton(onClick = {}, enabled = false) { Text("配置提供商与模型") }
                PhaseNote("占位入口，后续阶段实现。")
            }
        }
        item {
            SectionCard("API Key 管理") {
                FilledTonalButton(onClick = {}, enabled = false) { Text("管理 API Key") }
                PhaseNote("后续将使用安全存储，不会明文写入项目文件或日志，也不会暴露给模型。")
            }
        }
        item {
            SectionCard("Termux 连接") {
                StatusChip("未连接（尚未实现）", ChipTone.NEUTRAL)
                PhaseNote("这里将说明所需权限与连接方式。第一阶段不会与 Termux 通信。")
            }
        }
        item {
            SectionCard("显示") {
                FilledTonalButton(onClick = {}, enabled = false) { Text("主题 / 编辑器 / 终端显示") }
                PhaseNote("当前主题跟随系统深浅色。")
            }
        }
    }
}
