package com.milsat.capstone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.milsat.capstone.utils.ifElse

@Composable
fun UIComponentTextField(
    modifier: Modifier = Modifier,
    basicTextFieldModifier: Modifier = Modifier,
    value: String,
    hint: String,
    hintAlignment: Alignment = Alignment.CenterStart,
    label: String = "",
    labelColor: Color = Color(0XFF222222),
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: Int? = null,
    leadingIconTint: Color? = null,
    leadingIconContentDescription: String = "",
    onLeadingIconClicked: () -> Unit = {},
    leadingIconSize: Dp = 12.dp,
    leadingIconRightPadding: Dp = 5.dp,
    trailingIcon: ImageVector? = null,
    trailingIconTint: Color? = null,
    trailingIconContentDescription: String = "",
    onTrailingIconClicked: () -> Unit = {},
    trailingIconSize: Dp = 24.dp,
    onContainerClicked: (() -> Unit)? = null,
    requestFocusOnStart: Boolean = false,
    height: Dp = 59.dp,
    textAlignment: Alignment.Vertical = Alignment.CenterVertically,
    hintColor: Color = Color(0XFF737373),
    disableColor: Color = Color(0XFFC4C4C4),
    colorBorder: Color = Color.LightGray,
    textColor: Color = hintColor,
    shape: Shape = RoundedCornerShape(8.dp),
    minimumLines: Int = 1,
    singleLine: Boolean = true,
    isRequired: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }
    var borderWidth by remember { mutableStateOf(0.dp) }
    var borderColor by remember { mutableStateOf(Color.Transparent) }
    val focusRequester = remember { FocusRequester() }
    val localSoftKeyboard = LocalSoftwareKeyboardController.current
