package com.kjcommunities

/**
 * KJRichTextEditorWithMentions - A wrapper around RichTextEditor that adds @ mention autocomplete functionality.
 * 
 * This component provides:
 * - Detection of "@" typing to trigger mention suggestions
 * - Automatic positioning of mention dropdown
 * - Insertion of mentions when users are selected
 * - Proper handling of text replacement and cursor positioning
 * 
 * Usage:
 * ```
 * KJRichTextEditorWithMentions(
 *     state = richTextState,
 *     users = mentionUsers,
 *     placeholder = { Text("Type a message...") },
 *     modifier = Modifier.fillMaxWidth()
 * )
 * ```
 */

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.RichSpanStyle
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorColors
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi

@OptIn(ExperimentalRichTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KJRichTextEditorWithMentions(
    state: RichTextState,
    users: List<MentionUser>,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    colors: RichTextEditorColors = RichTextEditorDefaults.richTextEditorColors(),
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle.Default
) {
    var showMentionDropdown by remember { mutableStateOf(false) }
    var mentionSearchText by remember { mutableStateOf("") }
    var mentionStartIndex by remember { mutableStateOf(-1) }
    var editorPosition by remember { mutableStateOf(Offset.Zero) }
    var isFocused by remember { mutableStateOf(false) }

    // Monitor text changes to detect @ mentions
    LaunchedEffect(state.annotatedString.text, state.selection) {
        val text = state.annotatedString.text
        val cursorPosition = state.selection.end

        if (isFocused && cursorPosition > 0) {
            // Find the last @ symbol before the cursor
            var atIndex = -1
            for (i in cursorPosition - 1 downTo 0) {
                val char = text.getOrNull(i)
                if (char == '@') {
                    atIndex = i
                    break
                } else if (char == ' ' || char == '\n') {
                    // Stop if we hit whitespace without finding @
                    break
                }
            }

            if (atIndex != -1) {
                // Extract text after @ symbol
                val searchText = text.substring(atIndex + 1, cursorPosition)
                
                // Check if the search text is valid (no spaces or newlines)
                if (!searchText.contains(' ') && !searchText.contains('\n')) {
                    mentionSearchText = searchText
                    mentionStartIndex = atIndex
                    showMentionDropdown = true
                } else {
                    showMentionDropdown = false
                }
            } else {
                showMentionDropdown = false
            }
        } else {
            showMentionDropdown = false
        }
    }

    Box(modifier = modifier) {
        RichTextEditor(
            state = state,
            placeholder = placeholder,
            colors = colors,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (!isFocused) {
                        showMentionDropdown = false
                    }
                }
                .onGloballyPositioned { coordinates ->
                    editorPosition = coordinates.positionInWindow()
                }
        )

        // Mention dropdown popup
        if (showMentionDropdown && mentionSearchText.isNotEmpty()) {
            Popup(
                offset = IntOffset(
                    x = 0,
                    y = with(LocalDensity.current) { 40.dp.roundToPx() } // Position below current line
                ),
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    clippingEnabled = false
                ),
                onDismissRequest = { 
                    showMentionDropdown = false 
                }
            ) {
                KJMentionDropdown(
                    users = users,
                    searchText = mentionSearchText,
                    onUserSelected = { user ->
                        insertMention(state, user, mentionStartIndex, mentionStartIndex + mentionSearchText.length + 1)
                        showMentionDropdown = false
                    },
                    onDismiss = { 
                        showMentionDropdown = false 
                    }
                )
            }
        }
    }
}

/**
 * Inserts a mention into the rich text state, replacing the @ search text
 */
private fun insertMention(
    state: RichTextState,
    user: MentionUser,
    startIndex: Int,
    endIndex: Int
) {
    // Create the mention span style
    val mentionSpanStyle = RichSpanStyle.Mention(
        id = user.id,
        fullName = user.fullName,
        alphaName = RichSpanStyle.Mention.globalAlphaName
    )

    // Replace the @ search text with the mention text + space
    val mentionText = "@${user.fullName}"
    val textRange = TextRange(startIndex, endIndex.coerceAtMost(state.annotatedString.text.length))
    
    // Replace the text first with mention + space
    state.replaceTextRange(textRange, "$mentionText ")
    
    // Apply the mention style to the newly inserted mention text (excluding the space)
    val newTextRange = TextRange(startIndex, startIndex + mentionText.length)
    state.addRichSpan(mentionSpanStyle, newTextRange)
} 