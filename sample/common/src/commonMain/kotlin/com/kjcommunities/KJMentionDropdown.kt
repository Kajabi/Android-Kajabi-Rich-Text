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

@Composable
fun KJMentionDropdown(
    users: List<MentionUser>,
    searchText: String,
    onUserSelected: (MentionUser) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter users based on search text (case insensitive)
    val filteredUsers = remember(users, searchText) {
        if (searchText.isBlank()) {
            emptyList()
        } else {
            val minChars = if (users.size > 100) 2 else 1
            if (searchText.length < minChars) {
                emptyList()
            } else {
                users.filter { user ->
                    user.fullName.contains(searchText, ignoreCase = true)
                }.take(10) // Max 10 suggestions
            }
        }
    }

    if (filteredUsers.isNotEmpty()) {
        Card(
            modifier = modifier
                .width(250.dp)
                .heightIn(max = 300.dp),
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
    modifier: Modifier = Modifier
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
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = "${user.fullName} avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = {
                        // On error, the Box background and fallback icon will show
                    }
                )
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