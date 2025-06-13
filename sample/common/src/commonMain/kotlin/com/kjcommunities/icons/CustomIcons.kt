package com.kjcommunities.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Custom H1 icon showing "H" with "1" subscript
 */
val Icons.Outlined.H1: ImageVector
    get() {
        if (_h1 != null) {
            return _h1!!
        }
        _h1 = materialIcon(name = "Outlined.H1") {
            materialPath {
                // Draw "H" - left vertical line
                moveTo(4f, 4f)
                lineTo(6f, 4f)
                lineTo(6f, 20f)
                lineTo(4f, 20f)
                close()
                
                // Draw "H" - horizontal line
                moveTo(4f, 11f)
                lineTo(12f, 11f)
                lineTo(12f, 13f)
                lineTo(4f, 13f)
                close()
                
                // Draw "H" - right vertical line
                moveTo(10f, 4f)
                lineTo(12f, 4f)
                lineTo(12f, 20f)
                lineTo(10f, 20f)
                close()
                
                // Draw "1" 
                moveTo(16f, 6f)
                lineTo(18f, 4f)
                lineTo(20f, 4f)
                lineTo(20f, 6f)
                lineTo(18f, 6f)
                lineTo(18f, 18f)
                lineTo(20f, 18f)
                lineTo(20f, 20f)
                lineTo(16f, 20f)
                lineTo(16f, 18f)
                lineTo(16f, 6f)
                close()
            }
        }
        return _h1!!
    }

private var _h1: ImageVector? = null

/**
 * Custom H2 icon showing "H" with "2" subscript
 */
val Icons.Outlined.H2: ImageVector
    get() {
        if (_h2 != null) {
            return _h2!!
        }
        _h2 = materialIcon(name = "Outlined.H2") {
            materialPath {
                // Draw "H" - left vertical line
                moveTo(4f, 4f)
                lineTo(6f, 4f)
                lineTo(6f, 20f)
                lineTo(4f, 20f)
                close()
                
                // Draw "H" - horizontal line
                moveTo(4f, 11f)
                lineTo(12f, 11f)
                lineTo(12f, 13f)
                lineTo(4f, 13f)
                close()
                
                // Draw "H" - right vertical line
                moveTo(10f, 4f)
                lineTo(12f, 4f)
                lineTo(12f, 20f)
                lineTo(10f, 20f)
                close()
                
                // Draw "2" - top horizontal line
                moveTo(16f, 4f)
                lineTo(20f, 4f)
                lineTo(20f, 6f)
                lineTo(18f, 6f)
                lineTo(18f, 10f)
                lineTo(20f, 10f)
                lineTo(20f, 12f)
                lineTo(16f, 12f)
                lineTo(16f, 10f)
                lineTo(16f, 6f)
                lineTo(16f, 4f)
                close()
                
                // Draw "2" - bottom horizontal line
                moveTo(16f, 18f)
                lineTo(20f, 18f)
                lineTo(20f, 20f)
                lineTo(16f, 20f)
                close()
            }
        }
        return _h2!!
    }

private var _h2: ImageVector? = null

/**
 * Custom indent icon showing horizontal lines with right arrow
 */
val Icons.Outlined.FormatIndentIncrease: ImageVector
    get() {
        if (_formatIndentIncrease != null) {
            return _formatIndentIncrease!!
        }
        _formatIndentIncrease = materialIcon(name = "Outlined.FormatIndentIncrease") {
            materialPath {
                // Horizontal lines (representing text)
                moveTo(9f, 3f)
                lineTo(21f, 3f)
                lineTo(21f, 5f)
                lineTo(9f, 5f)
                close()
                
                moveTo(9f, 7f)
                lineTo(21f, 7f)
                lineTo(21f, 9f)
                lineTo(9f, 9f)
                close()
                
                moveTo(9f, 11f)
                lineTo(21f, 11f)
                lineTo(21f, 13f)
                lineTo(9f, 13f)
                close()
                
                moveTo(9f, 15f)
                lineTo(21f, 15f)
                lineTo(21f, 17f)
                lineTo(9f, 17f)
                close()
                
                moveTo(9f, 19f)
                lineTo(21f, 19f)
                lineTo(21f, 21f)
                lineTo(9f, 21f)
                close()
                
                // Right arrow for indent
                moveTo(3f, 8f)
                lineTo(7f, 12f)
                lineTo(3f, 16f)
                lineTo(3f, 14f)
                lineTo(5f, 12f)
                lineTo(3f, 10f)
                close()
            }
        }
        return _formatIndentIncrease!!
    }

private var _formatIndentIncrease: ImageVector? = null

/**
 * Custom outdent icon showing horizontal lines with left arrow
 */
val Icons.Outlined.FormatIndentDecrease: ImageVector
    get() {
        if (_formatIndentDecrease != null) {
            return _formatIndentDecrease!!
        }
        _formatIndentDecrease = materialIcon(name = "Outlined.FormatIndentDecrease") {
            materialPath {
                // Horizontal lines (representing text)
                moveTo(9f, 3f)
                lineTo(21f, 3f)
                lineTo(21f, 5f)
                lineTo(9f, 5f)
                close()
                
                moveTo(9f, 7f)
                lineTo(21f, 7f)
                lineTo(21f, 9f)
                lineTo(9f, 9f)
                close()
                
                moveTo(9f, 11f)
                lineTo(21f, 11f)
                lineTo(21f, 13f)
                lineTo(9f, 13f)
                close()
                
                moveTo(9f, 15f)
                lineTo(21f, 15f)
                lineTo(21f, 17f)
                lineTo(9f, 17f)
                close()
                
                moveTo(9f, 19f)
                lineTo(21f, 19f)
                lineTo(21f, 21f)
                lineTo(9f, 21f)
                close()
                
                // Left arrow for outdent
                moveTo(7f, 8f)
                lineTo(3f, 12f)
                lineTo(7f, 16f)
                lineTo(7f, 14f)
                lineTo(5f, 12f)
                lineTo(7f, 10f)
                close()
            }
        }
        return _formatIndentDecrease!!
    }

private var _formatIndentDecrease: ImageVector? = null 