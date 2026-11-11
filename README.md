# End Crystal Auto Look Mod

A Fabric mod for Minecraft 26.3 that automatically looks up when placing an End Crystal on Obsidian, triggers the explosion, and restores the original view angle very quickly.

## Features

- When you place an End Crystal on Obsidian, the camera automatically looks straight up (-90° pitch)
- The End Crystal explodes
- The camera immediately returns to your original view angle
- All happens in the same tick for maximum speed

## Building the Mod

### Prerequisites
- Java 25 (as specified in gradle.properties)
- Git

### Build Commands

**Windows:**
```cmd
gradlew.bat build
```

**Linux/Mac:**
```bash
./gradlew build
```

The built JAR file will be in `build/libs/` with a name like `endcrystal-autolook-1.0.0.jar`.

## Using with GitHub

To create a GitHub repository and publish releases:

### 1. Initialize Git Repository
```bash
git init
git add .
git commit -m "Initial commit: End Crystal Auto Look mod"
```

### 2. Create GitHub Repository
1. Go to https://github.com/new
2. Repository name: `endcrystal-autolook` (or your preferred name)
3. Set to Public or Private
4. **Don't** initialize with README, .gitignore, or license (we already have them)
5. Click "Create repository"

### 3. Push to GitHub
```bash
git remote add origin https://github.com/YOUR_USERNAME/endcrystal-autolook.git
git branch -M main
git push -u origin main
```

### 4. Create a Release (Generates .jar automatically via GitHub Actions)
The template includes a GitHub Actions workflow (`.github/workflows/build.yml`) that automatically builds the mod on every push and tag.

To create a release:
1. Go to your repo on GitHub
2. Click "Releases" on the right sidebar
3. Click "Create a new release"
4. Tag version: `v1.0.0` (matching your gradle.properties version)
5. Release title: `End Crystal Auto Look v1.0.0`
6. Describe the release
7. Click "Publish release"

The GitHub Action will automatically build and attach the JAR file to the release.

Alternatively, trigger a build manually:
- Go to Actions tab → "Build" workflow → "Run workflow"

## Installation

1. Install Fabric Loader for Minecraft 26.3
2. Install Fabric API
3. Place the built JAR in your `mods` folder
4. Launch Minecraft with the Fabric profile

## How It Works

The mod uses a client-side Mixin that injects into `EndCrystalItem.useOnBlock()`. When an End Crystal is placed on Obsidian:
1. Saves the current player pitch and yaw
2. Sets player pitch to -90 (looking straight up)
3. Schedules the original rotation to be restored on the next client tick
4. This all happens within the same game tick for instant response

## Configuration

Currently no configuration options. The behavior is automatic when placing End Crystals on Obsidian.

## License

CC0-1.0 - Feel free to use, modify, and distribute.