# Lock Configuration App

A demo Android application that lets a service technician configure lock parameters on a double‑door (primary & secondary leaves). Built with Kotlin, Jetpack Compose, Hilt DI, Room persistence, Clean Architecture and MVVM.

---

##  Features

- **List & Search**  
  – Show all lock parameters for the selected door leaf  
  – Filter by parameter name or current value  
  – Pull‑to‑refresh to re‑fetch from network (on `feature/network` branch)

- **Edit Screen**  
  – Edit each parameter for both Primary & Secondary doors in one screen  
  – Discrete parameters (radio buttons) and range parameters (integer slider)  
  – Save per‑leaf values locally in Room

- **Offline Support**  
  – On the **`main`** branch, JSON is loaded from **`assets/lock_config.json`**  
  – On the **`feature/network`** branch, JSON is fetched via Retrofit, cached in Room, and subsequent loads come from the database

---

## Architecture

We follow **Clean Architecture** & **MVVM**, split into three layers


- **Dependency Injection**: Hilt modules under `di/`  
  – `DatabaseModule` provides the singleton `AppDatabase`  
  – `LockConfigDatabaseModule` provides `ParamValueDao`  
  – `NetworkModule` (on `feature/network`) provides Retrofit & API  
  – `RepositoryModule` binds `LockConfigRepositoryImpl` to `LockConfigRepository`

- **Reactive streams** via **KotlinFlow** for definitions, saved values, and filtered results.

- **Compose UI** with state hoisting, reusable components, and previews.

---

##  Branches

- **`main`**  
  – Loads the lock-configuration JSON from **`app/src/main/assets/lock_config.json`**  
  – Parses with Gson → Domain models → seeds Room on first load  
  – All reads & filters come from Room & in‑memory definitions

- **`featurenetwork`**  
  – Adds `LockConfigApi` & `NetworkModule`  
  – On startup (and on pull‑to‑refresh) fetches JSON from a real API  
  – Clears & re‑seeds Room with defaults from the network response  
  – Subsequent reads come from Room database until next refresh

---

##  Getting Started

**Prerequisites:**
- Android Studio Flamingo or later
- Kotlin 1.9.0+, Android Gradle Plugin 8.1+
- minSdk 29, targetSdk 36

**Clone & run:**
git clone https://github.com/pandimani17/assatask.git


