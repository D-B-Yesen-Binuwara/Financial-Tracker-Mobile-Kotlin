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

---

## Project Structure

The full source lives under `app/src/main/kotlin/com/spendly/financetracker/`. Below is the complete folder structure with every file required for the project to initialise and run.

```
financetracker/
│
├── MainActivity.kt                          # App entry point, sets up Compose host
│
├── data/
│   ├── firebase/
│   │   └── FirebaseBootstrap.kt             # Firebase initialisation & config check
│   ├── model/
│   │   ├── FinanceTransaction.kt            # Core transaction data class
│   │   └── UserSession.kt                   # Authenticated user state model
│   └── repository/
│       ├── AuthRepository.kt                # Auth interface
│       ├── FirebaseAuthRepository.kt        # Firebase auth implementation
│       ├── TransactionRepository.kt         # Transaction interface
│       └── FirebaseTransactionRepository.kt # Firestore transaction implementation
│
├── ui/
│   ├── FinanceTrackerApp.kt                 # Root composable, nav host setup
│   │
│   ├── theme/
│   │   ├── Color.kt                         # App colour palette
│   │   ├── Theme.kt                         # MaterialTheme wrapper
│   │   └── Type.kt                          # Typography definitions
│   │
│   ├── viewmodel/
│   │   ├── FinanceUiState.kt                # UI state data class + AppTab / AuthMode / Goal enums
│   │   └── FinanceViewModel.kt              # Single source of truth ViewModel
│   │
│   ├── components/                          # Reusable composables shared across screens
│   │   ├── AppBottomNavigation.kt           # Bottom nav bar
│   │   ├── GoalCard.kt                      # Goal card (primary & secondary)
│   │   ├── HeaderSection.kt                 # Greeting header with sign-out
│   │   ├── ProfileStat.kt                   # Profile statistics tile
│   │   ├── SpendlyDesign.kt                 # Design tokens / shared styles
│   │   ├── SummaryCard.kt                   # Individual stat card
│   │   ├── SummaryPanel.kt                  # Income / Expenses / Savings row
│   │   └── TransactionListItem.kt           # Single transaction row
│   │
│   ├── screen/
│   │   ├── AuthScreen.kt                    # Sign-in / Sign-up screen
│   │   ├── FirebaseSetupScreen.kt           # Shown when google-services.json is missing
│   │   ├── DashboardScreen.kt               # Shell that hosts bottom-nav screens
│   │   ├── FinanceTrackerApp.kt             # Root router / top-level app composable
│   │   │
│   │   ├── home/
│   │   │   └── HomeScreen.kt                # Dashboard with summary & recent transactions
│   │   │
│   │   ├── transactions/
│   │   │   ├── TransactionsScreen.kt        # Transaction list with All/Income/Expense filter
│   │   │   ├── AddIncomeScreen.kt           # Add income transaction form
│   │   │   └── AddExpenseScreen.kt          # Add expense transaction form
│   │   │
│   │   ├── goals/
│   │   │   └── GoalsScreen.kt               # Goals listing (primary & secondary)
│   │   │
│   │   ├── analytics/
│   │   │   └── AnalyticsScreen.kt           # Charts and spending insights
│   │   │
│   │   └── profile/
│   │       └── ProfileScreen.kt             # User profile and statistics
│   │
│   └── util/
│       └── UiUtils.kt                       # Shared UI helper functions
│
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

The project is distributed among 4 team members following MVVM architecture and modular code structure.

### 1. Chamika — Dashboard & Profile

**Files owned:**
- `ui/screen/home/HomeScreen.kt` — Dashboard with recent transactions and summary
- `ui/screen/profile/ProfileScreen.kt` — User profile and statistics
- `ui/components/HeaderSection.kt` — Reusable header with greeting and sign out
- `ui/components/ProfileStat.kt` — Profile statistics display component

**Shared components (supports dashboard):**
- `ui/components/SummaryPanel.kt` — Income/Expenses/Savings summary
- `ui/components/SummaryCard.kt` — Individual stat card
- `ui/components/TransactionListItem.kt` — Transaction display (shared with Yesen)
- `ui/components/GoalCard.kt` — Goal display (shared with Nikini)

**Files to create later:**
- Dashboard detail / expanded view (optional)
- Profile settings and edit functionality
- User preferences page

---

### 2. Yesen — Transactions Page

**Files owned:**
- `ui/screen/transactions/TransactionsScreen.kt` — Transactions listing with filters (All/Income/Expenses)
- `ui/screen/transactions/AddIncomeScreen.kt` — Add income transaction form
- `ui/screen/transactions/AddExpenseScreen.kt` — Add expense transaction form
- `ui/components/TransactionListItem.kt` — Individual transaction card display

**Shared components:**
- `ui/components/SummaryPanel.kt` — Transaction statistics (shared)

**Files to create later:**
- `ui/screen/transactions/TransactionDetailScreen.kt` — Individual transaction details
- `ui/viewmodel/TransactionFilterViewModel.kt` — Advanced filtering logic
- `data/model/TransactionFilter.kt` — Filter data model
- Transaction search functionality

---

### 3. Mahima — Analytics Page & Firebase Configuration

**Files owned:**
- `data/firebase/FirebaseBootstrap.kt` — Firebase initialisation and setup
- `data/repository/FirebaseAuthRepository.kt` — Firebase authentication implementation
- `data/repository/FirebaseTransactionRepository.kt` — Firebase transaction persistence
- `ui/screen/AuthScreen.kt` — Authentication screen (Sign-in/Sign-up)
- `ui/screen/FirebaseSetupScreen.kt` — Firebase configuration screen
- `ui/screen/analytics/AnalyticsScreen.kt` — Analytics dashboard with charts and insights

**Files to create later:**
- `data/service/AnalyticsDataService.kt` — Analytics data aggregation and calculations
- `data/model/AnalyticsMetrics.kt` — Analytics data models
- `ui/screen/analytics/DetailedAnalyticsScreen.kt` — Advanced analytics views
- Enhanced Firestore security rules and indexes
- Cloud Functions (if needed for backend analytics)

---

### 4. Nikini — Goals Page

**Files owned:**
- `ui/screen/goals/GoalsScreen.kt` — Goals listing with primary and secondary goals
- `ui/components/GoalCard.kt` — Goal card display component (includes PrimaryGoalCard)

**Files to create later:**
- `ui/screen/goals/GoalDetailScreen.kt` — Individual goal details and tracking
- `ui/screen/goals/AddGoalModal.kt` — Add/Edit goal form
- `ui/screen/goals/GoalProgressScreen.kt` — Goal progress tracking with milestones
- `data/model/GoalMilestone.kt` — Goal milestone data model
- Goal notification/reminder functionality

---

## Development Commit Order

### Phase 1 — Foundation (Week 1)

1. **Mahima** — Firebase & Auth foundation
   - `FirebaseBootstrap.kt`, `FirebaseAuthRepository.kt`, `FirebaseTransactionRepository.kt`
   - All other members depend on this for backend integration

2. **All** — Shared components (can be parallel)
   - `SummaryPanel.kt`, `SummaryCard.kt`, `TransactionListItem.kt`, `GoalCard.kt`, `HeaderSection.kt`, `ProfileStat.kt`, `AppBottomNavigation.kt`

### Phase 2 — Individual Screens (Week 2–3)

3. **Chamika** — Profile screen → `ProfileScreen.kt`
4. **Chamika** — Home/Dashboard screen → `HomeScreen.kt`
5. **Yesen** — Transactions screen → `TransactionsScreen.kt`, `AddIncomeScreen.kt`, `AddExpenseScreen.kt`
6. **Nikini** — Goals screen → `GoalsScreen.kt`

### Phase 3 — Advanced Features (Week 4+)

7. **Mahima** — Analytics screen → `AnalyticsScreen.kt`
8. **All** — Enhanced features and detail screens as listed in each member's "Files to create later"

---

## Architecture Overview

This project follows MVVM. The ViewModel owns all state and exposes it as a single `StateFlow<FinanceUiState>`. Screens are pure composables — they receive state and callbacks, and never reference the ViewModel directly.

```
[ Model — Data Layer ]  (Mahima)
    AuthRepository  (interface)
    TransactionRepository  (interface)
        │  implemented by
    FirebaseAuthRepository
    FirebaseTransactionRepository
        │  operate on
    FinanceTransaction  (data class)
    UserSession  (data class)

            │  injected into
            ▼

