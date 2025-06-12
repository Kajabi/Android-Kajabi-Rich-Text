package com.mohamedrejeb.richeditor.parser.lexical

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichSpanStyle
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.paragraph.RichParagraph
import com.mohamedrejeb.richeditor.paragraph.type.*
import com.mohamedrejeb.richeditor.parser.RichTextStateParser
import androidx.compose.ui.util.fastForEach

internal object RichTextStateLexicalParser : RichTextStateParser<String> {

    @OptIn(ExperimentalRichTextApi::class)
    override fun encode(input: String): RichTextState {
        // TODO: Implement if needed for converting Lexical JSON to RichTextState
        // For now, focus on decode (RichTextState -> Lexical JSON)
        return RichTextState()
    }

    @OptIn(ExperimentalRichTextApi::class)
    override fun decode(richTextState: RichTextState): String {
        return try {
            val lexicalRoot = convertRichTextStateToLexical(richTextState)
            serializeLexicalRootToJson(lexicalRoot)
        } catch (e: Exception) {
            // Log.d would be used on Android platform
            println("Error converting RichTextState to Lexical: ${e.message}")
            e.printStackTrace()
            // Return basic structure on error
            createBasicLexicalJson(richTextState.annotatedString.text)
        }
    }

    /**
     * Converts a RichTextState to a LexicalRoot structure
     */
    @OptIn(ExperimentalRichTextApi::class)
    internal fun convertRichTextStateToLexical(richTextState: RichTextState): LexicalRoot {
        val children = mutableListOf<LexicalNode>()
        
        try {
            richTextState.richParagraphList.fastForEach { paragraph ->
                try {
                    val lexicalNode = convertRichParagraphToLexical(paragraph)
                    if (lexicalNode != null) {
                        children.add(lexicalNode)
                    }
                } catch (e: Exception) {
                    println("Error converting paragraph: ${e.message}")
                    // Add as simple text paragraph on error
                    val textContent = extractTextFromParagraph(paragraph)
                    if (textContent.isNotEmpty()) {
                        children.add(
                            LexicalParagraphNode(
                                children = listOf(LexicalTextNode(text = textContent))
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("Error converting RichTextState: ${e.message}")
        }

        // Ensure we have at least one node
        if (children.isEmpty()) {
            children.add(
                LexicalParagraphNode(
                    children = listOf(LexicalTextNode(text = ""))
                )
            )
        }

        return LexicalRoot(
            root = LexicalRootNode(children = children)
        )
    }

    /**
     * Converts a RichParagraph to a LexicalNode
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertRichParagraphToLexical(paragraph: RichParagraph): LexicalNode? {
        return try {
            when (val paragraphType = paragraph.type) {
                is OrderedList -> {
                    convertListToLexical(paragraph, "number", "ol")
                }
                is UnorderedList -> {
                    convertListToLexical(paragraph, "bullet", "ul")
                }
                else -> {
                    // Check if this is a heading based on span styles
                    val headingTag = detectHeadingTag(paragraph)
                    if (headingTag != null) {
                        LexicalHeadingNode(
                            children = convertRichSpansToLexical(paragraph.children),
                            tag = headingTag
                        )
                    } else {
                        val children = convertRichSpansToLexical(paragraph.children)
                        if (children.isNotEmpty()) {
                            LexicalParagraphNode(children = children)
                        } else {
                            LexicalParagraphNode(
                                children = listOf(LexicalTextNode(text = ""))
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Error converting paragraph type: ${e.message}")
            null
        }
    }

    /**
     * Converts a list paragraph to a LexicalListNode
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertListToLexical(paragraph: RichParagraph, listType: String, tag: String): LexicalListNode {
        val listItems = mutableListOf<LexicalListItemNode>()
        
        try {
            val children = convertRichSpansToLexical(paragraph.children)
            if (children.isNotEmpty()) {
                listItems.add(
                    LexicalListItemNode(
                        children = children,
                        value = 1 // TODO: Extract actual list item number if available
                    )
                )
            }
        } catch (e: Exception) {
            println("Error converting list item: ${e.message}")
            // Add empty list item on error
            listItems.add(
                LexicalListItemNode(
                    children = listOf(LexicalTextNode(text = "")),
                    value = 1
                )
            )
        }

        return LexicalListNode(
            children = listItems,
            listType = listType,
            tag = tag
        )
    }

    /**
     * Converts RichSpans to LexicalNodes
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertRichSpansToLexical(richSpans: List<RichSpan>): List<LexicalNode> {
        val nodes = mutableListOf<LexicalNode>()
        
        try {
            richSpans.fastForEach { span ->
                try {
                    val lexicalNodes = convertRichSpanToLexical(span)
                    nodes.addAll(lexicalNodes)
                } catch (e: Exception) {
                    println("Error converting span: ${e.message}")
                    // Add as simple text on error
                    if (span.text.isNotEmpty()) {
                        nodes.add(LexicalTextNode(text = span.text))
                    }
                }
            }
        } catch (e: Exception) {
            println("Error converting spans: ${e.message}")
        }
        
        return nodes
    }

    /**
     * Converts a single RichSpan to LexicalNodes
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertRichSpanToLexical(richSpan: RichSpan): List<LexicalNode> {
        val nodes = mutableListOf<LexicalNode>()
        
        try {
            // Handle link spans
            val richSpanStyle = richSpan.richSpanStyle
            if (richSpanStyle is RichSpanStyle.Link) {
                val linkChildren = if (richSpan.children.isNotEmpty()) {
                    convertRichSpansToLexical(richSpan.children)
                } else {
                    listOf(LexicalTextNode(text = richSpan.text))
                }
                
                nodes.add(
                    LexicalLinkNode(
                        children = linkChildren,
                        url = richSpanStyle.url
                    )
                )
                return nodes
            }

            // Handle regular text with formatting
            if (richSpan.text.isNotEmpty()) {
                val formatFlags = calculateFormatFlags(richSpan.spanStyle)
                nodes.add(
                    LexicalTextNode(
                        text = richSpan.text,
                        formatFlags = formatFlags
                    )
                )
            }

            // Handle children
            if (richSpan.children.isNotEmpty()) {
                nodes.addAll(convertRichSpansToLexical(richSpan.children))
            }
        } catch (e: Exception) {
            println("Error converting individual span: ${e.message}")
            // Fallback to simple text
            if (richSpan.text.isNotEmpty()) {
                nodes.add(LexicalTextNode(text = richSpan.text))
            }
        }
        
        return nodes
    }

    /**
     * Calculates format flags based on SpanStyle
     * 0=normal, 1=bold, 2=italic, 4=strikethrough, 8=underline
     */
    private fun calculateFormatFlags(spanStyle: SpanStyle): Int {
        var format = 0
        
        try {
            if (spanStyle.fontWeight == FontWeight.Bold) {
                format = format or 1 // Bold flag
            }
            
            if (spanStyle.fontStyle == FontStyle.Italic) {
                format = format or 2 // Italic flag
            }
            
            spanStyle.textDecoration?.let { decoration ->
                if (decoration.contains(TextDecoration.LineThrough)) {
                    format = format or 4 // Strikethrough flag
                }
                if (decoration.contains(TextDecoration.Underline)) {
                    format = format or 8 // Underline flag
                }
            }
        } catch (e: Exception) {
            println("Error calculating format flags: ${e.message}")
        }
        
        return format
    }

    /**
     * Detects if a paragraph should be a heading based on span styles
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun detectHeadingTag(paragraph: RichParagraph): String? {
        return try {
            // TODO: Implement heading detection logic
            // For now, return null as headings aren't in the current toolbar
            null
        } catch (e: Exception) {
            println("Error detecting heading: ${e.message}")
            null
        }
    }

    /**
     * Extracts plain text from a paragraph for error fallback
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun extractTextFromParagraph(paragraph: RichParagraph): String {
        return try {
            val text = StringBuilder()
            paragraph.children.fastForEach { span ->
                text.append(extractTextFromSpan(span))
            }
            text.toString()
        } catch (e: Exception) {
            println("Error extracting text from paragraph: ${e.message}")
            ""
        }
    }

    /**
     * Extracts plain text from a span for error fallback
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun extractTextFromSpan(span: RichSpan): String {
        return try {
            val text = StringBuilder()
            text.append(span.text)
            span.children.fastForEach { child ->
                text.append(extractTextFromSpan(child))
            }
            text.toString()
        } catch (e: Exception) {
            println("Error extracting text from span: ${e.message}")
            span.text
        }
    }

    /**
     * Manually serializes a LexicalRoot to JSON string
     */
    private fun serializeLexicalRootToJson(lexicalRoot: LexicalRoot): String {
        return try {
            val jsonBuilder = StringBuilder()
            jsonBuilder.append("{\"root\":")
            jsonBuilder.append(serializeLexicalRootNodeToJson(lexicalRoot.root))
            jsonBuilder.append("}")
            jsonBuilder.toString()
        } catch (e: Exception) {
            println("Error serializing LexicalRoot to JSON: ${e.message}")
            createBasicLexicalJson("")
        }
    }

    /**
     * Manually serializes a LexicalRootNode to JSON string
     */
    private fun serializeLexicalRootNodeToJson(rootNode: LexicalRootNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        rootNode.children.forEachIndexed { index, node ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(node))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${rootNode.direction}\",")
        jsonBuilder.append("\"format\":\"${rootNode.format}\",")
        jsonBuilder.append("\"indent\":${rootNode.indent},")
        jsonBuilder.append("\"type\":\"${rootNode.type}\",")
        jsonBuilder.append("\"version\":${rootNode.version}")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    /**
     * Manually serializes a LexicalNode to JSON string
     */
    private fun serializeLexicalNodeToJson(node: LexicalNode): String {
        return when (node) {
            is LexicalTextNode -> serializeTextNodeToJson(node)
            is LexicalParagraphNode -> serializeParagraphNodeToJson(node)
            is LexicalHeadingNode -> serializeHeadingNodeToJson(node)
            is LexicalLinkNode -> serializeLinkNodeToJson(node)
            is LexicalListNode -> serializeListNodeToJson(node)
            is LexicalListItemNode -> serializeListItemNodeToJson(node)
        }
    }

    private fun serializeTextNodeToJson(node: LexicalTextNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"detail\":${node.detail},")
        jsonBuilder.append("\"format\":${node.formatFlags},")
        jsonBuilder.append("\"mode\":\"${node.mode}\",")
        jsonBuilder.append("\"style\":\"${node.style}\",")
        jsonBuilder.append("\"text\":\"${escapeJsonString(node.text)}\",")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version}")
        jsonBuilder.append("}")
        return jsonBuilder.toString()
    }

    private fun serializeParagraphNodeToJson(node: LexicalParagraphNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        node.children.forEachIndexed { index, child ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(child))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${node.direction}\",")
        jsonBuilder.append("\"format\":\"${node.format}\",")
        jsonBuilder.append("\"indent\":${node.indent},")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version},")
        jsonBuilder.append("\"textFormat\":${node.textFormat},")
        jsonBuilder.append("\"textStyle\":\"${node.textStyle}\"")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    private fun serializeHeadingNodeToJson(node: LexicalHeadingNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        node.children.forEachIndexed { index, child ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(child))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${node.direction}\",")
        jsonBuilder.append("\"format\":\"${node.format}\",")
        jsonBuilder.append("\"indent\":${node.indent},")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version},")
        jsonBuilder.append("\"tag\":\"${node.tag}\"")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    private fun serializeLinkNodeToJson(node: LexicalLinkNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        node.children.forEachIndexed { index, child ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(child))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${node.direction}\",")
        jsonBuilder.append("\"format\":\"${node.format}\",")
        jsonBuilder.append("\"indent\":${node.indent},")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version},")
        jsonBuilder.append("\"rel\":\"${node.rel}\",")
        jsonBuilder.append("\"target\":\"${node.target}\",")
        if (node.title != null) {
            jsonBuilder.append("\"title\":\"${escapeJsonString(node.title)}\",")
        }
        jsonBuilder.append("\"url\":\"${escapeJsonString(node.url)}\"")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    private fun serializeListNodeToJson(node: LexicalListNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        node.children.forEachIndexed { index, child ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(child))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${node.direction}\",")
        jsonBuilder.append("\"format\":\"${node.format}\",")
        jsonBuilder.append("\"indent\":${node.indent},")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version},")
        jsonBuilder.append("\"listType\":\"${node.listType}\",")
        jsonBuilder.append("\"start\":${node.start},")
        jsonBuilder.append("\"tag\":\"${node.tag}\"")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    private fun serializeListItemNodeToJson(node: LexicalListItemNode): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("{")
        jsonBuilder.append("\"children\":[")
        
        node.children.forEachIndexed { index, child ->
            if (index > 0) jsonBuilder.append(",")
            jsonBuilder.append(serializeLexicalNodeToJson(child))
        }
        
        jsonBuilder.append("],")
        jsonBuilder.append("\"direction\":\"${node.direction}\",")
        jsonBuilder.append("\"format\":\"${node.format}\",")
        jsonBuilder.append("\"indent\":${node.indent},")
        jsonBuilder.append("\"type\":\"${node.type}\",")
        jsonBuilder.append("\"version\":${node.version},")
        jsonBuilder.append("\"value\":${node.value}")
        jsonBuilder.append("}")
        
        return jsonBuilder.toString()
    }

    /**
     * Escapes special characters in JSON strings
     */
    private fun escapeJsonString(text: String): String {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    /**
     * Creates a basic Lexical JSON structure for error fallback
     */
    private fun createBasicLexicalJson(text: String): String {
        return """{"root":{"children":[{"children":[{"detail":0,"format":0,"mode":"normal","style":"","text":"${escapeJsonString(text)}","type":"text","version":1}],"direction":"ltr","format":"","indent":0,"type":"paragraph","version":1,"textFormat":0,"textStyle":""}],"direction":"ltr","format":"","indent":0,"type":"root","version":1}}"""
    }
} 