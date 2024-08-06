package com.milsat.capstone.ui.screens.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.milsat.capstone.R
import com.milsat.capstone.utils.Screens
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.utils.Logger
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToRoute: (String) -> Unit) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val allFormState by viewModel.allFormsState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.jsonFileSelector.handleFileSelection(uri)
            } ?: run {
                Logger.debug("HomeScreen", "File selection cancelled")
            }
        } else {
            Logger.debug("HomeScreen", "File selection failed")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.selectNewConfigurationFile(
                        launcher = launcher,
                        onProceed = { formField ->
                            onNavigateToRoute(
                                Screens.Form.withArgs(
                                    obligatoryParams = listOf(
                                        formField.id,
                                        formField.name
                                    )
                                )
                            )
                        }
                    )
                },
            ) {
                Text(text = "Create a New Form")
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults
                    .topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
            )
        },
        containerColor = Color.White
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = if (allFormState.isEmpty()) Arrangement.Center else Arrangement.Top
            ) {
                if (allFormState.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "No Configuration file has been imported.",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(allFormState) { item: FormEntity ->
                    FormItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        formEntity = item,
                        onClick = {
                            onNavigateToRoute(
                                Screens.Form.withArgs(
                                    obligatoryParams = listOf(
                                        item.id,
                                        item.name
                                    )
                                )
                            )
                        },
                        displayDivider = allFormState.last() != item
                    )
                }
            }
        }
    }
}

@Composable
fun FormItem(
    modifier: Modifier = Modifier,
    formEntity: FormEntity,
    onClick: (FormEntity) -> Unit,
    displayDivider: Boolean
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        onClick = {
            onClick(formEntity)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(text = "#${formEntity.id}", style = MaterialTheme.typography.headlineLarge)
                Column {
                    Text(text = formEntity.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Page count: ${formEntity.pageCount}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (displayDivider) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.5.dp
                )
            }
        }
    }
}