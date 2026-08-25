# FootLib
FootLib is the API and utility library used by the Foot‑series mods.
It provides shared systems such as:

1. Currency definitions (WIP)

2. Exchange‑rate systems (WIP)

3. This is also the shared API for ALL of my mods (WIP)

FootLib is the utility for all Foot series mods.
## Versioning
FootLib uses a **four‑segment versioning system:**

### Overhaul.Major.Semi‑major.Patch/Hotfix/Minor‑alpha/beta/release
Example:
0.0.1.0 → Semi‑major update (1 in the third position)

### Meaning of each segment
- **Overhaul**  
Backward compatibility breaks.
Overhaul does not mean big update, it means there is no backward compatibility from this point.

- **Major**
New systems, new mechanics, or major expansions.

- **Semi‑major**
Significant content or system additions.
Big‑minor updates that don’t break previous API.

- **Patch / Hotfix / Minor**
Small updates, bugfixes, micro‑features, or emergency fixes.

### Pre‑release tags
**‑alpha** → Experimental, unstable

**‑beta** → Feature‑complete, stabilizing

**(blank)** → Release build/stable

### Important Note
An Overhaul (1.x.x.x, 2.x.x.x, etc.) ends support for all previous Foot mods.

This is why FootLib does not use SemVer — SemVer cannot express the difference between a major update and a update which breaks all dependencies overhaul.
FootLib’s versioning makes the reason for incompatibility explicit.
**DON'T WORRY THIS MOD IS STILL GETTING UPDATES AND NEW FEATURES, I ALSO JUST HAPPEN TO HAVE MORE PROJECTS IN MIND SO GETTING THEM COMPLETED IS KEY**

Usage
Maven Repo
```gradle
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}
```
Gradle (Groovy)
```gradle
dependencies {
    implementation("maven.modrinth:footlib:${footlib_version}")
}
```
neoforge.mods.toml
```toml
[[dependencies.${mod_id}]]
    modId="footlib"
    type="required"
    versionRange="[min_version, max_version)"
    ordering="NONE"
    side="BOTH"
#Put an actual version in min_version and max_version
```
Protips for those looking to have a dependency with this mod:
1. When setting you neoforge.mods.toml then usually you want to set the versionRange to [0.0.0.1, 1.0.0.0) and if you need a feature from non-overhaul versions then you can do something like this: [0.2.5.4, 1.0.0.0)
