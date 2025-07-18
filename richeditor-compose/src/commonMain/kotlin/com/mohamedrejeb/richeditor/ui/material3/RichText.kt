package com.mohamedrejeb.richeditor.ui.material3

/**
 * RichText (Material 3) - A Material Design 3 text component for displaying rich text content.
 * 
 * This composable provides a Material Design 3 styled text display with comprehensive rich text support:
 * 
 * **Material Design 3 Integration**:
 * - **Typography System**: Seamlessly integrates with Material 3 typography scale and font families
 * - **Color System**: Automatically adapts to Material 3 color schemes and theme changes
 * - **Content Color**: Respects Material 3 content color hierarchy and alpha values
 * - **Density Support**: Responds to Material density settings for optimal spacing
 * - **Theme Consistency**: Maintains visual consistency with other Material 3 components
 * 
 * **Rich Text Display Features**:
 * - **Text Formatting**: Displays bold, italic, underline, strikethrough, and colored text
 * - **Interactive Hyperlinks**: Clickable links with Material 3 color theming and hover states
 * - **List Rendering**: Properly formatted ordered and unordered lists with Material spacing
 * - **Code Spans**: Inline code display with Material 3 surface colors and typography
 * - **Custom Styles**: Support for custom rich span styles following Material guidelines
 * - **Paragraph Styling**: Text alignment, line height, and spacing following Material specs
 * 
 * **Typography and Layout**:
 * - **Font Scaling**: Respects system font size preferences and accessibility settings
 * - **Line Height**: Optimal line spacing for readability across different text sizes
 * - **Text Overflow**: Configurable overflow handling with ellipsis or clipping
 * - **Text Wrapping**: Intelligent word wrapping and line breaking
 * - **Baseline Alignment**: Proper baseline alignment with other Material components
 * 
 * **Accessibility and Interaction**:
 * - **Screen Reader Support**: Full accessibility support with proper content descriptions
 * - **High Contrast**: Adapts to high contrast accessibility settings
 * - **Touch Targets**: Appropriate touch target sizes for interactive elements
 * - **Keyboard Navigation**: Support for keyboard navigation of interactive content
 * - **Focus Management**: Proper focus handling for embedded interactive elements
 * 
 * **Performance and Platform**:
 * - **Efficient Rendering**: Optimized text rendering for smooth scrolling and performance
 * - **Cross-Platform**: Consistent appearance across Android, Desktop, iOS, and Web
 * - **Memory Optimization**: Efficient memory usage for large text documents
 * - **Image Integration**: Support for embedded images with Material loading states
 * 
 * **Common Use Cases**:
 * - **Content Display**: Articles, blog posts, and formatted content in Material apps
 * - **Message Display**: Chat messages and communication content with rich formatting
 * - **Help Content**: Documentation, tooltips, and instructional text
 * - **Card Content**: Rich text content within Material cards and surfaces
 * - **List Items**: Formatted text within Material list items and components
 * 
 * This component is ideal for displaying rich text content in Material Design 3 applications
 * where consistency with the design system and optimal user experience are priorities.
 */

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.mohamedrejeb.richeditor.model.ImageLoader
import com.mohamedrejeb.richeditor.model.LocalImageLoader
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichText

/**
 * High-level element that displays rich text and provides semantics / accessibility information.
 * [RichText] is a convenience wrapper around [Text] and [RichTextState] to allow for rich text.
 *
 * @param state [RichTextState] The rich text to be displayed.
 * @param modifier the [Modifier] to be applied to this layout node
 * @param color [Color] to apply to the text. If [Color.Unspecified], and [style] has no color set,
 * this will be [LocalContentColor].
 * @param fontSize the size of glyphs to use when painting the text. See [TextStyle.fontSize].
 * @param fontStyle the typeface variant to use when drawing the letters (e.g., italic).
 * See [TextStyle.fontStyle].
 * @param fontWeight the typeface thickness to use when painting the text (e.g., [FontWeight.Bold]).
 * @param fontFamily the font family to be used when rendering the text. See [TextStyle.fontFamily].
 * @param letterSpacing the amount of space to add between each letter.
 * See [TextStyle.letterSpacing].
 * @param textDecoration the decorations to paint on the text (e.g., an underline).
 * See [TextStyle.textDecoration].
 * @param textAlign the alignment of the text within the lines of the paragraph.
 * See [TextStyle.textAlign].
 * @param lineHeight line height for the [Paragraph] in [TextUnit] unit, e.g. SP or EM.
 * See [TextStyle.lineHeight].
 * @param overflow how visual overflow should be handled.
 * @param softWrap whether the text should break at soft line breaks. If false, the glyphs in the
 * text will be positioned as if there was unlimited horizontal space. If [softWrap] is false,
 * [overflow] and TextAlign may have unexpected effects.
 * @param maxLines an optional maximum number of lines for the text to span, wrapping if
 * necessary. If the text exceeds the given number of lines, it will be truncated according to
 * [overflow] and [softWrap]. If it is not null, then it must be greater than zero.
 * @param inlineContent a map storing composables that replaces certain ranges of the text, used to
 * insert composables into text layout. See [InlineTextContent].
 * @param onTextLayout callback that is executed when a new text layout is calculated. A
 * [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text. For example, to draw selection around the text.
 * @param style style configuration for the text such as color, font, line height etc.
 */
@Composable
public fun RichText(
    state: RichTextState,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    imageLoader: ImageLoader = LocalImageLoader.current,
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }
    // NOTE(text-perf-review): It might be worthwhile writing a bespoke merge implementation that
    // will avoid reallocating if all of the options here are the defaults
    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )
    BasicRichText(
        state = state,
        modifier = modifier,
        style = mergedStyle,
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        imageLoader = imageLoader,
    )
}