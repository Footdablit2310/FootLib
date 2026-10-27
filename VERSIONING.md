# Versioning rules applied to Foot: Organic Processing

## Versioning scheme

Foot: Organic Processing uses the O.M.S.H-tag versioning scheme (similar to Epoch SemVer, but with several modifications).

The version number is in the format of `O.M.S.H-tag` where:

- `O` is the overhaul version, which is incremented when changes are not backward compatible with previous versions. This includes API changes, internal implementation changes, and file structure changes. Any code that uses the API may be affected. Porting from a previous overhaul version may therefore require significant changes. An overhaul version does not necessarily indicate a large amount of new functionality; it simply indicates that backward compatibility has been broken.

- `M` is the major version, which is incremented when significant new features or improvements are added while maintaining backward compatibility.

- `S` is the semi-major (or big-minor) version, which is incremented when a release contains many changes but does not qualify as a major version.

- `H` is the hotfix (or patch/minor) version, which is incremented when a release contains urgent bug fixes, hotfixes, minor improvements, or similar maintenance changes.

- `-tag` is the release tag. It identifies the release channel, such as `alpha`, `beta`, `release`, `dev`, or `nightly`. If no tag is specified, the version is assumed to be a `release` build.