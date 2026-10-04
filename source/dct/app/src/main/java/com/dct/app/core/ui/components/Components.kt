@file:OptIn(ExperimentalLayoutApi::class)

package com.dct.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dct.app.core.ui.theme.CodeTextStyle
import com.dct.app.core.ui.theme.DctSpacing

/** 统一页面容器：标题 + 可滚动内容，平板上限制最大宽度。 */
@Composable
fun DctScreen(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 840.dp).fillMaxSize(),
            contentPadding = PaddingValues(DctSpacing.md),
            verticalArrangement = Arrangement.spacedBy(DctSpacing.md),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBack != null) {
                        TextButton(onClick = onBack) { Text("返回") }
                    }
                    Text(title, style = MaterialTheme.typography.headlineSmall)
                }
            }
            content()
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(modifier.fillMaxWidth()) {
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

/** 明确标注模拟/Mock 状态的横幅。 */
@Composable
fun SimulatedBanner(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text, Modifier.padding(DctSpacing.md), style = MaterialTheme.typography.bodyMedium)
    }
}

enum class ChipTone { NEUTRAL, INFO, SUCCESS, WARNING, ERROR }

@Composable
fun StatusChip(label: String, tone: ChipTone = ChipTone.NEUTRAL) {
    val scheme = MaterialTheme.colorScheme
    val (bg, fg) = when (tone) {
        ChipTone.NEUTRAL -> scheme.surfaceVariant to scheme.onSurfaceVariant
        ChipTone.INFO -> scheme.primaryContainer to scheme.onPrimaryContainer
        ChipTone.SUCCESS -> scheme.tertiaryContainer to scheme.onTertiaryContainer
        ChipTone.WARNING -> scheme.secondaryContainer to scheme.onSecondaryContainer
        ChipTone.ERROR -> scheme.errorContainer to scheme.onErrorContainer
    }
    Surface(color = bg, contentColor = fg, shape = MaterialTheme.shapes.small) {
        Text(label, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun LogBlock(lines: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .horizontalScroll(rememberScrollState())
            .padding(DctSpacing.sm),
    ) {
        lines.forEach { Text(it, style = CodeTextStyle, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
fun DiffView(diff: String, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier
            .fillMaxWidth()
            .background(scheme.surfaceVariant, MaterialTheme.shapes.small)
            .horizontalScroll(rememberScrollState())
            .padding(DctSpacing.sm),
    ) {
        diff.lines().forEach { line ->
            val color = when {
                line.startsWith("+++") || line.startsWith("---") -> scheme.onSurfaceVariant
                line.startsWith("+") -> scheme.tertiary
                line.startsWith("-") -> scheme.error
                line.startsWith("@@") -> scheme.primary
                else -> scheme.onSurfaceVariant
            }
            Text(line.ifEmpty { " " }, style = CodeTextStyle, color = color)
        }
    }
}
