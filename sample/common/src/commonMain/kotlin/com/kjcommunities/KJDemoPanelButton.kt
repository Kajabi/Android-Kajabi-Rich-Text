package com.kjcommunities

/**
 * KJDemoPanelButton - A reusable button component for rich text editor toolbar panels.
 * 
 * This class demonstrates how to create a consistent, accessible toolbar button that:
 * - Displays formatting icons with proper visual feedback
 * - Shows selected/active state with background highlighting
 * - Handles enabled/disabled states for context-sensitive operations
 * - Supports custom icon tinting for special cases (like color indicators)
 * - Maintains focus behavior to prevent editor focus loss (especially on Desktop)
 * 
 * Key features:
 * - **Visual States**: Normal, selected (highlighted background), and disabled states
 * - **Accessibility**: Proper role semantics and content descriptions
 * - **Focus Management**: Prevents focus stealing from the rich text editor
 * - **Consistent Styling**: Rounded corners, proper padding, and hover effects
 * - **Flexible Theming**: Supports custom tint colors while maintaining default styling
 * 
 * Design considerations:
 * - Uses a subtle background highlight for selected state instead of border changes
 * - Implements click handling with proper enabled state management
 * - Provides consistent spacing and sizing across all toolbar buttons
 * - Includes a desktop-specific focus workaround to maintain editor usability
 * 
 * This serves as a foundational component for building rich text editor toolbars
 * with consistent visual design and behavior across different platforms.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun KJDemoPanelButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color? = null,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            // Workaround to prevent the rich editor
            // from losing focus when clicking on the button
            // (Happens only on Desktop)
            .focusProperties { canFocus = false }
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = icon.name,
            tint = tint ?: Color(0xFFCBCCCD),
            modifier = Modifier
                .background(
                    color = if (isSelected) Color(0xFF393B3D)
                    else Color.Transparent,
                )
                .padding(6.dp)
        )
    }
}