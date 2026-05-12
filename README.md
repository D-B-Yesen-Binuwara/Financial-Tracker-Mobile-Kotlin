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
