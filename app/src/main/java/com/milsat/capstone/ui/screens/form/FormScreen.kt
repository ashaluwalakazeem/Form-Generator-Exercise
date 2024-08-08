package com.milsat.capstone.ui.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.milsat.capstone.R
import com.milsat.capstone.ui.components.SpacerHeight
import com.milsat.capstone.ui.screens.form.components.FieldTitle
import com.milsat.capstone.ui.screens.form.components.FormFieldItem
import com.milsat.capstone.ui.screens.form.components.Index
import com.milsat.core.domain.model.FormFieldState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onNavigateBack: () -> Unit,
    formTitle: String
) {
    val viewModel: FormScreenViewModel = koinViewModel()
    val uiState by viewModel.formPagesState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.LightGray.copy(.4f),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = formTitle, overflow = TextOverflow.Ellipsis, maxLines = 1)
                },
                colors = TopAppBarDefaults
                    .topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::exportForm) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = viewModel::syncToGoogleSheet) {
                        Icon(
                            painter = painterResource(id = R.drawable.cloud_sync_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.submit()
                },
            ) {
                Text(text = "Save Form")
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Save")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            SpacerHeight(height = 10.dp)
            Text(
                text = "Pages:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            if (uiState.formPages.isNotEmpty()) {
                ScrollableTabRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    edgePadding = 20.dp,
                ) {
                    uiState.formPages.forEachIndexed { tabIndex, tab ->
                        Tab(
                            selected = selectedTabIndex == tabIndex,
                            onClick = { selectedTabIndex = tabIndex },
                            text = { Text(text = tab.name) }
                        )
                    }
                }
                SpacerHeight(height = 20.dp)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(uiState.formPages[selectedTabIndex].formFields) { index: Int, item: FormFieldState ->
                        FormFieldItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            formFieldState = item,
                            index = index,
                            onSkipTo = { currentFieldIndex: Index, title: FieldTitle, enabled: Boolean ->
                                viewModel.onSkipTo(
                                    currentFieldIndex = currentFieldIndex,
                                    title,
                                    selectedPageIndex = selectedTabIndex,
                                    enable = enabled
                                )
                            }
                        )
                    }
                    item {}
                    item {}
                    item {}
                    item {}
                    item {}
                }
            }
        }
    }

    if (uiState.isSyncing) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    SpacerHeight(height = 20.dp)
                    Text(
                        text = "Syncing this form to the provided Google Sheet url.",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}