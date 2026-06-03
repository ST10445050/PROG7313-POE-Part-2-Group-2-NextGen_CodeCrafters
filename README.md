# 📱 PROG7313 – POE Part 3

## FinTrack Budgeting App

### NextGen_CodeCrafters

---

## 👩‍💻 Project Overview

**FinTrack** is a mobile budgeting application developed for **PROG7313 – Programming 3C Portfolio of Evidence Part 3**. The application helps users manage their personal finances by tracking expenses, organising spending into categories, setting monthly budget goals, and viewing spending insights through dashboards, filters, reports, progress tracking, and analytics graphs.

The application was developed using **Android Studio**, **Kotlin**, and **Jetpack Compose**, with **Supabase** used as the cloud backend for authentication, database storage, and receipt image storage.

---
## 📄 Comprehensive Project Report

### Purpose of the App

FinTrack was developed as a mobile budgeting application for **PROG7313 POE Part 3**. The purpose of the app is to help users manage their personal finances by allowing them to record expenses, organise spending into categories, set monthly budget goals, and monitor their spending behaviour through visual summaries and analytics.

The app supports users in making better financial decisions by showing them where their money is being spent and whether they are staying within their selected minimum and maximum monthly budget goals. FinTrack also provides a personalised dashboard experience through a budgeting questionnaire, allowing users to receive dashboard content that is more relevant to their financial habits.

---

### Design Considerations

The design of FinTrack focused on creating a clear, user-friendly, and visually consistent mobile experience. The interface was developed using **Jetpack Compose**, allowing the team to build modern screens with reusable components and responsive layouts.

The app uses a consistent colour theme, clear headings, icons, buttons, cards, and navigation components to make the system easy to understand. The bottom navigation bar gives users quick access to the main sections of the app, while the hamburger menu provides access to additional features such as Budget Goals, Analytics, Help, and Logout.

User experience was also considered through validation and feedback. For example, when adding an expense, required fields such as amount, date, and category display user-friendly error messages if left empty. Optional fields such as start time, end time, description, and receipt image do not prevent the user from saving an expense.

The Analytics screen was designed to provide a visual representation of spending behaviour. The graph allows users to view spending by category, filter by date range, zoom into the graph, and compare spending against budget goals. The progress tracking feature was designed to make it easier for users to visually understand whether they are below, within, or above their budget goal range.

---

### Cloud Implementation Considerations

FinTrack uses **Supabase** as the cloud backend. Supabase was implemented to replace local-only storage and allow user data to be stored securely in the cloud. This supports authentication, user-specific data, database storage, and receipt image uploads.

The following Supabase services were used:

* **Supabase Auth** for registration, login, logout, and password reset.
* **Supabase Database** for storing profiles, questionnaire answers, categories, expenses, budget goals, and help information.
* **Supabase Storage** for storing uploaded receipt images.
* **Row Level Security policies** to ensure users can only access their own data.

Using Supabase improved the app by allowing data to persist online instead of being stored only on one device. This also made it easier to manage dynamic content such as Help & Support information from the database.

---

### GitHub Usage

GitHub was used for version control and team collaboration throughout the project. Each team member worked on separate branches for their allocated features. This allowed features to be developed independently before being merged into the main project branch.

The team used commits to track progress and document changes made to the project. Branches were merged after testing, and merge conflicts were resolved when different members worked on overlapping files. This helped the team manage development in a structured way and maintain a clear history of project changes.

The use of GitHub supported collaboration by allowing the team to:

* Work on features separately.
* Track code changes through commits.
* Merge completed features into shared branches.
* Resolve conflicts between different feature implementations.
* Maintain a final `main` branch for the completed application.

---

### GitHub Actions Usage

GitHub Actions was used to automate the build and checking process for the app. The workflow helps confirm whether the application can compile successfully after code is pushed or merged.

The GitHub Actions workflow includes steps such as:

* Checking out the project code.
* Setting up the required Java Development Kit.
* Running build or test tasks.
* Running lint checks.
* Building the debug APK.
* Uploading reports and APK artifacts where applicable.

