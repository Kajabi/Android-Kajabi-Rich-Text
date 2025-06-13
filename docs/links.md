# Links

The Rich Text Editor provides comprehensive support for hyperlinks, allowing you to:
- Add links to new or existing text
- Update link URLs
- Remove links
- Customize link appearance
- Handle link clicks

## Adding Links

### New Text with Link

To add a new text with a link, use the `addLink` method:

```kotlin
// Add link after selection
richTextState.addLink(
    text = "Compose Rich Editor",
    url = "https://github.com/MohamedRejeb/Compose-Rich-Editor"
)
```

### Converting Text to Link

To convert selected text into a link, use the `addLinkToSelection` method:

```kotlin
// Add link to selected text
richTextState.addLinkToSelection(
    url = "https://kotlinlang.org/"
)
```

## Managing Links

### Updating Links

To update an existing link's URL:

```kotlin
// Update selected link URL
richTextState.updateLink(
    url = "https://kotlinlang.org/"
)
```

### Removing Links

To remove a link while keeping the text:

```kotlin
// Remove link from selected text
richTextState.removeLink()
```

## Link Information

### Checking Link Status

To check if the current selection is a link:

```kotlin
val isLink = richTextState.isLink
```

### Getting Link Details

To get the current link's text and URL:

```kotlin
// Get link text and URL
val linkText = richTextState.selectedLinkText
val linkUrl = richTextState.selectedLinkUrl
```

## Customizing Links

### Link Appearance

You can customize how links appear in the editor using the `RichTextConfig`:

```kotlin
val richTextState = rememberRichTextState()

LaunchedEffect(Unit) {
    // Set link color (default is Color.Blue)
    richTextState.config.linkColor = Color.Green
    
    // Set link text decoration (default is TextDecoration.Underline)
    richTextState.config.linkTextDecoration = TextDecoration.None
    
    // Or combine multiple decorations
    richTextState.config.linkTextDecoration = TextDecoration.combine(
        listOf(TextDecoration.Underline, TextDecoration.LineThrough)
    )
}
```

### Common Link Color Examples

```kotlin
// Material Design colors
richTextState.config.linkColor = Color(0xFF1976D2) // Material Blue
richTextState.config.linkColor = Color(0xFF388E3C) // Material Green
richTextState.config.linkColor = Color(0xFFD32F2F) // Material Red

// Custom brand colors
richTextState.config.linkColor = Color(0xFF1DA1F2) // Twitter Blue
richTextState.config.linkColor = Color(0xFF0077B5) // LinkedIn Blue
richTextState.config.linkColor = Color(0xFF25D366) // WhatsApp Green

// Theme-based colors (in a Composable)
@Composable
fun MyRichTextEditor() {
    val richTextState = rememberRichTextState()
    
    LaunchedEffect(Unit) {
        // Use theme colors
        richTextState.config.linkColor = MaterialTheme.colorScheme.primary
        // or
        richTextState.config.linkColor = MaterialTheme.colorScheme.secondary
    }
    
    RichTextEditor(state = richTextState)
}
```

### Dynamic Link Colors

You can change link colors dynamically based on app state:

```kotlin
@Composable
fun DynamicLinkColorEditor() {
    val richTextState = rememberRichTextState()
    var isDarkMode by remember { mutableStateOf(false) }
    
    LaunchedEffect(isDarkMode) {
        richTextState.config.linkColor = if (isDarkMode) {
            Color(0xFF64B5F6) // Light blue for dark mode
        } else {
            Color(0xFF1976D2) // Dark blue for light mode
        }
    }
    
    Column {
        Switch(
            checked = isDarkMode,
            onCheckedChange = { isDarkMode = it }
        )
        
        RichTextEditor(state = richTextState)
    }
}
```

### Complete Link Styling Example

```kotlin
@Composable
fun StyledLinkEditor() {
    val richTextState = rememberRichTextState()
    
    LaunchedEffect(Unit) {
        // Customize all link properties
        richTextState.config.linkColor = Color(0xFF00C851) // Green
        richTextState.config.linkTextDecoration = TextDecoration.None // No underline
    }
    
    RichTextEditor(
        state = richTextState,
        placeholder = { Text("Add some text with links...") }
    )
}
```

## Handling Link Clicks

By default, links are opened by your platform's `UriHandler`. To customize link handling:

```kotlin
val myUriHandler = remember {
    object : UriHandler {
        override fun openUri(uri: String) {
            // Custom link handling logic
            // For example: open in specific browser, validate URL, etc.
        }
    }
}

CompositionLocalProvider(LocalUriHandler provides myUriHandler) {
    RichText(
        state = richTextState,
        modifier = Modifier.fillMaxWidth()
    )
}
```
