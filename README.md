<img src="https://github.com/user-attachments/assets/de29ea74-183a-40e5-ac1f-7a0c03a92d26" width="90" height="90">

# Tasty meal
[![](https://img.shields.io/badge/android-000000.svg?style=for-the-badge&logo=android)](https://)

An application designed to help users easily discover recipes using ***[The Meal Database](https://www.themealdb.com/)***, while also allowing them to create, manage, and store their own recipes.

The project focuses on delivering a simple, intuitive, and responsive recipe search experience. Although the number of recipes available through the **REST API** is limited, users can enhance their experience by:

- Creating and storing custom recipes.

- Saving their favorite API-sourced recipes for **offline access**.

- Managing a personalized recipe collection within the app.

## Features

- **Offline access** to certain features, even when there's no internet connection.

- **Save** favorite recipes retrieved from the API to a local database for quick, offline access.

- **Create** your own recipes, with the option to add photos from your device's camera or gallery.

- **Built-in** search function for easy recipe finding.

- **Light, dark, and dynamic themes** adapt the app's appearance based on system settings or user preferences. On devices running Android 12 or higher, Material You is used to apply dynamic colors based on the system wallpaper.

> [!NOTE]
> No prerequisites or API key are required. Only the latest version of Android Studio.

## Build with

- Kotlin
  
- Jetpack Compose
  
- Material 3
  
- ***Jetpack libraries***
  - *Data Store*
  - *Splash screen*
  - *Navigation*
  - *Paging 3*
  - *Room database*

- Hilt
  
- Coil image library
  
- Kotlin serialization
  
- Kotlin coroutines
  
- OkHttp/Retrofit
  
- Rest Api

## Architecture

### MVVM (Model-View-ViewModel)

This project uses the **MVVM (Model-View-ViewModel)** architectural pattern, a widely adopted structure in modern Android development. MVVM encourages **separation of concerns**, improves **scalability**, enables **easier testing**, and promotes better **maintainability** of the codebase.

The architecture is divided into three main components:

### 1. Model

The **Model** represents the data layer and business logic. Its responsibilities include:

- Fetching, storing, and processing data.

- Interacting with external data sources, such as local databases (e.g., Room) or web services (REST APIs).

- Containing elements such as:

 - Data classes

 - Repositories

 - Data sources (remote or local)

### 2. View

The **View**, implemented using Jetpack Compose, is responsible for rendering the user interface. Key characteristics:

- Built using ***composable functions*** that define the UI declaratively.

- Reactively updates when the underlying observed data changes.

- Does not include business logic or directly access the data layer.

### 3. ViewModel

The **ViewModel** serves as a bridge between the Model and the View. Its primary role is to:

- Expose UI-ready data using `StateFlow`, `LiveData`, or other observable mechanisms.

- Handle user interactions and UI events.

- **Be lifecycle-aware**, allowing it to survive configuration changes such as screen rotations.

### Benefits of MVVM

- **Clear separation of responsibilities**: improves code readability, maintainability, and scalability.

- **Reactive, decoupled UI**: the View observes data without knowing its source.

- **Improved testability**: ViewModels and Models can be tested independently.

- **Composable-friendly**: aligns naturally with Jetpack Compose’s declarative and reactive approach.

![Mvvm arch](https://github.com/user-attachments/assets/011add8b-cd32-4ae7-b78e-60a2ca578a59)

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

## Video
<video src="https://github.com/user-attachments/assets/e3dd8e02-adfd-4320-9f15-5094e7a40f4c" width="300" />
