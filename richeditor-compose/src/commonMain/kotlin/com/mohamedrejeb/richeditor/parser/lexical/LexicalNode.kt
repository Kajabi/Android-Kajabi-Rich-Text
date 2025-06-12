package com.mohamedrejeb.richeditor.parser.lexical

/**
 * Represents the root structure of a Lexical document
 */
public data class LexicalRoot(
    val root: LexicalRootNode
)

/**
 * Represents the root node containing all content
 */
public data class LexicalRootNode(
    val children: List<LexicalNode>,
    val direction: String = "ltr",
    val format: String = "",
    val indent: Int = 0,
    val type: String = "root",
    val version: Int = 1
)

/**
 * Base interface for all Lexical nodes
 */
public sealed interface LexicalNode {
    public val direction: String
    public val format: String
    public val indent: Int
    public val type: String
    public val version: Int
}

/**
 * Represents a paragraph node
 */
public data class LexicalParagraphNode(
    val children: List<LexicalNode>,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0,
    override val type: String = "paragraph",
    override val version: Int = 1,
    val textFormat: Int = 0,
    val textStyle: String = ""
) : LexicalNode

/**
 * Represents a heading node
 */
public data class LexicalHeadingNode(
    val children: List<LexicalNode>,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0,
    override val type: String = "heading",
    override val version: Int = 1,
    val tag: String // "h1", "h2", etc.
) : LexicalNode

/**
 * Represents a text node with formatting
 */
public data class LexicalTextNode(
    val detail: Int = 0,
    val formatFlags: Int = 0, // 0=normal, 1=bold, 2=italic, 4=strikethrough, 8=underline
    val mode: String = "normal",
    val style: String = "",
    val text: String,
    override val type: String = "text",
    override val version: Int = 1,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0
) : LexicalNode

/**
 * Represents a link node
 */
public data class LexicalLinkNode(
    val children: List<LexicalNode>,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0,
    override val type: String = "link",
    override val version: Int = 1,
    val rel: String = "noreferrer",
    val target: String = "_blank",
    val title: String? = null,
    val url: String
) : LexicalNode

/**
 * Represents a list node
 */
public data class LexicalListNode(
    val children: List<LexicalNode>,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0,
    override val type: String = "list",
    override val version: Int = 1,
    val listType: String, // "bullet" or "number"
    val start: Int = 1,
    val tag: String // "ul" or "ol"
) : LexicalNode

/**
 * Represents a list item node
 */
public data class LexicalListItemNode(
    val children: List<LexicalNode>,
    override val direction: String = "ltr",
    override val format: String = "",
    override val indent: Int = 0,
    override val type: String = "listitem",
    override val version: Int = 1,
    val value: Int
) : LexicalNode 