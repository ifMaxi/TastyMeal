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

## Tech stack

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

TODO...

## Screenshots

TODO...
