# Financial-Tracker-Mobile-Kotlin

Spendly is a Kotlin-only Jetpack Compose finance tracker scaffolded for Android API 26+ with Firebase Authentication and Cloud Firestore persistence.

## Project Setup

- Java/Kotlin bytecode target: Java 17
- Minimum SDK: 26
- Target SDK: 34
- UI: Jetpack Compose only
- Architecture: MVVM with repositories in the data layer and ViewModel-owned UI state
- Firebase: Email/Password Authentication and Cloud Firestore

## Firebase Setup

1. Create a Firebase Android app with package name `com.spendly.financetracker`.
2. Download `google-services.json` from Firebase Console.
3. Place it at `app/google-services.json`.
4. Enable Authentication > Sign-in method > Email/Password.
5. Create a Firestore database.
6. Deploy or submit the included `firestore.rules`.

The app builds without a local Firebase config and shows a setup screen. Firebase sign-in and Firestore persistence become active after the real `google-services.json` file is added.

## Firestore Collection Paths

The app uses a per-user Firestore structure under the `users` collection.

| Path | Purpose |
|---|---|
| `users/{userId}/profile/main` | Stores the user profile document |
| `users/{userId}/income/{incomeId}` | Stores income transactions |
| `users/{userId}/expenses/{expenseId}` | Stores expense transactions |
| `users/{userId}/goals/{goalId}` | Stores savings goals |

### Document fields at a glance

- Profile documents store fields such as `uid`, `name`, `email`, `defaultCurrency`, `profileImageUri`, and notification settings.
- Income documents store transaction metadata such as `name`, `amountCents`, `source`, `dateMillis`, `note`, and crypto/exchange-rate fields.
- Expense documents store fields such as `name`, `amountCents`, `category`, `paymentMethod`, `expenseType`, and optional `goalId`.
- Goal documents store fields such as `title`, `status`, `targetCents`, `savedCents`, `dueDateMillis`, `isPrimary`, and `iconKey`.

---

## Project Structure

The source lives under `app/src/main/kotlin/com/spendly/financetracker/`. The structure below matches the actual codebase and lists the files inside each folder.

```text
financetracker/
├── MainActivity.kt
├── SpendlyApplication.kt
├── data/
│   ├── firebase/
│   │   └── FirebaseBootstrap.kt
│   ├── local/
│   │   ├── dao/
│   │   │   ├── ExpenseDao.kt
│   │   │   ├── GoalDao.kt
│   │   │   ├── IncomeDao.kt
│   │   │   └── UserProfileDao.kt
│   │   ├── db/
│   │   │   └── SpendlyDatabase.kt
│   │   └── entity/
│   │       ├── ExpenseEntryEntity.kt
│   │       ├── IncomeEntryEntity.kt
│   │       ├── SavingsGoalEntity.kt
│   │       └── UserProfileEntity.kt
│   ├── model/
│   │   ├── FinanceTransaction.kt
│   │   ├── SavingsGoal.kt
│   │   ├── UserProfile.kt
│   │   └── UserSession.kt
│   ├── remote/
│   │   ├── ExpenseRepositoryImpl.kt
│   │   ├── GoalRepositoryImpl.kt
│   │   ├── IncomeRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ExpenseRepository.kt
│   │   ├── FirebaseAuthRepository.kt
│   │   ├── FirebaseTransactionRepository.kt
│   │   ├── GoalRepository.kt
│   │   ├── IncomeRepository.kt
│   │   ├── TransactionRepository.kt
│   │   └── UserRepository.kt
│   └── service/
│       ├── CryptoRateService.kt
│       └── CurrencyRateService.kt
├── di/
│   ├── AppModule.kt
│   └── RepositoryModule.kt
├── ui/
│   ├── FinanceTrackerApp.kt
│   ├── components/
│   │   ├── AppBottomNavigation.kt
│   │   ├── GoalCard.kt
│   │   ├── HeaderSection.kt
│   │   ├── ProfileStat.kt
│   │   ├── SpendlyAddActionMenu.kt
│   │   ├── SpendlyDesign.kt
│   │   ├── SummaryCard.kt
│   │   ├── SummaryPanel.kt
│   │   └── TransactionListItem.kt
│   ├── navigation/
│   │   ├── BottomNavItem.kt
│   │   ├── Screen.kt
│   │   ├── SpendlyBottomNavBar.kt
│   │   └── SpendlyNavGraph.kt
│   ├── screen/
│   │   ├── AuthScreen.kt
│   │   ├── CreateAccountScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── FirebaseSetupScreen.kt
│   │   ├── SplashScreen.kt
│   │   ├── analytics/
│   │   │   └── AnalyticsScreen.kt
│   │   ├── goals/
│   │   │   ├── AddGoalScreen.kt
│   │   │   ├── EditGoalScreen.kt
│   │   │   ├── GoalDetailsScreen.kt
│   │   │   ├── GoalScreen.kt
│   │   │   └── GoalsScreen.kt
│   │   ├── home/
│   │   │   └── HomeScreen.kt
│   │   ├── profile/
│   │   │   └── ProfileScreen.kt
│   │   └── transactions/
│   │       ├── AddExpenseScreen.kt
│   │       ├── AddIncomeScreen.kt
│   │       └── TransactionsScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── util/
│   │   ├── AmountVisualTransformation.kt
│   │   ├── CategorySettings.kt
│   │   ├── GoalIconUtils.kt
│   │   ├── MonthOptions.kt
│   │   └── UiUtils.kt
│   └── viewmodel/
│       ├── AddExpenseViewModel.kt
│       ├── AddIncomeViewModel.kt
│       ├── AnalyticsViewModel.kt
│       ├── CreateAccountViewModel.kt
│       ├── FinanceUiState.kt
│       ├── FinanceViewModel.kt
│       ├── GoalsViewModel.kt
│       ├── HomeViewModel.kt
│       ├── ProfileViewModel.kt
│       └── TransactionsViewModel.kt
├── util/
│   ├── Mappers.kt
│   └── SyncManager.kt
└── worker/
    └── SpendlySyncWorker.kt
```

