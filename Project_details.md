A SAMPLE STRUCTURE to get an idea

com.spendly.financetracker

│

├── MainActivity.kt

├── FinanceTrackerApp.kt

│

├── core/

│ ├── navigation/

│ ├── utils/

│ ├── constants/

│ ├── components/

│ ├── theme/

│ └── state/

│

├── di/

│ ├── AppModule.kt

│ ├── FirebaseModule.kt

│ └── RepositoryModule.kt

│

├── data/

│ ├── local/

│ │ ├── dao/

│ │ ├── database/

│ │ └── entity/

│ │

│ ├── remote/

│ │ ├── auth/

│ │ └── firestore/

│ │

│ ├── model/

│ │

│ └── repository/

│

├── domain/

│ ├── model/

│ ├── repository/

│ └── usecase/

│

├── features/

│ ├── auth/

│ │ ├── ui/

│ │ ├── viewmodel/

│ │ └── state/

│ │

│ ├── dashboard/

│ │ ├── ui/

│ │ ├── viewmodel/

│ │ └── state/

│ │

│ ├── expenses/

│ ├── income/

│ ├── goals/

│ ├── analytics/

│ └── profile/

│

└── services/

├── notification/

└── sync/

&nbsp;

**01\. Project / Application Overview**

**1.1 System Name**

**Smart Personal Finance Management System (Android)**

**1.2 Problem Context**

The system is designed for users who:

- Have **multi-source, irregular income**
- Cannot track **actual earnings vs perceived earnings**
- Have **fragmented expenses across multiple channels**
- Fail to maintain financial tools due to **high friction and poor UX**
- Have a **specific financial goal but no measurable progress tracking**

**1.3 What the System Does**

This application is a **goal-driven, low-friction financial tracking system** that:

**Core Capabilities:**

- Aggregates **multi-source income** (salary, freelance, ads, crypto)
- Tracks **expenses with minimal user effort**
- Automatically **normalizes multi-currency values**
- Categorizes spending into:
    - **Committed (fixed)**
    - **Discretionary (variable)**
-  Provides **real-time financial insights**
- Links **daily spending behavior → long-term goal progress**
- Visualizes:
    - Spending distribution
    - Income trends
    - Savings trajectory

&nbsp;

**1.4 Scope of the System**

**In Scope**

- Android mobile application (API 26+)
- Manual data entry (optimized UX)
- Firebase-backed cloud system
- Offline-first support (Room DB)
- Real-time sync
- Goal tracking and projections

**Out of Scope**

- Direct bank API integrations
- Investment advisory systems
- Enterprise-scale accounting features

&nbsp;

<div class="joplin-table-wrapper"><table class="MsoTableGrid jop-noMdConv" border="1" cellspacing="0" cellpadding="0" width="100%" style="width: 100.0%; border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;"><tbody class="jop-noMdConv"><tr style="mso-yfti-irow: 0; mso-yfti-firstrow: yes;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p><b class="jop-noMdConv">Layer<o:p class="jop-noMdConv"></o:p></b></p></td><td width="44%" valign="top" style="width: 44.34%; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p><b class="jop-noMdConv">Technology<o:p class="jop-noMdConv"></o:p></b></p></td><td width="35%" valign="top" style="width: 35.42%; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p><b class="jop-noMdConv">Purpose<o:p class="jop-noMdConv"></o:p></b></p></td></tr><tr style="mso-yfti-irow: 1;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Language<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Kotlin<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Primary development<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 2;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>UI<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Jetpack Compose (Material 3)<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Declarative UI<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 3;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Architecture<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>MVVM<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Separation of concerns<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 4;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Navigation<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Navigation Compose<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Screen routing<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 5;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Backend<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Firebase<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Backend-as-a-Service<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 6;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Database<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Firestore<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Cloud data storage<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 7;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Auth<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Firebase Authentication<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>User identity<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 8;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Local DB<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Room<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Offline caching<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 9;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Async<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Kotlin Coroutines<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Concurrency<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 10;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>State<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>StateFlow / LiveData<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>UI state management<o:p class="jop-noMdConv"></o:p></p></td></tr><tr style="mso-yfti-irow: 11; mso-yfti-lastrow: yes;" class="jop-noMdConv"><td width="20%" valign="top" style="width: 20.24%; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>DI<o:p class="jop-noMdConv"></o:p></p></td><td width="44%" valign="top" style="width: 44.34%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Hilt<o:p class="jop-noMdConv"></o:p></p></td><td width="35%" valign="top" style="width: 35.42%; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;" class="jop-noMdConv"><p>Dependency injection<o:p class="jop-noMdConv"></o:p></p></td></tr></tbody></table></div>

