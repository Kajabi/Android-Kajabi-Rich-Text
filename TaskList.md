# ToDo list
The main purpose of this doc is to outline the changes needed for this library so that it can be used in conjunction with another project. 

# Task 1: Get Lexical Text
We need a function added "getLexicalText()" to all custom display views that can display Rich Text.
The purpose of this function is that it will return a string in a very specific format as outlined below. 

## [x] DONE: H1 and H2 Heading Support Added

### Overview
Successfully implemented H1 and H2 heading support in the rich text editor library. This includes:

1. **UI Controls**: Added H1 and H2 buttons to the KJ Demo Panel with proper icons and state management
2. **RichTextState Methods**: Added `toggleH1()` and `toggleH2()` methods to the RichTextState class
3. **State Management**: Added `isH1` and `isH2` properties to track current heading state
4. **Lexical Integration**: Updated the Lexical parser to properly handle heading nodes both:
   - Converting from Lexical JSON to RichTextState (applying proper H1/H2 styles)
   - Converting from RichTextState to Lexical JSON (detecting H1/H2 styles and creating heading nodes)
5. **Heading Detection**: Implemented the `detectHeadingTag()` function to properly identify H1 and H2 styles

### Changes Made
- **RichTextState.kt**: Added heading toggle methods and state tracking
- **RichTextStateLexicalParser.kt**: Enhanced to properly handle heading nodes and detect heading styles
- **KJDemoPanel.kt**: Added H1 and H2 buttons to the UI toolbar
- **LexicalNode.kt**: Already had proper LexicalHeadingNode structure
- **ElementsSpanStyle.kt**: Already had H1SpanStyle and H2SpanStyle defined

### Functionality
- **H1 Button**: Toggles large heading style (2.em font size, bold weight)
- **H2 Button**: Toggles medium heading style (1.5.em font size, bold weight)
- **Mutually Exclusive**: Selecting H1 removes H2 and vice versa
- **Lexical Compatibility**: Properly converts to/from Lexical JSON format with correct "heading" type and "h1"/"h2" tags
- **State Persistence**: Heading state is properly tracked and updated on selection changes

### Testing
- Application builds successfully for Android platform
- Heading buttons are visible in the KJ Demo toolbar
- No compilation errors or runtime issues

## Rich Text & Lexical Rich Text examples

Please see ![this screenshot](images/rich_text_base_reference.png) as a base reference point for how the rich text will display when on a web browser when we send it to our server in the Lexical Data text format. 

This is the raw text formatted in regular, non-rich text format within a JSON response 
```
"message": "We're going to test some rich text!\n\nFirst, we have bold text.\n\n\n\nNext, we have Italic text. \n\nThis is a heading 1.\n\nWhile this is heading 2.\n\nThis is strikethrough.\n\n\n\nThis is underline.\n\n\n\nThis is a hyperlink to Google while this is to Yahoo.\n\nThis is an ordered list\n\nStill an ordered list\n\nAn indented ordered list (at 1 pip indented)\n\n2 pips indented\n\n2 pips is max!\n\nThis is an unordered list\n\nStill an unordered list\n\nIndented unordered list. (at 1 pip indented) \n\n2 pips indented\n\n2 pips is the max!\n\nFinally, back to regular, normal text. "
```
This is the data formatted in `Lexical Format` within a JSON response 
```
                "{\"root\":{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"We're going to test some rich text!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"First, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":1,\"mode\":\"normal\",\"style\":\"\",\"text\":\"bold text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Next, we have \",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":2,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Italic text\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\". \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a heading 1.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h1\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"While this is heading 2.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"heading\",\"version\":1,\"tag\":\"h2\"},{\"children\":[{\"detail\":0,\"format\":4,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is strikethrough\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":4,\"textStyle\":\"\"},{\"children\":[{\"detail\":0,\"format\":8,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is underline\",\"type\":\"text\",\"version\":1},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\".\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":8,\"textStyle\":\"\"},{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is a hyperlink to Google\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"link\",\"version\":1,\"rel\":\"noreferrer\",\"target\":\"_blank\",\"title\":null,\"url\":\"https://www.google.com\"},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\" while \",\"type\":\"text\",\"version\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"this\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"link\",\"version\":1,\"rel\":\"noreferrer\",\"target\":\"_blank\",\"title\":null,\"url\":\"https://www.yahoo.com\"},{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\" is to Yahoo.\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"},{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is an ordered list\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Still an ordered list\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":2},{\"children\":[{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"An indented ordered list (at 1 pip indented)\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":1,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"2 pips indented\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":2,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"2 pips is max!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":2,\"type\":\"listitem\",\"version\":1,\"value\":2}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"bullet\",\"start\":1,\"tag\":\"ul\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":1,\"type\":\"listitem\",\"version\":1,\"value\":2}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"bullet\",\"start\":1,\"tag\":\"ul\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":3}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"bullet\",\"start\":1,\"tag\":\"ul\"},{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"This is an unordered list\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Still an unordered list\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":2},{\"children\":[{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Indented unordered list. (at 1 pip indented) \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":1,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"children\":[{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"2 pips indented\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":2,\"type\":\"listitem\",\"version\":1,\"value\":1},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"2 pips is the max!\",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":2,\"type\":\"listitem\",\"version\":1,\"value\":2}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"number\",\"start\":1,\"tag\":\"ol\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":1,\"type\":\"listitem\",\"version\":1,\"value\":2}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"number\",\"start\":1,\"tag\":\"ol\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"listitem\",\"version\":1,\"value\":3}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"list\",\"version\":1,\"listType\":\"number\",\"start\":1,\"tag\":\"ol\"},{\"children\":[{\"detail\":0,\"format\":0,\"mode\":\"normal\",\"style\":\"\",\"text\":\"Finally, back to regular, normal text. \",\"type\":\"text\",\"version\":1}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"text\",\"version\":1,\"textFormat\":0,\"textStyle\":\"\"}],\"direction\":\"ltr\",\"format\":\"\",\"indent\":0,\"type\":\"root\",\"version\":1}}"                
```

