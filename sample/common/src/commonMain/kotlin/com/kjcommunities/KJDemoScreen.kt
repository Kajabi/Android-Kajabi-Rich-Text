package com.kjcommunities

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
import androidx.compose.material.icons.outlined.Download
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
import com.mohamedrejeb.richeditor.common.generated.resources.slack_logo
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

    LaunchedEffect(Unit) {
        richTextState.config.linkColor = Color(0xFF1d9bd1)
        richTextState.config.linkTextDecoration = TextDecoration.None
        richTextState.config.codeSpanColor = Color(0xFFd7882d)
        richTextState.config.codeSpanBackgroundColor = Color.Transparent
        richTextState.config.codeSpanStrokeColor = Color(0xFF494b4d)
        richTextState.config.unorderedListIndent = 40
        richTextState.config.orderedListIndent = 50
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
                            // Test icon 0: Run round-trip test
                            IconButton(
                                onClick = {
                                    try {
                                        println("🧪 Running Lexical Round-Trip Test...")
                                        val testResult = LexicalRoundTripTest.runTest()
                                        
                                        if (testResult.success) {
                                            println("✅ ROUND-TRIP TEST PASSED!")
                                            println("- Text matches: ${testResult.textMatches}")
                                            println("- Demo JSON works: ${testResult.demoJsonWorks}")
                                            println("- Heading styles work: ${testResult.headingStylesWork}")
                                            println("- Original length: ${testResult.originalTextLength}")
                                            println("- Imported length: ${testResult.importedTextLength}")
                                            println("- JSON length: ${testResult.exportedJsonLength}")
                                        } else {
                                            println("❌ ROUND-TRIP TEST FAILED!")
                                            testResult.error?.let { println("Error: $it") }
                                            println("- Text matches: ${testResult.textMatches}")
                                            println("- Demo JSON works: ${testResult.demoJsonWorks}")
                                            println("- Heading styles work: ${testResult.headingStylesWork}")
                                        }
                                        
                                        println("Details: ${testResult.details}")
                                    } catch (e: Exception) {
                                        println("🧪 ❌ ERROR in round-trip test: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PlayArrow,
                                    contentDescription = "Run Round-Trip Test",
                                    tint = Color.Green
                                )
                            }
                            
                            // Test icon 1: Load minified JSON from resource file
                            IconButton(
                                onClick = {
                                    try {
                                        val minifiedJson = LexicalTestData.getMinifiedTestJson()
                                        
                                        println("🔥 JSON Length: ${minifiedJson.length}")
                                        println("🔥 Creating new RichTextState...")
                                        
                                        // Create a new RichTextState and load the JSON
                                        val testMessage = RichTextState()
                                        println("🔥 Calling setLexicalText...")
                                        testMessage.setLexicalText(minifiedJson)
                                        
                                        println("🔥 setLexicalText completed. Message text length: ${testMessage.annotatedString.text.length}")
                                        println("🔥 Message text: ${testMessage.annotatedString.text.take(100)}...")
                                        
                                        // Add it to the messages
                                        println("🔥 Current messages list size: ${messages.size}")
                                        messages.add(testMessage)
                                        println("🔥 Messages list size after add: ${messages.size}")
                                        
                                        println("🔥 ✅ MINIFIED JSON TEST COMPLETED SUCCESSFULLY")
                                    } catch (e: Exception) {
                                        println("🔥 ❌ ERROR in minified JSON test: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PlayArrow,
                                    contentDescription = "Load Minified JSON Test",
                                    tint = Color.White
                                )
                            }
                            
                            // Test icon 2: Load pretty-printed JSON from resource file
                            IconButton(
                                onClick = {
                                    try {
                                        val prettyJson = LexicalTestData.getPrettyTestJson()
                                        
                                        println("🌟 Pretty JSON Length: ${prettyJson.length}")
                                        println("🌟 Creating new RichTextState...")
                                        
                                        // Create a new RichTextState and load the JSON
                                        val testMessage = RichTextState()
                                        println("🌟 Calling setLexicalText...")
                                        testMessage.setLexicalText(prettyJson)
                                        
                                        println("🌟 setLexicalText completed. Message text length: ${testMessage.annotatedString.text.length}")
                                        println("🌟 Message text: ${testMessage.annotatedString.text.take(100)}...")
                                        
                                        // Add it to the messages
                                        println("🌟 Current messages list size: ${messages.size}")
                                        messages.add(testMessage)
                                        println("🌟 Messages list size after add: ${messages.size}")
                                        
                                        println("🌟 ✅ PRETTY JSON TEST COMPLETED SUCCESSFULLY")
                                    } catch (e: Exception) {
                                        println("🌟 ❌ ERROR in pretty JSON test: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Load Pretty JSON Test",
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
                                        painterResource(Res.drawable.slack_logo),
                                        contentDescription = "Slack Logo",
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
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Mohamed Rejeb",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        
                                        // Download/Copy Lexical JSON button
                                        IconButton(
                                            onClick = {
                                                try {
                                                    val lexicalJson = message.getLexicalText()
                                                    clipboardManager.setText(AnnotatedString(lexicalJson))
                                                    // TODO: Could add a toast/snackbar here to show success
                                                } catch (e: Exception) {
                                                    println("Error copying Lexical JSON: ${e.message}")
                                                }
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Download,
                                                contentDescription = "Copy Lexical JSON",
                                                tint = Color(0xFFCBCCCD),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
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
                                .padding(8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.End)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(
                                    onClick = {
                                        messages.add(richTextState.copy())
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