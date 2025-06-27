package com.kjcommunities

/**
 * KJDemoScreen - A comprehensive demonstration screen for the Rich Text Editor library.
 * 
 * This class serves as a Slack-like chat interface that demonstrates how to:
 * - Configure and use RichTextState with custom styling (green hyperlinks, code spans, list indentation)
 * - Implement a rich text editor with a custom toolbar panel
 * - Display formatted messages using the RichText component
 * - Handle Lexical JSON import/export functionality
 * - Copy Lexical JSON data to clipboard for debugging
 * - Manage multiple RichTextState instances for chat messages
 * - Integrate link dialog functionality for hyperlink creation/editing
 * 
 * The screen includes:
 * - Clean top navigation bar with back button
 * - Message display area showing formatted rich text content
 * - Rich text editor with formatting toolbar at the bottom
 * - Send functionality to add messages to the chat
 * 
 * This serves as a clean, production-ready demo interface for the rich text editor library.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send

import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.common.generated.resources.Res
import com.mohamedrejeb.richeditor.common.generated.resources.kajabi_logo
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalRichTextApi::class, ExperimentalResourceApi::class)
@Composable
fun KJDemoScreen(
    navigateBack: () -> Unit
) {
    val richTextState = rememberRichTextState()
    val clipboardManager = LocalClipboardManager.current

    val messages = remember {
        mutableStateListOf<RichTextState>()
    }

    val openLinkDialog = remember { mutableStateOf(false) }

    // Sample mention data from adding_at_mentions_support.md line 19
    val sampleMentionData = """{"root":{"children":[{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Alrighty, let's test some @ mentioning!","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"First, this one @ mentions someone who does ","type":"text","version":1},{"detail":0,"format":9,"mode":"normal","style":"","text":"not","type":"text","version":1},{"detail":0,"format":0,"mode":"normal","style":"","text":" exist, like @bob 👋 Hi Bob! ","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Next, this mentions someone who does exist, ","type":"text","version":1},{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Patrick (member)","type":"mention","version":1,"mentionName":"@Patrick (member)","mentionedUserId":"f6720dfe-0c11-44c9-bb4b-4d39a6cf4d03","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" ","type":"text","version":1},{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Etienne Tester 3","type":"mention","version":1,"mentionName":"@Etienne Tester 3","mentionedUserId":"90ebb878-7ed8-4b27-8ebc-ce95216cdb85","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" 😃 Hi Etienne and Patrick!","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Next, let's test @ mentioning people in an unordered list:","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"children":[{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Patrick (member)","type":"mention","version":1,"mentionName":"@Patrick (member)","mentionedUserId":"f6720dfe-0c11-44c9-bb4b-4d39a6cf4d03","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" ","type":"text","version":1}],"direction":null,"format":"","indent":0,"type":"listitem","version":1,"value":1},{"children":[{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Etienne Lawlor","type":"mention","version":1,"mentionName":"@Etienne Lawlor","mentionedUserId":"958abd34-7e2a-4327-9755-d7c02fb6a8fa","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" ","type":"text","version":1}],"direction":null,"format":"","indent":0,"type":"listitem","version":1,"value":2},{"children":[{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Etienne Tester 3","type":"mention","version":1,"mentionName":"@Etienne Tester 3","mentionedUserId":"90ebb878-7ed8-4b27-8ebc-ce95216cdb85","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" ","type":"text","version":1}],"direction":null,"format":"","indent":0,"type":"listitem","version":1,"value":3},{"children":[{"detail":1,"format":0,"mode":"segmented","style":"","text":"@Patrick (member)","type":"mention","version":1,"mentionName":"@Patrick (member)","mentionedUserId":"f6720dfe-0c11-44c9-bb4b-4d39a6cf4d03","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" ","type":"text","version":1}],"direction":null,"format":"","indent":0,"type":"listitem","version":1,"value":4}],"direction":null,"format":"","indent":0,"type":"list","version":1,"listType":"bullet","start":1,"tag":"ul"},{"children":[],"direction":null,"format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Finally let's test this dreaded ","type":"text","version":1},{"detail":1,"format":0,"mode":"segmented","style":"","text":"@everyone","type":"mention","version":1,"mentionName":"@everyone","mentionedUserId":"everyone","alphaName":"pgmacdesignscommunity"},{"detail":0,"format":0,"mode":"normal","style":"","text":" tag to see how much PN carnage it causes. ","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Also, FYI, I added in some emojis just to make this more complex. ","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Also, let's do a ","type":"text","version":1},{"detail":0,"format":1,"mode":"normal","style":"","text":"bold","type":"text","version":1},{"detail":0,"format":0,"mode":"normal","style":"","text":", ","type":"text","version":1},{"detail":0,"format":2,"mode":"normal","style":"","text":"italics","type":"text","version":1},{"detail":0,"format":0,"mode":"normal","style":"","text":", ","type":"text","version":1},{"detail":0,"format":8,"mode":"normal","style":"","text":"underline","type":"text","version":1},{"detail":0,"format":0,"mode":"normal","style":"","text":", ","type":"text","version":1},{"detail":0,"format":4,"mode":"normal","style":"","text":"strikethrough","type":"text","version":1},{"detail":0,"format":0,"mode":"normal","style":"","text":", and ","type":"text","version":1},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"hyperlink","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"link","version":1,"rel":null,"target":"_blank","title":null,"url":"https://www.google.com"},{"detail":0,"format":0,"mode":"normal","style":"","text":"... just because.","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""},{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"Also, here's an edit! ","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""}],"direction":"ltr","format":"","indent":0,"type":"root","version":1}}"""

    LaunchedEffect(Unit) {
        richTextState.config.linkColor = Color(0xFF00C851) // Green color for links
        richTextState.config.linkTextDecoration = TextDecoration.None
        richTextState.config.codeSpanColor = Color(0xFFd7882d)
        richTextState.config.codeSpanBackgroundColor = Color.Transparent
        richTextState.config.codeSpanStrokeColor = Color(0xFF494b4d)
        richTextState.config.unorderedListIndent = 40
        richTextState.config.orderedListIndent = 50
        // Configure mention styling
        richTextState.config.mentionColor = Color(0xFF0084ff) // Blue color for mentions
        richTextState.config.mentionTextDecoration = TextDecoration.None
    }

    Box(
        modifier = Modifier
            .background(Color(0xFF1a1d21))
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                ) {
                    TopAppBar(
                        title = { Text("KJ Communities Demo") },
                        navigationIcon = {
                            IconButton(
                                onClick = navigateBack
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    // Create a new RichTextState with the sample data and add it as a sent message
                                    val sampleMessageState = RichTextState()
                                    // Apply the same configuration as the main state
                                    sampleMessageState.config.linkColor = Color(0xFF00C851)
                                    sampleMessageState.config.linkTextDecoration = TextDecoration.None
                                    sampleMessageState.config.codeSpanColor = Color(0xFFd7882d)
                                    sampleMessageState.config.codeSpanBackgroundColor = Color.Transparent
                                    sampleMessageState.config.codeSpanStrokeColor = Color(0xFF494b4d)
                                    sampleMessageState.config.unorderedListIndent = 40
                                    sampleMessageState.config.orderedListIndent = 50
                                    sampleMessageState.config.mentionColor = Color(0xFF0084ff)
                                    sampleMessageState.config.mentionTextDecoration = TextDecoration.None
                                    // Set the lexical text
                                    sampleMessageState.setLexicalText(sampleMentionData)
                                    messages.add(sampleMessageState)
                                }
                            ) {
                                Icon(
                                    Icons.Outlined.PlayArrow,
                                    contentDescription = "Send Sample Mentions",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF1a1d21),
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White,
                            actionIconContentColor = Color.White
                        )
                    )

                    HorizontalDivider(color = Color(0xFFCBCCCD))
                }
            },
            containerColor = Color(0xFF1a1d21),
            modifier = Modifier
                .fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .windowInsetsPadding(WindowInsets.ime)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(messages) { message ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White)
                                ) {
                                    Image(
                                        painterResource(Res.drawable.kajabi_logo),
                                        contentDescription = "Kajabi Logo",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(6.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "John Doe",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    RichText(
                                        state = message,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF222528))
                            .border(1.dp, Color(0xFFCBCCCD), RoundedCornerShape(10.dp))
                    ) {
                        KJDemoPanel(
                            state = richTextState,
                            openLinkDialog = openLinkDialog,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp)
                        )

                        RichTextEditor(
                            state = richTextState,
                            placeholder = {
                                Text(
                                    text = "Message #compose-rich-text-editor",
                                )
                            },
                            colors = RichTextEditorDefaults.richTextEditorColors(
                                textColor = Color(0xFFCBCCCD),
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                placeholderColor = Color.White.copy(alpha = .6f),
                            ),
                            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp, max = 200.dp)
                                .padding(8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(
                                        onClick = {
                                            val messageState = richTextState.copy()
                                            // Apply configuration to the copied message state
                                            messageState.config.linkColor = Color(0xFF00C851)
                                            messageState.config.linkTextDecoration = TextDecoration.None
                                            messageState.config.codeSpanColor = Color(0xFFd7882d)
                                            messageState.config.codeSpanBackgroundColor = Color.Transparent
                                            messageState.config.codeSpanStrokeColor = Color(0xFF494b4d)
                                            messageState.config.unorderedListIndent = 40
                                            messageState.config.orderedListIndent = 50
                                            messageState.config.mentionColor = Color(0xFF0084ff)
                                            messageState.config.mentionTextDecoration = TextDecoration.None
                                            messages.add(messageState)
                                            richTextState.clear()
                                        },
                                        enabled = true,
                                        role = Role.Button,
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF007a5a))
                                        .padding(6.dp)
                                )
                            }
                        }
                    }
                }

                if (openLinkDialog.value)
                    Dialog(
                        onDismissRequest = {
                            openLinkDialog.value = false
                        }
                    ) {
                        KJDemoLinkDialog(
                            state = richTextState,
                            openLinkDialog = openLinkDialog
                        )
                    }
            }
        }
    }
}