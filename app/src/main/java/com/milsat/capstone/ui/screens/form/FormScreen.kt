package com.milsat.capstone.ui.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.milsat.capstone.ui.components.SpacerHeight
import com.milsat.capstone.ui.screens.form.components.FormFieldItem
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
                }
            )
        },
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
                    items(uiState.formPages[selectedTabIndex].formFields) { item: FormFieldState ->
                        FormFieldItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            formFieldState = item
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
}