# 📱 EPTW

_**Empowering seamless connections for smarter workflows.**_

![last commit](https://img.shields.io/github/last-commit/jaysinghpatel0/ePTW)
![language](https://img.shields.io/github/languages/top/jaysinghpatel0/ePTW)
![languages](https://img.shields.io/github/languages/count/jaysinghpatel0/ePTW)

---

### 🛠 Built with the tools and technologies:

![GitHub](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)
![SourceForge](https://img.shields.io/badge/SourceForge-orange?style=flat&logo=sourceforge&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)
![XML](https://img.shields.io/badge/XML-00599C?style=flat&logo=xml&logoColor=white)
![Google](https://img.shields.io/badge/Google-4285F4?style=flat&logo=google&logoColor=white)
![BAT](https://img.shields.io/badge/.bat-444444?style=flat&logo=windows&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [License](#license)

---

## 📖 Overview

The **EPTW Android app** is designed to enhance and digitize the permit-to-work (PTW) process at **JSW Grasim Chemicals** by enabling mobile-based actions integrated directly into their backend system — without using APIs.

---

## ✨ Features

- 🔗 WebView integration with JavaScript interface for embedded portal
- 📷 Capture image via camera or select from gallery
- 🌐 Fetch real-time location using Fused Location API
- ☁ Upload images to FTP (with short filename like `...123456.jpg`)
- 🗄️ Update image and device info to MSSQL Server directly using JDBC
- ⚙️ Background threading for FTP and DB operations
- 📍 Preview image and location instantly on capture

---

## ⚙️ Technologies Used

- Kotlin
- Android SDK (API 24–35)
- WebView + JavaScriptInterface
- jTDS JDBC Driver (for MSSQL)
- Apache Commons Net FTP
- FusedLocationProvider (Google Play Services)
- FileProvider
- StrictMode & Threading

---

## 🔧 Setup Instructions

1. **Clone the repo:**
   ```bash
   git clone https://github.com/jaysinghpatel0/ePTW
