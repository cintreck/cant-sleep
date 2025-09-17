# Can't Sleep (Fabric)

Can't Sleep adds unlucky nights where every attempt to use a bed is rejected with an insomnia warning. By default 15% of nights keep players awake, forcing them to wait until morning.

![An unlucky night..](https://cdn.modrinth.com/data/cached_images/23ea07e89ec77b7abd21b80aff4cfa52f1636d66.jpeg)

## Features
- Deterministic insomnia nights based on world seed and day so everyone shares the same sleepless experience.
- Configurable insomnia probability via `config/cant_sleep.toml` or the optional Mod Menu + Cloth Config screen.
- Runs anywhere Fabric does; keep it on the server for enforcement and optionally on clients for UI tweaks only.

## Install
- Requires [Fabric](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api).
- Optional UI: add [Mod Menu](https://modrinth.com/mod/modmenu) and [Cloth Config](https://modrinth.com/mod/cloth-config) to expose the configuration in-game.
- Place the built jar in `mods/` on the server (and clients if you want the config screen).

## Build
- Java 21 with Gradle + Fabric Loom.
- Versions live in `gradle.properties`. Run `./gradlew build`.

## License
- CC0-1.0. Use it freely.
