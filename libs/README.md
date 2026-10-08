# Vendored dependency jars

`neoforge.mods.toml` declares Farmer's Delight as a hard dependency, and AppleSkin is used for
one client HUD mixin. Neither has a 26.1 build on a public maven yet, so both are referenced as
plain files (26.1 ships unobfuscated, so no deobf step is needed) instead of a coordinate.

Drop these two jars here before building:

| File | Where it comes from |
|---|---|
| `FarmersDelight-26.1.2-1.3.3-fix4.jar` | the 26.1 community port; the file also has to be present in the modpack's `mods/` folder |
| `appleskin-neoforge-mc26.1-3.0.9.jar` | <https://modrinth.com/mod/appleskin> (26.1 build) |

Both are gitignored on purpose - they are third-party binaries, not part of this fork.
