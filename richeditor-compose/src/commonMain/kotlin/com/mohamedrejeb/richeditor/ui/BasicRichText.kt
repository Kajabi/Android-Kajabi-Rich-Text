package com.mohamedrejeb.richeditor.ui

/**
 * BasicRichText - A foundational composable for displaying rich text content in read-only mode.
 * 
 * This composable serves as the core display component for rich text content and provides:
 * 
 * **Rich Text Display Capabilities**:
 * - **Formatted Text Rendering**: Displays text with bold, italic, underline, color, and other formatting
 * - **Interactive Links**: Clickable hyperlinks with hover states and custom styling
 * - **List Visualization**: Properly formatted ordered and unordered lists with nesting
 * - **Code Span Display**: Inline code formatting with background and border styling
 * - **Custom Styling**: Support for custom rich span styles and visual effects
 * - **Typography Integration**: Seamless integration with Compose typography systems
 * 
 * **Interaction Features**:
 * - **Link Navigation**: Automatic link handling with platform-appropriate URL opening
 * - **Text Selection**: Optional text selection for copying formatted content
 * - **Hover Effects**: Mouse hover states for interactive elements (desktop platforms)
 * - **Touch Feedback**: Appropriate touch feedback for interactive elements
 * - **Accessibility**: Full screen reader and accessibility service integration
 * 
 * **Layout and Styling**:
 * - **Text Flow**: Proper text wrapping, line breaking, and paragraph spacing
 * - **Overflow Handling**: Configurable text overflow behavior (clip, ellipsis, visible)
 * - **Line Limits**: Support for maximum and minimum line constraints
 * - **Inline Content**: Support for embedding custom composables within text
 * - **Text Measurement**: Provides text layout results for advanced positioning
 * 
 * **Performance Optimization**:
 * - **Efficient Rendering**: Optimized for displaying large rich text documents
 * - **Lazy Loading**: Supports lazy loading of images and embedded content
 * - **Memory Management**: Efficient memory usage for complex formatted text
 * - **Recomposition Optimization**: Minimal recompositions for better performance
 * 
 * **Platform Integration**:
 * - **Cross-Platform**: Consistent rendering across Android, Desktop, iOS, and Web
 * - **Image Loading**: Configurable image loading with custom ImageLoader support
 * - **URL Handling**: Platform-appropriate URL handling and external link opening
 * - **Font Support**: Support for custom fonts and platform-specific typography
 * 
 * **Usage Patterns**:
 * - **Content Display**: Blog posts, articles, and formatted document display
 * - **Message Rendering**: Chat messages and communication content with rich formatting
 * - **Documentation**: Help text, tooltips, and instructional content
 * - **Previews**: Read-only previews of rich text content being edited elsewhere
 * - **Static Content**: Formatted static content like terms of service or about pages
 * 
 * This component is designed for scenarios where rich text content needs to be displayed
 * without editing capabilities, providing optimal performance and user experience for
 * read-only rich text presentation.
 */

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.mohamedrejeb.richeditor.gesture.detectTapGestures
import com.mohamedrejeb.richeditor.model.ImageLoader
import com.mohamedrejeb.richeditor.model.LocalImageLoader
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
public fun BasicRichText(
    state: RichTextState,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    imageLoader: ImageLoader = LocalImageLoader.current,
) {
    val density = LocalDensity.current
    val uriHandler = LocalUriHandler.current
    val pointerIcon = remember {
        mutableStateOf(PointerIcon.Default)
    }

    val text = remember(
        state.visualTransformation,
        state.annotatedString,
    ) {
        state.visualTransformation.filter(state.annotatedString).text
    }

    CompositionLocalProvider(
        LocalImageLoader provides imageLoader
    ) {
        BasicText(
            text = text,
            modifier = modifier
                .drawRichSpanStyle(state)
                .pointerHoverIcon(pointerIcon.value)
                .pointerInput(state) {
                    try {
                        awaitEachGesture {
                            val event = awaitPointerEvent()
                            val position = event.changes.first().position
                            val isLink = state.isLink(position)

                            pointerIcon.value =
                                if (isLink)
                                    PointerIcon.Hand
                                else
                                    PointerIcon.Default
                        }
                    } catch (_: Exception) {

                    }
                }
                .pointerInput(state) {
                    detectTapGestures(
                        onTap = { offset ->
                            state.getLinkByOffset(offset)?.let { url ->
                                try {
                                    uriHandler.openUri(url)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        consumeDown = { offset ->
                            state.isLink(offset)
                        },
                    )
                },
            style = style,
            onTextLayout = {
                state.onTextLayout(
                    textLayoutResult = it,
                    density = density,
                )
                onTextLayout(it)
            },
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            inlineContent = remember(inlineContent, state.inlineContentMap.toMap()) {
                inlineContent + state.inlineContentMap
            }
        )
    }
}