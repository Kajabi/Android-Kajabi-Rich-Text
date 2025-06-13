package com.kjcommunities

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import com.mohamedrejeb.richeditor.model.RichTextState

/**
 * Test to verify that lexical data round-trip works correctly.
 * This ensures that what's copied when we copy the lexical data matches 
 * what is being used to set when we use the top right buttons in the KJ Communities test activity.
 */
object LexicalRoundTripTest {
    
    // Define heading styles locally since internal ones aren't accessible
    private val H1Style = SpanStyle(fontSize = 2.em, fontWeight = FontWeight.Bold)
    private val H2Style = SpanStyle(fontSize = 1.5.em, fontWeight = FontWeight.Bold)
    
    fun runTest(): TestResult {
        return try {
            // Step 1: Create rich text content using UI methods
            val originalState = createTestContent()
            
            // Step 2: Export to Lexical JSON (same as copy button)
            val exportedJson = originalState.getLexicalText()
            
            // Step 3: Import back from Lexical JSON (same as test buttons)
            val importedState = RichTextState()
            importedState.setLexicalText(exportedJson)
            
            // Step 4: Compare results
            val originalText = originalState.annotatedString.text
            val importedText = importedState.annotatedString.text
            val textMatches = originalText == importedText
            
            // Step 5: Test with demo JSON
            val demoJsonWorks = testDemoJson()
            
            // Step 6: Verify JSON structure contains headings
            val jsonStructureValid = verifyJsonStructure(exportedJson)
            
            TestResult(
                success = textMatches && demoJsonWorks && jsonStructureValid,
                originalTextLength = originalText.length,
                importedTextLength = importedText.length,
                textMatches = textMatches,
                demoJsonWorks = demoJsonWorks,
                headingStylesWork = jsonStructureValid,
                exportedJsonLength = exportedJson.length,
                details = "Original: ${originalText.take(50)}...\nImported: ${importedText.take(50)}..."
            )
        } catch (e: Exception) {
            TestResult(
                success = false,
                error = e.message ?: "Unknown error"
            )
        }
    }
    
    private fun createTestContent(): RichTextState {
        val state = RichTextState()
        
        // Create content similar to TaskList.md reference
        state.setText("We're going to test some rich text!")
        
        // Add bold text
        state.addTextAfterSelection("\n\nFirst, we have ")
        val boldStart = state.annotatedString.text.length
        state.addTextAfterSelection("bold text")
        val boldEnd = state.annotatedString.text.length
        state.selection = TextRange(boldStart, boldEnd)
        state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
        state.addTextAfterSelection(".")
        
        // Add italic text
        state.addTextAfterSelection("\n\nNext, we have ")
        val italicStart = state.annotatedString.text.length
        state.addTextAfterSelection("Italic text")
        val italicEnd = state.annotatedString.text.length
        state.selection = TextRange(italicStart, italicEnd)
        state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
        state.addTextAfterSelection(". ")
        
        // Add H1 heading
        state.addTextAfterSelection("\n\n")
        val h1Start = state.annotatedString.text.length
        state.addTextAfterSelection("This is a heading 1.")
        val h1End = state.annotatedString.text.length
        state.selection = TextRange(h1Start, h1End)
        state.toggleH1()
        
        // Add H2 heading
        state.addTextAfterSelection("\n\n")
        val h2Start = state.annotatedString.text.length
        state.addTextAfterSelection("While this is heading 2.")
        val h2End = state.annotatedString.text.length
        state.selection = TextRange(h2Start, h2End)
        state.toggleH2()
        
        // Add strikethrough text
        state.addTextAfterSelection("\n\n")
        val strikeStart = state.annotatedString.text.length
        state.addTextAfterSelection("This is strikethrough")
        val strikeEnd = state.annotatedString.text.length
        state.selection = TextRange(strikeStart, strikeEnd)
        state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        state.addTextAfterSelection(".")
        
        // Add underline text
        state.addTextAfterSelection("\n\n")
        val underlineStart = state.annotatedString.text.length
        state.addTextAfterSelection("This is underline")
        val underlineEnd = state.annotatedString.text.length
        state.selection = TextRange(underlineStart, underlineEnd)
        state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        state.addTextAfterSelection(".")
        
        // Add final normal text
        state.addTextAfterSelection("\n\nFinally, back to regular, normal text. ")
        
        return state
    }
    
    private fun testDemoJson(): Boolean {
        return try {
            // Test with the exact minified JSON from KJDemoScreen.kt
            val demoJson = "{\"root\":{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"We're going to test some rich text!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"First, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":1,\"mode\":\"normal\",\"style\":\"\",\"text\":\"bold text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Next, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":2,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Italic text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\". \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a heading 1.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h1\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"While this is heading 2.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h2\"},{\"children\":[{\"detail\":0,\"format\":4,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is strikethrough\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":4,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":8,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is underline\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":8,\"textStyle\":\"\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"root\",\"version\":1}}"
            
            val demoState = RichTextState()
            demoState.setLexicalText(demoJson)
            
            // Re-export and check if it works
            val reExported = demoState.getLexicalText()
            
            // Basic validation - should contain heading nodes
            reExported.contains("\"type\":\"heading\"") && 
            reExported.contains("\"tag\":\"h1\"") && 
            reExported.contains("\"tag\":\"h2\"")
        } catch (e: Exception) {
            false
        }
    }
    
    private fun verifyJsonStructure(json: String): Boolean {
        return try {
            // Check if the JSON contains the expected heading structures
            json.contains("\"type\":\"heading\"") && 
            json.contains("\"tag\":\"h1\"") && 
            json.contains("\"tag\":\"h2\"") &&
            json.contains("This is a heading 1") &&
            json.contains("While this is heading 2")
        } catch (e: Exception) {
            false
        }
    }
}

data class TestResult(
    val success: Boolean,
    val originalTextLength: Int = 0,
    val importedTextLength: Int = 0,
    val textMatches: Boolean = false,
    val demoJsonWorks: Boolean = false,
    val headingStylesWork: Boolean = false,
    val exportedJsonLength: Int = 0,
    val details: String = "",
    val error: String? = null
) 