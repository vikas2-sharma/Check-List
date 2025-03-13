package com.app.open.checklist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.open.checklist.ui.theme.CheckListTheme
import com.app.open.checklist.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckListTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        title = {
                            Row {
                                Text(
                                    ContextCompat.getString(
                                        this@MainActivity, R.string.app_name
                                    )
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { Log.d(TAG, "onCreate: history")}
                            ) {
                                Icon(painterResource(R.drawable.baseline_history_24), "history")
                            }
                        }
                    )
                }) { innerPadding ->
                    // Add padding to content to respect status bar and top app bar
                    MainScreen(
                        name = "Android",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding) // Adds padding for status bar and top app bar
                    )
                }
            }
        }

    }
}

@Composable
fun MainScreen(
    name: String, modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()
) {
    Box(
        contentAlignment = Alignment.TopCenter, modifier = modifier.fillMaxSize(
        )
    ) {
        Column {
            TaskListScreen()
        }
        if (viewModel.deleteButtonVisible.collectAsState().value) {
            Button(
                onClick = {
                    Log.d(TAG, "button to delete: ")
                    viewModel.deleteTasks()
                    viewModel.toggleDeleteButtonVisibility()
                },
                modifier = Modifier
                    .padding(50.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }

}

@Composable
fun TaskListScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiStateList.collectAsState();
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            NewItem()
            LazyColumn(state = listState) {
                itemsIndexed(items = uiState) { index, taskItem ->
                    Item(name = "${index + 1}. ${taskItem.taskName}",
                        isCheckedRoot = taskItem.isTaskCompleted,
                        onCLick = {
                            viewModel.toggleTaskCompletion(index)
                            viewModel.toggleDeleteButtonVisibility()
                        }
                    )
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = {
                coroutineScope.launch {
//                    listState.scrollToItem(0)
//                    listState.animateScrollBy(500f)
//                    delay(2000)
                    viewModel.toggleNewItemVisibility()
                }
            },
            icon = { Icon(Icons.Filled.Add, "Add new item") },
            text = { Text(text = "Add New") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

@Composable
private fun Item(
    name: String,
    isCheckedRoot: Boolean = false,
    onCLick: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp, 5.dp), onClick = {
        onCLick()
    }) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Checkbox(checked = isCheckedRoot, onCheckedChange = {
                onCLick()
            })
            Text(
                text = name, textAlign = TextAlign.Center

            )
        }
    }
}


@Composable
private fun NewItem(viewModel: MainViewModel = viewModel()) {

    val newItemVisibility by viewModel.newItemVisible.collectAsState()
    Log.d(TAG, "NewItem: newItemVisibility: $newItemVisibility")
    if (newItemVisibility) {
        var text by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp), onClick = {}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Enter new item") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        Log.d(TAG, "NewItem: new task done")
                        keyboardController?.hide()
                        if (text.isNotBlank()) {
                            viewModel.addTask(text)
                        }
                        text = ""
                        viewModel.toggleNewItemVisibility()
                    }),
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CheckListTheme {
        MainScreen("Android")
    }
}