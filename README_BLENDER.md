<!DOCTYPE html>

<html lang="en">
<head>
  <meta charset="UTF-8" />
</head>
<body>
  <h1>FootLib UnitBlocks Blender Asset Library</h1>

  <p>
    This repository includes Blender asset libraries containing standardized
    Minecraft-ratio modeling primitives. All assets are <strong>pre‑marked</strong> and
    include a <code>blender_assets.cats.txt</code> catalog file so they appear correctly
    in Blender’s Asset Browser without any manual setup.
  </p>

  <h2>Included Asset Libraries</h2>

  <ul>
    <li><strong>UnitBlocksBasic.blend</strong> — 1/1, 1/2, 1/4, 1/8, 1/16 blocks</li>
    <li><strong>UnitBlocksAdvanced.blend</strong> — all blocks from 1/16 to 16/16 (Minecraft supports 16 as the denominator)</li>
    <li><strong>blender_assets.cats.txt</strong> — catalog definitions and UUIDs</li>
  </ul>

  <p>
    Each library has its own catalog entry and UUID, ensuring assets never appear
    under “Unassigned” and never collide with other libraries.
  </p>

  <h2>Setup: Installing the Catalog File</h2>

  <p>
    Blender uses <code>blender_assets.cats.txt</code> to organize assets into catalogs.
    To ensure the UnitBlocks libraries appear correctly:
  </p>

  <ul>
    <li>
      <strong>If your Blender asset folder already contains a <code>blender_assets.cats.txt</code> file:</strong><br />
      Extend it by <strong>copying the catalog entries</strong> from this repository’s file
      into your existing one. Blender supports multiple catalogs in a single file.
    </li>
    <li>
      <strong>If your Blender asset folder is empty:</strong><br />
      Simply <strong>copy the entire <code>blender_assets.cats.txt</code> file</strong> from this repository
      into your asset folder. No merging required.
    </li>
    <li>
      <strong>Copy the <code>.blend</code> files</strong> (<code>UnitBlocksBasic.blend</code> and
      <code>UnitBlocksAdvanced.blend</code>) directly into your Blender asset folder.
      Blender will automatically detect them and apply the catalog definitions.
    </li>
  </ul>

  <p>
    After copying the files, restart Blender to reload the catalog definitions.
  </p>

  <h2>Installation: Add as a Blender Asset Library</h2>

  <ol>
    <li>
      <strong>Download</strong> the <code>blender</code> folder or the
      <code>UnitBlocksAssets.zip</code> from the latest release tagged <strong>Blender</strong>.
    </li>
    <li>
      <strong>Locate</strong> the downloaded files on your machine.<br />
      You may install <strong>either</strong> of the blend files if you only need one catalog.
      If you install only one, remember to <strong>remove the unused catalog entry</strong>
      from <code>blender_assets.cats.txt</code>.
      <ul>
        <li><code>UnitBlocksBasic.blend</code></li>
        <li><code>UnitBlocksAdvanced.blend</code></li>
        <li><code>blender_assets.cats.txt</code></li>
      </ul>
    </li>
    <li>
      <strong>Open Blender</strong> and go to:
      <br />
      <code>Edit → Preferences → File Paths → Asset Libraries → Add</code>
    </li>
    <li>
      <strong>Configure the library:</strong>
      <ul>
        <li><strong>Name:</strong> something_reasonable</li>
        <li><strong>Path:</strong> the <code>/blender/</code> folder</li>
      </ul>
    </li>
    <li><strong>Restart Blender</strong> to apply the changes.</li>
  </ol>

  <h2>Using the UnitBlocks in Blender</h2>

  <ol>
    <li>
      <strong>Open the Asset Browser:</strong><br />
      <code>Window → Asset Browser</code> or change any editor to <strong>Asset Browser</strong>.
    </li>
    <li>
      <strong>Select the name you selected instead of “something_reasonable” library</strong> from the dropdown.
    </li>
    <li>
      <strong>Drag UnitBlocks into your scene</strong>:
      <ul>
        <li><code>UnitBlock1/1</code></li>
        <li><code>UnitBlock1/2</code></li>
        <li><code>UnitBlock1/4</code></li>
        <li><code>UnitBlock1/8</code></li>
        <li><code>UnitBlock1/16</code></li>
      </ul>
      All assets appear under their correct catalog:
      <ul>
        <li><strong>UnitBlocksBasic</strong></li>
        <li><strong>UnitBlocksAdvanced</strong></li>
      </ul>
    </li>
  </ol>

  <h2>If Assets Don’t Show Up</h2>

  <p>
    If the assets are not visible:
  </p>

  <ol>
    <li>Verify the <code>/blender/</code> folder is added as an Asset Library.</li>
    <li>Ensure <code>blender_assets.cats.txt</code> is present in the same folder as the blend files.</li>
    <li>Restart Blender to reload the catalog definitions.</li>
  </ol>

  <h2>Common Mistakes &amp; Fixes</h2>

  <h3>1. Incorrect Cube Sizing</h3>

  <p>
    Blender’s default cube is <strong>2 × 2 × 2 meters</strong>.  
    All UnitBlocks use correct metric dimensions:
  </p>

  <ul>
    <li><strong>UnitBlock1/1</strong> — 1.0 m</li>
    <li><strong>UnitBlock1/2</strong> — 0.5 m</li>
    <li><strong>UnitBlock1/4</strong> — 0.25 m</li>
    <li><strong>UnitBlock1/8</strong> — 0.125 m</li>
    <li><strong>UnitBlock1/16</strong> — 0.0625 m</li>
  </ul>

  <h3>2. Scale Not Applied</h3>

  <p>
    Apply scale before exporting models:
  </p>

  <pre><code>Ctrl + A → Apply Scale</code></pre>

  <h3>3. Asset Library Path Not Set</h3>

  <p>
    Blender will not display assets unless the folder containing the blend files
    is explicitly added as an Asset Library.
  </p>

  <pre><code>Edit → Preferences → File Paths → Asset Libraries → Add</code></pre>

  <h3>4. Expected Knowledge Level</h3>

  <p>
    This project assumes contributors have basic Blender knowledge or can read
    technical documentation in English.
  </p>

  <h2>License</h2>

  <p>
    This project, including all Blender assets and modeling primitives, is licensed
    under the MIT License.
  </p>

  <p>
    For full license details, see:
    <a href="LICENSE.md">LICENSE.md</a>
  </p>

</body>
</html>
