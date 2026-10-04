@file:OptIn(ExperimentalLayoutApi::class)

package com.dct.app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dct.app.core.ui.theme.CodeTextStyle
import com.dct.app.core.ui.theme.DctDimens
import com.dct.app.core.ui.theme.DctSpacing
import com.dct.app.core.ui.theme.StatusColors
import com.dct.app.core.ui.theme.dct

// ───────────────────────── 页面骨架 ─────────────────────────

/**
 * 统一页面容器：固定在顶部的标题栏 + 细分割线 + 可滚动内容，平板上限制最大宽度。
 * 参数签名与第一版保持一致。内容区带 imePadding，键盘弹出时输入框不会被遮挡。
 */
@Composable
fun DctScreen(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = DctDimens.contentMaxWidth).fillMaxSize()) {
            DctScreenHeader(title = title, onBack = onBack)
            HorizontalDivider(thickness = DctDimens.borderWidth, color = MaterialTheme.dct.divider)
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f).imePadding(),
                contentPadding = PaddingValues(DctSpacing.md),
                verticalArrangement = Arrangement.spacedBy(DctSpacing.md),
                content = content,
            )
        }
    }
}

@Composable
private fun DctScreenHeader(title: String, onBack: (() -> Unit)?) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = DctSpacing.md, vertical = DctSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
    ) {
        if (onBack != null) {
            TextButton(onClick = onBack) { Text("返回") }
        }
        Text(title, style = MaterialTheme.typography.headlineSmall)
    }
}

/** 分组小标题（列表、面板上方）。 */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

// ───────────────────────── 面板与分割线 ─────────────────────────

/** 无阴影、无填充的细边框面板：本设计语言的基础容器。 */
@Composable
fun DctPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(DctDimens.borderWidth, MaterialTheme.dct.border),
    ) {
        Column(content = content)
    }
}

@Composable
fun DctDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier, thickness = DctDimens.borderWidth, color = MaterialTheme.dct.divider)
}

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    DctPanel(modifier) {
        Column(Modifier.padding(DctSpacing.md), verticalArrangement = Arrangement.spacedBy(DctSpacing.sm)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
fun ActionFlow(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(DctSpacing.sm),
    ) { content() }
}

@Composable
fun PhaseNote(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

// ───────────────────────── 列表行 ─────────────────────────

/** 通用列表行：标题 + 可选副标题，左侧 [leading]、右侧 [trailing]，[onClick] 非空时可点击。 */
@Composable
fun ListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    monoTitle: Boolean = false,
) {
    Row(
        modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = DctSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (leading != null) leading()
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                title,
                style = (if (monoTitle) CodeTextStyle else MaterialTheme.typography.bodyMedium)
                    .copy(fontWeight = FontWeight.Medium),
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (trailing != null) trailing()
    }
}

/** 尚未实现的入口：弱化显示并带“即将推出”标签，不可点击（替代灰掉的按钮）。 */
@Composable
fun ComingSoonRow(title: String, modifier: Modifier = Modifier, description: String? = null) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = DctSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (description != null) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        StatusChip("即将推出", ChipTone.NEUTRAL)
    }
}

/** 键值行（项目详情等）：左侧标签，右侧内容。 */
@Composable
fun KeyValueRow(label: String, modifier: Modifier = Modifier, value: @Composable () -> Unit) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = DctSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DctSpacing.md),
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) { value() }
    }
}

@Composable
fun KeyValueRow(label: String, value: String, modifier: Modifier = Modifier, mono: Boolean = false) {
    KeyValueRow(label, modifier) {
        Text(
            value,
            style = if (mono) CodeTextStyle else MaterialTheme.typography.bodyMedium,
        )
    }
}

/** 等宽小标签：分支名、提交哈希等。 */
@Composable
fun CodeTag(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier
            .background(MaterialTheme.dct.subtleSurface, MaterialTheme.shapes.small)
            .border(DctDimens.borderWidth, MaterialTheme.dct.border, MaterialTheme.shapes.small)
            .padding(horizontal = 6.dp, vertical = 1.dp),
        style = CodeTextStyle.copy(fontSize = 12.sp, lineHeight = 16.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

// ───────────────────────── 按钮与入口 ─────────────────────────

enum class DctButtonVariant { PRIMARY, SECONDARY }

/** 统一按钮：小圆角矩形，主按钮实心，次按钮细边框。 */
@Composable
fun DctButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: DctButtonVariant = DctButtonVariant.PRIMARY,
) {
    val shape = MaterialTheme.shapes.small
    when (variant) {
        DctButtonVariant.PRIMARY -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
        ) { Text(text) }

        DctButtonVariant.SECONDARY -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            border = BorderStroke(DctDimens.borderWidth, MaterialTheme.dct.border),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
        ) { Text(text) }
    }
}

