package com.dct.app.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * 应用自绘的线性图标（24x24、2 宽描边、圆角端点），不依赖任何图标库。
 * 描边色为黑色，实际颜色由 Icon 的 tint 决定。
 */
object DctIcons {
    private fun icon(name: String, block: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.8f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathBuilder = block,
        ).build()

    /** 圆（以 cx, cy 为圆心、r 为半径）。 */
    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, true, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, true, -2 * r, 0f)
        close()
    }

    /** 工作台：房屋。 */
    val Workbench: ImageVector by lazy {
        icon("DctWorkbench") {
            moveTo(4f, 10.5f); lineTo(12f, 4f); lineTo(20f, 10.5f); lineTo(20f, 20f); lineTo(4f, 20f); close()
            moveTo(9.5f, 20f); lineTo(9.5f, 14f); lineTo(14.5f, 14f); lineTo(14.5f, 20f)
        }
    }

    /** AI 助手：四角星。 */
    val Assistant: ImageVector by lazy {
        icon("DctAssistant") {
            moveTo(12f, 3f); lineTo(14.2f, 9.8f); lineTo(21f, 12f); lineTo(14.2f, 14.2f)
            lineTo(12f, 21f); lineTo(9.8f, 14.2f); lineTo(3f, 12f); lineTo(9.8f, 9.8f); close()
        }
    }

    /** 终端：窗口加命令提示符。 */
    val Terminal: ImageVector by lazy {
        icon("DctTerminal") {
            moveTo(3f, 5f); lineTo(21f, 5f); lineTo(21f, 19f); lineTo(3f, 19f); close()
            moveTo(7f, 10f); lineTo(10f, 12.5f); lineTo(7f, 15f)
            moveTo(12.5f, 15f); lineTo(17f, 15f)
        }
    }

    /** GitHub 页：分支图标（仓库与分支）。 */
    val Branch: ImageVector by lazy {
        icon("DctBranch") {
            circle(6f, 5.5f, 2.5f)
            circle(6f, 18.5f, 2.5f)
            circle(18f, 9f, 2.5f)
            moveTo(6f, 8f); lineTo(6f, 16f)
            moveTo(18f, 11.5f); curveTo(18f, 14f, 14f, 14.5f, 10.5f, 15.2f); curveTo(8f, 15.7f, 6.6f, 16f, 6f, 16f)
        }
    }

    /** 设置：两组滑杆。 */
    val Settings: ImageVector by lazy {
        icon("DctSettings") {
            moveTo(4f, 8f); lineTo(20f, 8f)
            moveTo(4f, 16f); lineTo(20f, 16f)
            circle(9f, 8f, 2.2f)
            circle(15f, 16f, 2.2f)
        }
    }
}
