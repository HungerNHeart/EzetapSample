package com.task.ezetapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.task.ezetapapp.ui.theme.EzeTapAppTheme
import com.task.network.model.Status
import com.task.network.model.StatusCode
import com.task.network.model.UIDataResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AssignmentViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            AnimatedNavHost(navController = navController, startDestination = "Main") {
                composable(route = "Main") {
                    val data by viewModel.assignmentData.collectAsState(initial = null)
                    val errorCode by viewModel.errorCode.collectAsState(initial = null)
                    val loader by viewModel.assignmentNetworkState.collectAsState(initial = Status.LOADING)
                    val title by viewModel.title.collectAsState(initial = Status.LOADING)
                    EzeTapAppTheme {
                        Scaffold {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it)
                            ) {
                                val (logo, headingText, proceedButton) = createRefs()
                                Box(modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .aspectRatio(1.0f)
                                    .constrainAs(logo) {
                                        linkTo(parent.top, parent.bottom)
                                        linkTo(parent.start, parent.end)
                                    }) {
                                    AnimatedContent(
                                        modifier = Modifier.fillMaxSize(), targetState = loader
                                    ) {
                                        if (it == Status.LOADING) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(50.dp)
                                            )
                                        } else if (it == Status.SUCCESS) {
                                            SubcomposeAsyncImage(
                                                modifier = Modifier.fillMaxSize(),
                                                model = data?.logoUrl,
                                                contentDescription = "Description"
                                            ) {
                                                val state = painter.state
                                                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(50.dp)
                                                    )
                                                } else {
                                                    SubcomposeAsyncImageContent()
                                                }
                                            }
                                        }else if(it == Status.ERROR){
                                            when(errorCode?.first){
                                                StatusCode.NOT_NETWORK_FOUND->{
                                                    NoNetworkFound(){
                                                        viewModel.doRetry()
                                                    }
                                                }
                                                else->{
                                                    Text(text = errorCode?.second.orEmpty())
                                                }
                                            }
                                        }
                                    }
                                }
                                if(loader != Status.ERROR){
                                    AnimatedContent(targetState = title,
                                        modifier = Modifier.constrainAs(headingText) {
                                            top.linkTo(logo.bottom)
                                            linkTo(parent.start, parent.end)
                                        },
                                        transitionSpec = {
                                            slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut()
                                        }) {
                                        Text(
                                            text = data?.headingText ?: "Loading.."
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    modifier = Modifier.constrainAs(proceedButton) {
                                        linkTo(headingText.bottom, parent.bottom)
                                        linkTo(parent.start, parent.end)
                                    }, visible = loader == Status.SUCCESS
                                ) {
                                    FloatingActionButton(onClick = {
                                        navController.navigate(route = "Detail")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "Proceed"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                composable(route = "Detail", enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left
                    )
                }, exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right
                    )
                }, popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left
                    )
                }, popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right
                    )
                }) {
                    val uiData by viewModel.uiData.collectAsState(initial = mutableListOf())
                    val values by viewModel.inputStore.collectAsState(initial = SnapshotStateMap())
                    var alertData by remember {
                        mutableStateOf<UIDataResponse?>(null)
                    }
                    EzeTapAppTheme {

                        alertData?.let {
                            AlertDialog(onDismissRequest = {
                                alertData = null
                            }, buttons = {
                                TextButton(onClick = {
                                    alertData = null
                                }) {
                                    Text(text = "Okay")
                                }
                            }, title = {
                                Text(text = "Alert")
                            }, text = {
                                Text(text = "${it.uiType} clicked!")
                            }, properties = DialogProperties(
                                dismissOnBackPress = false, dismissOnClickOutside = false
                            )
                            )
                        }

                        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                            TopAppBar(modifier = Modifier.fillMaxWidth(), navigationIcon = {
                                IconButton(onClick = {
                                    navController.navigateUp()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.White
                                    )
                                }
                            }, title = {
                                Text(text = "Detail")
                            }, elevation = 2.dp
                            )
                        }) {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(it)
                                    .padding(horizontal = 16.dp)
                            ) {
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                                itemsIndexed(uiData) { index, data ->
                                    when (data.uiType) {
                                        "label" -> {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                text = data.value,
                                                style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                        "edittext" -> {
                                            EditText(
                                                modifier = Modifier.fillMaxWidth(),
                                                placeHolderText = data.hint.orEmpty(),
                                                key = data.key.orEmpty(),
                                                value = values[data.key.orEmpty()].orEmpty(),
                                            ) { key, value ->
                                                viewModel.onValueChanged(key, value)
                                            }
                                        }
                                        "button" -> {
                                            Button(onClick = {
                                                alertData = data
                                            }) {
                                                Text(text = data.value)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditText(
    modifier: Modifier,
    key: String,
    placeHolderText: String,
    value: String,
    onValueChanged: (String, String) -> Unit = { _, _ ->

    }
) {
    TextField(modifier = modifier, value = value, placeholder = {
        Text(text = placeHolderText)
    }, onValueChange = {
        onValueChanged(key, it)
    }, singleLine = true
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EzeTapAppTheme {
        Greeting("Android")
    }
}