GitHub Actions is important because it helps identify compile errors and project issues early. If a merge introduces a problem, the workflow fails and shows where the error occurred. This improves project quality by ensuring the team can check whether the app still builds correctly after changes are added.

---

### Testing Considerations

The app was tested manually using an emulator and physical device. Testing focused on confirming that the main features worked correctly and that data was saved and loaded from Supabase as expected.

The following areas were tested:

* User registration and login.
* Password reset functionality.
* Questionnaire completion and skip option.
* Personalised and generic dashboard behaviour.
* Category creation and default category seeding.
* Expense creation with required and optional fields.
* Receipt image upload to Supabase Storage.
* Budget goal saving and updating.
* Analytics filters for Today, This Week, This Month, All, and Custom.
* Graph display and progress tracking.
* Help & Support screen loading from Supabase.
* Logout and navigation flow.

Testing helped confirm that the app works as intended and that the main POE Part 3 requirements were met.

---


## 🔗 GitHub Repository

```text
https://github.com/ST10445050/PROG7313-POE-Part-2-Group-2-NextGen_CodeCrafters
```

---

## 🎥 Demonstration Video

```text
PASTE DEMONSTRATION VIDEO LINK HERE
```

---

## ✨ Features Implemented

### 🔐 User Authentication

FinTrack includes user registration, login, logout, and password reset functionality. Authentication is handled through Supabase Auth. Each user has their own profile and data, allowing the application to display user-specific categories, expenses, questionnaire answers, budget goals, and dashboard information.

---

### 📝 Questionnaire Feature

After successful registration, users are taken to a budgeting questionnaire. The questionnaire contains five budget-related questions that help personalise the user experience.

Users have two options:

* Complete the questionnaire to receive a personalised dashboard.
* Skip the questionnaire from Question 1 and continue to a generic dashboard.

If the user completes the questionnaire, their answers are saved in the Supabase database. When the user logs in again, the app retrieves the saved answers and displays a personalised dashboard based on those responses. If the user skips the questionnaire, the app displays a generic dashboard instead.

---

### 📊 Personalised Dashboard

The dashboard provides a user-specific overview of spending, recent expenses, budget progress, and personalised information. The dashboard uses the user’s saved questionnaire answers to customise the experience. If no questionnaire answers exist, a generic dashboard is shown.

---

### 💸 Expense Management

Users can add and view expenses. Each expense can include:

* Amount
* Date
* Category
* Optional start time
* Optional end time
* Optional description
* Optional receipt image

The required fields are amount, date, and category. If a user leaves any of these required fields empty, the app displays a clear user-friendly validation message. Optional fields do not block the expense from being saved.

---

### 🗂️ Category Management

Users can create, view, and delete expense categories. Default categories are seeded automatically for new users, including:

* Food
* Transport
* Groceries

Users can also add their own custom categories to better organise their spending.

---

### 🎯 Budget Goals

Users can set a minimum and maximum monthly budget goal. These goals are saved in Supabase and linked to the selected month and year. If a budget goal already exists for the selected month and year, the app updates the existing goal instead of creating duplicate records.

---

### 📈 Analytics and Interactive Graph

The Analytics screen displays spending in a visual graph format. Users can filter spending by:

* All expenses
* Today
* This Week
* This Month
* Custom date range

The “This Week” filter runs from Sunday to Saturday. The graph updates according to the selected date range and displays spending totals per category. It also supports tapping on graph bars, zooming in and out, horizontal scrolling, and viewing minimum and maximum goal indicators.

---

### 🧭 Progress Tracking

The progress tracking feature visually shows how well the user is doing in relation to their minimum and maximum monthly budget goals. It compares the user’s total spending against the saved goal range and uses visual indicators to show whether the user is below the minimum goal, within the goal range, or above the maximum goal.

This makes it easier for users to understand their spending behaviour without only relying on plain text values.

---

### 📋 Category Totals Report

Users can view total spending per category for selected time periods. This helps users identify which categories have the highest spending and supports better budget planning.

---

### 🆘 Help & Support Screen

The Help & Support screen provides users with guidance on how to use the app. The help content is stored in Supabase instead of being hardcoded directly in the app. This makes the content easier to manage and update.

---

