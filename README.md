# Restaurant Finder App
## Spent Time
- For the main features and logics including managing the alternating HTTP requests and show the data, I spent **95 Minutes**
- For the code enhancements and improvements including replacing **RxJava** with **Kotlin coroutines**, adding **Dagger/Hilt** as Dependency Injection library and separation of concerns I spent **6 hours**

## Overview
This project is a **Restaurant Finder App** that fetches restaurant data from both Google Places API and Yelp API, using Kotlin coroutines for asynchronous operations, alongside modern Android architecture patterns such as **MVVM, Jetpack Compose, and Dagger/Hilt** for dependency injection.

## Key Features
- Fetches restaurant data from **Google Places and Yelp APIs**.
- Dynamically alternates between APIs based on provided instructions and conditions.
- Displays restaurant images with **pagination** through a horizontal image pager.
- Manages location-based logic in the `MainActivity` and performs network requests asynchronously with **Kotlin Coroutines.**
- Includes unit tests for key ViewModel functions.
- Utilizes **Dagger/Hilt** for Dependency Injection (DI) to manage dependencies.
- Image loading uses **placeholders** to enhance user experience.
- Converts API-specific models to a shared **domain model** for unified processing.

## Architectural Decisions
### 1. Kotlin Coroutines vs RxJava/RxKotlin
   Kotlin Coroutines were chosen over RxJava due to **Simplicity, Integration with Jetpack components, Performance** (Coroutines have a lighter runtime overhead) and **Flow API**

### 2. Separation of Concerns
   - The project implements **MVVM architecture**, ensuring that **ViewModel** only handles business logic and interacts with the repository.
   - The **repository** manages data sources (Yelp API, Places API) and business rules, decoupling logic from the ViewModel.
   - Android-related logic (e.g., **location services**) is kept within the MainActivity and not the ViewModel, maintaining separation between platform-specific code and business logic.

### 3. Unified Domain Model
   Both Google Places API and Yelp API responses are transformed into a common domain model using the `toDomainModel()` function. This keeps the business logic independent of the API used and ensures a consistent data format across the app.

### 4. Testability
   The Repository is implemented through an interface (`RestaurantRepository`), making it easier to mock in tests and improving the testability of ViewModels.

### 5. Dependency Injection with Hilt
   Dagger/Hilt is used as the Dependency Injection framework for this project to **Manage dependencies** efficiently without manually passing objects through constructors and **Improve testability** by easily injecting mock dependencies in unit tests and **Maintain modularity**(allowing future changes or extensions without disrupting the entire project)
   
## Future Improvements (Nice-to-Have)
### 1. Offline-first support
   Making the app **offline-first** by caching API responses locally would significantly **improve user experience**

### 2. Enhanced UI with Animations
   Adding animations to the horizontal pager (e.g., **smooth transitions between restaurant images**) will enhance the visual appeal and interactivity of the app, making it more engaging for users.

### 3. Unit Tests
   Unit tests could be added for ViewModels (e.g., `MainViewModel`), Repository layer (e.g., `RestaurantRepository`), Edge cases like empty responses, network errors, and invalid locations and UI testing

### 4. UI/UX Improvements
   Improving the overall user interface by **using better layouts, Accessibility and user feedback**
   
### 5. Optimized Image Loading
   Implementing image loading optimizations like **loading upcoming images in the background, memory caching and lazy loading**
   
## Conclusion
   This project implements modern best practices for Android development with a focus on **separation of concerns, testability, and maintainability**. The choices made, such as Kotlin Coroutines for concurrency and Hilt for Dependency Injection, ensure that the codebase is clean, scalable, and easy to extend in the future.

