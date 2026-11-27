# File Structure
- Everything in api.common is a Util // This is meant to be used. It makes less Neoforge code and more general code which if I add fabric will be easier to port your mod to Fabric
- Everything in api.shared is a public (and the ONLY public) API for my mods. // This is meant to be used
- Everything in api.compat_connect connects my mods with others mods in an easier and cleaner way. // This is NOT meant to be used if you have not read source carfully.
- Everything outside .api is unsafe to use directly.