### 🖼️ Receipt Image Upload

Users can optionally upload a receipt image when adding an expense. Receipt images are uploaded to Supabase Storage, and the public image URL is saved with the expense record in the Supabase `expenses` table.

---

### 🔍 Search and Filter

Users can filter expenses and analytics data based on selected time periods. This improves usability by helping users quickly view spending information for a specific day, week, month, or custom range.

---

### 📝 Logging

Meaningful logging was implemented using a central `AppLogger` utility. Logging is used for debugging, tracking app behaviour, Supabase database operations, storage uploads, and error handling.

Sensitive information such as passwords, Supabase keys, access tokens, reset links, and full session data are not logged.

---

## ⭐ Own Features 

### 1. Questionnaire Personalisation Feature

The questionnaire personalisation feature allows users to answer five budgeting-related questions after registration. The user can either complete the questionnaire or skip it from Question 1.

If the user completes the questionnaire, their answers are stored in the Supabase database. These answers are then used to personalise the dashboard when the user returns to the app. This makes the dashboard more relevant to the user’s budgeting habits and financial preferences.

If the user chooses to skip the questionnaire, they are taken to a generic dashboard. This gives users flexibility because they are not forced to complete the questionnaire, but they still have the option to personalise their experience.

---

### 2. Help & Support Screen

The Help & Support screen provides users with useful guidance on how to use the FinTrack application. It helps users understand the main app features and supports a better user experience.

The help content is stored in Supabase, which means it is not hardcoded into the app. This improves maintainability because support information can be updated from the database when needed.

---

## 🧱 System Architecture

The application follows the **MVVM architecture pattern**.

### UI Layer

The UI layer is built using Jetpack Compose. It contains the screens, reusable components, navigation elements, and visual layouts.

### ViewModel Layer

The ViewModel layer manages UI state, user actions, loading states, validation, and error messages.

### Repository Layer

The repository layer handles communication with Supabase. Repositories are used for authentication, profiles, categories, expenses, budget goals, questionnaire answers, help information, and receipt storage.

### Cloud Data Layer

Supabase is used as the backend service for authentication, database tables, and image storage.


---

## 🗄️ Database Structure

### `profiles`

Stores user profile information.

### `questionnaire_answers`

Stores the user’s questionnaire responses for dashboard personalisation.

### `categories`

Stores user-created and default spending categories.

### `expenses`

Stores expense records, including amount, category, date, optional description, optional time values, and optional receipt URL.

### `budget_goals`

Stores minimum and maximum monthly budget goals.

### `help_faqs`

Stores Help & Support content displayed in the app.

---

## ⚙️ Technologies Used

* Kotlin
* Android Studio
* Jetpack Compose
* Supabase Auth
* Supabase Database
* Supabase Storage
* Kotlin Coroutines
* MVVM Architecture
* GitHub
* GitHub Actions


---

## 🚀 How to Run the App

### Prerequisites

Before running the app, ensure that you have:

* Android Studio installed
* Android SDK installed
* An Android emulator or physical Android device
* Internet connection for Supabase services

---

### Steps to Run

1. Clone the repository:

```bash
git clone https://github.com/ST10445050/PROG7313-POE-Part-2-Group-2-NextGen_CodeCrafters.git
```

2. Open the project in Android Studio.

3. Allow Gradle to sync.

4. Select an emulator or connect a physical Android device.

5. Click **Run** to launch the application.

6. Register a new account or log in with an existing account.

---

## 🖼️ Screenshots of the App

Add your screenshots below.

### Landing / Login Screen

<img width="517" height="921" alt="image" src="https://github.com/user-attachments/assets/1988cf92-cba9-4169-bcb0-dda27267162d" />


<img width="516" height="922" alt="image" src="https://github.com/user-attachments/assets/efd671a5-54e3-4636-ad90-2a376e217d20" />



### Register Screen

<img width="517" height="925" alt="image" src="https://github.com/user-attachments/assets/1eec8acd-81aa-44ca-bf61-4475e9d7a3da" />


### Questionnaire Screen

<img width="515" height="920" alt="image" src="https://github.com/user-attachments/assets/6f913775-8205-49e9-931d-81b5025f7385" />

