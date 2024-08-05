package com.milsat.capstone.ui.screens.home

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.milsat.core.common.Logger
import com.milsat.core.presentation.JsonFileSelector

class HomeScreenViewModel(
    val jsonFileSelector: JsonFileSelector
) : ViewModel() {


    fun selectNewConfigurationFile(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        jsonFileSelector.selectJsonFile(
            launcher = launcher,
            onJsonFileSelected = {
                Logger.debug(TAG, it)
            },
            onError = {
                Logger.debug(TAG, it)
            }
        )
    }

    companion object {
        private const val TAG = "HomeScreenViewModel"
    }
}