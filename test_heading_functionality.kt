import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.parser.utils.H1SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H2SpanStyle

fun testHeadingFunctionality() {
    // Create a RichTextState
    val richTextState = RichTextState()
    
    // Test H1 functionality
    richTextState.setText("This is a heading 1.")
    richTextState.toggleH1()
    
    // Test H2 functionality  
    richTextState.addTextAfterSelection("\nWhile this is heading 2.")
    richTextState.selection = TextRange(richTextState.annotatedString.text.length - 25, richTextState.annotatedString.text.length)
    richTextState.toggleH2()
    
    // Test normal text
    richTextState.addTextAfterSelection("\nFinally, back to regular, normal text.")
    
    // Get the Lexical output
    val lexicalText = richTextState.getLexicalText()
    
    println("Generated Lexical Text:")
    println(lexicalText)
    
    // Test setLexicalText functionality
    val testLexicalJson = """
        {
          "root": {
            "children": [
              {
                "children": [
                  {
                    "detail": 0,
                    "format": 0,
                    "mode": "normal",
                    "style": "",
                    "text": "This is a heading 1.",
                    "type": "text",
                    "version": 1
                  }
                ],
                "direction": "ltr",
                "format": "",
                "indent": 0,
                "type": "heading",
                "version": 1,
                "tag": "h1"
              },
              {
                "children": [
                  {
                    "detail": 0,
                    "format": 0,
                    "mode": "normal",
                    "style": "",
                    "text": "While this is heading 2.",
                    "type": "text",
                    "version": 1
                  }
                ],
                "direction": "ltr",
                "format": "",
                "indent": 0,
                "type": "heading",
                "version": 1,
                "tag": "h2"
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "root",
            "version": 1
          }
        }
    """.trimIndent()
    
    // Test parsing Lexical JSON back to RichTextState
    val testState = RichTextState()
    testState.setLexicalText(testLexicalJson)
    
    println("Parsed text from Lexical:")
    println(testState.toText())
} 