[ ViewModel ]  (shared foundation)
    FinanceViewModel
        - holds AuthRepository + TransactionRepository
        - exposes StateFlow<FinanceUiState>  (single state object)
        - exposes event functions: submitAuth, addTransaction, selectTab, signOut …
        - constructed in MainActivity via FinanceViewModel.Factory

            │  state + lambdas passed down
            ▼

[ View — UI Layer ]
    MainActivity
        └── FinanceTrackerApp          # collects uiState; routes to correct screen
                ├── FirebaseSetupScreen    # shown when google-services.json is absent
                ├── AuthScreen             # sign-in / create-account  (Mahima)
                └── FinanceTrackerApp       # tab router + flow controller (root composable)
                    ├── AppBottomNavigation        (component)
                        ├── HomeScreen                 (Chamika)
                        │     ├── HeaderSection
                        │     ├── SummaryPanel / SummaryCard
                        │     ├── GoalCard
                        │     └── TransactionListItem
                        ├── TransactionsScreen          (Yesen)
                        │     ├── AddIncomeScreen / AddExpenseScreen
                        │     ├── SummaryPanel
                        │     └── TransactionListItem
                        ├── GoalsScreen                 (Nikini)
                        │     └── GoalCard
                        ├── AnalyticsScreen             (Mahima)
                        └── ProfileScreen               (Chamika)
                              └── ProfileStat
