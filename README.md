<div align="center">

  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="Monja Logo" width="150"/>

# Monja: Real-time Mountaineer Monitoring System

**Empowering rescue teams with real-time IoT vital tracking for early hypothermia detection.**

  <br />

  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?style=flat-square&logo=kotlin" alt="Kotlin">
  </a>
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=flat-square&logo=android" alt="Jetpack Compose">
  </a>
  <a href="https://supabase.com/">
    <img src="https://img.shields.io/badge/Supabase-Realtime-3ECF8E?style=flat-square&logo=supabase" alt="Supabase">
  </a>
  <a href="https://docs.mapbox.com/android/maps/guides/">
    <img src="https://img.shields.io/badge/Mapbox-Maps_SDK-000000?style=flat-square&logo=mapbox" alt="Mapbox">
  </a>
  <a href="https://github.com/Masdikaa/Monja/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square" alt="License">
  </a>

</div>

<br/>

## 🏔️ About The Project

**Monja** is a mission-critical Android application designed specifically for mountain ranger stations and emergency evacuation teams. It serves as the central command dashboard for monitoring climbers equipped with specialized IoT-enabled jackets.

In high-altitude environments, rapid response is the difference between life and death. Monja eliminates the guesswork by providing a continuous, real-time stream of the climber's precise GPS location and biological telemetry.

### 🚨 Early Hypothermia Detection
The core objective of Monja is the early detection of hypothermia and other severe altitude-related conditions. By actively analyzing incoming data from the IoT jacket's sensors—specifically **Body Temperature, Heart Rate, and Oxygen Saturation (SpO2)**—the system can instantly identify critical drops in vitals.

When a severe condition is detected, Monja automatically triggers high-priority evacuation warnings to the rescue team's dashboard, ensuring immediate action can be taken before the situation becomes fatal.

---

> **Hardware Simulation Note:** > The real-time telemetry data (Vitals, GPS, and Alerts) visualized in this Android application is currently simulated using an **ESP32 microcontroller** to replicate a live IoT jacket environment.
>
> You can find the ESP32 C++ firmware and data streaming logic in the companion repository here:
> 🔗 **[Monja-ESP-Simulation](https://github.com/Masdikaa/Monja-ESP-Simulation)**

## ✨ Key Features

Monja is packed with features designed for high-stress, real-time monitoring environments.

* 🔴 **Automated Evacuation Alerts (Hypothermia Detection)**
    * **What it does:** Continuously evaluates incoming telemetry. If a climber's vitals drop to a critical threshold indicating hypothermia or other severe conditions, the system immediately triggers a high-priority UI alert.
    * **Tech Highlight:** Built using Kotlin `StateFlow` and Unidirectional Data Flow (UDF) to guarantee that critical state changes are emitted instantly and reliably without UI blocking.

* 📈 **Time-Based Custom Canvas Charts**
    * **What it does:** Visualizes historical and live biological data (Temperature, Heart Rate, SpO2) using a smooth, scrollable line chart.
    * **Tech Highlight:** **Built 100% from scratch** using Jetpack Compose `Canvas` (No 3rd-party chart libraries!). It features a custom time-based viewport calculation, dynamic gap detection (automatically breaking the line if data is lost), and mathematical Bezier curve rendering (`cubicTo`).

* ⚡ **Fast Data Streaming**
    * **What it does:** Ranger stations see climber data update on their dashboard the IoT jacket transmits it.
    * **Tech Highlight:** Utilizes **Supabase Realtime (Postgres Changes)** mapped perfectly into Kotlin Coroutines (`postgresChangeFlow`), ensuring a persistent, low-overhead WebSocket connection.

* 🗺️ **Live GPS Tracking & Tactical Mapping**
    * **What it does:** Displays the exact coordinates of climbers on a highly interactive map, helping evacuation teams plan their rescue routes.
    * **Tech Highlight:** Integrated with the **Mapbox Maps SDK for Compose**. Features custom device annotations, real-time pulsing location indicators, and dynamic map style switching (Satellite/Dark Mode).

* 🛡️ **Robust State Management & Error Handling**
    * **What it does:** Gracefully handles network losses or sensor disconnects in harsh mountain terrains without crashing the app.
    * **Tech Highlight:** Implements the **MVI (Model-View-Intent)** architecture pattern. Errors and network drops are caught at the Repository layer and cleanly propagated to the UI as snackbars and dynamic Empty States.

## 🏗️ Architecture & Tech Stack

Monja is built with modern industry standards, emphasizing **Clean Architecture** and **Unidirectional Data Flow (UDF)** to achieve a complete separation of business logic from UI components.

### 🏛️ Architecture Overview

This project adopts a **Multi-module Architecture** divided into two primary layers:

* **`:app` (UI/Presentation Layer):** Contains all Jetpack Compose components, ViewModels, and navigation logic. This focus on data presentation and user interaction.
* **`:data` (Data Layer):** Responsible for data persistence and network communication. It manages Supabase integration, maps Data Entities to Domain Models, and exposes reactive data streams to the UI layer via Repositories.

#### Key Architectural Principles:
* **MVI (Model-View-Intent):** Ensures predictable and consistent application states. State is managed using `StateFlow`, while single-shot events (like Snackbars and Alerts) are dispatched via `Channel`.
* **Separation of Concerns:** Strict isolation between Database Entities (Supabase representations) and Domain Models (Clean POJOs for the UI) prevents backend structural constraints from leaking into the presentation layer.
* **Reactive Repositories:** Heavily utilizes Kotlin Flows to handle real-time data streams from IoT sensors without blocking the main thread.

---

### 🛠️ Technical Stack

| Category | Technology |
| --- | --- |
| **Language** | **Kotlin** (v2.2.20) with Coroutines & Flows |
| **UI Framework** | **Jetpack Compose** (100%) with Material 3 |
| **Navigation** | **Navigation Compose** (Type-safe with Serialization) |
| **Dependency Injection** | **Dagger Hilt** + **KSP** for compile-time safety |
| **Backend / Realtime** | **Supabase-kt** (Postgrest & Realtime Engine) |
| **Networking** | **Ktor Client** with OkHttp Engine |
| **Maps** | **Mapbox Maps SDK** (Custom Annotations & Dynamic Viewports) |
| **Graphics** | **Custom Canvas API** (Bezier Curves, Time-based Viewports) |
| **Testing** | **JUnit 5 (Jupiter)**, **MockK**, **Turbine**, Ktor MockEngine |
| **Logging** | **Timber** |

---

### 🧪 Quality Assurance & Testing

Application stability is guaranteed through a comprehensive test suite covering business logic in the Data layer and state management in the UI layer:

* **Unit Testing:** Implemented using JUnit 5 for robust testing of reactive and asynchronous logic.
* **Flow Testing:** Utilizes **Turbine** to validate data emissions on real-time asynchronous streams perfectly.
* **Mocking:** Leverages **MockK** for dependency isolation and to simulate precise Repository behaviors within ViewModels.
* **Network Simulation:** Employs **Ktor MockEngine** to mock various Supabase server responses locally, enabling fast and deterministic tests without relying on actual internet connections. 