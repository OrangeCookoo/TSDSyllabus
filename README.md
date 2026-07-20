# TSD Syllabus — Tang Soo Do One-Step Trainer

A colorful Android app for learning the **Bristol Tang Soo Do Academy (BTSDA)**
one-step syllabus. It comes pre-loaded with short notes for every technique
across **Hands**, **Feet**, **Self Defense** and **Bo Staff**, from White belt
up to Blue belt, and links straight through to the matching BTSDA demonstration
video on YouTube.

## Features

- **4 categories** — Hands, Feet, Self Defense, Bo Staff — as swipeable tabs.
- **Belt sections** with rank colours (White → Orange → Green → Brown → Red →
  Red Tag → Blue) and the syllabus sub-notes (e.g. *"Opposite stance – front
  kick attack"*).
- **Pre-populated notes** for all 120 techniques — the author's own short-hand.
- **Fully editable** — tap *Edit note* on any technique to overwrite the note.
  Your version is saved on-device and marked *your note*; *Reset* restores the
  original.
- **Watch video** — jumps to the correct BTSDA video for that block of five
  (see below).
- **Search** across notes, belt and technique number.
- Dynamic Material 3 theming (uses your device's wallpaper colours on Android
  12+), gradient headers and animated cards. Light & dark mode.

## How the video links work

BTSDA publishes its one-steps in **blocks of five** (1‑5, 6‑10, 11‑15, …):

- Hands, Feet and Self Defense share the **"Il Soo Sik Dae Ryun (1 Step
  Sparring) *X‑Y*"** series.
- Bo Staff has its own **"Bo Staff 1 Steps *X‑Y*"** series.

Each *Watch video* button opens an in-channel YouTube search on
[@bristoltangsoodoacademy](https://youtube.com/@bristoltangsoodoacademy) for the
exact block title, so it lands on the right demonstration video and keeps
working even as the channel adds or re-uploads videos.

## Download / install

The CI workflow builds the APK on every push and attaches it to the
**`app-latest`** GitHub Release. Grab **`TSD-Syllabus.apk`** from the
[Releases page](../../releases), copy it to your Android phone and open it
(you may need to allow *install from unknown sources*).

## Build locally

```bash
./gradlew :app:assembleDebug     # -> app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:testDebugUnitTest # unit tests
```

Requires JDK 17 and the Android SDK (compileSdk 35).

## Project layout

```
app/src/main/java/uk/co/btsda/syllabus/
  data/     Models, the full syllabus dataset, and the notes repository
  ui/       Compose screens, the view-model and the theme
  MainActivity.kt
app/src/test/          JVM unit tests for the syllabus data
app/src/androidTest/   Compose UI tests
.github/workflows/     CI: build, test, publish APK
```

Notes are the author's personal short-hand and are intended as memory aids
alongside proper instruction at the dojang.
