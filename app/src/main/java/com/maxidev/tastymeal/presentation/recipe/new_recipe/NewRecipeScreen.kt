@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe.new_recipe

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.BuildConfig
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomFab
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    val titleField = viewModel.titleTextState
    val instructionsField = viewModel.instructionsTextState
    val ingredientsField = viewModel.ingredientsAndMeasuresState

    ScreenContent(
        titleField = titleField,
        instructionsField = instructionsField,
        ingredientsField = ingredientsField,
        popBackStack = popBackStack,
        onEvent = { events ->
            when(events) {
                is NewRecipeUiEvents.AddToDataBase -> {
                    viewModel.saveRecipe(events.add)
                }
            }
        }
    )
}

@Composable
private fun ScreenContent(
    titleField: TextFieldState = rememberTextFieldState(),
    instructionsField: TextFieldState = rememberTextFieldState(),
    ingredientsField: TextFieldState = rememberTextFieldState(),
    onEvent: (NewRecipeUiEvents) -> Unit,
    popBackStack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val file = context.createImageFile()
    var singlePhotoPickerImage by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    var capturedImage by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )

    // Photo picker
    val simplePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                singlePhotoPickerImage = uri
                Log.d("Uri result", "$uri")

                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val resolver = context.contentResolver

                resolver.takePersistableUriPermission(uri!!, flags)
            }
        )

    // Camera
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { capturedImage = uri }
        )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(
        topBar = {
            CustomCenteredTopBar(
                title = {
                    Text(text = "New recipe")
                }
            )
        },
        floatingActionButton = {
            CustomFab(
                onClick = {
                    // TODO: Change save method whit events.
                    onEvent(
                        NewRecipeUiEvents.AddToDataBase(
                            add = Recipe(
                                id = 0,
                                title = titleField.text.toString(),
                                image = singlePhotoPickerImage ?: Uri.EMPTY,
                                cameraImage = capturedImage,
                                instructions = instructionsField.text.toString(),
                                ingredientsAndMeasures = ingredientsField.text.toString()
                            )
                        )
                    )
                    popBackStack()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = "Save recipe"
                    )
                },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        }
    ) { innerPadding ->
        // TODO: Make a new creation screen.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabList = listOf("Overview", "Instructions", "Ingredients")
            val keyboardOptionsImeDone = KeyboardOptions(imeAction = ImeAction.Done)

            PrimaryTabRow(
                selectedTabIndex = selectedIndex,
                tabs = {
                    tabList.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            text = { Text(text = tab) }
                        )
                    }
                }
            )

            when (selectedIndex) {
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Title",
                            fontSize = 20.sp
                        )
                        // TODO: Add more fields.
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = titleField,
                            keyboardOptions = keyboardOptionsImeDone,
                            lineLimits = TextFieldLineLimits.MultiLine(
                                minHeightInLines = 1, maxHeightInLines = 4
                            ),
                            decorator = { innerTextField ->
                                Box(
                                    Modifier
                                        .border(
                                            width = Dp.Hairline,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(20.dp)
                                ) { innerTextField() }
                            }
                        )
                        HorizontalDivider()
                        Box {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                shape = RoundedCornerShape(10.dp),
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(imageVector = Icons.Rounded.PhotoLibrary, contentDescription = "Gallery")
                                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                Text(text = "Add photos")
                            }
                            DropdownMenu(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                content = {
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Photo,
                                                contentDescription = "Gallery"
                                            )
                                        },
                                        text = { Text("Add from gallery") },
                                        onClick = {
                                            simplePhotoPickerLauncher.launch(
                                                PickVisualMediaRequest(
                                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                                )
                                            )
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Camera,
                                                contentDescription = "Camera"
                                            )
                                        },
                                        text = { Text("Add from camera") },
                                        onClick = {
                                            val permissionCheck =
                                                ContextCompat.checkSelfPermission(
                                                    context, Manifest.permission.CAMERA
                                                )

                                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                                cameraLauncher.launch(uri)
                                            } else {
                                                permissionLauncher.launch(Manifest.permission.CAMERA)
                                            }

                                            expanded = false
                                        }
                                    )
                                }
                            )
                        }

                        val photo = if (singlePhotoPickerImage == Uri.EMPTY) {
                            capturedImage
                        } else {
                            singlePhotoPickerImage
                        }
                        Button(
                            onClick = {
                                if (singlePhotoPickerImage != Uri.EMPTY) singlePhotoPickerImage = Uri.EMPTY
                                if (capturedImage != Uri.EMPTY) capturedImage = Uri.EMPTY
                            }
                        ) { Text(text = "Remove photo") }
                        Image(
                            painter = rememberAsyncImagePainter(photo),
                            contentDescription = null,
                            //contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                //.fillMaxSize()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
                1 -> {
                    BasicTextField(
                        modifier = Modifier.fillMaxSize(),
                        state = instructionsField,
                        decorator = { innerTextField ->
                            if (instructionsField.text.isEmpty()) {
                                Text(text = "Add instructions here...")
                            }
                            innerTextField()
                        }
                    )
                }
                2 -> {
                    BasicTextField(
                        modifier = Modifier.fillMaxSize(),
                        state = ingredientsField,
                        decorator = { innerTextField ->
                            if (ingredientsField.text.isEmpty()) {
                                Text(text = "Add ingredients here...")
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}