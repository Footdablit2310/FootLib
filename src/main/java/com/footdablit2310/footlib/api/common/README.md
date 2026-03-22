# 📦 FootLib — Common Utilities
Core shared utilities for all FootDablit mods
The common package is the foundation of FootLib. It contains the essential utilities, helpers, and shared logic that every FootDablit mod relies on. These classes are intentionally lightweight, stable, and free of any gameplay‑specific logic. They exist to make development easier, safer, and more consistent across the entire ecosystem.
This is the code that every mod will import, so clarity and reliability are top priorities.

## 🎯 Purpose
The common package provides:
- A unified way to detect optional mods
- A consistent logging interface
- Safe JSON and NBT helpers
- Version lookup utilities
- Small math helpers used across systems
These utilities prevent duplication, reduce boilerplate, and ensure that all FootDablit mods behave consistently.

## 📁 Included Modules
### 🔍 ModPresence
Utilities for safely checking whether optional mods are installed.
Use this when integrating with optional modules like Foot Economy or Create Train Fares.
### 🔢 VersionUtil
Retrieves version information for any loaded mod.
Useful for compatibility checks, debugging, and analytics.
### 📝 Logging
Provides a unified logger for all FootDablit mods.
Ensures consistent formatting and makes debugging easier.
### 📄 JsonUtil
Helpers for reading and writing JSON safely, with null‑safety and fallback support.
Avoids repetitive boilerplate when working with config or data files.
### 📦 NbtUtil
Convenience methods for interacting with NBT tags.
Prevents crashes from missing keys and keeps serialization code clean.
### ➗ MathUtil
Small math helpers (clamp, lerp, etc.) used across multiple systems.

## 🧠 Design Principles
### 1. Stability First
These utilities must remain stable across versions.
Other mods depend on them, so breaking changes are avoided unless absolutely necessary.
### 2. Zero External Dependencies
The common package does not depend on:
- Create Train Fares
- CRN
- Any gameplay system
This ensures FootLib remains usable even when other modules are delayed or unavailable.
### 3. Minimalism
Only essential utilities are included.
This keeps the package lightweight and easy to understand.
### 4. Consistency Across Mods
All FootDablit mods should use the same:
- logging style
- JSON/NBT patterns
- version checks
- math helpers
This makes the ecosystem feel unified and predictable.

## 📌 When to Use the Common Package
Use these utilities whenever you need:
- to check if another mod is installed
- to log information or errors
- to serialize or deserialize data
- to read/write JSON or NBT safely
- to perform small math operations
If you find yourself writing boilerplate code, the common package probably already solves it.

## 🚫 What This Package Does Not Include
The common package intentionally avoids:
- gameplay logic
- economy systems
- ticketing systems
- transit systems
- CRN integration
- Create Train Fares logic
Those belong in other modules.
common stays clean, stable, and universal.

## 🧩 Future Expansion
The common package may grow over time, but only with utilities that:
- are broadly useful
- are safe to depend on
- do not introduce gameplay coupling
- improve consistency across mods
Every addition must serve the entire ecosystem.