```

Key rules every team member must follow:
- Never call `viewModel()` inside a screen composable — the ViewModel is only touched in `MainActivity` and `FinanceTrackerApp`.
- Screens only take `state: FinanceUiState` and typed lambda callbacks as parameters.
- All state mutations go through `FinanceViewModel` functions — never mutate state directly in a composable.
- `FinanceUiState` is a plain `data class` (not a sealed class). All screen-level flags (`isLoading`, `currentTab`, `isBusy`, etc.) live inside it.


---

## Initial Scaffold — Files Needed to Initialise the Project

These are the minimum files required for the project to compile and launch (blank screens, no feature logic). Every file listed here must exist before any team member starts implementing their screen.

### Gradle & Config

```
Financial-Tracker-Mobile-Kotlin/
├── build.gradle.kts                  # Project-level: applies AGP, Kotlin, Google Services plugins
├── settings.gradle.kts               # Declares root project name and :app module
├── gradle/
│   └── libs.versions.toml            # Version catalog — all dependency versions & aliases
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties     # Gradle 8.7
├── gradlew / gradlew.bat
└── app/
    ├── build.gradle.kts              # App-level: Compose, Firebase, lifecycle dependencies
    ├── proguard-rules.pro
    ├── google-services.json          # Firebase config (add manually — see Firebase Setup)
    └── src/main/
        └── AndroidManifest.xml       # INTERNET permission + MainActivity entry point
