@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.BuildConfig
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomButton
import com.maxidev.tastymeal.presentation.components.CustomFab
import com.maxidev.tastymeal.presentation.components.CustomTextField
import com.maxidev.tastymeal.presentation.recipe.new_recipe.NewRecipeUiEvents
import com.maxidev.tastymeal.presentation.settings.HeaderTitleItem
import com.maxidev.tastymeal.utils.createImageFile
import java.util.Objects

@Composable
fun EditRecipeScreen(
    id: Long,
    viewModel: EditRecipeDetailViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    // Displays data on screen if it exists in the DB.
    LaunchedEffect(id) {
        viewModel.getId(id)
    }

    val image by viewModel.imageUri.collectAsStateWithLifecycle()
    val imageCamera by viewModel.cameraImageUri.collectAsStateWithLifecycle()

    RecipeScreenContent(
        id = id,
        titleField = viewModel.titleTextState,
        instructionsField = viewModel.instructionsTextState,
        ingredientsField = viewModel.ingredientsAndMeasuresState,
        portionsField = viewModel.portionsTextState,
        preparationTimeField = viewModel.preparationTimeTextState,
        cookingTimeField = viewModel.cookingTimeTextState,
        sourceField = viewModel.sourceTextState,
        notesField = viewModel.notesTextState,
        initialImageUri = image,
        initialCameraImageUri = imageCamera,
        onImageUriChange = viewModel::updateImageUri,
        onCameraImageUriChange = viewModel::updateCameraImageUri,
        popBackStack = popBackStack,
        onEvent = { events ->
            when (events) {
                is NewRecipeUiEvents.AddToDataBase -> {
                    viewModel.editRecipe(events.add)
                }
            }
        }
    )
}

