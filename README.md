# Compose Rich Text Editor with @ Mentions

A powerful, multiplatform rich text editor for Jetpack Compose with comprehensive formatting capabilities and advanced @ mention functionality. This library provides everything you need to create sophisticated text editing experiences across Android, Desktop, iOS, JS, and WASM platforms.

## What This Library Does

This rich text editor offers a complete text formatting solution with:

- **Rich Text Formatting**: Bold, italic, underline, strikethrough, headings (H1, H2), hyperlinks
- **List Support**: Ordered and unordered lists with multi-level indentation
- **@ Mention System**: Advanced user mention functionality with autocomplete dropdown
- **Lexical JSON Compatibility**: Full import/export support for Lexical editor format
- **Atomic Behavior**: @ mentions behave as single units (can't be partially edited)
- **Customizable Styling**: Configurable colors, decorations, and visual appearance
- **Cross-Platform**: Works seamlessly across all Compose Multiplatform targets

The library is built on a solid foundation with `RichTextState` for state management, comprehensive parsing systems for HTML/Markdown/Lexical formats, and highly customizable UI components.

## KJ Communities Demo - The Complete Implementation

The **KJ Communities Demo** (`/sample/common/src/commonMain/kotlin/com/kjcommunities/`) represents the flagship example of this library in action. It demonstrates a Slack-like chat interface with full @ mention functionality and serves as the primary reference for implementing @ mentions in production applications.

### Live Demo Video

https://github.com/Kajabi/Android-Kajabi-Rich-Text/raw/main/docs/videos/demo.mp4

> **📱 Click the link above to watch the demo video** showing @ mention autocomplete, atomic behavior, and chat-like interface in action!

### Key Features Demonstrated

- **@ Mention Autocomplete**: Type `@` followed by a name to see filtered user suggestions
- **Intelligent Dropdown Positioning**: Dropdown appears above the text input to avoid covering the keyboard
- **Avatar Support**: User profile pictures with fallback icons using Coil3 image loading
- **Atomic Mention Behavior**: @ mentions can only be deleted entirely, not partially edited
- **Rich Formatting Toolbar**: Complete formatting controls optimized for Lexical JSON compatibility
- **Message History**: Chat-like interface showing formatted messages with long-press to copy Lexical JSON
- **Real-time Text Processing**: Seamless integration between typing, autocomplete, and mention insertion

### Live Demo Features

The demo includes sample mention data and test buttons that demonstrate:
- Complex @ mention scenarios including @everyone mentions
- @ mentions within lists and formatted text
- Round-trip Lexical JSON import/export functionality
- Integration with bold, italic, underline, strikethrough, and hyperlinks

## Platform Support

This library supports all Compose Multiplatform targets:
- **Android** - Full feature support
- **Desktop** (JVM) - Complete functionality including focus management
- **iOS** - Native iOS integration
- **JavaScript** - Web deployment ready
- **WASM** - WebAssembly support for modern web apps

## How to Use This Demo in Your Project

To integrate the @ mentions functionality from the KJ Communities demo into your own application, copy these modular components:

### Core @ Mention Components

**Essential Files:**
```
com/kjcommunities/KJRichTextEditorWithMentions.kt    # Main wrapper component
com/kjcommunities/KJMentionDropdown.kt               # Autocomplete dropdown UI
```

**Data Model:**
```kotlin
// Add this data class to your project
data class MentionUser(
    val id: String,
    val fullName: String,
    val imageUrl: String?
)
```

### Optional Formatting Components

**For Complete Rich Text Toolbar:**
```
com/kjcommunities/KJDemoPanel.kt                     # Formatting toolbar
com/kjcommunities/KJDemoPanelButton.kt              # Reusable toolbar buttons
com/kjcommunities/KJDemoLinkDialog.kt               # Link creation dialog
com/kjcommunities/icons/CustomIcons.kt              # H1, H2, indent icons
```

**For Full Demo Interface:**
```
com/kjcommunities/KJDemoScreen.kt                    # Complete chat-like demo
```

### Integration Steps

1. **Copy the Core Files**: Add `KJRichTextEditorWithMentions.kt` and `KJMentionDropdown.kt` to your project

2. **Replace the Standard Editor**:
   ```kotlin
   // Instead of RichTextEditor
   RichTextEditor(state = richTextState, ...)
   
   // Use the mentions-enabled version
   KJRichTextEditorWithMentions(
       state = richTextState,
       users = yourUserList,
       placeholder = { Text("Type a message...") }
   )
   ```

3. **Configure @ Mention Styling**:
   ```kotlin
   // Set global alpha name for your community
   RichSpanStyle.Mention.globalAlphaName = "yourcommunityname"
   
   // Configure mention appearance
   richTextState.config.mentionColor = Color(0xFF0084ff) // Blue mentions
   richTextState.config.mentionTextDecoration = TextDecoration.None
   ```

4. **Provide User Data**: Supply your `List<MentionUser>` with user IDs, names, and optional avatar URLs

5. **Handle Lexical Export** (Optional):
   ```kotlin
   val lexicalJson = richTextState.getLexicalText() // Export for backend
   richTextState.setLexicalText(lexicalJson)        // Import from backend
   ```

### Dependencies Required

The @ mention functionality requires:

```kotlin
// Add JitPack repository to your root build.gradle.kts
repositories {
    maven { url = uri("https://jitpack.io") }
}

// Image loading for user avatars
implementation("io.coil-kt.coil3:coil-compose:3.0.0")

// Enhanced rich text editor with @ mentions support
implementation("com.github.Kajabi:Android-Kajabi-Rich-Text:1.2.0")
```

**Note**: This enhanced version includes all the original rich text editor functionality plus the @ mentions features. You don't need to include the original `com.mohamedrejeb.richeditor` dependency.

### Customization Options

The components are designed to be highly customizable:

- **Dropdown Appearance**: Modify colors, sizing, and layout in `KJMentionDropdown.kt`
- **Search Behavior**: Adjust minimum character requirements and result limits
- **Avatar Handling**: Customize fallback icons and image loading behavior  
- **Mention Styling**: Configure colors, decorations, and visual appearance
- **Positioning Logic**: Fine-tune dropdown positioning for your UI layout

## Advanced Features

### Atomic Mention Behavior
@ mentions are implemented with atomic behavior at the library level - users cannot partially edit mention text. They must either leave mentions intact or delete them entirely.

### Lexical JSON Compatibility
Full compatibility with Lexical editor JSON format enables seamless data exchange with web-based Lexical editors and backend storage systems.

### Intelligent Autocomplete
The mention system includes smart filtering with configurable minimum character requirements (1 char for <100 users, 2 chars for >100 users) and result limiting for optimal performance.

### Cross-Platform Considerations
- **Desktop**: Includes focus management to prevent editor focus loss when clicking toolbar buttons
- **Mobile**: Intelligent dropdown positioning prevents keyboard obstruction
- **Web**: Full WASM compatibility for modern web deployment

## Publishing New Versions

This project uses **JitPack** for easy publishing. To release a new version:

1. **Update the version** in `convention-plugins/src/main/kotlin/root.publication.gradle.kts`:
   ```kotlin
   version = System.getenv("VERSION") ?: "1.3.0"  // Increment version
   ```

2. **Commit and push** your changes

3. **Create a GitHub release**:
   - Go to your GitHub repository
   - Click "Releases" → "Create a new release"
   - Tag version: `1.3.0` (should match the version above)
   - Release title: `v1.3.0`
   - Add release notes describing changes

4. **JitPack will automatically build** the release within a few minutes

5. **Users can then depend on the new version**:
   ```kotlin
   implementation("com.github.Kajabi:Android-Kajabi-Rich-Text:1.3.0")
   ```

### Alternative: GitHub Packages

If you prefer GitHub Packages instead of JitPack, uncomment the publishing configuration in the gradle files and set up GitHub Actions with your repository secrets.

---

This implementation provides a production-ready @ mention system that can be easily integrated into existing Compose applications while maintaining the flexibility to customize appearance and behavior to match your application's design requirements.
