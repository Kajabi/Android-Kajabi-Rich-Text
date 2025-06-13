package com.kjcommunities

/**
 * KJDemoPanel - A custom formatting toolbar component for the Rich Text Editor.
 * 
 * This class demonstrates how to create a comprehensive formatting toolbar that provides:
 * - Text formatting buttons (Bold, Italic, Underline, Strikethrough)
 * - Hyperlink creation and editing functionality
 * - List formatting (Unordered/Bulleted and Ordered/Numbered lists)
 * - List indentation controls (Increase/Decrease list level)
 * - Heading formatting (H1 and H2 with custom icons)
 * - Visual separators between button groups
 * 
 * Key features:
 * - Uses custom icons for H1, H2, and indent/outdent operations
 * - Integrates with RichTextState to reflect current formatting state
 * - Shows selected state for active formatting options
 * - Handles enabled/disabled states for context-sensitive buttons (like list indentation)
 * - Opens link dialog for hyperlink management
 * 
 * The toolbar is optimized for Lexical JSON format compatibility, with non-supported
 * features (text alignment, font size, colors) commented out but preserved for reference.
 * 
 * This serves as a reference implementation for building custom rich text editor toolbars.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.FormatAlignRight
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.kjcommunities.icons.H1
import com.kjcommunities.icons.H2
import com.kjcommunities.icons.FormatIndentIncrease
import com.kjcommunities.icons.FormatIndentDecrease

@Composable
fun KJDemoPanel(
    state: RichTextState,
    openLinkDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        // Text alignment buttons removed - not supported in Lexical format
        /*
        item {
            KJDemoPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Left,
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                icon = Icons.AutoMirrored.Outlined.FormatAlignLeft
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Center
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
                icon = Icons.Outlined.FormatAlignCenter
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.addParagraphStyle(
                        ParagraphStyle(
                            textAlign = TextAlign.Right
                        )
                    )
                },
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                icon = Icons.AutoMirrored.Outlined.FormatAlignRight
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }
        */

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = Icons.Outlined.FormatBold
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = Icons.Outlined.FormatItalic
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = Icons.Outlined.FormatUnderlined
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = Icons.Outlined.FormatStrikethrough
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    openLinkDialog.value = true
                },
                isSelected = state.isLink,
                icon = Icons.Outlined.Link
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        // Custom font size button removed - not supported in Lexical format
        /*
        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 28.sp
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontSize == 28.sp,
                icon = Icons.Outlined.FormatSize
            )
        }
        */

        // Text color button removed - not supported in Lexical format
        /*
        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            color = Color.Red
                        )
                    )
                },
                isSelected = state.currentSpanStyle.color == Color.Red,
                icon = Icons.Filled.Circle,
                tint = Color.Red
            )
        }
        */

        // Background color button removed - not supported in Lexical format
        /*
        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            background = Color.Yellow
                        )
                    )
                },
                isSelected = state.currentSpanStyle.background == Color.Yellow,
                icon = Icons.Outlined.Circle,
                tint = Color.Yellow
            )
        }

        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }
        */

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleUnorderedList()
                },
                isSelected = state.isUnorderedList,
                icon = Icons.AutoMirrored.Outlined.FormatListBulleted,
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleOrderedList()
                },
                isSelected = state.isOrderedList,
                icon = Icons.Outlined.FormatListNumbered,
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.increaseListLevel()
                },
                enabled = state.canIncreaseListLevel,
                icon = Icons.Outlined.FormatIndentIncrease,
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.decreaseListLevel()
                },
                enabled = state.canDecreaseListLevel,
                icon = Icons.Outlined.FormatIndentDecrease,
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleH1()
                },
                isSelected = state.isH1,
                icon = Icons.Outlined.H1
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleH2()
                },
                isSelected = state.isH2,
                icon = Icons.Outlined.H2
            )
        }

        // Code span button removed - not supported in Lexical format
        /*
        item {
            Box(
                Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color(0xFF393B3D))
            )
        }

        item {
            KJDemoPanelButton(
                onClick = {
                    state.toggleCodeSpan()
                },
                isSelected = state.isCodeSpan,
                icon = Icons.Outlined.Code,
            )
        }
        */
    }
}