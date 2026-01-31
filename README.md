# Slipstream Android Plugin

Client-only SIP003 plugin built from slipstream-rust.

## NekoBox Compatibility

This plugin is now compatible with **NekoBox for Android** as a native plugin.

### Installation

1. Build the plugin APK:
   ```bash
   ./gradlew assembleRelease
   ```

2. Install the plugin APK on your Android device

3. In NekoBox:
   - The plugin will be automatically detected as `slipstream-plugin`
   - You can use it with various protocols (not just Shadowsocks)
   - Configure the plugin settings through the NekoBox UI

### Plugin Details

- **Plugin ID**: `slipstream-plugin`
- **Package Name**: `moe.matsuri.exe.slipstream`
- **Authority**: `moe.matsuri.exe.slipstream`

### Configuration Options

The plugin supports the same configuration options as before:
- Domain
- Resolver (Cloudflare, Google, etc. or custom IP)
- Authoritative mode
- Certificate
- Keep-alive interval

## Setup

- Initialize submodules:

```bash
git submodule update --init --recursive
```

- Install Rust targets:

```bash
rustup target add armv7-linux-androideabi aarch64-linux-android i686-linux-android x86_64-linux-android
```

- Ensure the Android NDK, CMake, Python, and the usual build tools (make/perl for vendored OpenSSL) are available.

## Build

```bash
./gradlew assembleDebug
```

## Notes

- The plugin uses `app/src/main/rust/slipstream-rust` as a submodule.
- picoquic auto-build uses `app/src/main/rust/slipstream-rust/scripts/build_picoquic.sh`.
- OpenSSL is built from source via the Rust `openssl` crate (vendored) for Android.
- Resolver selection relies on `SS_REMOTE_*`; the UI only sets domain, authoritative mode, cert, and keep-alive.
