<!DOCTYPE html>

<html lang="en">
<head>
  <meta charset="UTF-8" />
</head>
<body>

<h1>FootLib</h1>

<p>
  FootLib is the API and utility library used by the Foot‑series mods.
  It provides shared systems such as:
</p>

<ol>
  <li>Currency definitions (WIP)</li>
  <li>Exchange‑rate systems (WIP)</li>
  <li>This is also the shared API for ALL of my mods (WIP)</li>
</ol>

<p>
  FootLib is the utility for all Foot series mods.
</p>

<h2>Versioning</h2>

<p>FootLib uses a <strong>four‑segment versioning system:</strong></p>

<h3>Overhaul.Major.Semi‑major.Patch/Hotfix/Minor‑alpha/beta/release</h3>

<p>Example:<br />
<code>0.0.1.0</code> → Semi‑major update (1 in the third position)</p>

<h3>Meaning of each segment</h3>

<ul>
  <li><strong>Overhaul</strong><br />
    Backward compatibility breaks.<br />
    Overhaul does not mean big update, it means there is no backward compatibility from this point.
  </li>

<li><strong>Major</strong><br />
    New systems, new mechanics, or major expansions.
  </li>

<li><strong>Semi‑major</strong><br />
    Significant content or system additions.<br />
    Big‑minor updates that don’t break previous API.
  </li>

<li><strong>Patch / Hotfix / Minor</strong><br />
    Small updates, bugfixes, micro‑features, or emergency fixes.
  </li>
</ul>

<h3>Pre‑release tags</h3>

<ul>
  <li><strong>-alpha</strong> → Experimental, unstable</li>
  <li><strong>-beta</strong> → Feature‑complete, stabilizing</li>
  <li><strong>(blank)</strong> → Release build/stable</li>
</ul>

<h3>Important Note</h3>

<p>
  An Overhaul (1.x.x.x, 2.x.x.x, etc.) ends support for all previous Foot mods.
</p>

<p>
  This is why FootLib does not use SemVer — SemVer cannot express the difference between a major update and an update which breaks all dependencies (overhaul).  
  FootLib’s versioning makes the reason for incompatibility explicit.
</p>

<p><strong>DON'T WORRY THIS MOD IS STILL GETTING UPDATES AND NEW FEATURES, I ALSO JUST HAPPEN TO HAVE MORE PROJECTS IN MIND SO GETTING THEM COMPLETED IS KEY</strong></p>

<h2>Usage</h2>

<h3>Maven Repo</h3>

<pre><code>repositories {
    maven { url = "https://api.modrinth.com/maven" }
}
</code></pre>

<h3>Gradle (Groovy)</h3>

<pre><code>dependencies {
    implementation("maven.modrinth:footlib:${footlib_version}")
}
</code></pre>

<h3>neoforge.mods.toml</h3>

<pre><code>[[dependencies.${mod_id}]]
    modId="footlib"
    type="required"
    versionRange="[min_version, max_version)"
    ordering="NONE"
    side="BOTH"
# Put an actual version in min_version and max_version
</code></pre>

<h3>Protips</h3>

<ol>
  <li>
    When setting your <code>neoforge.mods.toml</code>, you usually want:
    <br /><code>[0.0.0.1, 1.0.0.0)</code>
  </li>
  <li>
    If you need a feature from non‑overhaul versions:
    <br /><code>[0.2.5.4, 1.0.0.0)</code>
  </li>
</ol>

<h2>Blender Assets</h2>

<p>
  Blender modeling assets are documented separately.<br />
  See <a href="README_BLENDER.md">README_BLENDER.md</a> for details.
</p>

Download link: https://modrinth.com/project/18KUjTNM

</body>
</html>
