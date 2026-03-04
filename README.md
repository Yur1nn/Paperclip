# Paperclip

Paperclip is a fork of Paper focused on a clean, maintainable customization layer while staying easy to update from upstream.

## Requirements

- JDK 21
- Internet connection (for dependencies and upstream artifacts)

## Build From Source

From repository root:

```bash
./gradlew applyPatches
./gradlew createMojmapPaperclipJar
```

Windows PowerShell:

```powershell
.\gradlew.bat applyPatches
.\gradlew.bat createMojmapPaperclipJar
```

Output jars are in:

- `paper-server/build/libs/`

## Production Release Build

Use this for release-ready metadata and artifact naming:

```powershell
.\gradlew.bat applyPatches
$env:BUILD_NUMBER="1"
$env:BUILD_STARTED_AT=(Get-Date).ToUniversalTime().ToString("o")
.\gradlew.bat :paper-server:createReleasePaperclipJar
```

Release artifact:

- `paper-server/build/libs/paperclip-<version>-mojmap.jar`

Debug/advanced alternatives:

- `createMojmapBundlerJar`

`reobf` jar tasks are debug-only in paperweight 2.x and are not the supported release path.
Use the `mojmap` variant for release builds.

## Development Run Tasks

- `runPaperclip`
- `runServer`
- `runDevServer`

`runReobfPaperclip` exists for debugging workflows only.

Example:

```powershell
.\gradlew.bat runPaperclip
```

## Default Runtime Layout

Paperclip uses organized runtime paths:

- `config/` for YAML configuration files
- `data/` for JSON/state files
- `worlds/` as world container root

Current default config files:

- `config/server.yml`
- `config/server-commands.yml`
- `config/server-help.yml`
- `config/server-permissions.yml`
- `config/global-spigot.yml`
- `config/global-paper.yml`

Current default data files:

- `data/usercache.json`
- `data/version_history.json`
- `data/ops.json`
- `data/whitelist.json`
- `data/banned-players.json`
- `data/banned-ips.json`

## Plugin API

This fork keeps Paper API compatibility targets unless explicitly changed in code.

For API sources, see:

- `paper-api/`

## Contributing

See:

- `CONTRIBUTING.md`
