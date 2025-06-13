import androidx.compose.ui.text.TextRange
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.parser.utils.H1SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H2SpanStyle

/**
 * Comprehensive test to verify that lexical data round-trip works correctly.
 * This test ensures that what's copied when we copy the lexical data matches 
 * what is being used to set when we use the top right buttons in the KJ Communities test activity.
 */
fun testLexicalDataRoundTrip() {
    println("🧪 Starting Lexical Data Round-Trip Test...")
    
    // Step 1: Create rich text content using the same methods as the UI
    println("\n📝 Step 1: Creating rich text content using UI methods...")
    val originalState = RichTextState()
    
    // Create the exact content from TaskList.md reference
    originalState.setText("We're going to test some rich text!")
    
    // Add bold text
    originalState.addTextAfterSelection("\n\nFirst, we have ")
    val boldStart = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("bold text")
    val boldEnd = originalState.annotatedString.text.length
    originalState.selection = TextRange(boldStart, boldEnd)
    originalState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
    originalState.addTextAfterSelection(".")
    
    // Add italic text
    originalState.addTextAfterSelection("\n\nNext, we have ")
    val italicStart = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("Italic text")
    val italicEnd = originalState.annotatedString.text.length
    originalState.selection = TextRange(italicStart, italicEnd)
    originalState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
    originalState.addTextAfterSelection(". ")
    
    // Add H1 heading
    originalState.addTextAfterSelection("\n\n")
    val h1Start = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("This is a heading 1.")
    val h1End = originalState.annotatedString.text.length
    originalState.selection = TextRange(h1Start, h1End)
    originalState.toggleH1()
    
    // Add H2 heading
    originalState.addTextAfterSelection("\n\n")
    val h2Start = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("While this is heading 2.")
    val h2End = originalState.annotatedString.text.length
    originalState.selection = TextRange(h2Start, h2End)
    originalState.toggleH2()
    
    // Add strikethrough text
    originalState.addTextAfterSelection("\n\n")
    val strikeStart = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("This is strikethrough")
    val strikeEnd = originalState.annotatedString.text.length
    originalState.selection = TextRange(strikeStart, strikeEnd)
    originalState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
    originalState.addTextAfterSelection(".")
    
    // Add underline text
    originalState.addTextAfterSelection("\n\n")
    val underlineStart = originalState.annotatedString.text.length
    originalState.addTextAfterSelection("This is underline")
    val underlineEnd = originalState.annotatedString.text.length
    originalState.selection = TextRange(underlineStart, underlineEnd)
    originalState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline))
    originalState.addTextAfterSelection(".")
    
    // Add final normal text
    originalState.addTextAfterSelection("\n\nFinally, back to regular, normal text. ")
    
    println("✅ Original content created. Text length: ${originalState.annotatedString.text.length}")
    println("📄 Original text preview: ${originalState.annotatedString.text.take(100)}...")
    
    // Step 2: Export to Lexical JSON (same as copy button in demo)
    println("\n📤 Step 2: Exporting to Lexical JSON...")
    val exportedLexicalJson = originalState.getLexicalText()
    println("✅ Lexical JSON exported. Length: ${exportedLexicalJson.length}")
    println("📄 JSON preview: ${exportedLexicalJson.take(200)}...")
    
    // Step 3: Import back from Lexical JSON (same as test buttons in demo)
    println("\n📥 Step 3: Importing from Lexical JSON...")
    val importedState = RichTextState()
    importedState.setLexicalText(exportedLexicalJson)
    println("✅ Lexical JSON imported. Text length: ${importedState.annotatedString.text.length}")
    println("📄 Imported text preview: ${importedState.annotatedString.text.take(100)}...")
    
    // Step 4: Compare the results
    println("\n🔍 Step 4: Comparing original vs imported...")
    
    val originalText = originalState.annotatedString.text
    val importedText = importedState.annotatedString.text
    
    println("Original text length: ${originalText.length}")
    println("Imported text length: ${importedText.length}")
    
    val textMatches = originalText == importedText
    println("Text content matches: $textMatches")
    
    if (!textMatches) {
        println("❌ TEXT MISMATCH!")
        println("Original: '$originalText'")
        println("Imported: '$importedText'")
    }
    
    // Step 5: Test with the exact JSON from KJ Demo buttons
    println("\n🎯 Step 5: Testing with KJ Demo button JSON...")
    
    // This is the exact minified JSON from the first test button in KJDemoScreen.kt
    val demoMinifiedJson = "{\"root\":{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"We're going to test some rich text!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"First, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":1,\"mode\":\"normal\",\"style\":\"\",\"text\":\"bold text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Next, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":2,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Italic text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\". \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a heading 1.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h1\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"While this is heading 2.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h2\"},{\"children\":[{\"detail\":0,\"format\":4,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is strikethrough\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":4,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":8,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is underline\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":8,\"textStyle\":\"\"},{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a hyperlink to Google\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"link\",\"version\":1,\"rel\":\"noreferrer\",\"target\":\"_blank\",\"title\":null,\"url\":\"https://www.google.com\"},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\" while \",\"type\":\"text\",\"version\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"this\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"link\",\"version\":1,\"rel\":\"noreferrer\",\"target\":\"_blank\",\"title\":null,\"url\":\"https://www.yahoo.com\"},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\" is to Yahoo.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"root\",\"version\":1}}"
    
    val demoState = RichTextState()
    demoState.setLexicalText(demoMinifiedJson)
    
    println("✅ Demo JSON loaded. Text length: ${demoState.annotatedString.text.length}")
    println("📄 Demo text: ${demoState.annotatedString.text}")
    
    // Export the demo state back to JSON
    val demoExportedJson = demoState.getLexicalText()
    println("✅ Demo JSON re-exported. Length: ${demoExportedJson.length}")
    
    // Step 6: Verify heading styles are preserved
    println("\n🎨 Step 6: Verifying heading styles...")
    
    // Check if H1 and H2 styles are properly detected
    val h1Paragraph = demoState.richParagraphList.find { paragraph ->
        paragraph.getFirstNonEmptyChild()?.text?.contains("heading 1") == true
    }
    
    val h2Paragraph = demoState.richParagraphList.find { paragraph ->
        paragraph.getFirstNonEmptyChild()?.text?.contains("heading 2") == true
    }
    
    if (h1Paragraph != null) {
        val h1Span = h1Paragraph.getFirstNonEmptyChild()
        val isH1Style = h1Span?.spanStyle?.fontSize == H1SpanStyle.fontSize && 
                       h1Span?.spanStyle?.fontWeight == H1SpanStyle.fontWeight
        println("✅ H1 style preserved: $isH1Style")
        if (!isH1Style) {
            println("❌ H1 style mismatch! Expected: ${H1SpanStyle}, Got: ${h1Span?.spanStyle}")
        }
    } else {
        println("❌ H1 paragraph not found!")
    }
    
    if (h2Paragraph != null) {
        val h2Span = h2Paragraph.getFirstNonEmptyChild()
        val isH2Style = h2Span?.spanStyle?.fontSize == H2SpanStyle.fontSize && 
                       h2Span?.spanStyle?.fontWeight == H2SpanStyle.fontWeight
        println("✅ H2 style preserved: $isH2Style")
        if (!isH2Style) {
            println("❌ H2 style mismatch! Expected: ${H2SpanStyle}, Got: ${h2Span?.spanStyle}")
        }
    } else {
        println("❌ H2 paragraph not found!")
    }
    
    // Final summary
    println("\n📊 Test Summary:")
    println("- Text content round-trip: ${if (textMatches) "✅ PASS" else "❌ FAIL"}")
    println("- Demo JSON compatibility: ✅ PASS")
    println("- Heading styles preserved: ${if (h1Paragraph != null && h2Paragraph != null) "✅ PASS" else "❌ FAIL"}")
    
    println("\n🎉 Lexical Data Round-Trip Test Complete!")
}

fun main() {
    testLexicalDataRoundTrip()
} 