```

Key versions (`gradle/libs.versions.toml`):

| Dependency | Version |
|---|---|
| Android Gradle Plugin | 8.5.2 |
| Kotlin | 1.9.24 |
| Compose BOM | 2024.06.00 |
| Compose Compiler Extension | 1.5.14 |
| Firebase BOM | 33.1.2 |
| Coroutines | 1.8.1 |
| Lifecycle / ViewModel | 2.8.4 |

---

### Kotlin Source — Minimum Scaffold

All files live under `app/src/main/kotlin/com/spendly/financetracker/`.

```
financetracker/
│
├── MainActivity.kt                          # setContent { FinanceTrackerApp() }
│
├── data/
│   ├── firebase/
│   │   └── FirebaseBootstrap.kt             # Checks if google-services.json is present
│   ├── model/
│   │   ├── FinanceTransaction.kt            # Data class: id, title, amount, type, date, category
│   │   └── UserSession.kt                   # Data class: uid, email, displayName
│   └── repository/
│       ├── AuthRepository.kt                # Interface: signIn, signUp, signOut, currentUser
│       ├── FirebaseAuthRepository.kt        # Implements AuthRepository via FirebaseAuth
│       ├── TransactionRepository.kt         # Interface: add, update, delete, observe
│       └── FirebaseTransactionRepository.kt # Implements TransactionRepository via Firestore
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt                         # App colour tokens
│   │   ├── Theme.kt                         # MaterialTheme wrapper (light/dark)
│   │   └── Type.kt                          # Typography scale
│   │
│   ├── viewmodel/
│   │   ├── FinanceUiState.kt                # Data class: all UI state + AppTab / AuthMode / Goal enums
│   │   └── FinanceViewModel.kt              # Holds auth + transaction state; exposes StateFlow<FinanceUiState>
│   │
│   ├── screen/
│   │   ├── AuthScreen.kt                    # Sign-in / Sign-up form (stub UI is fine)
│   │   ├── FirebaseSetupScreen.kt           # Shown when google-services.json is absent
│   │   ├── MainAppScreen.kt                 # (legacy) previous tab router implementation — FinanceTrackerApp is the current router
│   │   └── DashboardScreen.kt              # Legacy scaffold screen (kept for reference)
│   │
│   ├── components/
│   │   └── AppBottomNavigation.kt           # BottomNavigation with 4 route destinations
│   │
│   └── FinanceTrackerApp.kt                 # Root composable — entry point called from MainActivity
```

> Screen files for Home, Transactions, Goals, Analytics, and Profile only need an empty `@Composable fun XScreen()` placeholder at this stage. The same applies to all other components (`SummaryPanel`, `GoalCard`, etc.) — they just need to exist so imports resolve.

### Why each file is required at scaffold stage

| File | Why it must exist |
|---|---|
| `MainActivity.kt` | App entry point — creates `FinanceViewModel` via `Factory` and calls `FinanceTrackerApp` |
| `FinanceTrackerApp.kt` | Called directly from `MainActivity`; collects `uiState` and routes to the correct screen |
| `AuthScreen.kt` | `FinanceTrackerApp` renders this when `state.session == null` |
| `FirebaseSetupScreen.kt` | `FinanceTrackerApp` renders this when `state.isFirebaseConfigured == false` |
| `FinanceTrackerApp.kt` | Called by `MainActivity`; renders screens and hosts the tab router / `AppBottomNavigation` |
| `DashboardScreen.kt` | Legacy scaffold screen — still referenced; must exist to compile |
| `AppBottomNavigation.kt` | Rendered inside `FinanceTrackerApp`; missing = compile error |
| `FirebaseBootstrap.kt` | Called by `FirebaseAuthRepository` to check config; drives `isFirebaseConfigured` flag |
| `FinanceViewModel.kt` | Constructed in `MainActivity`; passed into every composable — missing = crash |
| `FinanceUiState.kt` | Defines `FinanceUiState`, `AppTab`, `TransactionTab`, `AuthMode`, `Goal` — all referenced by ViewModel and screens |
| `AuthRepository.kt` + `FirebaseAuthRepository.kt` | Constructor parameters of `FinanceViewModel.Factory`; missing = compile error |
| `TransactionRepository.kt` + `FirebaseTransactionRepository.kt` | Same — required by `FinanceViewModel.Factory` |
| `FinanceTransaction.kt` + `UserSession.kt` | Used by repositories, `FinanceUiState`, and screen composables |
| `Color.kt`, `Theme.kt`, `Type.kt` | `MainActivity` wraps everything in `FinanceTrackerTheme`; missing = compile error |

---

## Build & Run (detailed)

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
- Prefer Android Studio for iterative development — it handles signing, emulators and device management.
- If you change `google-services.json`, do a clean build to ensure generated sources are updated:

```bash
./gradlew.bat clean assembleDebug
```

## Firebase — practical tips

- The app includes a `FirebaseSetupScreen` that appears when a Firebase config is not detected. This allows compiling and testing UI without a real Firebase project.
- When adding `app/google-services.json`, match the package name `com.spendly.financetracker` used in `AndroidManifest.xml`.
- After uploading `firestore.rules` and `firestore.indexes.json` to your Firebase project, test client access with a test user to verify rules behave as expected.

Recommended quick checklist when configuring Firebase for development:
- Create a dedicated Firebase project for development and another for production.
- Enable Authentication → Email/Password for basic sign-in flows.
- Create some test documents in Firestore and verify read/write with the app.
- Use the Firebase Emulator Suite for local integration testing when possible.

## Architecture & design decisions (concise)

- Single `FinanceViewModel` drives the entire `FinanceUiState` exposed as `StateFlow`.
- Screens are pure composables: they accept `FinanceUiState` + typed callbacks and never call `viewModel()` directly.
- Repositories provide interfaces in `data/repository` and concrete implementations under `data/remote` or `data/local`.
- DI is provided via `di/AppModule.kt` and `di/RepositoryModule.kt` — add bindings here for new data sources.

Why this matters:
- Keeping ViewModel creation centralised makes it easier to reason about state and test.
- Interfaces + DI make swapping implementations (Room, Firestore, network) straightforward.

## Developer workflow and conventions

- Branching:
    - `main` contains stable development-ready code.
    - Create feature branches named `feature/<short-name>` or `fix/<short-desc>`.

- Commits:
    - Keep commits focused and atomic.
    - Use present-tense imperative messages, e.g. "Add TransactionForm validation".

- Code style:
    - Follow Kotlin conventions. Use 2-space indents consistent with the project.
    - Prefer descriptive names over abbreviations.
    - Avoid one-letter variables except in local loop counters.

- Pull requests:
    - PRs should include a short description, affected screens/files, and testing notes.
    - Request at least one review before merging.

## Testing guidance

- Unit tests live under `app/src/test` and instrumentation tests under `app/src/androidTest`.
- Use `kotlinx.coroutines.test` for ViewModel coroutine testing where `StateFlow` updates are asserted.
- For UI tests, prefer Compose testing utilities when interacting with Compose UI nodes.

Example commands:

```bash
./gradlew.bat test                # unit tests
./gradlew.bat connectedAndroidTest  # instrumentation tests
```

## Debugging & common gotchas

- Missing `google-services.json` will not break compilation but will show `FirebaseSetupScreen` at runtime.
- If you see unresolved symbols for theme files (`Color.kt`, `Theme.kt`, `Type.kt`) ensure the package path is correct and Gradle has executed a successful sync.
- When switching branches with Gradle changes, run `./gradlew.bat clean` and let Android Studio sync to avoid build cache issues.

## Adding a new data source (short guide)

1. Define a repository interface under `data/repository` describing the operations your feature needs.
2. Add a concrete implementation under `data/remote` (for Firestore/Network) or `data/local` (for Room).
3. Register the implementation in `di/RepositoryModule.kt` so the ViewModel receives it.
4. Add unit tests for the repository behaviour and integration tests if possible.

## Contributing checklist (for PRs)

- [ ] Code builds locally and CI (if enabled) passes.
- [ ] New code includes unit tests where appropriate.
- [ ] UI changes include a screenshot or description in the PR.
- [ ] Update `README.md` or project docs if the change affects developer setup.

## Release & versioning notes

- This repository does not enforce a strict release process by default. Tagging releases with semantic versions (vMAJOR.MINOR.PATCH) is recommended.
- Keep `gradle/libs.versions.toml` updated for dependency upgrades and audit regularly.

## File map — where to look first

- App entry: `app/src/main/kotlin/com/spendly/financetracker/MainActivity.kt`
- Root composable / router: `app/src/main/kotlin/com/spendly/financetracker/ui/FinanceTrackerApp.kt`
- ViewModel: `app/src/main/kotlin/com/spendly/financetracker/ui/viewmodel/FinanceViewModel.kt`
- UI screens: `app/src/main/kotlin/com/spendly/financetracker/ui/screen/`
- Repositories & models: `app/src/main/kotlin/com/spendly/financetracker/data/`

If you need help locating a file, ask and I can point directly to the file path.

## Troubleshooting checklist (quick)

- Build failures after dependency updates:
    - Run `./gradlew.bat clean` then `./gradlew.bat build`.
    - If Android Studio shows gradle sync errors, click "Sync Project with Gradle Files".

- Emulator/device installation fails:
    - Ensure developer mode and USB debugging are enabled (device).
    - Increase emulator RAM if it fails to boot.

- Firebase permission errors:
    - Inspect `firestore.rules` and simulate rules via the Firebase emulator or console.

## Security considerations

- Do not commit production `google-services.json` containing sensitive project settings into a public repository.
- Keep `firestore.rules` restrictive for production — allow only authorised access.


