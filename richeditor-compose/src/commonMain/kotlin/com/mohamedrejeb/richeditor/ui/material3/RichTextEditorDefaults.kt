package com.mohamedrejeb.richeditor.ui.material3

/**
 * RichTextEditorDefaults - Default values, colors, and styling configurations for Material 3 Rich Text Editors.
 * 
 * This object provides comprehensive default configurations for Material Design 3 rich text editing components:
 * 
 * **Shape and Dimension Defaults**:
 * - `outlinedShape`: Default shape for outlined text fields with rounded corners
 * - `filledShape`: Default shape for filled text fields with top-rounded corners
 * - `MinHeight`: Minimum height constraint (56.dp) for touch accessibility
 * - `MinWidth`: Minimum width constraint (280.dp) for usability
 * - `UnfocusedBorderThickness`: Border thickness for unfocused state (1.dp)
 * - `FocusedBorderThickness`: Border thickness for focused state (2.dp)
 * 
 * **Color System Integration**:
 * - `richTextEditorColors()`: Complete color scheme for filled text fields
 * - `outlinedRichTextEditorColors()`: Complete color scheme for outlined text fields
 * - **State-Aware Colors**: Different colors for enabled, disabled, focused, and error states
 * - **Semantic Colors**: Proper color roles for text, containers, indicators, and icons
 * - **Theme Adaptation**: Automatic adaptation to light/dark themes and color schemes
 * 
 * **Padding and Spacing Configurations**:
 * - `richTextEditorWithLabelPadding()`: Padding for text fields with floating labels
 * - `richTextEditorWithoutLabelPadding()`: Padding for text fields without labels
 * - `outlinedRichTextEditorPadding()`: Specific padding for outlined variants
 * - `supportingTextPadding()`: Padding for helper/error text below fields
 * 
 * **Container and Decoration Components**:
 * - `FilledContainerBox()`: Background container for filled text fields
 * - `OutlinedBorderContainerBox()`: Border container for outlined text fields
 * - `RichTextEditorDecorationBox()`: Complete decoration system for filled fields
 * - `OutlinedRichTextEditorDecorationBox()`: Complete decoration system for outlined fields
 * 
 * **Animation and Interaction**:
 * - **Border Animations**: Smooth border color and thickness transitions
 * - **Focus Animations**: Animated focus indicators and state changes
 * - **Color Transitions**: Smooth color transitions between different states
 * - **Label Animations**: Floating label animations with proper timing
 * 
 * **Accessibility Features**:
 * - **Touch Target Sizes**: Minimum touch target sizes for accessibility
 * - **Color Contrast**: High contrast ratios for text and background colors
 * - **Focus Indicators**: Clear visual focus indicators for keyboard navigation
 * - **Screen Reader Support**: Proper semantic structure for assistive technologies
 * 
 * **Customization Support**:
 * - **Color Overrides**: All colors can be customized while maintaining consistency
 * - **Shape Customization**: Custom shapes for different design requirements
 * - **Padding Adjustments**: Flexible padding configurations for dense layouts
 * - **Animation Customization**: Configurable animation durations and curves
 * 
 * **Usage Patterns**:
 * - **Default Styling**: Use default functions for standard Material 3 appearance
 * - **Theme Integration**: Automatically adapts to Material 3 theme changes
 * - **Custom Themes**: Override specific values for custom design systems
 * - **Consistent Styling**: Ensures consistency across all rich text editor instances
 * 
 * This defaults system ensures that rich text editors maintain visual consistency with
 * Material Design 3 principles while providing flexibility for customization and theming.
 */

import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.ui.material3.tokens.FiledRichTextEditorTokens
import com.mohamedrejeb.richeditor.ui.material3.tokens.OutlinedRichTextEditorTokens

/**
 * Contains the default values used by [TextField] and [OutlinedTextField].
 */
@ExperimentalMaterial3Api
@Immutable
public object RichTextEditorDefaults {
    /** Default shape for an outlined text field. */
    public val outlinedShape: Shape @Composable get() = OutlinedRichTextEditorTokens.ContainerShape.toShape()

    /** Default shape for a filled text field. */
    public val filledShape: Shape @Composable get() = FiledRichTextEditorTokens.ContainerShape.toShape()

    /**
     * The default min width applied for a [TextField] and [OutlinedTextField].
     * Note that you can override it by applying Modifier.heightIn directly on a text field.
     */
    public val MinHeight: Dp = 56.dp

    /**
     * The default min width applied for a [TextField] and [OutlinedTextField].
     * Note that you can override it by applying Modifier.widthIn directly on a text field.
     */
    public val MinWidth: Dp = 280.dp

    /**
     * The default thickness of the border in [OutlinedTextField] or indicator line in [TextField]
     * in unfocused state.
     */
    public val UnfocusedBorderThickness: Dp = 1.dp

    /**
     * The default thickness of the border in [OutlinedTextField] or indicator line in [TextField]
     * in focused state.
     */
    public val FocusedBorderThickness: Dp = 2.dp