### Root-level config files

| File | Purpose |
|---|---|
| `app/google-services.json` | Firebase project config (add manually — see Firebase Setup) |
| `app/build.gradle.kts` | App-level Gradle build with Compose & Firebase dependencies |
| `build.gradle.kts` | Project-level Gradle build |
| `gradle/libs.versions.toml` | Version catalog for all dependencies |
| `settings.gradle.kts` | Module declarations |
| `firestore.rules` | Firestore security rules |
| `firestore.indexes.json` | Firestore composite index definitions |
| `firebase.json` | Firebase CLI project config |

---

## Team Workload Distribution

The project is distributed among 4 team members following MVVM architecture and modular code structure. Each person is listed with the screen work plus the backend/data files that drive those screens.

- W P C L Pathirana [IT23230910] - Dashboard & Profile
  - Screens
    - `ui/screen/home/HomeScreen.kt`
    - `ui/screen/profile/ProfileScreen.kt`
  - Backend logic and DB communication
    - `ui/viewmodel/HomeViewModel.kt`
    - `ui/viewmodel/ProfileViewModel.kt`
    - `data/repository/UserRepository.kt`
    - `data/repository/FirebaseTransactionRepository.kt`
    - `data/remote/UserRepositoryImpl.kt`
    - `data/remote/ExpenseRepositoryImpl.kt`
    - `data/remote/IncomeRepositoryImpl.kt`
    - `data/remote/GoalRepositoryImpl.kt`
    - `data/local/dao/UserProfileDao.kt`
    - `data/local/dao/ExpenseDao.kt`
    - `data/local/dao/IncomeDao.kt`
    - `data/local/dao/GoalDao.kt`
- D B Y Binuwara [IT23184558] - Transactions & Create Account
  - Screens
    - `ui/screen/transactions/TransactionsScreen.kt`
    - `ui/screen/transactions/AddIncomeScreen.kt`
    - `ui/screen/transactions/AddExpenseScreen.kt`
    - `ui/screen/CreateAccountScreen.kt`
  - Backend logic and DB communication
    - `ui/viewmodel/AddIncomeViewModel.kt`
    - `ui/viewmodel/AddExpenseViewModel.kt`
    - `ui/viewmodel/TransactionsViewModel.kt`
    - `ui/viewmodel/CreateAccountViewModel.kt`
    - `data/repository/TransactionRepository.kt`
    - `data/repository/IncomeRepository.kt`
    - `data/repository/ExpenseRepository.kt`
    - `data/repository/AuthRepository.kt`
    - `data/repository/FirebaseTransactionRepository.kt`
    - `data/repository/FirebaseAuthRepository.kt`
    - `data/remote/IncomeRepositoryImpl.kt`
    - `data/remote/ExpenseRepositoryImpl.kt`
    - `data/remote/UserRepositoryImpl.kt`
    - `data/local/dao/IncomeDao.kt`
    - `data/local/dao/ExpenseDao.kt`
