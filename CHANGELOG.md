# Changelog

All notable changes to the FloatMenu project will be documented in this file.

## [Unreleased]

### Added
- **Smart Auto-Shrink Feature**: Added intelligent auto-shrink functionality with configurable delay
  - Floating ball automatically shrinks to screen edge after 3 seconds of inactivity (configurable via `autoShrinkDelay()`)
  - Shrink state: 50% visible, 50% off-screen
  - Smooth animations (300ms shrink, 200ms restore)
  - Configurable shrink delay via `Builder.autoShrinkDelay(int milliseconds)` - default is 3000ms (3 seconds)
- **Drag Boundary Constraints**: Added strict boundary enforcement during dragging
  - X-axis: `[0, screenWidth - logoWidth]`
  - Y-axis: `[statusBarHeight, screenHeight - logoHeight]`
  - Floating ball cannot be dragged outside screen boundaries
- **Improved Drag Interaction**: Enhanced dragging experience
  - Finger center aligns with logo center
  - Only the center icon rotates during drag (background circle and red dot remain stationary)
  - Removed scaling animation during drag
- **Smart Click Interactions**: Implemented state-aware click handling
  - Shrunk state → Click → Restore to 100% visible (does not open menu)
  - Normal state → Click → Open menu
  - Expanded state → Click logo → Close menu
- **Enhanced Edge Detection**: Improved left/right edge detection logic
  - Based on logo center position rather than touch point
  - More accurate edge snapping behavior

### Changed
- Removed unused variables and methods (~200 lines of dead code eliminated)
- Optimized code structure and improved maintainability
- Updated documentation with comprehensive feature descriptions and interaction details

### Fixed
- Fixed initial logo position to respect `mDefaultLocation` setting
- Fixed timing bug where click on shrunk logo would open menu instead of restoring
- Fixed logo not being able to extend beyond screen boundaries (added FLAG_LAYOUT_NO_LIMITS)

### Improved
- Code quality improvements: removed duplicate variables, fixed compilation errors
- Better state management for drag, shrink, and expanded states
- Enhanced logging for debugging and troubleshooting

---

## [2.2.0] - 2017

### Added
- Migrated to AndroidX from Android Support Library
- Updated to Gradle 8.5 and AGP 8.1.4
- Added JDK 17 support
- Updated compileSdk to 36
- Added namespace declarations for AGP 8.x compatibility
- Fixed duplicate `openMenu()` method in BaseFloatDialog
- Added `android:exported` attribute for Android 12+ compatibility
- Added `@Deprecated` annotation to legacy API
- Fixed lint warnings and improved code quality

### Changed
- Removed deprecated JFrog Bintray publishing configuration
- Removed dependency on `com.yw.game.sclib:shortCutLib`
- Improved error handling and logging

### Fixed
- Fixed issue where `android.support.annotation.Nullable` was used instead of `androidx.annotation.Nullable`
- Fixed Gradle compatibility issues with older AGP versions

---

## [2.2.0] - 2017

### Added
- Initial public release
- Support for floating menu inside app
- Support for desktop floating menu (with SYSTEM_ALERT_WINDOW permission)
- Support for Unity3D game engine

---

## [2.0.0] - 2016

### Added
- First release
- Basic floating menu functionality