//    var hasStartedTyping by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isFocused, isError, isEnabled) {
        borderWidth = 1.dp
        borderColor = if(isError) Color(0xFFBA1A1A) else if (isEnabled) colorBorder else disableColor
    }

    LaunchedEffect(key1 = requestFocusOnStart) {
        if (requestFocusOnStart && isFocused.not()) focusRequester.requestFocus()
    }

    Column(
        modifier = modifier,
    ) {
        if (label.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    lineHeight = 27.sp,
                    color = if (isEnabled) labelColor else disableColor
                )
                if (isRequired.not()) return@Row
                Text(
                    text = "*",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    lineHeight = 27.sp,
                    color = if (isEnabled) Color.Red else disableColor
                )
            }
            SpacerHeight(height = 10.dp)
        }
        Surface(
            modifier = Modifier
                .clickable {
                    onContainerClicked?.invoke()
                    if (isEnabled.not()) return@clickable
                    if (isFocused.not()) focusRequester.requestFocus()
                    localSoftKeyboard?.show()
                }
                .fillMaxWidth(),
            shape = shape,
            color = Color.Transparent,
            contentColor = Color(0XFF737373),
            border = BorderStroke(
                width = borderWidth,
                color = borderColor
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .ifElse(
                        condition = { minimumLines == 1 },
                        ifTrueModifier = Modifier.height(height),
                        ifFalseModifier = Modifier
                    ),
                verticalAlignment = textAlignment,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        leadingIcon?.let {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = leadingIconContentDescription,
                                tint = leadingIconTint ?: Color(0XFF737373),
                                modifier = Modifier
                                    .size(leadingIconSize)
                                    .clickable(enabled = isEnabled, onClick = onLeadingIconClicked)
                            )
                        }
                        BasicTextField(
                            modifier = basicTextFieldModifier
                                .ifElse(
                                    condition = { leadingIcon != null },
                                    ifTrueModifier = Modifier.padding(start = leadingIconRightPadding)
                                )
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    isFocused = it.isFocused
                                }
                                .weight(1f),
                            value = value,
                            onValueChange = {
//                                hasStartedTyping = true
                                onValueChange(it)
                            },
                            keyboardActions = keyboardActions,
                            keyboardOptions = keyboardOptions,
                            singleLine = singleLine,
                            visualTransformation = visualTransformation,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = if (isEnabled) textColor else disableColor
                            ),
                            enabled = isEnabled,
                            readOnly = isReadOnly,
                            cursorBrush = SolidColor(textColor),
                            interactionSource = MutableInteractionSource()
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect {
                                            if (it is PressInteraction.Release) {
                                                onContainerClicked?.invoke()
                                            }
                                        }
                                    }
                                },
                            minLines = minimumLines
                        )
                        trailingIcon?.run {
                            SpacerWidth(width = 10.dp)
                            Icon(
                                imageVector = this,
                                contentDescription = trailingIconContentDescription,
                                tint = trailingIconTint ?: Color(0XFF737373),
                                modifier = Modifier
                                    .clickable(enabled = isEnabled, onClick = onTrailingIconClicked)
                            )
                        }
                    }
                    if (value.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .align(hintAlignment)
                                .ifElse(
                                    condition = { leadingIcon != null },
                                    ifTrueModifier = Modifier
                                        .padding(start = leadingIconSize)
                                        .padding(start = leadingIconRightPadding)
                                ),
                            text = hint,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 27.sp,
                            color = if (isEnabled) hintColor else disableColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        errorMessage?.run {
            if (isError) {
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFBA1A1A),
                    lineHeight = 12.sp
                )
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun UIComponentTextFieldWithDropDown(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    label: String = "",
    labelColor: Color = Color(0XFF222222),
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: Int? = null,
    leadingIconTint: Color? = null,
    leadingIconContentDescription: String = "",
    onLeadingIconClicked: () -> Unit = {},
    leadingIconSize: Dp = 12.dp,
    trailingIcon: ImageVector? = Icons.Rounded.KeyboardArrowDown,
    trailingIconContentDescription: String = "",
    onTrailingIconClicked: () -> Unit = {},
    trailingIconSize: Dp = 12.dp,
    requestFocusOnStart: Boolean = false,
    height: Dp = 59.dp,
    textAlignment: Alignment.Vertical = Alignment.CenterVertically,
    hintColor: Color = Color(0XFF737373),
    options: List<String>,
    onOptionClicked: (String) -> Unit,
    disableColor: Color = Color(0XFFC4C4C4),
    colorBorder: Color = Color.LightGray,
    enabledDropDown: Boolean = true,
    trailingIconTint: Color? = colorBorder,
    textColor: Color = hintColor,
    dropDownBackgroundColor: Color = Color.White,
    dropDownContentColor: Color = Color.Black,
    isRequired: Boolean = false,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = enabledDropDown) {
        if (enabledDropDown.not()) {
            expanded = false
        }
    }

    BoxWithConstraints(
        modifier = Modifier
    ) {
        UIComponentTextField(
            modifier = modifier,
            basicTextFieldModifier = Modifier,
            value = value,
            hint = hint,
            label = label,
            onValueChange = {
                onValueChange(it)
                if (options.isNotEmpty()) expanded = true
            },
            isError = isError,
            errorMessage = errorMessage,
            isEnabled = isEnabled,
            isReadOnly = isReadOnly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            leadingIconTint = leadingIconTint,
            leadingIconContentDescription = leadingIconContentDescription,
            onLeadingIconClicked = onLeadingIconClicked,
            leadingIconSize = leadingIconSize,
            trailingIcon = trailingIcon,
            trailingIconTint = trailingIconTint,
            trailingIconContentDescription = trailingIconContentDescription,
            onTrailingIconClicked = onTrailingIconClicked,
            trailingIconSize = trailingIconSize,
            onContainerClicked = {
                if (isEnabled) {
                    expanded = true
                }
            },
            requestFocusOnStart = requestFocusOnStart,
            height = height,
            textAlignment = textAlignment,
            hintColor = hintColor,
            labelColor = labelColor,
            disableColor = disableColor,
            colorBorder = colorBorder,
            textColor = textColor,
            isRequired = isRequired,
        )

        DropdownMenu(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .background(dropDownBackgroundColor)
                .width(this.maxWidth),
            expanded = expanded && options.isNotEmpty(),
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false),
            offset = DpOffset(0.dp, (-15).dp),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = dropDownContentColor,
                            fontWeight = FontWeight(400),
                            fontSize = 20.sp,
                        )
                    },
                    onClick = {
                        onOptionClicked(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun rememberKeyboardVisibilityAsState(): Boolean {
    return WindowInsets.isImeVisible
}