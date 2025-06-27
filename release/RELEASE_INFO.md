# Android Rich Text Editor with @ Mentions - Release 1.0.0-mentions-alpha01

## AAR Files Included

### Main Library
- **File**: `richeditor-compose-release.aar` (532 KB)
- **Description**: Core rich text editor library with @ mentions functionality
- **Contains**: All rich text editing features, @ mention support, Lexical JSON compatibility

### Coil3 Extension  
- **File**: `richeditor-compose-coil3-release.aar` (6.4 KB)
- **Description**: Coil3 image loading integration for @ mention avatars
- **Contains**: Image loading utilities for user profile pictures in mentions

## Usage

### Option 1: JitPack (Recommended)
```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Main library with @ mentions support
    implementation("com.github.Kajabi:Android-Kajabi-Rich-Text:1.0.0-mentions-alpha01")
    
    // For avatar image loading in mentions
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
}
```

### Option 2: Local AAR Files
```kotlin
dependencies {
    // Place AAR files in app/libs/ directory
    implementation(files("libs/richeditor-compose-release.aar"))
    implementation(files("libs/richeditor-compose-coil3-release.aar"))
    
    // Required dependencies
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("androidx.compose.ui:ui:1.8.0-alpha03")
    implementation("androidx.compose.material3:material3:1.8.0-alpha03")
}
```

## Integration

Replace your existing rich text editor with the mentions-enabled version:

```kotlin
// Instead of RichTextEditor
KJRichTextEditorWithMentions(
    state = richTextState,
    users = yourUserList, // List<MentionUser>
    placeholder = { Text("Type a message...") }
)
```

## Features Included

- ✅ **@ Mention Autocomplete**: Type `@` + name for filtered suggestions
- ✅ **Atomic Mention Behavior**: Mentions can only be deleted entirely
- ✅ **Avatar Support**: User profile pictures with Coil3 image loading
- ✅ **Rich Text Formatting**: Bold, italic, underline, strikethrough, headings
- ✅ **List Support**: Ordered/unordered lists with indentation
- ✅ **Lexical JSON Compatibility**: Full import/export support
- ✅ **Cross-Platform**: Android, Desktop, iOS, JS, WASM support

## Version Info

- **Version**: 1.0.0-mentions-alpha01
- **Build Date**: June 27, 2024
- **Group ID**: com.github.Kajabi
- **Repository**: https://github.com/Kajabi/Android-Kajabi-Rich-Text

## Support

For issues and questions, visit: https://github.com/Kajabi/Android-Kajabi-Rich-Text/issues 