@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe.new_recipe

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.BuildConfig
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomButton
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomFab
import com.maxidev.tastymeal.presentation.components.CustomTextField
import com.maxidev.tastymeal.utils.createImageFile
import java.util.Objects

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    val titleField = viewModel.titleTextState
    val portionsField = viewModel.portionsTextState
    val preparationTimeField = viewModel.preparationTimeTextState
    val cookingTimeField = viewModel.cookingTimeTextState
    val sourceField = viewModel.sourceTextState
    val instructionsField = viewModel.instructionsTextState
    val ingredientsField = viewModel.ingredientsAndMeasuresState
    val notesField = viewModel.notesTextState

    RecipeScreenContent(
        titleField = titleField,
        portionsField = portionsField,
        preparationTimeField = preparationTimeField,
        cookingTimeField = cookingTimeField,
        sourceField = sourceField,
        instructionsField = instructionsField,
        ingredientsField = ingredientsField,
        notesField = notesField,
        popBackStack = popBackStack,
        onEvent = { events ->
            when(events) {
                is NewRecipeUiEvents.AddToDataBase -> { viewModel.saveRecipe(events.add) }
            }
        }
    )
}

@Composable
private fun RecipeScreenContent(
    titleField: TextFieldState = rememberTextFieldState(),
    portionsField: TextFieldState = rememberTextFieldState(),
    preparationTimeField: TextFieldState = rememberTextFieldState(),
    cookingTimeField: TextFieldState = rememberTextFieldState(),
    sourceField: TextFieldState = rememberTextFieldState(),
    instructionsField: TextFieldState = rememberTextFieldState(),
    ingredientsField: TextFieldState = rememberTextFieldState(),
    notesField: TextFieldState = rememberTextFieldState(),
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
    var singlePhotoPickerImage by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    var capturedImage by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    // Photo picker
    val simplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            singlePhotoPickerImage = uri

            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val resolver = context.contentResolver

            uri?.let { resolver.takePersistableUriPermission(it, flags) }
        }
    )

    // Camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { capturedImage = uri }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { cameraLauncher.launch(uri) }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomCenteredTopBar(
                title = { Text(text = "New recipe") },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            CustomFab(
                onClick = {
                    onEvent(
                        NewRecipeUiEvents.AddToDataBase(
                            add = Recipe(
                                id = 0,
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