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

Useful alternatives:

- `createReobfPaperclipJar`
- `createMojmapBundlerJar`
- `createReobfBundlerJar`

## Development Run Tasks

- `runPaperclip`
- `runReobfPaperclip`
- `runServer`
- `runDevServer`

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