&nbsp;

**5.3 UX Strategy**

- Reduce friction > add features
- Show value in **first 7 days**
- Prioritize:
    - Dashboard
    - Expense entry
    - Goal progress

&nbsp;

**Functional Requirements**

**User Management**

The system shall provide a secure authentication mechanism that allows users to register, log in, and log out using email and password credentials via Firebase Authentication. Once authenticated, the system must maintain persistent user sessions to prevent repeated logins and ensure a seamless user experience. Each user’s financial data must be logically isolated, ensuring that all income, expense, and goal-related information is securely associated with the authenticated user only. Additionally, the system should handle authentication errors gracefully, including invalid credentials, network failures, and session timeouts, without disrupting the user experience.

**Income Management**

The system shall enable users to record income from multiple heterogeneous sources, including but not limited to salary, freelance work, advertisement revenue, and cryptocurrency transactions. Each income entry must capture attributes such as source type, amount, currency, date, frequency (recurring or one-time), and optional descriptive notes. Given the multi-currency nature of the user’s income streams, the system must normalize all values into a base currency (LKR) using an appropriate conversion mechanism to ensure consistency in financial calculations. The application must compute and present aggregated income insights, including total monthly income, average income over time, and variability across different periods. It must also support irregular and unpredictable income patterns without enforcing rigid structures, thereby accurately reflecting the user’s real earning behavior.

**Expense Management**

The system shall allow users to log expenses quickly and efficiently with minimal interaction overhead, ensuring that a typical expense entry can be completed within a few seconds. Each expense record must include details such as amount, category (e.g., food, transport, rent, utilities, subscriptions, or custom-defined), payment method, and date. The system must support classification of expenses into committed (fixed/essential) and discretionary (variable/non-essential) categories to enable deeper financial analysis. Users should be able to modify or delete existing entries, and the system must maintain accurate aggregations of expenses across daily, weekly, and monthly timeframes. Furthermore, the system must consolidate expenses across all input channels into a unified dataset to eliminate fragmentation and provide a coherent financial overview.

**Goal Management**

The system shall provide functionality for users to define and manage financial goals with clear parameters, including a goal name, target amount, and deadline. Based on the user’s income and expense data, the system must dynamically calculate key metrics such as required monthly savings, current progress as a percentage, remaining amount, and projected completion timeline. The system should continuously update these calculations in real time as new financial data is recorded. Additionally, it must present goal progress through intuitive visual indicators, such as progress bars and projections, ensuring that users can clearly understand how their daily financial behavior impacts their long-term objectives.

**Financial Insights and Analytics**

The system shall generate analytical insights that provide users with a clear understanding of their financial behavior. This includes visualizing income trends over time, breaking down income by source, and presenting expense distributions across categories using appropriate graphical representations. The system must also compute and display the ratio between committed and discretionary spending, enabling users to assess financial discipline. Beyond static summaries, the system should derive behavioral insights, such as highlighting unusual spending patterns or deviations from expected savings rates, thereby assisting users in making informed financial decisions.

**Dashboard Functionality**

The system shall provide a centralized dashboard that serves as the primary interface for financial awareness. This dashboard must present a consolidated view of key financial indicators, including total available balance, current month’s income, current month’s expenses, net savings, and goal progress. The information must be updated in real time and presented in a concise, visually interpretable format, allowing users to quickly assess their financial status without navigating through multiple screens.

**Notification and Reminder System**

The system should include a notification mechanism designed to improve user engagement and adherence to financial tracking. This includes reminders to log daily expenses, alerts when spending exceeds typical patterns or predefined thresholds, and updates on goal progress milestones. Notifications must be context-aware and non-intrusive, ensuring that they provide value without overwhelming the user.

**Offline Functionality**

The system shall support offline operation to ensure uninterrupted usability in environments with limited or no internet connectivity. Users must be able to record income and expenses without an active network connection, with all data stored locally using Room persistence. Once connectivity is restored, the system must automatically synchronize local data with the cloud database, ensuring consistency without requiring manual intervention.

**Data Synchronization and Real-Time Updates**

The system shall implement real-time data synchronization using Firebase Firestore, ensuring that any updates to income, expenses, or goals are immediately reflected across the application. It must handle potential data conflicts using a consistent strategy, such as last-write-wins, while maintaining data integrity. The system should also ensure that synchronization processes are efficient and do not degrade application performance, even with increasing data volumes.

&nbsp;