<img width="516" height="917" alt="image" src="https://github.com/user-attachments/assets/5a79e4de-fcb1-4055-8b47-337db20f97dd" />

<img width="516" height="912" alt="image" src="https://github.com/user-attachments/assets/d00e4a07-0cf7-465f-9f41-3a1be83300b3" />

<img width="515" height="926" alt="image" src="https://github.com/user-attachments/assets/08fc0a63-0108-49b5-8562-f07ef13ee01b" />

<img width="512" height="920" alt="image" src="https://github.com/user-attachments/assets/f7577e1d-0128-4690-85b1-964730d32359" />

### Dashboard Screen

<img width="507" height="921" alt="image" src="https://github.com/user-attachments/assets/a55c3935-9ea7-4144-b3d4-afafc124d8d3" />

### Categories Screen

<img width="507" height="921" alt="image" src="https://github.com/user-attachments/assets/f69831c2-9d37-4c03-b626-0334202c616e" />


### Add Expense Screen

<img width="517" height="917" alt="image" src="https://github.com/user-attachments/assets/c11acad2-34fc-4007-9e37-2c062b150d54" />


### Expense List Screen

<img width="515" height="922" alt="image" src="https://github.com/user-attachments/assets/e250bcba-8567-4ca4-9d98-c1a5fd92d351" />


### Budget Goals Screen

<img width="510" height="857" alt="image" src="https://github.com/user-attachments/assets/1d0305a3-76cf-4be1-81c1-c875f992326d" />


### Analytics Graph Screen

<img width="502" height="801" alt="image" src="https://github.com/user-attachments/assets/309e0cc6-1a18-47f6-bd8d-4cdbad033cb0" />


### Help & Support Screen

<img width="507" height="922" alt="image" src="https://github.com/user-attachments/assets/00b9ad6b-781a-46db-9e3e-b4297cc9e6ed" />


---

## 👥 Team Contributions

### Keona Mackan – ST10445050

* Registration and login functionality
* Supabase authentication
* Questionnaire flow
* Dashboard implementation
* Interactive graph feature

### Teah Andrew – ST10440926

* Budget goal functionality
* Minimum and maximum goal setup
* Total amount per category feature
* Help & Support screen

### Ethan Govender – ST10250993

* Expense list page
* Add expense logic
* Expense management support
* Progress tracking for minimum and maximum goals

### Kiara Israel – ST10277747

* Search and filter functionality
* Analytics date filtering
* Questionnaire personalisation feature
* Dashboard personalisation based on questionnaire answers
* Progress tracking for minimum and maximum goals

---

## 🔀 Git Workflow

The team used Git and GitHub for version control. Each member worked on separate feature branches. Features were tested before being merged into the main branch.

Branches included:

* Authentication and navigation
* Supabase authentication
* Expense management
* Categories
* Interactive graph
* Visual progress tracking
* Help screen

---

## ⚠️ Challenges Faced

Some challenges faced during development included:

* Migrating from RoomDB to Supabase
* Handling merge conflicts between branches
* Managing state in Jetpack Compose
* Implementing user-specific Supabase data
* Uploading receipt images to Supabase Storage
* Ensuring analytics filters update correctly
* Ensuring GitHub Actions builds successfully

---

## 💡 Future Improvements

Possible future improvements include:

* Push notifications for budget warnings
* More advanced financial reports
* Exporting reports as PDF
* Dark and light mode toggle
* Improved accessibility options
* More detailed spending predictions
* Multi-currency support

---

## 📚 Conclusion

FinTrack successfully demonstrates a modern mobile budgeting application using Kotlin, Jetpack Compose, Supabase, and MVVM architecture. The app allows users to manage expenses, create categories, set budget goals, upload receipts, view analytics, and receive personalised dashboard content based on questionnaire responses.

The project also demonstrates teamwork through GitHub, feature branching, cloud database integration, GitHub Actions, and structured testing.

---

## 📌 Authors

* Keona Mackan (ST10445050)
* Teah Andrew (ST10440926)
* Ethan Govender (ST10250993)
* Kiara Israel (ST10277747)
