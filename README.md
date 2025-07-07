<img src="https://github.com/user-attachments/assets/de29ea74-183a-40e5-ac1f-7a0c03a92d26" width="90" height="90">

# Tasty meal
[![](https://img.shields.io/badge/android-000000.svg?style=for-the-badge&logo=android)](https://)

An app that lets you easily find recipes from [The Meal Database](https://www.themealdb.com/) API, and also lets you create and manage your recipes.

The project consists of implementing a simple, intuitive, and fast way to search for recipes. Although the number of recipes is limited by the REST API, the user will be able to create and save their own recipes, as well as save their favorite recipes from the API and view them offline.

## Features

- Access to certain app features in case you don't have an internet connection. 
- Allows you to save your favorite recipes from the API into a database.
- Lets you create your own recipes, including adding photos from the camera or gallery.
- A search feature.
- The ability to change the app's themes.

## Build with

- Kotlin
- Jetpack Compose
- Material 3
- Jetpack libraries
  - Data Store
  - Splash screen
  - Navigation
  - Paging 3
  - Room database
- Hilt
- Coil image library
- Kotlin serialization
- Kotlin coroutines
- OkHttp/Retrofit
- Rest Api

## Architecture

The pattern used for this project is MVVM (Model-View-ViewModel) which is a widely used architectural pattern in Android development that promotes separation of concerns, testability, and maintainability. It divides an application into three interconnected parts:

1. **Model**: Represents the data and business logic of the application. It's responsible for fetching, storing, and manipulating data. This might include data classes, repositories, and data sources like databases or network APIs.
2. **View**: In Compose, the View is represented by composable functions. These functions describe the UI declaratively, defining how it should look based on the current state. Instead of directly manipulating UI elements, composable functions recompose when the underlying data changes.
3. **ViewModel**: Acts as an intermediary between the Model and the View. It exposes data from the Model in a way that's easily consumable by the View. It also handles user interactions from the View and updates the Model accordingly. The ViewModel is lifecycle-aware, meaning it survives configuration changes like screen rotations.

![Mvvm arch](https://github.com/user-attachments/assets/011add8b-cd32-4ae7-b78e-60a2ca578a59)

## Requirements

No prerequisites are required. Only the latest version of Android Studio.

## Video
<video src="https://github.com/user-attachments/assets/e3dd8e02-adfd-4320-9f15-5094e7a40f4c" width="300" />

## Screenshots

<img src="https://github.com/user-attachments/assets/6f19ac38-ce26-4ff5-b510-d3cf2441d8b0" width="190" height="400">
<img src="https://github.com/user-attachments/assets/f6afe1a3-fb63-4c44-9c6c-53ca17258ce7" width="190" height="400">
<img src="https://github.com/user-attachments/assets/8c29b067-a77f-46b6-89f3-1f1295214c2d" width="190" height="400">
<img src="https://github.com/user-attachments/assets/d9c40dc0-fc66-4ef4-93d7-99c6d16239e8" width="190" height="400">
<img src="https://github.com/user-attachments/assets/20d955cd-342c-4b67-8b19-ee419ca57581" width="190" height="400">
<img src="https://github.com/user-attachments/assets/0cadb20c-a30e-4a64-979c-dae64116d6fd" width="190" height="400">
<img src="https://github.com/user-attachments/assets/8b888a08-f4dc-43d1-bbd9-c7918346f973" width="190" height="400">
<img src="https://github.com/user-attachments/assets/e38c199f-5ea5-4e74-89ab-189d8ac88b56" width="190" height="400">
<img src="https://github.com/user-attachments/assets/7e69f5c7-28cf-4452-a6be-0775a95b108b" width="190" height="400">
<img src="https://github.com/user-attachments/assets/fb3ee039-20df-4d23-aa0f-f7ae86afbc95" width="190" height="400">
<img src="https://github.com/user-attachments/assets/bd2192bd-fc01-49f1-a374-6e1096d138d8" width="190" height="400">
<img src="https://github.com/user-attachments/assets/b12687ea-5b4b-4c99-98df-382df93c8605" width="190" height="400">