/** 首页的大入口：[primary] 为实心主色，否则为细边框。 */
@Composable
fun ActionTile(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = if (primary) scheme.primary else scheme.surface,
        contentColor = if (primary) scheme.onPrimary else scheme.onSurface,
        border = if (primary) null else BorderStroke(DctDimens.borderWidth, MaterialTheme.dct.border),
    ) {
        Column(Modifier.padding(DctSpacing.md), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.9f),
            )
        }
    }
}

// ───────────────────────── 状态与提示 ─────────────────────────

/** 明确标注模拟/Mock 状态的横幅：淡警示底加圆点，文字保持完整可读。 */
@Composable
fun SimulatedBanner(text: String) {
    val w = MaterialTheme.dct.warning
    Surface(
        color = w.container,
        contentColor = w.text,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(DctDimens.borderWidth, w.dot.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = DctSpacing.md, vertical = DctSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DctSpacing.sm),
        ) {
            Box(Modifier.size(DctDimens.statusDot).background(w.dot, CircleShape))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

enum class ChipTone { NEUTRAL, INFO, SUCCESS, WARNING, ERROR }

/** 状态标签：圆点 + 文字 + 淡底，不使用实心色块。 */
@Composable
fun StatusChip(label: String, tone: ChipTone = ChipTone.NEUTRAL) {
    val colors = MaterialTheme.dct
    val s: StatusColors = when (tone) {
        ChipTone.NEUTRAL -> colors.neutral
        ChipTone.INFO -> colors.info
        ChipTone.SUCCESS -> colors.success
        ChipTone.WARNING -> colors.warning
        ChipTone.ERROR -> colors.danger
    }
    Row(
        Modifier
            .background(s.container, MaterialTheme.shapes.small)
            .padding(horizontal = DctSpacing.sm, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(DctDimens.statusDot).background(s.dot, CircleShape))
        Text(label, style = MaterialTheme.typography.labelMedium, color = s.text)
    }
}

// ───────────────────────── 代码与日志 ─────────────────────────

/** 日志块：实色背景、等宽字体；长行保留横向滚动，不强制折行。 */
@Composable
fun LogBlock(lines: List<String>, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.dct
    val shape = MaterialTheme.shapes.small
    Column(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.codeBackground, shape)
            .border(DctDimens.borderWidth, colors.border, shape)
            .horizontalScroll(rememberScrollState())
            .padding(DctSpacing.sm),
    ) {
        lines.forEach { Text(it, style = CodeTextStyle, color = colors.codeText, softWrap = false) }
    }
}

/**
 * diff 视图：实色背景，增/删/块头整行淡底，保留行首 +/- 符号与横向滚动。
 * 所有行的宽度取“最宽行与可视宽度的较大者”，因此整行底色能铺满。
 */
@Composable
fun DiffView(diff: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.dct
    val shape = MaterialTheme.shapes.small
    BoxWithConstraints(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.codeBackground, shape)
            .border(DctDimens.borderWidth, colors.border, shape),
    ) {
        val viewport = maxWidth
        Column(
            Modifier
                .horizontalScroll(rememberScrollState())
                .widthIn(min = viewport)
                .width(IntrinsicSize.Max)
                .padding(vertical = DctSpacing.xs),
        ) {
            diff.lines().forEach { line ->
                val (bg, fg) = when {
                    line.startsWith("+++") || line.startsWith("---") -> Color.Transparent to colors.codeMutedText
                    line.startsWith("+") -> colors.diffAddBackground to colors.diffAddText
                    line.startsWith("-") -> colors.diffDelBackground to colors.diffDelText
                    line.startsWith("@@") -> colors.diffHunkBackground to colors.diffHunkText
                    else -> Color.Transparent to colors.codeText
                }
                Text(
                    text = line.ifEmpty { " " },
                    style = CodeTextStyle,
                    color = fg,
                    softWrap = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bg)
                        .padding(horizontal = DctSpacing.sm, vertical = 1.dp),
                )
            }
        }
    }
}
