package com.kjcommunities

/**
 * KJRichTextEditorWithMentions - A wrapper around RichTextEditor that adds @ mention autocomplete functionality.
 * 
 * This component provides:
 * - Detection of "@" typing to trigger mention suggestions
 * - Automatic positioning of mention dropdown
 * - Insertion of mentions when users are selected
 * - Proper handling of text replacement and cursor positioning
 * - Optional debug logging for troubleshooting
 * 
 * Usage:
 * ```
 * KJRichTextEditorWithMentions(
 *     state = richTextState,
 *     users = mentionUsers,
 *     placeholder = { Text("Type a message...") },
 *     enableDebugLogging = true, // Enable for debugging image loading issues
 *     modifier = Modifier.fillMaxWidth()
 * )
 * ```
 * 
 * @param state The RichTextState that manages the editor content
 * @param users List of MentionUser objects that can be mentioned
 * @param modifier Modifier for the component
 * @param placeholder Optional placeholder composable when text is empty
 * @param colors Color configuration for the editor
 * @param textStyle Text styling for the editor
 * @param enableDebugLogging When true, prints detailed logs to help debug @ mention and image loading issues
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
import androidx.compose.ui.platform.LocalConfiguration
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
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle.Default,
    enableDebugLogging: Boolean = false
) {
    var showMentionDropdown by remember { mutableStateOf(false) }
    var mentionSearchText by remember { mutableStateOf("") }
    var mentionStartIndex by remember { mutableStateOf(-1) }
    var editorPosition by remember { mutableStateOf(Offset.Zero) }
    var editorHeight by remember { mutableStateOf(0) }
    var isFocused by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    // Monitor text changes to detect @ mentions
    LaunchedEffect(state.annotatedString.text, state.selection) {
        val text = state.annotatedString.text
        val cursorPosition = state.selection.end

        if (enableDebugLogging) {
            println("🔍 [MentionDetect] Text changed or cursor moved")
            println("📝 [MentionDetect] Current text: '$text'")
            println("🎯 [MentionDetect] Cursor position: $cursorPosition")
            println("🔍 [MentionDetect] Is focused: $isFocused")
        }

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

            if (enableDebugLogging) {
                println("📍 [MentionDetect] Found @ at index: $atIndex")
            }

            if (atIndex != -1) {
                // Extract text after @ symbol
                val searchText = text.substring(atIndex + 1, cursorPosition)
                
                if (enableDebugLogging) {
                    println("🔤 [MentionDetect] Search text: '$searchText'")
                }
                
                // Check if the search text is valid (no spaces or newlines)
                if (!searchText.contains(' ') && !searchText.contains('\n')) {
                    if (enableDebugLogging) {
                        println("✅ [MentionDetect] Valid search text - showing dropdown")
                    }
                    mentionSearchText = searchText
                    mentionStartIndex = atIndex
                    showMentionDropdown = true
                } else {
                    if (enableDebugLogging) {
                        println("❌ [MentionDetect] Invalid search text (contains spaces/newlines) - hiding dropdown")
                    }
                    showMentionDropdown = false
                }
            } else {
                if (enableDebugLogging) {
                    println("🚫 [MentionDetect] No @ symbol found - hiding dropdown")
                }
                showMentionDropdown = false
            }
        } else {
            if (enableDebugLogging) {
                println("🔒 [MentionDetect] Not focused or cursor at start - hiding dropdown")
            }
            showMentionDropdown = false
        }
        
        if (enableDebugLogging) {
            println("👁️ [MentionDetect] Dropdown visible: $showMentionDropdown")
        }
    }

    Box(modifier = modifier) {
        RichTextEditor(
            state = state,
            colors = colors,
            textStyle = textStyle,
            placeholder = placeholder,
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
                    editorHeight = coordinates.size.height
                }
        )

        // Mention dropdown popup
        if (showMentionDropdown && mentionSearchText.isNotEmpty()) {
            val dropdownHeight = 300.dp
            val dropdownHeightPx = with(density) { dropdownHeight.toPx() }
            val margin = with(density) { 8.dp.toPx() }
            
            // Always position dropdown above the text input area
            // Anchor it directly above the editor with a small margin
            val dropdownOffset = -(dropdownHeightPx.toInt() + margin.toInt())
            
            // Calculate available space above for the dropdown height
            // Allow it to extend up even if it overlaps the top panel
            val availableSpaceAbove = editorPosition.y
            val maxDropdownHeight = with(density) { 
                // Use all available space above, but ensure minimum of 150dp for usability
                val availableHeight = (availableSpaceAbove - margin * 2).toDp()
                maxOf(availableHeight, 150.dp).coerceAtMost(dropdownHeight)
            }

            Popup(
                offset = IntOffset(
                    x = 0,
                    y = dropdownOffset
                ),
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    clippingEnabled = false // Allow overlapping with top panel
                ),
                onDismissRequest = { 
                    showMentionDropdown = false 
                }
            ) {
                KJMentionDropdown(
                    users = users,
                    searchText = mentionSearchText,
                    maxHeight = maxDropdownHeight,
                    enableDebugLogging = enableDebugLogging,
                    onUserSelected = { user ->
                        insertMention(state, user, mentionStartIndex, mentionStartIndex + mentionSearchText.length + 1, enableDebugLogging)
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
    endIndex: Int,
    enableDebugLogging: Boolean = false
) {
    if (enableDebugLogging) {
        println("🏷️ [MentionInsert] Starting mention insertion for user: ${user.fullName}")
        println("📍 [MentionInsert] User ID: ${user.id}")
        println("🖼️ [MentionInsert] User image URL: ${user.imageUrl}")
        println("📝 [MentionInsert] Text range: $startIndex to $endIndex")
        println("⚡ [MentionInsert] Current text length: ${state.annotatedString.text.length}")
    }
    
    try {
        // Create the mention span style
        val mentionSpanStyle = RichSpanStyle.Mention(
            id = user.id,
            fullName = user.fullName,
            alphaName = RichSpanStyle.Mention.globalAlphaName
        )
        
        if (enableDebugLogging) {
            println("🎨 [MentionInsert] Created mention span style with alphaName: ${RichSpanStyle.Mention.globalAlphaName}")
        }

        // Replace the @ search text with the mention text + space
        val mentionText = "@${user.fullName}"
        val textRange = TextRange(startIndex, endIndex.coerceAtMost(state.annotatedString.text.length))
        
        if (enableDebugLogging) {
            println("🔄 [MentionInsert] Replacing text range $textRange with: '$mentionText '")
        }
        
        // Replace the text first with mention + space
        state.replaceTextRange(textRange, "$mentionText ")
        
        if (enableDebugLogging) {
            println("✅ [MentionInsert] Text replaced successfully")
        }
        
        // Apply the mention style to the newly inserted mention text (excluding the space)
        val newTextRange = TextRange(startIndex, startIndex + mentionText.length)
        if (enableDebugLogging) {
            println("🎯 [MentionInsert] Applying mention style to range: $newTextRange")
        }
        
        state.addRichSpan(mentionSpanStyle, newTextRange)
        
        if (enableDebugLogging) {
            println("🎉 [MentionInsert] Mention insertion completed successfully!")
        }
        
    } catch (e: Exception) {
        if (enableDebugLogging) {
            println("💥 [MentionInsert] Error during mention insertion: ${e.message}")
            println("🔍 [MentionInsert] Exception type: ${e.javaClass.simpleName}")
            e.printStackTrace()
        }
    }
} 