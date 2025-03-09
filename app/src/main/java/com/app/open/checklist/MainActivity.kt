package com.app.open.checklist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.app.open.checklist.ui.theme.CheckListTheme
import com.app.open.checklist.viewmodel.MainViewModel

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private val viewmodel: MainViewModel by viewModels()


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckListTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    ContextCompat.getString(
                                        this@MainActivity,
                                        R.string.app_name
                                    )
                                )
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {}, modifier = Modifier.padding(24.dp)) {
                            Icon(Icons.Filled.Add, "Add new item")
                        }
                    }
                ) { innerPadding ->
                    // Add padding to content to respect status bar and top app bar
                    Greeting(
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
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    Box(contentAlignment = Alignment.TopCenter, modifier = modifier) {
        Column {
            Item(
                name,
                viewModel.uiState2.collectAsState().value.isTaskCompleted,
                { viewModel.updateCheckStatusuiState2() }
            )
            Item(
                name,
                viewModel.uiState3.collectAsState().value.isTaskCompleted,
                    { viewModel.updateCheckStatusuiState3() }
            )
            Item(
                name,
                viewModel.uiState.collectAsState().value.isTaskCompleted,
                    { viewModel.updateCheckStatusuiState() }
            )
            NewItem()
        }
    }
}

@Composable
private fun Item(
    name: String,
    isCheckedRoot: Boolean = false,
    onCLick: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp),
        onClick = {
            onCLick()
        }
    ) {

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
                text = "Hello $name!", textAlign = TextAlign.Center

            )
        }
    }
}


@Composable
private fun NewItem() {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp),
        onClick = {}
    ) {

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
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.d(TAG, "NewItem: new task done")
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CheckListTheme {
        Greeting("Android")
    }
}