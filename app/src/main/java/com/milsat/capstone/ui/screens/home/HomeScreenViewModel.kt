package com.milsat.capstone.ui.screens.home

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.domain.usecase.CreateNewFormUseCase
import com.milsat.core.domain.usecase.GetAllFormUseCase
import com.milsat.core.utils.Logger
import com.milsat.core.utils.JsonFileSelector
import com.milsat.core.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    val jsonFileSelector: JsonFileSelector,
    private val createNewFormUseCase: CreateNewFormUseCase,
    private val getAllFormUseCase: GetAllFormUseCase
) : ViewModel() {

    val allFormsState = getAllFormUseCase()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

    fun selectNewConfigurationFile(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        jsonFileSelector.selectJsonFile(
            launcher = launcher,
            onJsonFileSelected = { jsonString: String ->
                viewModelScope.launch {
                    when(val formGenerationResult = createNewFormUseCase(jsonString = jsonString)){
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