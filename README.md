# Financial-Tracker-Mobile-Kotlin

Spendly is a Kotlin-only Jetpack Compose finance tracker scaffolded for Android API 26+ with Firebase Authentication and Cloud Firestore persistence.

## Project setup

- Java/Kotlin bytecode target: Java 17
- Minimum SDK: 26
- Target SDK: 36
- UI: Jetpack Compose only
- Architecture: MVVM with repositories in the data layer and ViewModel-owned UI state
- Firebase: Email/Password Authentication and Cloud Firestore

## Firebase setup

1. Create a Firebase Android app with package name `com.spendly.financetracker`.
2. Download `google-services.json` from Firebase Console.
3. Place it at `app/google-services.json`.
4. Enable Authentication > Sign-in method > Email/Password.
5. Create a Firestore database.
6. Deploy or submit the included `firestore.rules`.

The app builds without a local Firebase config and shows a setup screen. Firebase sign-in and Firestore persistence become active after the real `google-services.json` file is added.

---

## Team Workload Distribution

The project is distributed among 4 team members following MVVM architecture and modular code structure:

### 1. **Chamika** - Dashboard & Profile

**Files Created (Currently):**
- `ui/screen/home/HomeScreen.kt` - Dashboard with recent transactions and summary
- `ui/screen/profile/ProfileScreen.kt` - User profile and statistics
- `ui/components/HeaderSection.kt` - Reusable header with greeting and sign out
- `ui/components/ProfileStat.kt` - Profile statistics display component

**Shared Components (Supports Dashboard):**
- `ui/components/SummaryPanel.kt` - Income/Expenses/Savings summary
- `ui/components/SummaryCard.kt` - Individual stat card
- `ui/components/TransactionListItem.kt` - Transaction display (shared with Yesen)
- `ui/components/GoalCard.kt` - Goal display (shared with Nikini)

**Files to Create Later:**
- Dashboard detail/expanded view (optional)
- Profile settings and edit functionality
- User preferences page

---

### 2. **Yesen** - Transactions Page

**Files Created (Currently):**
- `ui/screen/transactions/TransactionsScreen.kt` - Transactions listing with filters (All/Income/Expenses)
- `ui/components/TransactionListItem.kt` - Individual transaction card display

**Shared Components:**
- `ui/components/SummaryPanel.kt` - Transaction statistics (shared)

**Files to Create Later:**
- `ui/screen/transactions/TransactionDetailScreen.kt` - Individual transaction details
- `ui/screen/transactions/AddTransactionModal.kt` - Add/Edit transaction form
- `ui/viewmodel/TransactionFilterViewModel.kt` - Advanced filtering logic
- `data/model/TransactionFilter.kt` - Filter data model
- Transaction search functionality

---

### 3. **Mahima** - Analytics Page & Firebase Configurations

**Files Created (Currently):**
- `ui/screen/analytics/AnalyticsScreen.kt` - Analytics dashboard with charts and insights
- `data/firebase/FirebaseBootstrap.kt` - Firebase initialization and setup
- `data/repository/FirebaseAuthRepository.kt` - Firebase authentication implementation
- `data/repository/FirebaseTransactionRepository.kt` - Firebase transaction persistence
- `ui/screen/AuthScreen.kt` - Authentication screen (Sign-in/Sign-up)
- `ui/screen/FirebaseSetupScreen.kt` - Firebase configuration screen

**Files to Create Later:**
- `data/service/AnalyticsDataService.kt` - Analytics data aggregation and calculations
- `data/model/AnalyticsMetrics.kt` - Analytics data models
- `ui/screen/analytics/DetailedAnalyticsScreen.kt` - Advanced analytics views
- Enhanced Firestore security rules and indexes
- Cloud Functions (if needed for backend analytics)

---

### 4. **Nikini** - Goals Page

**Files Created (Currently):**
- `ui/screen/goals/GoalsScreen.kt` - Goals listing with primary and secondary goals
- `ui/components/GoalCard.kt` - Goal card display component (includes PrimaryGoalCard)

**Files to Create Later:**
- `ui/screen/goals/GoalDetailScreen.kt` - Individual goal details and tracking
- `ui/screen/goals/AddGoalModal.kt` - Add/Edit goal form
- `ui/screen/goals/GoalProgressScreen.kt` - Goal progress tracking with milestones
- `data/model/GoalMilestone.kt` - Goal milestone data model
- Goal notification/reminder functionality

---

## Development Commit Order (Natural Flow)

Follow this order for committing work to maintain a coherent development progression:

### **Phase 1: Foundation (Week 1)**
1. **Mahima - Firebase & Auth Foundation**
   - Commit: Firebase configuration files and authentication repositories
   - Files: `FirebaseBootstrap.kt`, `FirebaseAuthRepository.kt`, `FirebaseTransactionRepository.kt`
   - Reason: All other team members depend on Firebase setup for backend integration