@Composable
private fun RecipeScreenContent(
    id: Long,
    titleField: TextFieldState = rememberTextFieldState(),
    portionsField: TextFieldState = rememberTextFieldState(),
    preparationTimeField: TextFieldState = rememberTextFieldState(),
    cookingTimeField: TextFieldState = rememberTextFieldState(),
    sourceField: TextFieldState = rememberTextFieldState(),
    instructionsField: TextFieldState = rememberTextFieldState(),
    ingredientsField: TextFieldState = rememberTextFieldState(),
    notesField: TextFieldState = rememberTextFieldState(),
    initialImageUri: Uri? = null,
    initialCameraImageUri: Uri? = null,
    onImageUriChange: (Uri?) -> Unit,
    onCameraImageUriChange: (Uri?) -> Unit,
    onEvent: (NewRecipeUiEvents) -> Unit,
    popBackStack: () -> Unit
) {
    val context = LocalContext.current
    val keyboardOptionsImeDone = KeyboardOptions(imeAction = ImeAction.Done)
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )
    var expanded by remember { mutableStateOf(false) }
    // Initializes the loaded images.
    var singlePhotoPickerImage by remember { mutableStateOf(initialImageUri ?: Uri.EMPTY) }
    var capturedImage by remember { mutableStateOf(initialCameraImageUri ?: Uri.EMPTY) }

    // Update when initial values change.
    LaunchedEffect(initialImageUri, initialCameraImageUri) {
        initialImageUri?.let { singlePhotoPickerImage = it }
        initialCameraImageUri?.let { capturedImage = it }
    }

    // Photo picker
    val simplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            singlePhotoPickerImage = uri ?: Uri.EMPTY
            onImageUriChange(uri)

            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val resolver = context.contentResolver

            uri?.let { resolver.takePersistableUriPermission(it, flags) }
        }
    )

    // Camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                capturedImage = uri
                onCameraImageUriChange(uri)
            }
        }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { cameraLauncher.launch(uri) }
    )

    Scaffold(
        floatingActionButton = {
            CustomFab(
                onClick = {
                    onEvent(
                        NewRecipeUiEvents.AddToDataBase(
                            add = Recipe(
                                id = id,
                                title = titleField.text.toString(),
                                portions = portionsField.text.toString(),
                                preparationTime = preparationTimeField.text.toString(),
                                cookingTime = cookingTimeField.text.toString(),
                                source = sourceField.text.toString(),
                                image = singlePhotoPickerImage ?: Uri.EMPTY,
                                cameraImage = capturedImage,
                                instructions = instructionsField.text.toString(),
                                ingredientsAndMeasures = ingredientsField.text.toString(),
                                notes = notesField.text.toString()
                            )
                        )
                    )
                    popBackStack()
                },
                icon = { Icon(imageVector = Icons.Rounded.Save, contentDescription = "Save recipe") },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabList = listOf("Overview", "Instructions", "Ingredients", "Notes")

            HeaderTitleItem(title = "Edit Recipe")

            PrimaryScrollableTabRow(
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
                    /**
                     * Text fields for the overview.
                     */
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeContentPadding()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val fontSize = 20.sp

                        Text(
                            text = "Title",
                            fontSize = fontSize,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = titleField,
                            keyboardOptions = keyboardOptionsImeDone,
                        )
                        Text(
                            text = "Portions",
                            fontSize = fontSize,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = portionsField,
                            keyboardOptions = keyboardOptionsImeDone,
                        )
                        Text(
                            text = "Preparation time (Optional)",
                            fontSize = fontSize,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = preparationTimeField,
                            keyboardOptions = keyboardOptionsImeDone,
                        )
                        Text(
                            text = "Cooking time (Optional)",
                            fontSize = fontSize,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = cookingTimeField,
                            keyboardOptions = keyboardOptionsImeDone,
                        )
                        Text(
                            text = "Source (Optional)",
                            fontSize = fontSize,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = sourceField,
                            keyboardOptions = keyboardOptionsImeDone,
                        )
                        HorizontalDivider()
                        /**
                         * The following buttons have the following functions...
                         *
                         * A button to remove the photo if it wasn't the desired one,
                         * and another button to add a single photo from the gallery or camera.
                         */
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomButton(
                                imageVector = Icons.Rounded.Clear,
                                name = "Remove photo",
                                contentDescription = "Remove photo",
                                onClick = {
                                    if (singlePhotoPickerImage != Uri.EMPTY) singlePhotoPickerImage = Uri.EMPTY
                                    if (capturedImage != Uri.EMPTY) capturedImage = Uri.EMPTY
                                },
                                modifier = Modifier
                                    .height(60.dp)
                            )
                            Box {
                                CustomButton(
                                    imageVector = Icons.Rounded.PhotoLibrary,
                                    name = "Add photo",
                                    contentDescription = "Add photo",
                                    onClick = { expanded = !expanded },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                )
                                DropdownMenu(
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
                        }

                        val photo = if (singlePhotoPickerImage == Uri.EMPTY) capturedImage else singlePhotoPickerImage
                        Image(
                            painter = rememberAsyncImagePainter(photo),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeContentPadding()
                    ) {
                        BasicTextField(
                            state = instructionsField,
                            keyboardOptions = keyboardOptionsImeDone,
                            decorator = { innerTextField ->
                                if (instructionsField.text.isEmpty()) {
                                    Text(text = "Add instructions.")
                                }
                                innerTextField()
                            }
                        )
                    }
                }
                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeContentPadding()
                    ) {
                        BasicTextField(
                            state = ingredientsField,
                            keyboardOptions = keyboardOptionsImeDone,
                            decorator = { innerTextField ->
                                if (ingredientsField.text.isEmpty()) {
                                    Text(text = "Add ingredients.")
                                }
                                innerTextField()
                            }
                        )
                    }
                }
                3 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeContentPadding()
                    ) {
                        BasicTextField(
                            state = notesField,
                            keyboardOptions = keyboardOptionsImeDone,
                            decorator = { innerTextField ->
                                if (notesField.text.isEmpty()) {
                                    Text(text = "Add notes.")
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }
        }
    }
}