<img src="docs/img/logo.webp" alt="KAppStats" width="300">

# KAppStats

KAppStats is an open-source project that makes it easy to collect metrics and statistics from websites, apps, and systems.

## Our Mission
Make a KMP (Kotlin Multiplatform) app — cross-platform and efficient — viable for data collection and statistics, contributing to an open and transparent ecosystem.

---

## Summary

- About
- Key features
- Architecture
- Structure
- Installation
- Basic usage
- Examples
- Contribution
- License
- Contact
---

# About

KAppStats provides lightweight, reusable components to collect, aggregate, and export performance metrics, events, and usage statistics. The project focuses on being cross-platform using Kotlin Multiplatform (KMP), enabling integration on Android, iOS, JVM, Linux, macOS, and other targets supported by Kotlin.

---

# Key Features

#### TODO : ADD SESSION

---

# Architecture

- Core : 
- Current platforms: Kotlin JVM/Android, Kotlin/Native, Kotlin/JS, Kotlin/iOS.

---
# Structure

#### TODO: UPDATE THAT SESSION

[/composeApp](/kappstats/kmp/-/tree/main/composeApp/src) is for code that will be shared across your Compose Multiplatform applications. 

[/iosApp](/kappstats/kmp/-/tree/main/iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

---
# Installation

#### TODO: ADD SESSION

---

# Basic Usage

#### TODO: ADD SESSION

---

## Usage examples

#### TODO: ADD SUB SESSION

---

## Best Practices

#### TODO ADD SUB SESSION

---

# Contribution

All contributions are welcome and encouraged. Feel free to:

1. Fork the repository
2. Create a branch for your feature/fix
3. Open a Pull Request with a clear description
4. Follow code and test standards (see CONTRIBUTING.md)

Issues, suggestions, and bug reports are encouraged.


## Clone project

---

### Clone project via SSH
<pre>
git clone git@gitlab.com:kappstats/kmp.git
</pre>
### or
### Clone project via HTTP
<pre>
git clone https://gitlab.com/kappstats/kmp.git
</pre>

## Checkout to dev branch (current dev branch)
<pre>
git checkout dev
</pre>

## How-to develop
### Server
#### Create a test class by extending BaseIntegrationTest. Use the baseTestApplication helper to perform requests:
```kotlin
class TestClass : BaseIntegrationTest() {
    
    @Test
    fun `Should return OK when posting valid info`() = baseTestApplication { client ->
        val bodyInfo = "{ \"key\": \"value\" }"

        val response = client.post("route") {
            contentType(ContentType.Application.Json)
            bearerAuth("token")
            
            setBody(bodyInfo)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
    
}
```
---

# License

#### GNU GENERAL PUBLIC LICENSE 2.0

---

# Contact

Open issues on the GitLab repository or submit PRs.