This is the JSON text pretty printed with the escaped characters removed for reference:
```
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
            "text": "We're going to test some rich text!",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 0,
        "textStyle": ""
      },
      {
        "children": [
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": "First, we have ",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 1,
            "mode": "normal",
            "style": "",
            "text": "bold text",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": ".",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 0,
        "textStyle": ""
      },
      {
        "children": [
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": "Next, we have ",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 2,
            "mode": "normal",
            "style": "",
            "text": "Italic text",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": ". ",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 0,
        "textStyle": ""
      },
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
      },
      {
        "children": [
          {
            "detail": 0,
            "format": 4,
            "mode": "normal",
            "style": "",
            "text": "This is strikethrough",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": ".",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 4,
        "textStyle": ""
      },
      {
        "children": [
          {
            "detail": 0,
            "format": 8,
            "mode": "normal",
            "style": "",
            "text": "This is underline",
            "type": "text",
            "version": 1
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": ".",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 8,
        "textStyle": ""
      },
      {
        "children": [
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "This is a hyperlink to Google",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "link",
            "version": 1,
            "rel": "noreferrer",
            "target": "_blank",
            "title": null,
            "url": "https://www.google.com"
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": " while ",
            "type": "text",
            "version": 1
          },
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "this",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "link",
            "version": 1,
            "rel": "noreferrer",
            "target": "_blank",
            "title": null,
            "url": "https://www.yahoo.com"
          },
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": " is to Yahoo.",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 0,
        "textStyle": ""
      },
      {
        "children": [
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "This is an ordered list",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 1
          },
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "Still an ordered list",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 2
          },
          {
            "children": [
              {
                "children": [
                  {
                    "children": [
                      {
                        "detail": 0,
                        "format": 0,
                        "mode": "normal",
                        "style": "",
                        "text": "An indented ordered list (at 1 pip indented)",
                        "type": "text",
                        "version": 1
                      }
                    ],
                    "direction": "ltr",
                    "format": "",
                    "indent": 1,
                    "type": "listitem",
                    "version": 1,
                    "value": 1
                  },
                  {
                    "children": [
                      {
                        "children": [
                          {
                            "children": [
                              {
                                "detail": 0,
                                "format": 0,
                                "mode": "normal",
                                "style": "",
                                "text": "2 pips indented",
                                "type": "text",
                                "version": 1
                              }
                            ],
                            "direction": "ltr",
                            "format": "",
                            "indent": 2,
                            "type": "listitem",
                            "version": 1,
                            "value": 1
                          },
                          {
                            "children": [
                              {
                                "detail": 0,
                                "format": 0,
                                "mode": "normal",
                                "style": "",
                                "text": "2 pips is max!",
                                "type": "text",
                                "version": 1
                              }
                            ],
                            "direction": "ltr",
                            "format": "",
                            "indent": 2,
                            "type": "listitem",
                            "version": 1,
                            "value": 2
                          }
                        ],
                        "direction": "ltr",
                        "format": "",
                        "indent": 0,
                        "type": "list",
                        "version": 1,
                        "listType": "bullet",
                        "start": 1,
                        "tag": "ul"
                      }
                    ],
                    "direction": "ltr",
                    "format": "",
                    "indent": 1,
                    "type": "listitem",
                    "version": 1,
                    "value": 2
                  }
                ],
                "direction": "ltr",
                "format": "",
                "indent": 0,
                "type": "list",
                "version": 1,
                "listType": "bullet",
                "start": 1,
                "tag": "ul"
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 3
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "list",
        "version": 1,
        "listType": "bullet",
        "start": 1,
        "tag": "ul"
      },
      {
        "children": [
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "This is an unordered list",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 1
          },
          {
            "children": [
              {
                "detail": 0,
                "format": 0,
                "mode": "normal",
                "style": "",
                "text": "Still an unordered list",
                "type": "text",
                "version": 1
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 2
          },
          {
            "children": [
              {
                "children": [
                  {
                    "children": [
                      {
                        "detail": 0,
                        "format": 0,
                        "mode": "normal",
                        "style": "",
                        "text": "Indented unordered list. (at 1 pip indented) ",
                        "type": "text",
                        "version": 1
                      }
                    ],
                    "direction": "ltr",
                    "format": "",
                    "indent": 1,
                    "type": "listitem",
                    "version": 1,
                    "value": 1
                  },
                  {
                    "children": [
                      {
                        "children": [
                          {
                            "children": [
                              {
                                "detail": 0,
                                "format": 0,
                                "mode": "normal",
                                "style": "",
                                "text": "2 pips indented",
                                "type": "text",
                                "version": 1
                              }
                            ],
                            "direction": "ltr",
                            "format": "",
                            "indent": 2,
                            "type": "listitem",
                            "version": 1,
                            "value": 1
                          },
                          {
                            "children": [
                              {
                                "detail": 0,
                                "format": 0,
                                "mode": "normal",
                                "style": "",
                                "text": "2 pips is the max!",
                                "type": "text",
                                "version": 1
                              }
                            ],
                            "direction": "ltr",
                            "format": "",
                            "indent": 2,
                            "type": "listitem",
                            "version": 1,
                            "value": 2
                          }
                        ],
                        "direction": "ltr",
                        "format": "",
                        "indent": 0,
                        "type": "list",
                        "version": 1,
                        "listType": "number",
                        "start": 1,
                        "tag": "ol"
                      }
                    ],
                    "direction": "ltr",
                    "format": "",
                    "indent": 1,
                    "type": "listitem",
                    "version": 1,
                    "value": 2
                  }
                ],
                "direction": "ltr",
                "format": "",
                "indent": 0,
                "type": "list",
                "version": 1,
                "listType": "number",
                "start": 1,
                "tag": "ol"
              }
            ],
            "direction": "ltr",
            "format": "",
            "indent": 0,
            "type": "listitem",
            "version": 1,
            "value": 3
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "list",
        "version": 1,
        "listType": "number",
        "start": 1,
        "tag": "ol"
      },
      {
        "children": [
          {
            "detail": 0,
            "format": 0,
            "mode": "normal",
            "style": "",
            "text": "Finally, back to regular, normal text. ",
            "type": "text",
            "version": 1
          }
        ],
        "direction": "ltr",
        "format": "",
        "indent": 0,
        "type": "text",
        "version": 1,
        "textFormat": 0,
        "textStyle": ""
      }
    ],
    "direction": "ltr",
    "format": "",
    "indent": 0,
    "type": "root",
    "version": 1
  }
}
```
The Lexical SDK should be able to provide the developer either of these texts 

## [x] DONE: Task 2: Hide non-lexical supported Rich Text in a Sample Application

I have copied the files within the "com.mohamedrejeb.richeditor.sample.common.slack" directory into the "com.kjcommunities" directory and the goal is to first fix and then augment these classes. 

There are a lot of rich text items that this library supports, that LexicalData does not. The goal is to therefore either disable, comment out, or hide access to rich text elements that are not supported. 

The primary acceptance criteria of this task is to get the codebase compiling, working, and ready to use, but in a state where the non-supported rich text elements can no longer be used. 

### List of items that need to be removed from the Sample kjcommunities activities 

1. Code Block - Not supported in Lexical format
2. Image - Not supported in Lexical format
3. Table - Not supported in Lexical format
4. Check List - Not supported in Lexical format
5. Quote - Not supported in Lexical format
6. Divider - Not supported in Lexical format

## Task 3: TBD