    /**
     * Composable that draws a default container for the content of [TextField], with an indicator
     * line at the bottom. You can use it to draw a container for your custom text field based on
     * [RichTextEditorDecorationBox]. [TextField] applies it automatically.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     * @param colors [RichTextEditorColors] used to resolve colors of the text field
     * @param shape shape of the container
     */
    @ExperimentalMaterial3Api
    @Composable
    public fun FilledContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: RichTextEditorColors,
        shape: Shape = filledShape,
    ) {
        Box(
            Modifier
                .background(colors.containerColor().value, shape)
                .indicatorLine(enabled, isError, interactionSource, colors))
    }

    /**
     * A modifier to draw a default bottom indicator line in [TextField]. You can use this modifier
     * if you build your custom text field using [RichTextEditorDecorationBox] whilst the [TextField]
     * applies it automatically.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     * @param colors [RichTextEditorColors] used to resolve colors of the text field
     * @param focusedIndicatorLineThickness thickness of the indicator line when text field is
     * focused
     * @param unfocusedIndicatorLineThickness thickness of the indicator line when text field is
     * not focused
     */
    @ExperimentalMaterial3Api
    public fun Modifier.indicatorLine(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: RichTextEditorColors,
        focusedIndicatorLineThickness: Dp = FocusedBorderThickness,
        unfocusedIndicatorLineThickness: Dp = UnfocusedBorderThickness
    ): Modifier = composed(inspectorInfo = debugInspectorInfo {
        name = "indicatorLine"
        properties["enabled"] = enabled
        properties["isError"] = isError
        properties["interactionSource"] = interactionSource
        properties["colors"] = colors
        properties["focusedIndicatorLineThickness"] = focusedIndicatorLineThickness
        properties["unfocusedIndicatorLineThickness"] = unfocusedIndicatorLineThickness
    }) {
        val stroke = animateBorderStrokeAsState(
            enabled,
            isError,
            interactionSource,
            colors,
            focusedIndicatorLineThickness,
            unfocusedIndicatorLineThickness
        )
        Modifier.drawIndicatorLine(stroke.value)
    }

    /**
     * Composable that draws a default container for [OutlinedTextField] with a border stroke. You
     * can use it to draw a border stroke in your custom text field based on
     * [OutlinedRichTextEditorDecorationBox]. The [OutlinedTextField] applies it automatically.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     * @param colors [RichTextEditorColors] used to resolve colors of the text field
     * @param focusedBorderThickness thickness of the [OutlinedTextField]'s border when it is in
     * focused state
     * @param unfocusedBorderThickness thickness of the [OutlinedTextField]'s border when it is not
     * in focused state
     */
    @ExperimentalMaterial3Api
    @Composable
    public fun OutlinedBorderContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: RichTextEditorColors,
        shape: Shape = OutlinedRichTextEditorTokens.ContainerShape.toShape(),
        focusedBorderThickness: Dp = FocusedBorderThickness,
        unfocusedBorderThickness: Dp = UnfocusedBorderThickness
    ) {
        val borderStroke = animateBorderStrokeAsState(
            enabled,
            isError,
            interactionSource,
            colors,
            focusedBorderThickness,
            unfocusedBorderThickness
        )
        Box(
            Modifier
                .border(borderStroke.value, shape)
                .background(colors.containerColor().value, shape))
    }

    /**
     * Default content padding applied to [TextField] when there is a label.
     *
     * Note that when label is present, the "top" padding (unlike rest of the paddings) is a
     * distance between the label's last baseline and the top edge of the [TextField]. If the "top"
     * value is smaller than the last baseline of the label, then there will be no space between
     * the label and top edge of the [TextField].
     *
     * See [PaddingValues]
     */
    @ExperimentalMaterial3Api
    public fun richTextEditorWithLabelPadding(
        start: Dp = TextFieldPadding,
        end: Dp = TextFieldPadding,
        top: Dp = FirstBaselineOffset,
        bottom: Dp = TextFieldBottomPadding
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    /**
     * Default content padding applied to [TextField] when the label is null.
     * See [PaddingValues] for more details.
     */
    @ExperimentalMaterial3Api
    public fun richTextEditorWithoutLabelPadding(
        start: Dp = TextFieldPadding,
        top: Dp = TextFieldPadding,
        end: Dp = TextFieldPadding,
        bottom: Dp = TextFieldPadding
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    /**
     * Default content padding applied to [OutlinedTextField].
     * See [PaddingValues] for more details.
     */
    @ExperimentalMaterial3Api
    public fun outlinedRichTextEditorPadding(
        start: Dp = TextFieldPadding,
        top: Dp = TextFieldPadding,
        end: Dp = TextFieldPadding,
        bottom: Dp = TextFieldPadding
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    /**
     * Default padding applied to supporting text for both [TextField] and [OutlinedTextField].
     * See [PaddingValues] for more details.
     */
    // TODO(246775477): consider making this public
    @ExperimentalMaterial3Api
    internal fun supportingTextPadding(
        start: Dp = TextFieldPadding,
        top: Dp = SupportingTopPadding,
        end: Dp = TextFieldPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    /**
     * Creates a [RichTextEditorColors] that represents the default input text, container, and content
     * (including label, placeholder, leading and trailing icons) colors used in a [TextField].
     *
     * @param textColor the color used for the input text of this text field
     * @param disabledTextColor the color used for the input text of this text field when disabled
     * @param containerColor the container color for this text field
     * @param cursorColor the cursor color for this text field
     * @param errorCursorColor the cursor color for this text field when in error state
     * @param selectionColors the colors used when the input text of this text field is selected
     * @param focusedIndicatorColor the indicator color for this text field when focused
     * @param unfocusedIndicatorColor the indicator color for this text field when not focused
     * @param disabledIndicatorColor the indicator color for this text field when disabled
     * @param errorIndicatorColor the indicator color for this text field when in error state
     * @param focusedLeadingIconColor the leading icon color for this text field when focused
     * @param unfocusedLeadingIconColor the leading icon color for this text field when not focused
     * @param disabledLeadingIconColor the leading icon color for this text field when disabled
     * @param errorLeadingIconColor the leading icon color for this text field when in error state
     * @param focusedTrailingIconColor the trailing icon color for this text field when focused
     * @param unfocusedTrailingIconColor the trailing icon color for this text field when not
     * focused
     * @param disabledTrailingIconColor the trailing icon color for this text field when disabled
     * @param errorTrailingIconColor the trailing icon color for this text field when in error state
     * @param focusedLabelColor the label color for this text field when focused
     * @param unfocusedLabelColor the label color for this text field when not focused
     * @param disabledLabelColor the label color for this text field when disabled
     * @param errorLabelColor the label color for this text field when in error state
     * @param placeholderColor the placeholder color for this text field
     * @param disabledPlaceholderColor the placeholder color for this text field when disabled
     * @param focusedSupportingTextColor the supporting text color for this text field when focused
     * @param unfocusedSupportingTextColor the supporting text color for this text field when not
     * focused
     * @param disabledSupportingTextColor the supporting text color for this text field when
     * disabled
     * @param errorSupportingTextColor the supporting text color for this text field when in error
     * state
     */
    @ExperimentalMaterial3Api
    @Composable
    public fun richTextEditorColors(
        textColor: Color = FiledRichTextEditorTokens.InputColor.toColor(),
        disabledTextColor: Color = FiledRichTextEditorTokens.DisabledInputColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledInputOpacity),
        containerColor: Color = FiledRichTextEditorTokens.ContainerColor.toColor(),
        cursorColor: Color = FiledRichTextEditorTokens.CaretColor.toColor(),
        errorCursorColor: Color = FiledRichTextEditorTokens.ErrorFocusCaretColor.toColor(),
        selectionColors: TextSelectionColors = LocalTextSelectionColors.current,
        focusedIndicatorColor: Color = FiledRichTextEditorTokens.FocusActiveIndicatorColor.toColor(),
        unfocusedIndicatorColor: Color = FiledRichTextEditorTokens.ActiveIndicatorColor.toColor(),
        disabledIndicatorColor: Color = FiledRichTextEditorTokens.DisabledActiveIndicatorColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledActiveIndicatorOpacity),
        errorIndicatorColor: Color = FiledRichTextEditorTokens.ErrorActiveIndicatorColor.toColor(),
        focusedLeadingIconColor: Color = FiledRichTextEditorTokens.FocusLeadingIconColor.toColor(),
        unfocusedLeadingIconColor: Color = FiledRichTextEditorTokens.LeadingIconColor.toColor(),
        disabledLeadingIconColor: Color = FiledRichTextEditorTokens.DisabledLeadingIconColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledLeadingIconOpacity),
        errorLeadingIconColor: Color = FiledRichTextEditorTokens.ErrorLeadingIconColor.toColor(),
        focusedTrailingIconColor: Color = FiledRichTextEditorTokens.FocusTrailingIconColor.toColor(),
        unfocusedTrailingIconColor: Color = FiledRichTextEditorTokens.TrailingIconColor.toColor(),
        disabledTrailingIconColor: Color = FiledRichTextEditorTokens.DisabledTrailingIconColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledTrailingIconOpacity),
        errorTrailingIconColor: Color = FiledRichTextEditorTokens.ErrorTrailingIconColor.toColor(),
        focusedLabelColor: Color = FiledRichTextEditorTokens.FocusLabelColor.toColor(),
        unfocusedLabelColor: Color = FiledRichTextEditorTokens.LabelColor.toColor(),
        disabledLabelColor: Color = FiledRichTextEditorTokens.DisabledLabelColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledLabelOpacity),
        errorLabelColor: Color = FiledRichTextEditorTokens.ErrorLabelColor.toColor(),
        placeholderColor: Color = FiledRichTextEditorTokens.InputPlaceholderColor.toColor(),
        disabledPlaceholderColor: Color = FiledRichTextEditorTokens.DisabledInputColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledInputOpacity),
        focusedSupportingTextColor: Color = FiledRichTextEditorTokens.FocusSupportingColor.toColor(),
        unfocusedSupportingTextColor: Color = FiledRichTextEditorTokens.SupportingColor.toColor(),
        disabledSupportingTextColor: Color = FiledRichTextEditorTokens.DisabledSupportingColor.toColor()
            .copy(alpha = FiledRichTextEditorTokens.DisabledSupportingOpacity),
        errorSupportingTextColor: Color = FiledRichTextEditorTokens.ErrorSupportingColor.toColor(),
    ): RichTextEditorColors =
        RichTextEditorColors(
            textColor = textColor,
            disabledTextColor = disabledTextColor,
            containerColor = containerColor,
            cursorColor = cursorColor,
            errorCursorColor = errorCursorColor,
            textSelectionColors = selectionColors,
            focusedIndicatorColor = focusedIndicatorColor,
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            errorIndicatorColor = errorIndicatorColor,
            disabledIndicatorColor = disabledIndicatorColor,
            focusedLeadingIconColor = focusedLeadingIconColor,
            unfocusedLeadingIconColor = unfocusedLeadingIconColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            errorLeadingIconColor = errorLeadingIconColor,
            focusedTrailingIconColor = focusedTrailingIconColor,
            unfocusedTrailingIconColor = unfocusedTrailingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
            errorTrailingIconColor = errorTrailingIconColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            disabledLabelColor = disabledLabelColor,
            errorLabelColor = errorLabelColor,
            placeholderColor = placeholderColor,
            disabledPlaceholderColor = disabledPlaceholderColor,
            focusedSupportingTextColor = focusedSupportingTextColor,
            unfocusedSupportingTextColor = unfocusedSupportingTextColor,
            disabledSupportingTextColor = disabledSupportingTextColor,
            errorSupportingTextColor = errorSupportingTextColor,
        )

    /**
     * Creates a [RichTextEditorColors] that represents the default input text, container, and content
     * (including label, placeholder, leading and trailing icons) colors used in an
     * [OutlinedTextField].
     *
     * @param textColor the color used for the input text of this text field
     * @param disabledTextColor the color used for the input text of this text field when disabled
     * @param containerColor the container color for this text field
     * @param cursorColor the cursor color for this text field
     * @param errorCursorColor the cursor color for this text field when in error state
     * @param selectionColors the colors used when the input text of this text field is selected
     * @param focusedBorderColor the border color for this text field when focused
     * @param unfocusedBorderColor the border color for this text field when not focused
     * @param disabledBorderColor the border color for this text field when disabled
     * @param errorBorderColor the border color for this text field when in error state
     * @param focusedLeadingIconColor the leading icon color for this text field when focused
     * @param unfocusedLeadingIconColor the leading icon color for this text field when not focused
     * @param disabledLeadingIconColor the leading icon color for this text field when disabled
     * @param errorLeadingIconColor the leading icon color for this text field when in error state
     * @param focusedTrailingIconColor the trailing icon color for this text field when focused
     * @param unfocusedTrailingIconColor the trailing icon color for this text field when not focused
     * @param disabledTrailingIconColor the trailing icon color for this text field when disabled
     * @param errorTrailingIconColor the trailing icon color for this text field when in error state
     * @param focusedLabelColor the label color for this text field when focused
     * @param unfocusedLabelColor the label color for this text field when not focused
     * @param disabledLabelColor the label color for this text field when disabled
     * @param errorLabelColor the label color for this text field when in error state
     * @param placeholderColor the placeholder color for this text field
     * @param disabledPlaceholderColor the placeholder color for this text field when disabled
     * @param focusedSupportingTextColor the supporting text color for this text field when focused
     * @param unfocusedSupportingTextColor the supporting text color for this text field when not
     * focused
     * @param disabledSupportingTextColor the supporting text color for this text field when
     * disabled
     * @param errorSupportingTextColor the supporting text color for this text field when in error
     * state
     */
    @ExperimentalMaterial3Api
    @Composable
    public fun outlinedRichTextEditorColors(
        textColor: Color = OutlinedRichTextEditorTokens.InputColor.toColor(),
        disabledTextColor: Color = OutlinedRichTextEditorTokens.DisabledInputColor.toColor()
            .copy(alpha = OutlinedRichTextEditorTokens.DisabledInputOpacity),
        containerColor: Color = Color.Transparent,
        cursorColor: Color = OutlinedRichTextEditorTokens.CaretColor.toColor(),
        errorCursorColor: Color = OutlinedRichTextEditorTokens.ErrorFocusCaretColor.toColor(),
        selectionColors: TextSelectionColors = LocalTextSelectionColors.current,
        focusedBorderColor: Color = OutlinedRichTextEditorTokens.FocusOutlineColor.toColor(),
        unfocusedBorderColor: Color = OutlinedRichTextEditorTokens.OutlineColor.toColor(),
        disabledBorderColor: Color = OutlinedRichTextEditorTokens.DisabledOutlineColor.toColor()
            .copy(alpha = OutlinedRichTextEditorTokens.DisabledOutlineOpacity),
        errorBorderColor: Color = OutlinedRichTextEditorTokens.ErrorOutlineColor.toColor(),
        focusedLeadingIconColor: Color = OutlinedRichTextEditorTokens.FocusLeadingIconColor.toColor(),
        unfocusedLeadingIconColor: Color = OutlinedRichTextEditorTokens.LeadingIconColor.toColor(),
        disabledLeadingIconColor: Color = OutlinedRichTextEditorTokens.DisabledLeadingIconColor.toColor()
            .copy(alpha = OutlinedRichTextEditorTokens.DisabledLeadingIconOpacity),
        errorLeadingIconColor: Color = OutlinedRichTextEditorTokens.ErrorLeadingIconColor.toColor(),
        focusedTrailingIconColor: Color = OutlinedRichTextEditorTokens.FocusTrailingIconColor.toColor(),
        unfocusedTrailingIconColor: Color = OutlinedRichTextEditorTokens.TrailingIconColor.toColor(),
        disabledTrailingIconColor: Color = OutlinedRichTextEditorTokens.DisabledTrailingIconColor
            .toColor().copy(alpha = OutlinedRichTextEditorTokens.DisabledTrailingIconOpacity),
        errorTrailingIconColor: Color = OutlinedRichTextEditorTokens.ErrorTrailingIconColor.toColor(),
        focusedLabelColor: Color = OutlinedRichTextEditorTokens.FocusLabelColor.toColor(),
        unfocusedLabelColor: Color = OutlinedRichTextEditorTokens.LabelColor.toColor(),
        disabledLabelColor: Color = OutlinedRichTextEditorTokens.DisabledLabelColor.toColor()
            .copy(alpha = OutlinedRichTextEditorTokens.DisabledLabelOpacity),
        errorLabelColor: Color = OutlinedRichTextEditorTokens.ErrorLabelColor.toColor(),
        placeholderColor: Color = OutlinedRichTextEditorTokens.InputPlaceholderColor.toColor(),
        disabledPlaceholderColor: Color = OutlinedRichTextEditorTokens.DisabledInputColor.toColor()
            .copy(alpha = OutlinedRichTextEditorTokens.DisabledInputOpacity),
        focusedSupportingTextColor: Color = OutlinedRichTextEditorTokens.FocusSupportingColor.toColor(),
        unfocusedSupportingTextColor: Color = OutlinedRichTextEditorTokens.SupportingColor.toColor(),
        disabledSupportingTextColor: Color = OutlinedRichTextEditorTokens.DisabledSupportingColor
            .toColor().copy(alpha = OutlinedRichTextEditorTokens.DisabledSupportingOpacity),
        errorSupportingTextColor: Color = OutlinedRichTextEditorTokens.ErrorSupportingColor.toColor(),
    ): RichTextEditorColors =
        RichTextEditorColors(
            textColor = textColor,
            disabledTextColor = disabledTextColor,
            cursorColor = cursorColor,
            errorCursorColor = errorCursorColor,
            textSelectionColors = selectionColors,
            focusedIndicatorColor = focusedBorderColor,
            unfocusedIndicatorColor = unfocusedBorderColor,
            errorIndicatorColor = errorBorderColor,
            disabledIndicatorColor = disabledBorderColor,
            focusedLeadingIconColor = focusedLeadingIconColor,
            unfocusedLeadingIconColor = unfocusedLeadingIconColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            errorLeadingIconColor = errorLeadingIconColor,
            focusedTrailingIconColor = focusedTrailingIconColor,
            unfocusedTrailingIconColor = unfocusedTrailingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
            errorTrailingIconColor = errorTrailingIconColor,
            containerColor = containerColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            disabledLabelColor = disabledLabelColor,
            errorLabelColor = errorLabelColor,
            placeholderColor = placeholderColor,
            disabledPlaceholderColor = disabledPlaceholderColor,
            focusedSupportingTextColor = focusedSupportingTextColor,
            unfocusedSupportingTextColor = unfocusedSupportingTextColor,
            disabledSupportingTextColor = disabledSupportingTextColor,
            errorSupportingTextColor = errorSupportingTextColor,
        )

    /**
     * A decoration box which helps creating custom text fields based on
     * <a href="https://material.io/components/text-fields#filled-text-field" class="external" target="_blank">Material Design filled text field</a>.
     *
     * If your text field requires customising elements that aren't exposed by [TextField],
     * consider using this decoration box to achieve the desired design.
     *
     * For example, if you need to create a dense text field, use [contentPadding] parameter to
     * decrease the paddings around the input field. If you need to customise the bottom indicator,
     * apply [indicatorLine] modifier to achieve that.
     *
     * See example of using [RichTextEditorDecorationBox] to build your own custom text field
     * @sample androidx.compose.material3.samples.CustomTextFieldBasedOnDecorationBox
     *
     * @param value the input [String] shown by the text field
     * @param innerTextField input text field that this decoration box wraps. You will pass here a
     * framework-controlled composable parameter "innerTextField" from the decorationBox lambda of
     * the [BasicTextField]
     * @param enabled controls the enabled state of the text field. When `false`, this component
     * will not respond to user input, and it will appear visually disabled and disabled to
     * accessibility services. You must also pass the same value to the [BasicTextField] for it to
     * adjust the behavior accordingly.
     * @param singleLine indicates if this is a single line or multi line text field. You must pass
     * the same value as to [BasicTextField].
     * @param visualTransformation transforms the visual representation of the input [value]. You
     * must pass the same value as to [BasicTextField].
     * @param interactionSource the read-only [InteractionSource] representing the stream of
     * [Interaction]s for this text field. You must first create and pass in your own `remember`ed
     * [MutableInteractionSource] instance to the [BasicTextField] for it to dispatch events. And
     * then pass the same instance to this decoration box to observe [Interaction]s and customize
     * the appearance / behavior of this text field in different states.
     * @param isError indicates if the text field's current value is in error state. If set to
     * true, the label, bottom indicator and trailing icon by default will be displayed in error
     * color.
     * @param label the optional label to be displayed inside the text field container. The default
     * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
     * [Typography.bodyLarge] when the text field is not in focus.
     * @param placeholder the optional placeholder to be displayed when the text field is in focus
     * and the input text is empty. The default text style for internal [Text] is
     * [Typography.bodyLarge].
     * @param leadingIcon the optional leading icon to be displayed at the beginning of the text
     * field container
     * @param trailingIcon the optional trailing icon to be displayed at the end of the text field
     * container
     * @param supportingText the optional supporting text to be displayed below the text field
     * @param colors [RichTextEditorColors] that will be used to resolve the colors used for this text
     * field in different states. See [TextFieldDefaults.textFieldColors].
     * @param contentPadding the spacing values to apply internally between the internals of text
     * field and the decoration box container. You can use it to implement dense text fields or
     * simply to control horizontal padding. See [TextFieldDefaults.textFieldWithLabelPadding] and
     * [TextFieldDefaults.textFieldWithoutLabelPadding]
     * Note that if there's a label in the text field, the [top][PaddingValues.calculateTopPadding]
     * padding will mean the distance from label's [last baseline][LastBaseline] to the top edge of
     * the container. All other paddings mean the distance from the corresponding edge of the
     * container to the corresponding edge of the closest to it element
     * @param container the container to be drawn behind the text field. By default, this includes
     * the bottom indicator line. Default colors for the container come from the [colors].
     */
    @Composable
    @ExperimentalMaterial3Api
    public fun RichTextEditorDecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        singleLine: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        shape: Shape = filledShape,
        colors: RichTextEditorColors = richTextEditorColors(),
        contentPadding: PaddingValues =
            if (label == null) {
                richTextEditorWithoutLabelPadding()
            } else {
                richTextEditorWithLabelPadding()
            },
        container: @Composable () -> Unit = {
            FilledContainerBox(enabled, isError, interactionSource, colors, shape)
        }
    ) {
        CommonDecorationBox(
            type = TextFieldType.Filled,
            value = value,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding,
            container = container
        )
    }

    /**
     * A decoration box which helps creating custom text fields based on
     * <a href="https://material.io/components/text-fields#outlined-text-field" class="external" target="_blank">Material Design outlined text field</a>.
     *
     * If your text field requires customising elements that aren't exposed by [OutlinedTextField],
     * consider using this decoration box to achieve the desired design.
     *
     * For example, if you need to create a dense outlined text field, use [contentPadding]
     * parameter to decrease the paddings around the input field. If you need to change the
     * thickness of the border, use [container] parameter to achieve that.
     *
     * Example of custom text field based on [OutlinedRichTextEditorDecorationBox]:
     * @sample androidx.compose.material3.samples.CustomOutlinedTextFieldBasedOnDecorationBox
     *
     * @param value the input [String] shown by the text field
     * @param innerTextField input text field that this decoration box wraps. You will pass here a
     * framework-controlled composable parameter "innerTextField" from the decorationBox lambda of
     * the [BasicTextField]
     * @param enabled controls the enabled state of the text field. When `false`, this component
     * will not respond to user input, and it will appear visually disabled and disabled to
     * accessibility services. You must also pass the same value to the [BasicTextField] for it to
     * adjust the behavior accordingly.
     * @param singleLine indicates if this is a single line or multi line text field. You must pass
     * the same value as to [BasicTextField].
     * @param visualTransformation transforms the visual representation of the input [value]. You
     * must pass the same value as to [BasicTextField].
     * @param interactionSource the read-only [InteractionSource] representing the stream of
     * [Interaction]s for this text field. You must first create and pass in your own `remember`ed
     * [MutableInteractionSource] instance to the [BasicTextField] for it to dispatch events. And
     * then pass the same instance to this decoration box to observe [Interaction]s and customize
     * the appearance / behavior of this text field in different states.
     * @param isError indicates if the text field's current value is in error state. If set to
     * true, the label, bottom indicator and trailing icon by default will be displayed in error
     * color.
     * @param label the optional label to be displayed inside the text field container. The default
     * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
     * [Typography.bodyLarge] when the text field is not in focus.
     * @param placeholder the optional placeholder to be displayed when the text field is in focus
     * and the input text is empty. The default text style for internal [Text] is
     * [Typography.bodyLarge].
     * @param leadingIcon the optional leading icon to be displayed at the beginning of the text
     * field container
     * @param trailingIcon the optional trailing icon to be displayed at the end of the text field
     * container
     * @param supportingText the optional supporting text to be displayed below the text field
     * @param colors [RichTextEditorColors] that will be used to resolve the colors used for this text
     * field in different states. See [TextFieldDefaults.outlinedTextFieldColors].
     * @param contentPadding the spacing values to apply internally between the internals of text
     * field and the decoration box container. You can use it to implement dense text fields or
     * simply to control horizontal padding. See [TextFieldDefaults.outlinedTextFieldPadding].
     * @param container the container to be drawn behind the text field. By default, this is
     * transparent and only includes a border. The cutout in the border to fit the [label] will be
     * automatically added by the framework. Note that by default the color of the border comes from
     * the [colors].
     */
    @Composable
    @ExperimentalMaterial3Api
    public fun OutlinedRichTextEditorDecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        singleLine: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        colors: RichTextEditorColors = outlinedRichTextEditorColors(),
        contentPadding: PaddingValues = outlinedRichTextEditorPadding(),
        container: @Composable () -> Unit = {
            OutlinedBorderContainerBox(enabled, isError, interactionSource, colors)
        }
    ) {
        CommonDecorationBox(
            type = TextFieldType.Outlined,
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding,
            container = container
        )
    }
}