- A M N D Bandara [IT23148840] - Goals & User Login
  - Screens
    - `ui/screen/goals/GoalsScreen.kt`
    - `ui/screen/goals/GoalScreen.kt`
    - `ui/screen/goals/AddGoalScreen.kt`
    - `ui/screen/goals/EditGoalScreen.kt`
    - `ui/screen/goals/GoalDetailsScreen.kt`
    - `ui/screen/AuthScreen.kt`
  - Backend logic and DB communication
    - `ui/viewmodel/GoalsViewModel.kt`
    - `data/repository/GoalRepository.kt`
    - `data/repository/AuthRepository.kt`
    - `data/repository/FirebaseAuthRepository.kt`
    - `data/remote/GoalRepositoryImpl.kt`
    - `data/remote/UserRepositoryImpl.kt`
    - `data/local/dao/GoalDao.kt`
    - `data/local/dao/UserProfileDao.kt`
    - `data/local/dao/ExpenseDao.kt`
- M V M Linash [IT23442566] - DB and entity setup
  - Database setup
    - `data/local/db/SpendlyDatabase.kt`
  - Entity setup
    - `data/local/entity/ExpenseEntryEntity.kt`
    - `data/local/entity/IncomeEntryEntity.kt`
    - `data/local/entity/SavingsGoalEntity.kt`
    - `data/local/entity/UserProfileEntity.kt`
  - Analytics page and backend logic
    - `ui/screen/analytics/AnalyticsScreen.kt`
    - `ui/viewmodel/AnalyticsViewModel.kt`
    - `data/service/CurrencyRateService.kt`
    - `data/service/CryptoRateService.kt`
    - `util/Mappers.kt`
    - `util/SyncManager.kt`
    - `worker/SpendlySyncWorker.kt`
---

## Key Versions

These versions are defined in `gradle/libs.versions.toml`.

| Dependency | Version |
|---|---|
| Android Gradle Plugin | 8.5.2 |
| Kotlin | 1.9.24 |
| Compose BOM | 2024.06.00 |
| Compose Compiler Extension | 1.5.14 |
| Firebase BOM | 33.1.2 |
| Coroutines | 1.8.1 |
| Lifecycle / ViewModel | 2.8.4 |

## Build & Run

These instructions assume you have Android Studio or the Android SDK installed and a working Java/JDK environment compatible with the project's Gradle settings.

1. Ensure the Gradle wrapper is executable on your machine. On Windows you can use the included `gradlew.bat`.

2. Build the debug APK:

```bash
./gradlew.bat assembleDebug
```

3. Install to a connected device or emulator:

```bash
./gradlew.bat installDebug
```

4. Run unit tests locally:

```bash
./gradlew.bat test
```

5. Run instrumentation tests (requires a connected device or emulator):

```bash
./gradlew.bat connectedAndroidTest
```

Notes:
- Prefer Android Studio for iterative development — it handles signing, emulators, and device management.
- If you change `google-services.json`, do a clean build to ensure generated sources are updated:

```bash
./gradlew.bat clean assembleDebug
```

## Architecture & Design Decisions

- Single `FinanceViewModel` drives the entire `FinanceUiState` exposed as `StateFlow`.
- Screens are pure composables: they accept `FinanceUiState` + typed callbacks and never call `viewModel()` directly.
- Repositories provide interfaces in `data/repository` and concrete implementations under `data/remote` or `data/local`.
- DI is provided via `di/AppModule.kt` and `di/RepositoryModule.kt` — add bindings here for new data sources.

Why this matters:
- Keeping ViewModel creation centralised makes it easier to reason about state and test.
- Interfaces + DI make swapping implementations (Room, Firestore, network) straightforward.
