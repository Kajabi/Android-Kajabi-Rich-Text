package com.mohamedrejeb.richeditor.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlin.math.max
import kotlin.math.min

/**
 * Builds a read-only display [AnnotatedString] in which block paragraphs are separated by a real
 * line break ([paragraphSeparator], default "\n") instead of the single space that
 * [RichTextState.annotatedString] uses.
 *
 * Why this exists: [RichTextState.annotatedString] is built for the *editor*. It joins paragraphs
 * with a space (see `updateRichParagraphList`) and encodes block breaks only as `ParagraphStyle`
 * ranges, which the library's own `RichText` / `BasicRichTextEditor` composables lay out as
 * separate lines. When that same AnnotatedString is handed to a plain `Text` / `BasicText` for
 * read-only display, the `ParagraphStyle` breaks are not honored and every paragraph runs together
 * on one line. This helper bakes hard line breaks so multi-paragraph content renders correctly in
 * any text component.
 *
 * It is a **pure transform**: it reads [RichTextState.annotatedString] and does NOT mutate the
 * state (the internal span builders rewrite `RichSpan.textRange`, so a read-only path must avoid
 * them). All inline span styles and string annotations are preserved; `ParagraphStyle` ranges are
 * intentionally dropped so the inserted [paragraphSeparator] is the single source of block
 * separation. Single-paragraph content is returned unchanged.
 */
public fun RichTextState.toDisplayAnnotatedString(
    paragraphSeparator: String = "\n",
): AnnotatedString {
    val source = annotatedString
    val paragraphs = source.paragraphStyles.sortedBy { it.start }
    if (paragraphs.size <= 1) return source

    return buildAnnotatedString {
        paragraphs.fastForEachIndexed { i, paragraph ->
            val newStart = length
            val start = paragraph.start
            val isLast = i == paragraphs.lastIndex
            // The editor appends one separator space after every non-final paragraph; it falls
            // inside that paragraph's range, so drop it here to avoid a trailing space before
            // our line break.
            val end =
                if (!isLast && paragraph.end > start && source.text[paragraph.end - 1] == ' ')
                    paragraph.end - 1
                else
                    paragraph.end

            append(source.text.substring(start, end))

            source.spanStyles.fastForEach { span ->
                val from = max(span.start, start)
                val to = min(span.end, end)
                if (from < to) {
                    addStyle(span.item, newStart + (from - start), newStart + (to - start))
                }
            }
            source.getStringAnnotations(start, end).fastForEach { annotation ->
                val from = max(annotation.start, start)
                val to = min(annotation.end, end)
                if (from < to) {
                    addStringAnnotation(
                        annotation.tag,
                        annotation.item,
                        newStart + (from - start),
                        newStart + (to - start),
                    )
                }
            }

            if (!isLast) append(paragraphSeparator)
        }
    }
}
