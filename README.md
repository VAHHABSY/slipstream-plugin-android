# Slipstream Android Plugin

Client-only SIP003 plugin built from slipstream-rust.

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
