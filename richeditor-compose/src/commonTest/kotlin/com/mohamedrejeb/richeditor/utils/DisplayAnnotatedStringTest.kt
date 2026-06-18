package com.mohamedrejeb.richeditor.utils

import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Verifies [toDisplayAnnotatedString] turns the editor's space-joined paragraphs into a read-only
 * display string with real line breaks, without mutating the state or losing inline spans.
 */
@OptIn(ExperimentalRichTextApi::class)
class DisplayAnnotatedStringTest {

    private val multiParagraphJson = """
        {"root":{"children":[
          {"type":"paragraph","children":[{"type":"text","text":"First paragraph."}]},
          {"type":"paragraph","children":[{"type":"text","text":"Second paragraph.","format":1}]},
          {"type":"paragraph","children":[{"type":"text","text":"Third paragraph."}]}
        ]}}
    """.trimIndent()

    @Test
    fun toDisplayAnnotatedString_multiParagraph_insertsLineBreaks() {
        val state = RichTextState().apply { setLexicalText(multiParagraphJson) }

        val editor = state.annotatedString.text
        val display = state.toDisplayAnnotatedString().text

        assertFalse(editor.contains('\n'), "editor string joins paragraphs with spaces")
        assertEquals(
            "First paragraph.\nSecond paragraph.\nThird paragraph.",
            display,
            "display string should separate the three paragraphs with newlines",
        )
    }

    @Test
    fun toDisplayAnnotatedString_preservesInlineSpans() {
        val state = RichTextState().apply { setLexicalText(multiParagraphJson) }

        // The bold run on "Second paragraph." must survive the transform.
        assertTrue(state.toDisplayAnnotatedString().spanStyles.isNotEmpty())
    }

    @Test
    fun toDisplayAnnotatedString_doesNotMutateState() {
        val state = RichTextState().apply { setLexicalText(multiParagraphJson) }
        val before = state.annotatedString.text

        state.toDisplayAnnotatedString()

        assertEquals(before, state.annotatedString.text, "helper must not mutate the source state")
    }

    @Test
    fun toDisplayAnnotatedString_singleParagraph_returnsUnchanged() {
        val json = """{"root":{"children":[{"type":"paragraph","children":[{"type":"text","text":"Only one."}]}]}}"""
        val state = RichTextState().apply { setLexicalText(json) }

        val display = state.toDisplayAnnotatedString().text

        assertFalse(display.contains('\n'))
        assertEquals("Only one.", display)
    }

    @Test
    fun toDisplayAnnotatedString_customSeparator_isHonored() {
        val state = RichTextState().apply { setLexicalText(multiParagraphJson) }

        val display = state.toDisplayAnnotatedString(paragraphSeparator = "\n\n").text

        assertEquals("First paragraph.\n\nSecond paragraph.\n\nThird paragraph.", display)
    }
}
