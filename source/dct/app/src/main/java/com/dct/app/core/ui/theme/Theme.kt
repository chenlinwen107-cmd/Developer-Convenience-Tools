package com.dct.app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** 状态色三元组：圆点、文字、淡底。 */
@Immutable
class StatusColors(val dot: Color, val text: Color, val container: Color)

/**
 * Material 3 色板之外的语义色。通过 [dct]（`MaterialTheme.dct`）读取。
 * 色值经 WCAG 对比度检查：正文类文字 >= 4.5:1。
 */
@Immutable
class DctColors(
    val isDark: Boolean,
    val border: Color,
    val divider: Color,
    val subtleSurface: Color,
    val codeBackground: Color,
    val codeText: Color,
    val codeMutedText: Color,
    val diffAddBackground: Color,
    val diffAddText: Color,
    val diffDelBackground: Color,
    val diffDelText: Color,
    val diffHunkBackground: Color,
    val diffHunkText: Color,
    val neutral: StatusColors,
    val info: StatusColors,
    val success: StatusColors,
    val warning: StatusColors,
    val danger: StatusColors,
)

private val LightDctColors = DctColors(
    isDark = false,
    border = Color(0xFFE5E7EB),
    divider = Color(0xFFEEF0F3),
    subtleSurface = Color(0xFFF6F7F9),
    codeBackground = Color(0xFFF6F8FA),
    codeText = Color(0xFF1F2328),
    codeMutedText = Color(0xFF59636E),
    diffAddBackground = Color(0xFFE6FFEC),
    diffAddText = Color(0xFF116329),
    diffDelBackground = Color(0xFFFFEBE9),
    diffDelText = Color(0xFF82071E),
    diffHunkBackground = Color(0xFFDDF4FF),
    diffHunkText = Color(0xFF0550AE),
    neutral = StatusColors(Color(0xFF9CA3AF), Color(0xFF4B5563), Color(0xFFF3F4F6)),
    info = StatusColors(Color(0xFF3F5BD9), Color(0xFF2B3FA8), Color(0xFFEEF2FF)),
    success = StatusColors(Color(0xFF10B981), Color(0xFF047857), Color(0xFFECFDF5)),
    warning = StatusColors(Color(0xFFF59E0B), Color(0xFF92400E), Color(0xFFFFFBEB)),
    danger = StatusColors(Color(0xFFEF4444), Color(0xFFB91C1C), Color(0xFFFEF2F2)),
)

private val DarkDctColors = DctColors(
    isDark = true,
    border = Color(0xFF232A33),
    divider = Color(0xFF1B222B),
    subtleSurface = Color(0xFF11161C),
    codeBackground = Color(0xFF0D1117),
    codeText = Color(0xFFE6EDF3),
    codeMutedText = Color(0xFF9AA4B2),
    diffAddBackground = Color(0xFF0F2A1A),
    diffAddText = Color(0xFF7EE787),
    diffDelBackground = Color(0xFF301517),
    diffDelText = Color(0xFFFFA198),
    diffHunkBackground = Color(0xFF0D2238),
    diffHunkText = Color(0xFF79C0FF),
    neutral = StatusColors(Color(0xFF6B7685), Color(0xFF9AA4B2), Color(0xFF1B222B)),
    info = StatusColors(Color(0xFF7C93FF), Color(0xFFC7D2FF), Color(0xFF1B2447)),
    success = StatusColors(Color(0xFF34D399), Color(0xFF34D399), Color(0xFF0E2A20)),
    warning = StatusColors(Color(0xFFFBBF24), Color(0xFFFBBF24), Color(0xFF2A2110)),
    danger = StatusColors(Color(0xFFF87171), Color(0xFFF87171), Color(0xFF2D1417)),
)

val LocalDctColors = staticCompositionLocalOf { LightDctColors }

/** 读取扩展语义色：`MaterialTheme.dct.border`。需位于 [DctTheme] 内，否则回退为浅色值。 */
val MaterialTheme.dct: DctColors
    @Composable
    @ReadOnlyComposable
    get() = LocalDctColors.current

private val LightColors = lightColorScheme(
    primary = Color(0xFF3F5BD9),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEEF2FF),
    onPrimaryContainer = Color(0xFF2B3FA8),
    secondary = Color(0xFF4B5563),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF3F4F6),
    onSecondaryContainer = Color(0xFF111827),
    tertiary = Color(0xFF047857),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFECFDF5),
    onTertiaryContainer = Color(0xFF065F46),
    error = Color(0xFFB91C1C),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEF2F2),
    onErrorContainer = Color(0xFFB91C1C),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF5F6673),
    surfaceTint = Color.Transparent,
    outline = Color(0xFF8A94A3),
    outlineVariant = Color(0xFFE5E7EB),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFF3F4F6),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAFAFB),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFF6F7F9),
    surfaceContainerHighest = Color(0xFFF3F4F6),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7C93FF),
    onPrimary = Color(0xFF0B1030),
    primaryContainer = Color(0xFF1B2447),
    onPrimaryContainer = Color(0xFFC7D2FF),
    secondary = Color(0xFF9AA4B2),
    onSecondary = Color(0xFF0B0F14),
    secondaryContainer = Color(0xFF1B222B),
    onSecondaryContainer = Color(0xFFE6EDF3),
    tertiary = Color(0xFF34D399),
    onTertiary = Color(0xFF052E1F),
    tertiaryContainer = Color(0xFF0E2A20),
    onTertiaryContainer = Color(0xFF34D399),
    error = Color(0xFFF87171),
    onError = Color(0xFF2D0A0C),
    errorContainer = Color(0xFF2D1417),
    onErrorContainer = Color(0xFFF87171),
    background = Color(0xFF0B0F14),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF0B0F14),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF161C24),
    onSurfaceVariant = Color(0xFF9AA4B2),
    surfaceTint = Color.Transparent,
    outline = Color(0xFF6B7685),
    outlineVariant = Color(0xFF232A33),
    surfaceBright = Color(0xFF1B222B),
    surfaceDim = Color(0xFF0B0F14),
    surfaceContainerLowest = Color(0xFF080B0F),
    surfaceContainerLow = Color(0xFF0E1318),
    surfaceContainer = Color(0xFF11161C),
    surfaceContainerHigh = Color(0xFF161C24),
    surfaceContainerHighest = Color(0xFF1B222B),
)

@Composable
fun DctTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDctColors provides if (darkTheme) DarkDctColors else LightDctColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = DctTypography,
            shapes = DctShapes,
            content = content,
        )
    }
}
