package com.mohamedrejeb.richeditor.model

/**
 * RichTextConfig - Configuration management class for customizing Rich Text Editor appearance and behavior.
 * 
 * This class provides centralized configuration for all visual and behavioral aspects of the rich text editor:
 * 
 * **Link Styling Configuration**:
 * - `linkColor`: Sets the color for hyperlink text (default: Color.Blue)
 * - `linkTextDecoration`: Controls link text decoration like underlines (default: TextDecoration.Underline)
 * 
 * **Code Span Styling Configuration**:
 * - `codeSpanColor`: Text color for inline code spans (default: Color.Unspecified)
 * - `codeSpanBackgroundColor`: Background color for code spans (default: Color.Transparent)
 * - `codeSpanStrokeColor`: Border/stroke color for code spans (default: Color.LightGray)
 * 
 * **Mention Styling Configuration**:
 * - `mentionColor`: Sets the color for mention text (default: Color.Blue)
 * - `mentionTextDecoration`: Controls mention text decoration like underlines (default: TextDecoration.None)
 * 
 * **List Indentation Configuration**:
 * - `listIndent`: Universal indentation for both ordered and unordered lists (default: 38)
 * - `orderedListIndent`: Specific indentation for numbered lists (default: 38)
 * - `unorderedListIndent`: Specific indentation for bullet lists (default: 38)
 * 
 * **List Style Type Configuration**:
 * - `orderedListStyleType`: Numbering style for ordered lists (1,2,3 vs i,ii,iii vs A,B,C)
 * - `unorderedListStyleType`: Bullet style for unordered lists (•, ◦, ▪)
 * 
 * **Editor Behavior Configuration**:
 * - `preserveStyleOnEmptyLine`: Whether formatting persists on empty lines (default: true)
 * - `exitListOnEmptyItem`: Whether pressing Enter on empty list item exits the list (default: true)
 * 
 * **Key Features**:
 * - **Reactive Updates**: All configuration changes automatically trigger UI updates
 * - **Nested List Support**: Different styles for different nesting levels
 * - **Cross-Platform Consistency**: Same configuration works across all supported platforms
 * - **Runtime Modification**: Configuration can be changed dynamically during editor usage
 * 
 * **Usage Patterns**:
 * - Access via `richTextState.config` property
 * - Modify properties directly to see immediate visual changes
 * - Commonly used for theming and customizing editor appearance
 * - Essential for implementing custom formatting toolbars and style controls
 * 
 * This configuration system enables developers to create rich text editors that match their
 * application's design system and user experience requirements.
 */

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.paragraph.type.OrderedListStyleType
import com.mohamedrejeb.richeditor.paragraph.type.UnorderedListStyleType

public class RichTextConfig internal constructor(
    private val updateText: () -> Unit,
) {
    public var linkColor: Color = Color.Blue
        set(value) {
            field = value
            updateText()
        }

    public var linkTextDecoration: TextDecoration = TextDecoration.Underline
        set(value) {
            field = value
            updateText()
        }

    public var codeSpanColor: Color = Color.Unspecified
        set(value) {
            field = value
            updateText()
        }

    public var codeSpanBackgroundColor: Color = Color.Transparent
        set(value) {
            field = value
            updateText()
        }

    public var codeSpanStrokeColor: Color = Color.LightGray
        set(value) {
            field = value
            updateText()
        }

    public var mentionColor: Color = Color.Blue
        set(value) {
            field = value
            updateText()
        }

    public var mentionTextDecoration: TextDecoration = TextDecoration.None
        set(value) {
            field = value
            updateText()
        }

    /**
     * The indent for ordered lists.
     */
    public var orderedListIndent: Int = DefaultListIndent
        set(value) {
            field = value
            updateText()
        }

    /**
     * The indent for unordered lists.
     */
    public var unorderedListIndent: Int = DefaultListIndent
        set(value) {
            field = value
            updateText()
        }

    /**
     * The indent for both ordered and unordered lists.
     *
     * This property is a shortcut for setting both [orderedListIndent] and [unorderedListIndent].
     */
    public var listIndent: Int = DefaultListIndent
        get() {
            if (orderedListIndent == unorderedListIndent)
                field = orderedListIndent

            return field
        }
        set(value) {
            field = value
            orderedListIndent = value
            unorderedListIndent = value
        }

    /**
     * The prefixes for unordered lists items.
     *
     * The prefixes are used in order if the list is nested.
     *
     * For example, if the list is nested twice, the first prefix is used for the first level,
     * the second prefix is used for the second level, and so on.
     *
     * If the list is nested more than the number of prefixes, the last prefix is used.
     *
     * The default prefixes are `•`, `◦`, and `▪`.
     */
    public var unorderedListStyleType: UnorderedListStyleType = DefaultUnorderedListStyleType
        set(value) {
            field = value
            updateText()
        }

    public var orderedListStyleType: OrderedListStyleType = DefaultOrderedListStyleType
        set(value) {
            field = value
            updateText()
        }

    /**
     * Whether to preserve the style when the line is empty.
     * The line can be empty when the user deletes all the characters
     * or when the user presses `enter` to create a new line.
     *
     * Default is `true`.
     */
    public var preserveStyleOnEmptyLine: Boolean = true

    /**
     * Whether to exit the list when pressing Enter on an empty list item.
     * When true, pressing Enter on an empty list item will convert it to a normal paragraph.
     * When false, pressing Enter on an empty list item will create a new list item.
     *
     * Default is `true`.
     */
    public var exitListOnEmptyItem: Boolean = true
}

internal const val DefaultListIndent = 38

internal val DefaultUnorderedListStyleType =
    UnorderedListStyleType.from("•", "◦", "▪")

internal val DefaultOrderedListStyleType: OrderedListStyleType =
    OrderedListStyleType.Multiple(
        OrderedListStyleType.Decimal,
        OrderedListStyleType.LowerRoman,
        OrderedListStyleType.LowerAlpha,
    )
