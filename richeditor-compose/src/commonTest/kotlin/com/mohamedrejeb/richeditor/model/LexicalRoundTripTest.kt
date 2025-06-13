package com.mohamedrejeb.richeditor.model

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

/**
 * Test to verify that lexical data round-trip works correctly.
 * This ensures that what's copied when we copy the lexical data matches 
 * what is being used to set when we use the top right buttons in the KJ Communities test activity.
 */
class LexicalRoundTripTest {
    
    @OptIn(ExperimentalRichTextApi::class)
    @Test
    fun testLexicalRoundTrip() {
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
        
        println("🧪 Lexical Round-Trip Test Results:")
        println("Original text length: ${originalText.length}")
        println("Imported text length: ${importedText.length}")
        println("JSON length: ${exportedJson.length}")
        println("Text matches: ${originalText == importedText}")
        
        // Verify text content matches
        assertEquals(originalText, importedText, "Text content should match after round-trip")
        
        // Verify JSON structure contains headings
        assertTrue(exportedJson.contains("\"type\":\"heading\""), "JSON should contain heading nodes")
        assertTrue(exportedJson.contains("\"tag\":\"h1\""), "JSON should contain H1 tags")
        assertTrue(exportedJson.contains("\"tag\":\"h2\""), "JSON should contain H2 tags")
        assertTrue(exportedJson.contains("This is a heading 1"), "JSON should contain H1 text")
        assertTrue(exportedJson.contains("While this is heading 2"), "JSON should contain H2 text")
        
        println("✅ All assertions passed!")
    }
    
    @OptIn(ExperimentalRichTextApi::class)
    @Test
    fun testDemoJsonCompatibility() {
        // Test with the exact minified JSON from KJDemoScreen.kt
        val demoJson = "{\"root\":{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"We're going to test some rich text!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"First, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":1,\"mode\":\"normal\",\"style\":\"\",\"text\":\"bold text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Next, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":2,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Italic text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\". \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a heading 1.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h1\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"While this is heading 2.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h2\"},{\"children\":[{\"detail\":0,\"format\":4,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is strikethrough\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":4,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":8,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is underline\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"paragraph\",\"version\":1,\"textFormat\":8,\"textStyle\":\"\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"root\",\"version\":1}}"
        
        val demoState = RichTextState()
        demoState.setLexicalText(demoJson)
        
        // Re-export and check if it works
        val reExported = demoState.getLexicalText()
        
        println("🎯 Demo JSON Compatibility Test:")
        println("Demo state text length: ${demoState.annotatedString.text.length}")
        println("Re-exported JSON length: ${reExported.length}")
        
        // Basic validation - should contain heading nodes
        assertTrue(reExported.contains("\"type\":\"heading\""), "Re-exported JSON should contain heading nodes")
        assertTrue(reExported.contains("\"tag\":\"h1\""), "Re-exported JSON should contain H1 tags")
        assertTrue(reExported.contains("\"tag\":\"h2\""), "Re-exported JSON should contain H2 tags")
        
        // Verify the text content is preserved
        val expectedText = demoState.annotatedString.text
        assertTrue(expectedText.contains("We're going to test some rich text!"), "Should contain intro text")
        assertTrue(expectedText.contains("This is a heading 1"), "Should contain H1 text")
        assertTrue(expectedText.contains("While this is heading 2"), "Should contain H2 text")
        assertTrue(expectedText.contains("bold text"), "Should contain bold text")
        assertTrue(expectedText.contains("Italic text"), "Should contain italic text")
        
        println("✅ Demo JSON compatibility verified!")
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
} 