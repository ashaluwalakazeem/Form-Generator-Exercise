package com.milsat.capstone.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.milsat.capstone.R

/**
 * This is a utility function to generate error messages for our input fields such as (This field is required...)
 * @param fieldName The field label name
 * @param isRequired A boolean expression to get a required error message
 * @param hasFixedLength Enable this if the field requires a specific number of digits or characters
 * @param fixedLength The required specific length
 * @param isDigit This is used to get error message for maximum or minimum length required such as (Phone Number requires 11 digits)
 * @param enableMinimum Enable to return a minimum digits or characters error message
 * @param minimumCount Set the minimum accepted input
 * @param enableMaximum Enable to return a maximum digits or characters error message
 * @param maximumCount Set the maximum accepted input
 * @param isAnInputField Set to denote if the field is a selection field or an input field
 * @param mustMatchAnotherField Set if you want to get an error message like (Confirm password must match password)
 * @param customMessage Set if you want to return a custom message as the last resort error message
 */
@Composable
fun String.generateErrorMessage(
    fieldName: String,
    isRequired: Boolean,
    hasFixedLength: Boolean = false,
    fixedLength: Int = Int.MAX_VALUE,
    isDigit: Boolean = true,
    enableMinimum: Boolean = false,
    minimumCount: Int = 0,
    enableMaximum: Boolean = false,
    maximumCount: Int = Int.MAX_VALUE,
    isAnInputField: Boolean = true,
    mustMatchAnotherField: Pair<Boolean, String> = Pair(false, ""),
    customMessage: String? = null
): String {
    return when {
        this.isBlank() && isRequired -> "$fieldName ${stringResource(R.string.is_required)}"
        hasFixedLength && this.trim().length != fixedLength -> stringResource(
            R.string.requires,
            fieldName,
            fixedLength,
            if (isDigit) stringResource(R.string.digits) else stringResource(R.string.characters)
        )

        enableMinimum && this.trim().length < minimumCount -> stringResource(
            R.string.requires_a_minimum_number_of,
            fieldName,
            minimumCount,
            if (isDigit) stringResource(R.string.digits) else stringResource(R.string.characters)
        )

        enableMaximum && this.trim().length > maximumCount -> stringResource(
            R.string.maximum_is,
            if (isDigit) stringResource(R.string.digits) else stringResource(R.string.characters),
            maximumCount
        )

        mustMatchAnotherField.first -> stringResource(
            R.string.must_match,
            fieldName,
            mustMatchAnotherField.second
        )

        else -> customMessage ?: stringResource(
            R.string.please_a_valid,
            if (isAnInputField) stringResource(R.string.enter) else stringResource(R.string.select),
            fieldName
        )
    }
}

@Composable
fun Boolean.generateErrorMessage(
    isRequired: Boolean,
    fieldName: String,
): String {
    return when {
        isRequired && this.not() -> "$fieldName ${stringResource(R.string.is_required)}"

        else -> "$fieldName ${stringResource(R.string.not_required)}"
    }
}
