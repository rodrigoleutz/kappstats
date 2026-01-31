<img src="docs/img/logo.webp" alt="KAppStats" width="300">

---

[![Version](https://img.shields.io/badge/version-development-blue?style=flat-square)](VERSION)
[![License](https://img.shields.io/badge/license-GPL%202.0-brightgreen.svg?style=flat-square)](LICENSE)
![GitLab Release](https://img.shields.io/gitlab/v/release/kappstats%2Fkmp)
[![pipeline status](https://gitlab.com/kappstats/kmp/badges/main/pipeline.svg)](https://gitlab.com/kappstats/kmp/-/commits/main)


# 📈 KAppStats

KAppStats is an open-source project that makes it easy to collect metrics and statistics from websites, apps, and systems.

---

## 📝 Summary

* [About](#-about)
* [Our Mission](#-our-mission)
* [Structure](#-structure)
* [Contributing](#-contributing)
* [Clone project](#-clone-project)
* [Getting Started](#-getting-started)
* [Pro-Tip: Development Workflow](#-pro-tip-development-workflow)
* [License](#-license)
* [Contact](#-contact)
---

## 🔍 About

KAppStats provides lightweight, reusable components to collect, aggregate, and export performance metrics, events, and usage statistics. The project focuses on being cross-platform using Kotlin Multiplatform (KMP), enabling integration on Android, iOS, JVM, Linux, macOS, and other targets supported by Kotlin.

---

## 🎯 Our Mission
Make a KMP (Kotlin Multiplatform) app — cross-platform and efficient — viable for data collection and statistics, contributing to an open and transparent ecosystem.

---
## 🧩 Structure

[/androidApp](./androidApp/src) contains Android application code. 

[/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications. 

[/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

[/server](./server/src/main/kotlin) is for the Ktor server application.

[/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

---

## 🤝 Contributing

All contributions are welcome and encouraged. Feel free to:

1. Fork the repository
2. Create a branch for your feature/fix
3. Open a Pull Request with a clear description
4. Follow code and test standards (see CONTRIBUTING.md)

Issues, suggestions, and bug reports are encouraged.

---

## 📂 Clone project
---

### SSH Clone project
```bash
git clone git@gitlab.com:kappstats/kmp.git
```
### HTTP Clone project 
```bash
git clone https://gitlab.com/kappstats/kmp.git
```

---
## 🚀 Getting Started

### Prerequisites
* **JDK 17** or higher.
* **Android Studio** or **IntelliJ IDEA**.
* **Docker** and **docker compose plugin** for database.

### Running the Project

1.  **Initialize Environment Files**
 
    Copy the templates to active environment files. It is highly recommended to open and read these files to understand the system's ports, credentials, and behavioral flags:
    ```bash
    # Server configuration (DB, Ports, JWT)
    cp server/.env-sample server/.env
    # Shared configurations used across modules
    cp shared/.env-sample shared/.env
    # Development & Docker orchestration settings
    cp docs/dev/.env-sample docs/dev/.env
    ```
2.  **Launch the Development Stack**
       
    Once you have reviewed the .env files and ensured the settings match your local environment, run the orchestration script:
    ```bash
    ./docs/dev/runDev.sh
    ```

3.  **Start the Server:**
    ```bash
    ./gradlew :server:run
    ```
    The server starts by default on port `8080`.

4.  **Run the Desktop App:**
    ```bash
    ./gradlew :composeApp:run
    ```
    or development Hot Reload script:
    ```bash
    ./runDesktopHotReload.sh
    ```

5.  **Run the Android App:**
    ```bash
    ./gradlew :androidApp:installDebug
    ```

6.  **Run the Web App:**
    ```bash
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
    or
    ```bash
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
---

## 💡 Pro-Tip: Development Workflow

---
### Checkout to dev branch (current dev branch)
```bash
git checkout -b dev
```
---
### Install pre-commit test helper
Install pre-commit in `.git/hooks` to run tests before commit: 
```bash
./install_pre_commit.sh
```
or
```bash
chmod +x install_pre_commit.sh && ./install_pre_commit.sh
```
---
### Install pre-push build test helper
Install pre-push in `.git/hooks` to run build tests before commit:
```bash
./install_pre_push.sh
```
or
```bash
chmod +x install_pre_push.sh && ./install_pre_push.sh
```
---
### Server
Create a test class by extending BaseIntegrationTest. Use the baseTestApplication helper to perform requests:
```kotlin
class TestClass : BaseIntegrationTest() {
    
    @Test
    fun `Should return OK when posting valid info`() = baseTestApplication { callProvider, client ->
        val bodyInfo = "{ \"key\": \"value\" }"

        val response = client.post("route") {
            contentType(ContentType.Application.Json)
            bearerAuth("token")
            header("X-HeaderA", "Test Header")
            
            setBody(bodyInfo)
        }

        val call = callProvider()
        val headerA = call.request.header("X-HeaderA")
        assertEquals("Test Header", headerA)
        
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
```
---
## 📜 License
GNU GENERAL PUBLIC LICENSE 2.0

---

## 📬 Contact
Open issues on the GitLab repository or submit PRs.


