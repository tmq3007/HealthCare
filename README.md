# HealthCare - Android Application

A healthcare management Android application that allows users to find doctors, book appointments, and manage their medical bookings.

## 📱 Features

- **User Authentication**
  - User Registration
  - User Login
  
- **Doctor Management**
  - Find Doctors
  - View Doctor Details
  - Search and filter doctors by specialty
  
- **Appointment System**
  - Book Appointments with doctors
  - View My Bookings
  - Manage appointment history

- **User-Friendly Interface**
  - Material Design UI
  - Intuitive navigation
  - Responsive layouts

## 🛠️ Tech Stack

- **Language:** Java
- **Build System:** Gradle (Kotlin DSL)
- **Minimum SDK:** Android 7.0 (API 24)
- **Target SDK:** Android 15 (API 36)
- **Architecture Components:**
  - Activities
  - Adapters
  - DAO (Data Access Objects)
  - Model classes

### Dependencies

- AndroidX AppCompat
- Material Design Components
- ConstraintLayout
- AndroidX Activity
- JUnit (Testing)
- Espresso (UI Testing)

## 📋 Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK API 36
- Gradle 8.x

## 🚀 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd HealthCare
   ```

2. **Open the project in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the HealthCare directory

3. **Sync Gradle files**
   - Android Studio will automatically prompt you to sync
   - Or click "File" → "Sync Project with Gradle Files"

4. **Run the application**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

### Build from Command Line

```bash
# Windows
gradlew.bat assembleDebug

# The APK will be generated at:
# app/build/outputs/apk/debug/app-debug.apk
```

## 📂 Project Structure

```
HealthCare/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/healthcare/
│   │   │   │   ├── Activities/
│   │   │   │   │   ├── ClientActivities/
│   │   │   │   │   │   ├── HomeActivity
│   │   │   │   │   │   ├── LoginActivity
│   │   │   │   │   │   ├── RegisterActivity
│   │   │   │   │   │   ├── FindDoctorActivity
│   │   │   │   │   │   ├── DoctorDetailsActivity
│   │   │   │   │   │   ├── BookAppointmentActivity
│   │   │   │   │   │   └── MyBookingActivity
│   │   │   │   │   └── MainActivity
│   │   │   │   ├── Adapters/
│   │   │   │   ├── DAO/
│   │   │   │   └── Model/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🎯 Usage

1. **First Time Users:**
   - Launch the app
   - Register a new account
   - Complete your profile

2. **Finding a Doctor:**
   - Navigate to "Find Doctor"
   - Browse or search for doctors
   - View doctor details and specialties

3. **Booking an Appointment:**
   - Select a doctor
   - Choose an available time slot
   - Confirm your booking

4. **Managing Bookings:**
   - Access "My Bookings" from the home screen
   - View upcoming and past appointments

## 🧪 Testing

Run unit tests:
```bash
gradlew.bat test
```

Run instrumented tests:
```bash
gradlew.bat connectedAndroidTest
```

## 📝 Version History

- **v1.0** - Initial Release
  - User registration and login
  - Doctor search and viewing
  - Appointment booking system
  - Booking management

## 👨‍💻 Developer

**Student:** PRM392 Course Project  
**Institution:** FPT University  
**Year:** 2025

## 📄 License

This project is developed for educational purposes as part of the PRM392 course at FPT University.

## 🤝 Contributing

This is a student project. If you're a collaborator:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For support or questions, please contact through the course platform or raise an issue in the repository.

---

**Note:** This application is a student project for PRM392 course at FPT University and is intended for educational purposes only.

