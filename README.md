# Fuel Tracker

A simple and intuitive Android application to track vehicle refueling and display comprehensive fuel-related statistics. Built with modern Android development practices using Kotlin and Jetpack Compose.

<div align="center">
  <img src="images/Fuel%20Tracker%201.png" width="200" alt="Home Screen"/>
  <img src="images/Fuel%20Tracker%203.png" width="200" alt="Vehicle Details"/>
  <img src="images/Fuel%20Tracker%202.png" width="200" alt="Add Fuel"/>
  <img src="images/Fuel%20Tracker%204.png" width="200" alt="Settings and Profile"/>
</div>

## Features

### Core Functionality
- **Vehicle Management**: Add, edit, and delete multiple vehicles with detailed information (manufacturer, model, year, fuel capacity)
- **Fuel Tracking**: Record refueling sessions with odometer readings, fuel amount, cost, and dates
- **Automatic Calculations**: Automatically calculates fuel economy, trip distance, and fuel percentage
- **Comprehensive Statistics**: View detailed analytics including:
    - Latest odometer reading
    - Average fuel economy
    - Total fuel added and money spent
    - Refuel frequency per month
    - Average fuel amount per refuel
    - Average cost per refuel

### User Experience
- **Modern UI**: Clean, intuitive interface built with Jetpack Compose and Material Design 3
- **Dark Theme Support**: Automatic theme switching based on system preferences
- **Smooth Animations**: Engaging transitions and animations throughout the app
- **Multi-locale Support**: Internationalization support for different locales

### Data & Sync
- **Local Storage**: Reliable offline data storage using Room database
- **Cloud Sync**: Firebase integration for data synchronization across devices
- **Authentication**: Secure user authentication with Firebase Auth
- **Data Backup**: Automatic cloud backup of your fuel tracking data

### Technical Features
- **Dependency Injection**: Clean architecture using Dagger Hilt
- **MVVM Pattern**: Well-structured codebase following MVVM architecture
- **Reactive Programming**: Real-time UI updates using Kotlin Coroutines and Flow
- **Data Validation**: Input validation and error handling
- **Edge-to-Edge Display**: Modern Android UI design with edge-to-edge content

## Installation & Usage

### Prerequisites
- Android device running Android 7.0 (API level 24) or higher
- Internet connection for cloud sync features (optional for offline use)

### Installation
1. Download the APK from the [Releases](https://github.com/Reishandy/Android-Fuel-Tracker/releases) page
2. Enable "Install from unknown sources" in your Android settings (if not already enabled)
3. Install the APK file
4. Launch the Fuel Tracker app

### Getting Started
1. **Add Your First Vehicle**
    - Tap the "+" button on the home screen
    - Enter vehicle details (name, manufacturer, model, year, fuel capacity)
    - Save the vehicle

2. **Record Your First Refuel**
    - Select your vehicle from the home screen
    - Tap the "Record Refuel" button
    - Enter refuel details:
        - Current odometer reading
        - Current trip reading
        - Amount of fuel added
        - Fuel type
        - Cost per liter
        - Date of refuel
    - Save the record

3. **View Statistics**
    - Access detailed statistics on the vehicle detail screen
    - Monitor fuel economy trends
    - Track total spent on fuel
    - Analyze refueling frequency

4. **Sync Data (Optional)**
    - Sign in with your Google account
    - Your data will automatically sync across devices
    - Access your data from any device with the app installed

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Database**: Room (SQLite)
- **Dependency Injection**: Dagger Hilt
- **Backend**: Firebase (Auth, Firestore)
- **Async Programming**: Kotlin Coroutines & Flow
- **Navigation**: Navigation Compose
- **Build System**: Gradle with Kotlin DSL

## Contributing

Contributions are welcome! If you have ideas for new features or improvements, please follow these steps:

1. Fork the repository
2. Create a new feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes and commit them (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please ensure your code follows the existing style.

## License

This project is licensed under the AGPL-3.0 License - see the [LICENSE](LICENSE) file for details.

## Contact

**Developer**: Reishandy  
**Email**: [akbar@reishandy.id](mailto:akbar@reishandy.id)
**GitHub**: [@Reishandy](https://github.com/Reishandy)

## Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/Reishandy/Android-Fuel-Tracker/issues) page for existing problems
2. Create a new issue with detailed information about your problem
3. Contact the developer directly at [akbar@reishandy.id](mailto:akbar@reishandy.id)