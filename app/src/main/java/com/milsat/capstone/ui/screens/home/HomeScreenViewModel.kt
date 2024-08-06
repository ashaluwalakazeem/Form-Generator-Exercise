package com.milsat.capstone.ui.screens.home

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.Logger
import com.milsat.core.utils.JsonFileSelector
import com.milsat.core.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    val jsonFileSelector: JsonFileSelector,
    private val formRepository: FormRepository
) : ViewModel() {


    fun selectNewConfigurationFile(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        jsonFileSelector.selectJsonFile(
            launcher = launcher,
            onJsonFileSelected = { jsonString: String ->
                viewModelScope.launch(Dispatchers.IO) {
                    when(val formGenerationResult = formRepository.createNewForm(config = jsonString)){
                        is Result.Success -> {
                            Logger.debug(TAG, "Success: ${formGenerationResult.data}")
                        }
                        is Result.Error -> {
                            Logger.error(TAG, formGenerationResult.cause.message)
                        }
                    }
                }
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