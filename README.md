# Calculator (Android)

A simple calculator app built with **Kotlin** for Android. Supports basic arithmetic operations with smart input handling — so pressing a number after an operation starts fresh instead of appending to old digits.

## 📱 Features

- Basic arithmetic: addition, subtraction, multiplication, division
- Smart digit input handling via `isnewinput` flag:
  - Continues building the current number as digits are pressed
  - Automatically starts a new number after an operator or result is calculated
- Clear/Reset functionality
- Clean numeric keypad UI

## 🖼️ Screenshots

> *(Add screenshots of your app here once built — e.g. `screenshots/calculator.png`)*

## 🛠️ Built With

- **Kotlin** — primary language
- **Android SDK** — native Android app
- **XML** — UI layout (numeric keypad + display)

## 📂 Project Structure

```
calculator/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/calculator/
│           │   └── MainActivity.kt      # Calculator logic (input, operations, display)
│           └── res/
│               └── layout/
│                   └── activity_main.xml # Calculator UI (display + button grid)
│
├── README.md
└── build.gradle
```

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version)
- Android SDK (API level 21+)
- A device or emulator running Android 5.0 (Lollipop) or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/calculator.git
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync and download dependencies.
4. Run the app on an emulator or physical device (`Shift + F10` or click ▶️ Run).

## 🧠 How the Input Logic Works

The core input handling revolves around one key flag: `isnewinput`.

```kotlin
fun onNumberClick(value: String) {
    if (isnewinput) {
        currentnumber = value
        isnewinput = false
    } else {
        currentnumber += value
    }
}
```

- **`isnewinput = true`** → the next digit pressed **replaces** the current display (used right after an operator or `=` is pressed)
- **`isnewinput = false`** → the next digit pressed **appends** to the current number (normal typing)

This prevents digits from incorrectly sticking to leftover numbers after an operation.

## 🎮 How to Use

1. Tap number buttons to build a number.
2. Tap an operator (`+`, `-`, `×`, `÷`) to select an operation.
3. Enter the second number.
4. Tap `=` to see the result.
5. Tap `C` / `Clear` to reset the calculator.

## 🔮 Planned Improvements

- [ ] Add decimal point support
- [ ] Add percentage (%) function
- [ ] Add calculation history
- [ ] Handle divide-by-zero errors gracefully
- [ ] Add unit tests for calculation logic
- [ ] Dark mode support

## 🤝 Contributing

Contributions are welcome! Feel free to fork this repo, make changes, and submit a pull request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Your Name**
- GitHub: [@your-username](https://github.com/your-username)