/**
 * Represents the colors of the input text, container, and content (including label, placeholder,
 * leading and trailing icons) used in a text field in different states.
 *
 * See [TextFieldDefaults.textFieldColors] for the default colors used in [TextField].
 * See [TextFieldDefaults.outlinedTextFieldColors] for the default colors used in
 * [OutlinedTextField].
 */
@Immutable
public class RichTextEditorColors internal constructor(
    private val textColor: Color,
    private val disabledTextColor: Color,
    private val containerColor: Color,
    private val cursorColor: Color,
    private val errorCursorColor: Color,
    private val textSelectionColors: TextSelectionColors,
    private val focusedIndicatorColor: Color,
    private val unfocusedIndicatorColor: Color,
    private val errorIndicatorColor: Color,
    private val disabledIndicatorColor: Color,
    private val focusedLeadingIconColor: Color,
    private val unfocusedLeadingIconColor: Color,
    private val disabledLeadingIconColor: Color,
    private val errorLeadingIconColor: Color,
    private val focusedTrailingIconColor: Color,
    private val unfocusedTrailingIconColor: Color,
    private val disabledTrailingIconColor: Color,
    private val errorTrailingIconColor: Color,
    private val focusedLabelColor: Color,
    private val unfocusedLabelColor: Color,
    private val disabledLabelColor: Color,
    private val errorLabelColor: Color,
    private val placeholderColor: Color,
    private val disabledPlaceholderColor: Color,
    private val focusedSupportingTextColor: Color,
    private val unfocusedSupportingTextColor: Color,
    private val disabledSupportingTextColor: Color,
    private val errorSupportingTextColor: Color,
) {
    /**
     * Represents the color used for the leading icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun leadingIconColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledLeadingIconColor
                isError -> errorLeadingIconColor
                focused -> focusedLeadingIconColor
                else -> unfocusedLeadingIconColor
            }
        )
    }

    /**
     * Represents the color used for the trailing icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun trailingIconColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledTrailingIconColor
                isError -> errorTrailingIconColor
                focused -> focusedTrailingIconColor
                else -> unfocusedTrailingIconColor
            }
        )
    }

    /**
     * Represents the color used for the border indicator of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun indicatorColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> disabledIndicatorColor
            isError -> errorIndicatorColor
            focused -> focusedIndicatorColor
            else -> unfocusedIndicatorColor
        }
        return if (enabled) {
            animateColorAsState(targetValue, tween(durationMillis = AnimationDuration))
        } else {
            rememberUpdatedState(targetValue)
        }
    }

    /**
     * Represents the container color for this text field.
     */
    @Composable
    internal fun containerColor(): State<Color> {
        return rememberUpdatedState(containerColor)
    }

    /**
     * Represents the color used for the placeholder of this text field.
     *
     * @param enabled whether the text field is enabled
     */
    @Composable
    internal fun placeholderColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) placeholderColor else disabledPlaceholderColor)
    }

    /**
     * Represents the color used for the label of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun labelColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> disabledLabelColor
            isError -> errorLabelColor
            focused -> focusedLabelColor
            else -> unfocusedLabelColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    internal fun textColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) textColor else disabledTextColor)
    }

    @Composable
    internal fun supportingTextColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledSupportingTextColor
                isError -> errorSupportingTextColor
                focused -> focusedSupportingTextColor
                else -> unfocusedSupportingTextColor
            }
        )
    }

    /**
     * Represents the color used for the cursor of this text field.
     *
     * @param isError whether the text field's current value is in error
     */
    @Composable
    internal fun cursorColor(isError: Boolean): State<Color> {
        return rememberUpdatedState(if (isError) errorCursorColor else cursorColor)
    }

    /**
     * Represents the colors used for text selection in this text field.
     */
    internal val selectionColors: TextSelectionColors
        @Composable get() = textSelectionColors

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is RichTextEditorColors) return false

        if (textColor != other.textColor) return false
        if (disabledTextColor != other.disabledTextColor) return false
        if (cursorColor != other.cursorColor) return false
        if (errorCursorColor != other.errorCursorColor) return false
        if (textSelectionColors != other.textSelectionColors) return false
        if (focusedIndicatorColor != other.focusedIndicatorColor) return false
        if (unfocusedIndicatorColor != other.unfocusedIndicatorColor) return false
        if (errorIndicatorColor != other.errorIndicatorColor) return false
        if (disabledIndicatorColor != other.disabledIndicatorColor) return false
        if (focusedLeadingIconColor != other.focusedLeadingIconColor) return false
        if (unfocusedLeadingIconColor != other.unfocusedLeadingIconColor) return false
        if (disabledLeadingIconColor != other.disabledLeadingIconColor) return false
        if (errorLeadingIconColor != other.errorLeadingIconColor) return false
        if (focusedTrailingIconColor != other.focusedTrailingIconColor) return false
        if (unfocusedTrailingIconColor != other.unfocusedTrailingIconColor) return false
        if (disabledTrailingIconColor != other.disabledTrailingIconColor) return false
        if (errorTrailingIconColor != other.errorTrailingIconColor) return false
        if (containerColor != other.containerColor) return false
        if (focusedLabelColor != other.focusedLabelColor) return false
        if (unfocusedLabelColor != other.unfocusedLabelColor) return false
        if (disabledLabelColor != other.disabledLabelColor) return false
        if (errorLabelColor != other.errorLabelColor) return false
        if (placeholderColor != other.placeholderColor) return false
        if (disabledPlaceholderColor != other.disabledPlaceholderColor) return false
        if (focusedSupportingTextColor != other.focusedSupportingTextColor) return false
        if (unfocusedSupportingTextColor != other.unfocusedSupportingTextColor) return false
        if (disabledSupportingTextColor != other.disabledSupportingTextColor) return false
        if (errorSupportingTextColor != other.errorSupportingTextColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = textColor.hashCode()
        result = 31 * result + disabledTextColor.hashCode()
        result = 31 * result + cursorColor.hashCode()
        result = 31 * result + errorCursorColor.hashCode()
        result = 31 * result + textSelectionColors.hashCode()
        result = 31 * result + focusedIndicatorColor.hashCode()
        result = 31 * result + unfocusedIndicatorColor.hashCode()
        result = 31 * result + errorIndicatorColor.hashCode()
        result = 31 * result + disabledIndicatorColor.hashCode()
        result = 31 * result + focusedLeadingIconColor.hashCode()
        result = 31 * result + unfocusedLeadingIconColor.hashCode()
        result = 31 * result + disabledLeadingIconColor.hashCode()
        result = 31 * result + errorLeadingIconColor.hashCode()
        result = 31 * result + focusedTrailingIconColor.hashCode()
        result = 31 * result + unfocusedTrailingIconColor.hashCode()
        result = 31 * result + disabledTrailingIconColor.hashCode()
        result = 31 * result + errorTrailingIconColor.hashCode()
        result = 31 * result + containerColor.hashCode()
        result = 31 * result + focusedLabelColor.hashCode()
        result = 31 * result + unfocusedLabelColor.hashCode()
        result = 31 * result + disabledLabelColor.hashCode()
        result = 31 * result + errorLabelColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        result = 31 * result + disabledPlaceholderColor.hashCode()
        result = 31 * result + focusedSupportingTextColor.hashCode()
        result = 31 * result + unfocusedSupportingTextColor.hashCode()
        result = 31 * result + disabledSupportingTextColor.hashCode()
        result = 31 * result + errorSupportingTextColor.hashCode()
        return result
    }
}

@Composable
private fun animateBorderStrokeAsState(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    colors: RichTextEditorColors,
    focusedBorderThickness: Dp,
    unfocusedBorderThickness: Dp
): State<BorderStroke> {
    val focused by interactionSource.collectIsFocusedAsState()
    val indicatorColor = colors.indicatorColor(enabled, isError, interactionSource)
    val targetThickness = if (focused) focusedBorderThickness else unfocusedBorderThickness
    val animatedThickness = if (enabled) {
        animateDpAsState(targetThickness, tween(durationMillis = AnimationDuration))
    } else {
        rememberUpdatedState(unfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(animatedThickness.value, SolidColor(indicatorColor.value))
    )
}