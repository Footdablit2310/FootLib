# FootLib

FootLib is a lightweight API and Util Mod/Library used by Foot mods.
Footlib is also a lightweight Util library used by Foot mods.
It provides shared systems such as:

- Foot Economy API
- Currency definitions
- Exchange rate systems
- Auto‑rate engine hooks
- Service injection for mod interoperability
### Versioning
Overhaul.Major.Semi-major.Patch/Hotfix/Minor-alpha(if alpha)/beta(if beta)/blank(if release)
example 0.0.1.0
1 Semi-major version
NOTE: Overhaul ends support for all previos mods if not explicitly written otherwise
## Usage
### Maven Repo
```gradle
repositories {
  https://api.modrinth.com/maven
}
```
### Dependency gradle(groovy)
```gradle
dependencies {
  implementation("maven.modrinth:footlib:${footlib_version}")
}
```
### neoforge.mods.toml
Add FootLib as a dependency in your mod's `neoforge.mods.toml`:

```toml
[[dependencies.yourmod]]
modId="footlib"
mandatory=true
versionRange="[1.0.0,)"
```