2. **All - Shared Components** (can be parallel)
   - Commit: All reusable UI components
   - Files: `SummaryPanel.kt`, `SummaryCard.kt`, `TransactionListItem.kt`, `GoalCard.kt`, `HeaderSection.kt`, `ProfileStat.kt`, `AppBottomNavigation.kt`
   - Reason: Components are foundational for all screens

### **Phase 2: Individual Screens (Week 2-3)**
3. **Chamika - Profile Screen**
   - Commit: Profile screen implementation
   - Files: `ProfileScreen.kt`, `ui/screen/profile/`
   - Dependencies: Components from Phase 1

4. **Chamika - Home/Dashboard Screen**
   - Commit: Home screen implementation  
   - Files: `HomeScreen.kt`, `ui/screen/home/`
   - Dependencies: Components from Phase 1, ProfileScreen logic

5. **Yesen - Transactions Screen**
   - Commit: Transactions listing and filtering
   - Files: `TransactionsScreen.kt`, `ui/screen/transactions/`
   - Dependencies: Components from Phase 1

6. **Nikini - Goals Screen**
   - Commit: Goals listing and management
   - Files: `GoalsScreen.kt`, `ui/screen/goals/`
   - Dependencies: Components from Phase 1

### **Phase 3: Advanced Features (Week 4+)**
7. **Mahima - Analytics Screen**
   - Commit: Analytics dashboard with visualizations
   - Files: `AnalyticsScreen.kt`, `ui/screen/analytics/`
   - Dependencies: Phase 1 foundation, ViewModel integration

8. **Each Member - Enhanced Features (Parallel)**
   - Commit individual enhancements and detail screens as defined in "Files to Create Later"
   - Examples: Transaction details, Goal progress tracking, Advanced analytics, etc.

---

## Architecture Overview

```
MVVM Architecture with Modular Components:

FinanceViewModel (Single Source of Truth)
    ↓
    ├── HomeScreen (Chamika)
    ├── TransactionsScreen (Yesen)
    ├── GoalsScreen (Nikini)
    ├── AnalyticsScreen (Mahima)
    └── ProfileScreen (Chamika)
        ↓ (All use)
        ├── Reusable Components
        │   ├── SummaryPanel
        │   ├── SummaryCard
        │   ├── TransactionListItem
        │   ├── GoalCard
        │   └── ... (more)
        ↓ (All depend on)
        ├── Firebase Repositories (Mahima)
        ├── Data Models
        └── ViewModels
```

---

      ## Shared / Core Files and Ownership

      These files are core to the app and should be treated as shared ownership. Some files have a clear primary maintainer (listed), but everyone depends on them.

      **Belongs to all (shared / core):**
      - `app/src/main/kotlin/com/spendly/financetracker/ui/viewmodel/FinanceViewModel.kt` — central ViewModel coordinating UI state
      - `app/src/main/kotlin/com/spendly/financetracker/ui/viewmodel/FinanceUiState.kt` — UI state definitions used by all screens
      - `app/src/main/kotlin/com/spendly/financetracker/ui/FinanceTrackerApp.kt` — app entry + high-level orchestration
      - Data models:
         - `app/src/main/kotlin/com/spendly/financetracker/data/model/FinanceTransaction.kt`
         - `app/src/main/kotlin/com/spendly/financetracker/data/model/UserSession.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/data/repository/TransactionRepository.kt` — repository interface (implementation owned by Mahima)

      **Primary owner — Mahima (Firebase / backend):**
      - `app/src/main/kotlin/com/spendly/financetracker/data/firebase/FirebaseBootstrap.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/data/repository/FirebaseAuthRepository.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/data/repository/FirebaseTransactionRepository.kt`

      **Shared UI components (`<belongs to all>`):**
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/AppBottomNavigation.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/HeaderSection.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/SummaryCard.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/SummaryPanel.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/TransactionListItem.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/GoalCard.kt`
      - `app/src/main/kotlin/com/spendly/financetracker/ui/components/ProfileStat.kt`

      **Distribution check & guidance:**
      - The `FinanceViewModel` and `FinanceUiState` are intentionally shared — they coordinate state across all screens and should be reviewed by any PR that changes them.
      - `TransactionRepository.kt` is the shared abstraction; keep the interface stable. Mahima owns the Firebase implementation.
      - Components under `ui/components` are shared utilities and belong to all; small UI fixes can be made by any member but large refactors should be coordinated.

      **Commit guidance:**
      - Core/shared files (ViewModel, UI state, components) should be committed early (Phase 1). Mahima's Firebase commits should precede integration work that depends on the backend.
