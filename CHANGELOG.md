# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.4.0] - 2024-01-30

### Added
- 🎉 **New Simplified API** - `FloatMenu` class with chain-style method calls
  - Reduced from 12 lines to 3 lines for basic usage
  - Maintains backward compatibility with `FloatLogoMenu.Builder`
- 🎯 **Service Support** - New `FloatMenuService` example for background floating menu
  - Display floating menu outside the application
  - Persistent menu in background service
- 🔧 **Dynamic Configuration Methods**
  - `setLogoDrawNum(boolean)` - Dynamically control red dot display
  - `setLogoDrawBg(boolean)` - Dynamically control circular background display
- 📚 **Documentation**
  - New API_GUIDE.md with detailed API documentation
  - New CHANGELOG.md with version history
  - New CONTRIBUTING.md with contribution guidelines
  - Updated README.md with improved structure

### Fixed
- 🐛 Fixed right-side menu jumping to left side when expanding
- 🐛 Fixed logo background disappearing after menu expansion
- 🐛 Fixed all lint warnings:
  - ClickableViewAccessibility - Added performClick() support
  - RtlHardcoded - Added @SuppressWarnings for intentional LEFT/RIGHT usage
  - PrivateApi - Added @SuppressWarnings for status bar height reflection
- 🐛 Fixed AnimatorListener crash issues
- 🐛 Fixed refreshDot NullPointerException

### Changed
- ♿ **Accessibility Improvements** - Full accessibility support for custom views
- 🎨 **Unified Background Color** - Consistent gray (0xffe4e3e1) across Activity and Service
- 📐 **Code Refactoring** - Simplified positioning logic with absolute coordinates
- 🗑️ **Removed Rotation Animation** - Removed logo rotation during drag

### Technical Details
- Migrated from mixed gravity (LEFT/RIGHT) to unified LEFT gravity with absolute x-coordinates
- Improved state management for shrink/restore cycles
- Enhanced error handling in lifecycle methods

## [2.3.0] - Previous Release

### Added
- Auto-shrink delay configuration
- Support for custom background Drawable
- Red dot number display

### Changed
- Gradle 8.x compatibility
- Updated to compileSdk 36
- Migrated to AndroidX

## [2.2.0]

### Added
- Unity3D game engine support
- Custom floating window dialogs

## [2.1.0]

### Added
- Circular background drawing option
- Menu item color customization

## [2.0.0]

### Added
- Initial public release
- Basic floating menu functionality
- Left/Right positioning support

---

## Versioning Scheme

- **Major version (X.0.0)**: Breaking changes, major API updates
- **Minor version (0.X.0)**: New features, backward compatible
- **Patch version (0.0.X)**: Bug fixes, minor improvements

---

## Links

- [GitHub Releases](https://github.com/fanOfDemo/FloatMenuSample/releases)
- [API Documentation](API_GUIDE.md)
- [Contributing Guidelines](CONTRIBUTING.md)
