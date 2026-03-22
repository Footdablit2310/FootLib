# FootLib

FootLib is a lightweight API library used by Foot mods.
It provides shared systems such as:

- Foot Economy API
- Currency definitions
- Exchange rate systems
- Auto‑rate engine hooks
- Service injection for mod interoperability

## Usage

Add FootLib as a dependency in your mod's `mods.toml`:

```toml
[[dependencies.yourmod]]
modId="footlib"
mandatory=true
versionRange="[1.0.0,)"