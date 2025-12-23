# Offline News Reader App (Assignment 04)

This is a robust, offline-capable Android news reading application built with Java and XML. It demonstrates advanced Android development concepts including networking, local database storage, lifecycle management, and dynamic theming.

## 📱 Features

### 1. User Authentication
*   **Persistent Login**: State is preserved across app restarts using `SharedPreferences`.
*   **Logout Functionality**: Securely clears session data and returns to the login screen.

### 2. News Feed & Offline Caching
*   **REST API Integration**: Fetches the latest news articles from a live remote API.
*   **Offline Support**: Automatically caches fetched articles into a local **SQLite** database. Users can read previously loaded news even without an internet connection.
*   **Smart Refresh**: The app prioritizes network data when available but seamlessly falls back to the database when offline.

### 3. Detailed Article View
*   **WebView Integration**: Tap on any news item to view the full article content within the app.
*   **Share Functionality**: Easily share article links with other apps.

### 4. Dynamic Theming
*   **Multiple Themes**: Switch between **Light**, **Dark**, and a custom **Blue/High-Contrast** theme at runtime.
*   **Theme Persistence**: The selected theme is saved and automatically applied on next launch.

### 5. Lifecycle & State Management
*   **Robust Lifecycle Handling**: Correctly handles screen rotations and process death without losing user position or data.
*   **Efficient Networking**: Uses background threads for network operations to keep the UI smooth (ANR-free).

## 🛠 Tech Stack

*   **Language**: Java
*   **UI**: XML Layouts (ConstraintLayout, RecyclerView, CardView)
*   **Architecture**: MVC (Model-View-Controller) pattern
*   **Local Storage**: SQLite (`SQLiteOpenHelper`)
*   **Networking**: `java.net.HttpURLConnection` (No heavy third-party networking libraries)
*   **JSON Parsing**: `org.json`
*   **Async Operations**: `ExecutorService` / `ThreadPoolExecutor`

## 🚀 How to Run

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/wasfa20/assignment-04-app.git
    ```
2.  **Open in Android Studio**:
    *   File -> Open -> Select the cloned folder.
3.  **Sync Gradle**: Allow Android Studio to download necessary dependencies.
4.  **Run**: Press the green **Run** button (Shift+F10) to deploy to an emulator or physical device.

## 📂 Project Structure

*   `activities/`: `LoginActivity`, `MainActivity`, `DetailActivity`
*   `adapter/`: `NewsAdapter` for RecyclerView binding.
*   `database/`: `DatabaseHelper` for SQLite CRUD operations.
*   `network/`: `NewsFetcher` for API calls associated with background threads.
*   `utils/`: `SessionManager` (Auth), `ThemeUtils` (Theming).

---
**Developed for Assignment 04.**
