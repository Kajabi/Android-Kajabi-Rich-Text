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
        // Parse the JSON and convert to RichTextState
        // If there's an error, it will be caught by the caller
        println("🔄 LexicalParser.encode: Starting with input length ${input.length}")
        println("🔄 LexicalParser.encode: Input preview: ${input.take(100)}...")
        
        val lexicalRoot = parseLexicalJsonToRoot(input)
        println("🔄 LexicalParser.encode: Parsed root with ${lexicalRoot.root.children.size} children")
        
        val result = convertLexicalToRichTextState(lexicalRoot)
        println("🔄 LexicalParser.encode: Converted to RichTextState with text length ${result.annotatedString.text.length}")
        
        return result
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
     * Parses a Lexical JSON string into a LexicalRoot object
     */
    private fun parseLexicalJsonToRoot(jsonString: String): LexicalRoot {
        return try {
            println("🔍 parseLexicalJsonToRoot: Starting parse")
            // Simple JSON parsing - find the root object
            val trimmed = jsonString.trim()
            println("🔍 Trimmed length: ${trimmed.length}")
            if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
                throw IllegalArgumentException("Invalid JSON format")
            }

            // Extract the "root" field
            val rootStart = trimmed.indexOf("\"root\":")
            println("🔍 Root field found at position: $rootStart")
            if (rootStart == -1) {
                throw IllegalArgumentException("No root field found")
            }

            // Find the root object boundaries
            val rootValueStart = trimmed.indexOf("{", rootStart)
            println("🔍 Root object starts at position: $rootValueStart")
            if (rootValueStart == -1) {
                throw IllegalArgumentException("Root field is not an object")
            }

            val rootJson = extractJsonObject(trimmed, rootValueStart)
            println("🔍 Extracted root JSON length: ${rootJson.length}")
            println("🔍 Root JSON preview: ${rootJson.take(200)}...")
            
            val rootNode = parseRootNode(rootJson)
            println("🔍 Parsed root node with ${rootNode.children.size} children")
            
            LexicalRoot(root = rootNode)
        } catch (e: Exception) {
            println("🔍 ❌ Error parsing Lexical JSON: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Parses a root node JSON object
     */
    private fun parseRootNode(jsonString: String): LexicalRootNode {
        val children = mutableListOf<LexicalNode>()
        
        // Extract children array
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalRootNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = extractStringField(jsonString, "type") ?: "root",
            version = extractIntField(jsonString, "version") ?: 1
        )
    }

    /**
     * Parses an array of child nodes
     */
    private fun parseChildrenArray(arrayJson: String): List<LexicalNode> {
        println("🧩 parseChildrenArray: Starting with array length ${arrayJson.length}")
        val children = mutableListOf<LexicalNode>()
        
        try {
            val objects = extractJsonObjectsFromArray(arrayJson)
            println("🧩 Extracted ${objects.size} objects from array")
            objects.forEach { objectJson ->
                try {
                    println("🧩 Parsing object: ${objectJson.take(100)}...")
                    val node = parseNodeFromJson(objectJson)
                    if (node != null) {
                        println("🧩 Successfully parsed node: ${node::class.simpleName}")
                        children.add(node)
                    } else {
                        println("🧩 ⚠️ parseNodeFromJson returned null")
                    }
                } catch (e: Exception) {
                    println("🧩 ❌ Error parsing child node: ${e.message}")
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            println("🧩 ❌ Error parsing children array: ${e.message}")
            e.printStackTrace()
        }
        
        println("🧩 parseChildrenArray: Returning ${children.size} children")
        return children
    }

    /**
     * Parses a single node from JSON based on its type
     */
    private fun parseNodeFromJson(jsonString: String): LexicalNode? {
        return try {
            println("🎯 parseNodeFromJson: Input length ${jsonString.length}")
            println("🎯 parseNodeFromJson: Input preview: ${jsonString.take(200)}")
            
            val type = extractStringField(jsonString, "type")
            println("🎯 parseNodeFromJson: Extracted type = '$type'")
            if (type == null) return null
            
            val result = when (type) {
                "text" -> {
                    println("🎯 parseNodeFromJson: Creating text node")
                    parseTextNode(jsonString)
                }
                "paragraph" -> {
                    println("🎯 parseNodeFromJson: Creating paragraph node")
                    parseParagraphNode(jsonString)
                }
                "heading" -> {
                    println("🎯 parseNodeFromJson: Creating heading node")
                    parseHeadingNode(jsonString)
                }
                "link" -> {
                    println("🎯 parseNodeFromJson: Creating link node")
                    parseLinkNode(jsonString)
                }
                "list" -> {
                    println("🎯 parseNodeFromJson: Creating list node")
                    parseListNode(jsonString)
                }
                "listitem" -> {
                    println("🎯 parseNodeFromJson: Creating listitem node")
                    parseListItemNode(jsonString)
                }
                else -> {
                    println("🎯 ❌ parseNodeFromJson: Unknown node type: $type")
                    null
                }
            }
            
            println("🎯 parseNodeFromJson: Result = ${result?.let { it::class.simpleName } ?: "null"}")
            result
        } catch (e: Exception) {
            println("🎯 ❌ parseNodeFromJson: Error parsing node: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun parseTextNode(jsonString: String): LexicalTextNode {
        return LexicalTextNode(
            detail = extractIntField(jsonString, "detail") ?: 0,
            formatFlags = extractIntField(jsonString, "format") ?: 0,
            mode = extractStringField(jsonString, "mode") ?: "normal",
            style = extractStringField(jsonString, "style") ?: "",
            text = extractStringField(jsonString, "text") ?: "",
            type = "text",
            version = extractIntField(jsonString, "version") ?: 1
        )
    }

    private fun parseParagraphNode(jsonString: String): LexicalParagraphNode {
        val children = mutableListOf<LexicalNode>()
        
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalParagraphNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = "paragraph",
            version = extractIntField(jsonString, "version") ?: 1,
            textFormat = extractIntField(jsonString, "textFormat") ?: 0,
            textStyle = extractStringField(jsonString, "textStyle") ?: ""
        )
    }

    private fun parseHeadingNode(jsonString: String): LexicalHeadingNode {
        val children = mutableListOf<LexicalNode>()
        
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalHeadingNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = "heading",
            version = extractIntField(jsonString, "version") ?: 1,
            tag = extractStringField(jsonString, "tag") ?: "h1"
        )
    }

    private fun parseLinkNode(jsonString: String): LexicalLinkNode {
        val children = mutableListOf<LexicalNode>()
        
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalLinkNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = "link",
            version = extractIntField(jsonString, "version") ?: 1,
            rel = extractStringField(jsonString, "rel") ?: "noreferrer",
            target = extractStringField(jsonString, "target") ?: "_blank",
            title = extractStringField(jsonString, "title"),
            url = extractStringField(jsonString, "url") ?: ""
        )
    }

    private fun parseListNode(jsonString: String): LexicalListNode {
        val children = mutableListOf<LexicalNode>()
        
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalListNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = "list",
            version = extractIntField(jsonString, "version") ?: 1,
            listType = extractStringField(jsonString, "listType") ?: "bullet",
            start = extractIntField(jsonString, "start") ?: 1,
            tag = extractStringField(jsonString, "tag") ?: "ul"
        )
    }

    private fun parseListItemNode(jsonString: String): LexicalListItemNode {
        val children = mutableListOf<LexicalNode>()
        
        val childrenStart = jsonString.indexOf("\"children\":")
        if (childrenStart != -1) {
            val arrayStart = jsonString.indexOf("[", childrenStart)
            if (arrayStart != -1) {
                val childrenJson = extractJsonArray(jsonString, arrayStart)
                children.addAll(parseChildrenArray(childrenJson))
            }
        }

        return LexicalListItemNode(
            children = children,
            direction = extractStringField(jsonString, "direction") ?: "ltr",
            format = extractStringField(jsonString, "format") ?: "",
            indent = extractIntField(jsonString, "indent") ?: 0,
            type = "listitem",
            version = extractIntField(jsonString, "version") ?: 1,
            value = extractIntField(jsonString, "value") ?: 1
        )
    }

    /**
     * Converts a LexicalRoot back to RichTextState
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertLexicalToRichTextState(lexicalRoot: LexicalRoot): RichTextState {
        println("🔧 convertLexicalToRichTextState: Starting conversion")
        val richTextState = RichTextState()
        val richParagraphs = mutableListOf<RichParagraph>()
        
        try {
            println("🔧 Processing ${lexicalRoot.root.children.size} root children")
            lexicalRoot.root.children.fastForEach { node ->
                try {
                    println("🔧 Converting node type: ${node::class.simpleName}")
                    val paragraphs = convertLexicalNodeToRichParagraph(node)
                    println("🔧 Node converted to ${paragraphs.size} paragraphs")
                    richParagraphs.addAll(paragraphs)
                } catch (e: Exception) {
                    println("🔧 ❌ Error converting node to RichParagraph: ${e.message}")
                    e.printStackTrace()
                    // Add fallback paragraph with just text
                    richParagraphs.add(RichParagraph())
                }
            }
        } catch (e: Exception) {
            println("🔧 ❌ Error converting Lexical to RichTextState: ${e.message}")
            e.printStackTrace()
        }

        println("🔧 Total paragraphs created: ${richParagraphs.size}")

        // Ensure we have at least one paragraph
        if (richParagraphs.isEmpty()) {
            println("🔧 No paragraphs created, adding empty paragraph")
            richParagraphs.add(RichParagraph())
        }

        // Set the paragraphs
        richTextState.richParagraphList.clear()
        richTextState.richParagraphList.addAll(richParagraphs)
        
        println("🔧 Final RichTextState text preview: ${richTextState.annotatedString.text.take(100)}")
        return richTextState
    }

    /**
     * Converts a single LexicalNode to RichParagraph(s)
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertLexicalNodeToRichParagraph(node: LexicalNode): List<RichParagraph> {
        return when (node) {
            is LexicalParagraphNode -> {
                val richParagraph = RichParagraph()
                richParagraph.children.addAll(convertLexicalNodesToRichSpans(node.children, richParagraph))
                listOf(richParagraph)
            }
            
            is LexicalHeadingNode -> {
                // Convert headings to normal paragraphs (unsupported feature)
                val richParagraph = RichParagraph()
                richParagraph.children.addAll(convertLexicalNodesToRichSpans(node.children, richParagraph))
                listOf(richParagraph)
            }
            
            is LexicalListNode -> {
                val paragraphs = mutableListOf<RichParagraph>()
                node.children.fastForEach { listItem ->
                    if (listItem is LexicalListItemNode) {
                        val richParagraph = RichParagraph()
                        richParagraph.type = when (node.listType) {
                            "number" -> OrderedList(number = listItem.value)
                            else -> UnorderedList()
                        }
                        richParagraph.children.addAll(convertLexicalNodesToRichSpans(listItem.children, richParagraph))
                        paragraphs.add(richParagraph)
                    }
                }
                paragraphs
            }
            
            else -> {
                // Convert unknown nodes to simple paragraphs
                val richParagraph = RichParagraph()
                listOf(richParagraph)
            }
        }
    }

    /**
     * Converts LexicalNodes to RichSpans
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertLexicalNodesToRichSpans(nodes: List<LexicalNode>, paragraph: RichParagraph): List<RichSpan> {
        val richSpans = mutableListOf<RichSpan>()
        
        nodes.fastForEach { node ->
            try {
                val spans = convertLexicalNodeToRichSpans(node, paragraph)
                richSpans.addAll(spans)
            } catch (e: Exception) {
                println("Error converting node to RichSpan: ${e.message}")
                // Add fallback span
                richSpans.add(RichSpan(paragraph = paragraph))
            }
        }
        
        return richSpans
    }

    /**
     * Converts a single LexicalNode to RichSpan(s)
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun convertLexicalNodeToRichSpans(node: LexicalNode, paragraph: RichParagraph): List<RichSpan> {
        println("🎨 convertLexicalNodeToRichSpans: Converting ${node::class.simpleName}")
        return when (node) {
            is LexicalTextNode -> {
                println("🎨 convertLexicalNodeToRichSpans: Text node with text='${node.text}', format=${node.formatFlags}")
                val richSpan = RichSpan(paragraph = paragraph)
                richSpan.text = node.text
                richSpan.spanStyle = convertFormatFlagsToSpanStyle(node.formatFlags)
                println("🎨 convertLexicalNodeToRichSpans: Created span with text='${richSpan.text}'")
                listOf(richSpan)
            }
            
            is LexicalLinkNode -> {
                println("🎨 convertLexicalNodeToRichSpans: Link node with ${node.children.size} children")
                val richSpan = RichSpan(paragraph = paragraph)
                richSpan.richSpanStyle = RichSpanStyle.Link(url = node.url)
                
                // Add text from children
                val childSpans = convertLexicalNodesToRichSpans(node.children, paragraph)
                if (childSpans.isNotEmpty()) {
                    richSpan.text = childSpans.joinToString("") { it.text }
                }
                println("🎨 convertLexicalNodeToRichSpans: Link span text='${richSpan.text}'")
                
                listOf(richSpan)
            }
            
            else -> {
                println("🎨 convertLexicalNodeToRichSpans: Unknown node type ${node::class.simpleName}")
                // Unknown node type - return empty span
                listOf(RichSpan(paragraph = paragraph))
            }
        }
    }

    /**
     * Converts format flags back to SpanStyle
     * 0=normal, 1=bold, 2=italic, 4=strikethrough, 8=underline
     */
    private fun convertFormatFlagsToSpanStyle(formatFlags: Int): SpanStyle {
        var spanStyle = SpanStyle()
        
        try {
            if ((formatFlags and 1) != 0) { // Bold
                spanStyle = spanStyle.copy(fontWeight = FontWeight.Bold)
            }
            
            if ((formatFlags and 2) != 0) { // Italic
                spanStyle = spanStyle.copy(fontStyle = FontStyle.Italic)
            }
            
            val decorations = mutableListOf<TextDecoration>()
            if ((formatFlags and 4) != 0) { // Strikethrough
                decorations.add(TextDecoration.LineThrough)
            }
            if ((formatFlags and 8) != 0) { // Underline
                decorations.add(TextDecoration.Underline)
            }
            
            if (decorations.isNotEmpty()) {
                spanStyle = spanStyle.copy(textDecoration = TextDecoration.combine(decorations))
            }
        } catch (e: Exception) {
            println("Error converting format flags: ${e.message}")
        }
        
        return spanStyle
    }

    // JSON parsing utility functions
    private fun extractJsonObject(json: String, startIndex: Int): String {
        var braceCount = 0
        var i = startIndex
        
        while (i < json.length) {
            when (json[i]) {
                '{' -> braceCount++
                '}' -> {
                    braceCount--
                    if (braceCount == 0) {
                        return json.substring(startIndex, i + 1)
                    }
                }
            }
            i++
        }
        
        throw IllegalArgumentException("Unterminated JSON object")
    }

    private fun extractJsonArray(json: String, startIndex: Int): String {
        var bracketCount = 0
        var i = startIndex
        
        while (i < json.length) {
            when (json[i]) {
                '[' -> bracketCount++
                ']' -> {
                    bracketCount--
                    if (bracketCount == 0) {
                        return json.substring(startIndex, i + 1)
                    }
                }
            }
            i++
        }
        
        throw IllegalArgumentException("Unterminated JSON array")
    }

    private fun extractJsonObjectsFromArray(arrayJson: String): List<String> {
        val objects = mutableListOf<String>()
        val content = arrayJson.substring(1, arrayJson.length - 1).trim() // Remove [ ]
        
        if (content.isEmpty()) return objects
        
        var i = 0
        while (i < content.length) {
            if (content[i] == '{') {
                val objJson = extractJsonObject(content, i)
                objects.add(objJson)
                i += objJson.length
            } else {
                i++
            }
        }
        
        return objects
    }

    private fun extractStringField(json: String, fieldName: String): String? {
        // Try both patterns: with and without space after colon
        val patterns = listOf("\"$fieldName\":\"", "\"$fieldName\": \"")
        
        println("🔍 extractStringField: Looking for '$fieldName' in JSON length ${json.length}")
        
        for (pattern in patterns) {
            // For "type" field, use lastIndexOf to get the node's type, not child's type
            val startIndex = if (fieldName == "type") {
                json.lastIndexOf(pattern)
            } else {
                json.indexOf(pattern)
            }
            println("🔍 extractStringField: Pattern '$pattern' found at index $startIndex")
            
            if (startIndex != -1) {
                val valueStart = startIndex + pattern.length
                var valueEnd = valueStart
                
                while (valueEnd < json.length && json[valueEnd] != '"') {
                    if (json[valueEnd] == '\\') valueEnd++ // Skip escaped characters
                    valueEnd++
                }
                
                val result = if (valueEnd < json.length) {
                    json.substring(valueStart, valueEnd).replace("\\\"", "\"").replace("\\\\", "\\")
                } else null
                
                println("🔍 extractStringField: Extracted value = '$result'")
                return result
            }
        }
        
        println("🔍 extractStringField: Field '$fieldName' not found")
        return null
    }

    private fun extractIntField(json: String, fieldName: String): Int? {
        val pattern = "\"$fieldName\":"
        val startIndex = json.indexOf(pattern)
        if (startIndex == -1) return null
        
        val valueStart = startIndex + pattern.length
        var valueEnd = valueStart
        
        // Skip whitespace
        while (valueEnd < json.length && json[valueEnd].isWhitespace()) {
            valueEnd++
        }
        
        val numStart = valueEnd
        while (valueEnd < json.length && (json[valueEnd].isDigit() || json[valueEnd] == '-')) {
            valueEnd++
        }
        
        return if (valueEnd > numStart) {
            json.substring(numStart, valueEnd).toIntOrNull()
        } else null
    }

    /**
     * Converts a RichTextState to a LexicalRoot structure
     */
    @OptIn(ExperimentalRichTextApi::class)
    fun convertRichTextStateToLexical(richTextState: RichTextState): LexicalRoot {
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