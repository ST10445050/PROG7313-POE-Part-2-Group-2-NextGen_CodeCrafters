# 📱 PROG7313 – POE Part 2

## FinTrack Budgeting App (NextGen_CodeCrafters)

---

## 👩‍💻 Project Overview

FinTrack is a mobile budgeting application developed as part of the **PROG7313 – Programming 3C** Portfolio of Evidence (Part 2).
The application enables users to track their daily expenses, manage categories, and monitor spending habits efficiently.

🔗 **GitHub Repository:**
https://github.com/ST10445050/PROG7313-POE-Part-2-Group-2-NextGen_CodeCrafters

---

## 🎯 Purpose of the Application

The purpose of FinTrack is to:

* Help users manage personal finances
* Track expenses with detailed entries
* Categorize spending for better insights
* Provide a structured and user-friendly budgeting system

---

## 🧩 Key Features

### 🔐 User Authentication

* User registration and login functionality
* Secure access to user-specific data

---

### 📝 Questionnaire (Innovative Feature)

* Users complete a questionnaire after registration
* Enhances personalization of the app

---

### 💸 Expense Management

Users can:

* Add new expense entries
* Capture:

  * Date
  * Start Time
  * End Time
  * Description
  * Category
  * Amount
  * Optional photo

---

### 🗂️ Category Management

* Create and manage categories such as:

  * Food
  * Groceries
  * Clothing
  * Transport
  * General
* Icon-based UI for better user experience

---

### 📊 Budgeting & Insights

* View total amount spent per category
* Set minimum and maximum budget goals
* Track spending behaviour

---

### 🔍 Search & Filter

* Filter expenses based on user-selected time periods
* Improves usability and reporting

---

### 🧭 Navigation

* Bottom navigation bar for:

  * Dashboard
  * Categories
  * Expenses

---

## 🏗️ System Architecture

The application follows **MVVM Architecture**:

* **UI Layer** – Jetpack Compose
* **ViewModel Layer** – Handles logic and state
* **Data Layer** – RoomDB (Database, DAO, Entities)

---

## 🗄️ Database Structure

### 📌 Database Name:

`AppDatabase`

### 📌 Entities:

#### 👤 User

* id
* username
* password

#### 🗂️ Category

* id
* name
* icon

#### 💸 Expense

* id
* userId
* categoryId
* date
* startTime
* endTime
* description
* amount
* imageUri (optional)

---

## ⚙️ Technologies Used

* Kotlin
* Android Studio
* Jetpack Compose
* Room Database (RoomDB)
* Kotlin Coroutines
* MVVM Architecture

---

## 🚀 How to Run the Project

### ✅ Prerequisites

* Android Studio
* Android SDK (API 26+)
* Emulator or physical device

---

### ▶️ Steps

1. Clone the repository:

```bash id="1v91s3"
git clone https://github.com/ST10445050/PROG7313-POE-Part-2-Group-2-NextGen_CodeCrafters.git
```

2. Open the project in Android Studio

3. Allow Gradle to sync

4. Run the application on an emulator or device

---

## 🧪 Testing the Application

* Register a user
* Login
* Complete questionnaire
* Add categories
* Add expenses
* View dashboard
* Apply filters

---

## 👥 Team Contributions

### 👩‍💻 Keona Mackan (ST10445050)

* Registration & Login functionality
* Questionnaire (Innovative Feature)
* Dashboard implementation

---

### 👩‍💻 Teah Andrew (ST10440926)

* Total amount per category feature
* Min & Max budget goal functionality

---

### 👨‍💻 Ethan Govender (ST10250993)

* Expense List Page
* Add Expense logic

---

### 👩‍💻 Kiara Israel (ST10277747)

* Search functionality
* Filter logic based on time periods

---

## 🔀 Git Workflow

* Each member worked on separate branches
* Features developed independently
* Merged into `main` after testing
* Commit history reflects contributions

---

## ⚠️ Challenges Faced

* RoomDB integration and relationships
* Compose state management
* Merge conflicts between branches
* Debugging runtime errors

---

## 💡 Future Improvements

* Firebase / cloud database
* Multi-device sync
* Notifications for budgets
* Advanced analytics dashboard

---

## 📚 Conclusion

FinTrack demonstrates:

* Modern Android development using Jetpack Compose
* Local data persistence using RoomDB
* Clean architecture (MVVM)
* Effective team collaboration using Git

The application successfully meets the requirements of **PROG7313 POE Part 2**.

---

## 📌 Authors

* Keona Mackan (ST10445050)
* Teah Andrew (ST10440926)
* Ethan Govender (ST10250993)
* Kiara Israel (ST10277747)

---
