package com.kjcommunities

/**
 * KJMentionDropdown - A reusable dropdown component for @ mention autocomplete.
 * 
 * This component provides:
 * - Case-insensitive user filtering based on search text
 * - Avatar images with fallback to default profile icon
 * - Scrollable list with max 10 suggestions
 * - Click handling to insert mentions into rich text
 * - Automatic positioning relative to text cursor
 * 
 * Usage:
 * ```
 * KJMentionDropdown(
 *     users = listOf(...),
 *     searchText = "pat",
 *     onUserSelected = { user -> ... },
 *     onDismiss = { ... }
 * )
 * ```
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter

@Composable
fun KJMentionDropdown(
    users: List<MentionUser>,
    searchText: String,
    onUserSelected: (MentionUser) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    maxHeight: androidx.compose.ui.unit.Dp = 300.dp,
    enableDebugLogging: Boolean = false
) {
    // Filter users based on search text (case insensitive)
    val filteredUsers = remember(users, searchText) {
        if (enableDebugLogging) {
            println("🔍 [MentionFilter] Filtering users with search text: '$searchText'")
            println("👥 [MentionFilter] Total users available: ${users.size}")
        }
        
        if (searchText.isBlank()) {
            if (enableDebugLogging) {
                println("🚫 [MentionFilter] Search text is blank - returning empty list")
            }
            emptyList()
        } else {
            val minChars = if (users.size > 100) 2 else 1
            if (enableDebugLogging) {
                println("📏 [MentionFilter] Minimum characters required: $minChars")
            }
            
            if (searchText.length < minChars) {
                if (enableDebugLogging) {
                    println("❌ [MentionFilter] Search text too short (${searchText.length} < $minChars) - returning empty list")
                }
                emptyList()
            } else {
                val filtered = users.filter { user ->
                    val matches = user.fullName.contains(searchText, ignoreCase = true)
                    if (enableDebugLogging) {
                        println("🔍 [MentionFilter] User '${user.fullName}' matches '$searchText': $matches")
                    }
                    matches
                }.take(10) // Max 10 suggestions
                
                if (enableDebugLogging) {
                    println("✅ [MentionFilter] Found ${filtered.size} matching users")
                    filtered.forEach { user ->
                        println("👤 [MentionFilter] Matched user: ${user.fullName} (ID: ${user.id}, Image: ${user.imageUrl})")
                    }
                }
                
                filtered
            }
        }
    }

    if (filteredUsers.isNotEmpty()) {
        Card(
            modifier = modifier
                .width(250.dp)
                .heightIn(max = maxHeight),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2a2d31)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(filteredUsers) { user ->
                    MentionUserItem(
                        user = user,
                        enableDebugLogging = enableDebugLogging,
                        onClick = { onUserSelected(user) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MentionUserItem(
    user: MentionUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableDebugLogging: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar image with fallback
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF404449))
        ) {
            if (user.imageUrl != null) {
                if (enableDebugLogging) {
                    println("🖼️ [MentionAvatar] Loading image for ${user.fullName}: ${user.imageUrl}")
                }
                
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = "${user.fullName} avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        if (enableDebugLogging) {
                            println("⏳ [MentionAvatar] Loading started for ${user.fullName}")
                        }
                    },
                    onSuccess = { result ->
                        if (enableDebugLogging) {
                            println("✅ [MentionAvatar] Image loaded successfully for ${user.fullName}")
                            println("📏 [MentionAvatar] Image size: ${result.painter.intrinsicSize}")
                        }
                    },
                    onError = { error ->
                        if (enableDebugLogging) {
                            println("❌ [MentionAvatar] Image failed to load for ${user.fullName}")
                            println("🔗 [MentionAvatar] Failed URL: ${user.imageUrl}")
                            println("💥 [MentionAvatar] Error result: ${error.result}")
                            println("🚨 [MentionAvatar] Error throwable: ${error.result.throwable}")
                            
                            // Additional debugging info
                            when (val result = error.result) {
                                is coil3.request.ErrorResult -> {
                                    println("🔍 [MentionAvatar] Error type: ${result.throwable?.javaClass?.simpleName}")
                                    println("📝 [MentionAvatar] Error message: ${result.throwable?.message}")
                                }
                            }
                        }
                    }
                )
            } else {
                if (enableDebugLogging) {
                    println("🚫 [MentionAvatar] No image URL provided for ${user.fullName}")
                }
            }
            
            // Always show the fallback icon, it will be covered by the image if it loads successfully
            Icon(
                Icons.Filled.Person,
                contentDescription = "Default avatar",
                tint = Color(0xFF8e9297),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // User name
        Text(
            text = user.fullName,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
} 