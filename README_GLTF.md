<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>FootLib GLTF System Documentation</title>
</head>
<body>

<h1>FootLib GLTF System — API Documentation</h1>

<p>This document describes the FootLib GLTF attachment and rendering system for NeoForge 21.1 / Minecraft 1.21.1.  
It covers:</p>

<ul>
<li>GLTF config format</li>
<li>Attachment builder</li>
<li>Attach event</li>
<li>Binding registry</li>
<li>High‑level pipeline</li>
</ul>

<hr>

<h2>1. GLTF Config Format (<code>*.footcfg.json</code>)</h2>

<p>FootLib uses JSON configs to describe how GLTF files are used and mapped to states, animations, and nodes.</p>

<h3>1.1 File Layout</h3>

<p>GLTF files are resolved under:</p>

<pre>assets/&lt;modid&gt;/gltf_data/&lt;path&gt;</pre>

<p>Example config:</p>

<pre>{
  "files": {
    "default": {
      "path": "machine.gltf",
      "type": "gltf",
      "data": {}
    }
  },
  "states": {
    "idle": {
      "file": "default",
      "animation": "Idle"
    }
  }
}
</pre>

<h3>1.2 Path Resolution</h3>

<p>All GLTF file paths are resolved using MC 1.21.1 API:</p>

<pre>ResourceLocation.fromNamespaceAndPath(namespace, "gltf_data/" + file.path);</pre>

<hr>

<h2>2. FootGltfBindType (Binding Target Type)</h2>

<p>The binding target type enum:</p>

<pre>
public enum FootGltfBindType {
    BLOCK,
    ITEM,
    ENTITY
}
</pre>

<p>This describes what registry type the GLTF model attaches to.</p>

<ul>
<li><strong>BLOCK</strong> → BuiltInRegistries.BLOCK</li>
<li><strong>ITEM</strong> → BuiltInRegistries.ITEM</li>
<li><strong>ENTITY</strong> → BuiltInRegistries.ENTITY_TYPE</li>
</ul>

<p>Bindings use <code>ResourceLocation</code> names, not objects, so they are safe before registry freeze.</p>

<hr>

<h2>3. FootLibAttachGltfEvent (Mod Event Bus)</h2>

<p>This event is fired when a GLTF config is attached.  
It is <strong>fireable</strong>, <strong>hookable</strong>, and <strong>NOT</strong> a data store.</p>

<pre>
public class FootLibAttachGltfEvent extends Event {

    private final ResourceLocation cfgLocation;
    private final FootGltfConfig config;

    private final FootGltfBindType bindType;
    private final ResourceLocation bindTarget;

    public FootLibAttachGltfEvent(ResourceLocation cfgLocation,
                                  FootGltfConfig config,
                                  FootGltfBindType bindType,
                                  ResourceLocation bindTarget) {
        this.cfgLocation = cfgLocation;
        this.config = config;
        this.bindType = bindType;
        this.bindTarget = bindTarget;
    }

    public ResourceLocation getCfgLocation() { return cfgLocation; }
    public FootGltfConfig getConfig() { return config; }
    public FootGltfBindType getBindType() { return bindType; }
    public ResourceLocation getBindTarget() { return bindTarget; }
}
</pre>

<h3>3.1 Purpose</h3>

<ul>
<li>Notify FootLib that a GLTF config was attached</li>
<li>Allow other mods to hook into FootLib</li>
<li>Allow extensions (custom animations, materials, etc.)</li>
</ul>

<h3>3.2 Bus Choice</h3>

<p><strong>This event MUST be posted on the MOD EVENT BUS.</strong></p>

<hr>

<h2>4. FootGltfAttachmentBuilder</h2>

<p>A minimal, non‑DSL builder that:</p>

<ul>
<li>Loads the config</li>
<li>Resolves GLTF paths</li>
<li>Fires <code>FootLibAttachGltfEvent</code></li>
<li>Returns the parsed config</li>
</ul>

<pre>
public final class FootGltfAttachmentBuilder {

    private final ResourceManager resourceManager;
    private final FootGltfConfigHandler handler;

    private ResourceLocation cfgLocation;
    private FootGltfBindType bindType;
    private ResourceLocation bindTarget;

    private FootGltfAttachmentBuilder(ResourceManager rm) {
        this.resourceManager = rm;
        this.handler = new FootGltfConfigHandler();
    }

    public static FootGltfAttachmentBuilder create(ResourceManager rm) {
        return new FootGltfAttachmentBuilder(rm);
    }

    public FootGltfAttachmentBuilder config(ResourceLocation cfgLocation) {
        this.cfgLocation = cfgLocation;
        return this;
    }

    public FootGltfAttachmentBuilder bind(FootGltfBindType type, ResourceLocation target) {
        this.bindType = type;
        this.bindTarget = target;
        return this;
    }

    public FootGltfConfig attach() throws IOException {
        FootGltfConfig cfg = handler.load(resourceManager, cfgLocation);

        FootLib.EVENT_BUS.post(
            new FootLibAttachGltfEvent(cfgLocation, cfg, bindType, bindTarget)
        );

        return cfg;
    }
}
</pre>

<h3>4.1 Usage Examples</h3>

<p>Bind GLTF to a block:</p>

<pre>
FootGltfAttachmentBuilder.create(resourceManager)
    .config(ResourceLocation.fromNamespaceAndPath("modid", "models/machine.footcfg.json"))
    .bind(FootGltfBindType.BLOCK, ResourceLocation.fromNamespaceAndPath("modid", "machine_block"))
    .attach();
</pre>

<p>Bind GLTF to an item:</p>

<pre>
FootGltfAttachmentBuilder.create(resourceManager)
    .config(ResourceLocation.fromNamespaceAndPath("modid", "models/machine.footcfg.json"))
    .bind(FootGltfBindType.ITEM, ResourceLocation.fromNamespaceAndPath("modid", "machine_item"))
    .attach();
</pre>

<p>Bind GLTF to an entity:</p>

<pre>
FootGltfAttachmentBuilder.create(resourceManager)
    .config(ResourceLocation.fromNamespaceAndPath("modid", "models/machine.footcfg.json"))
    .bind(FootGltfBindType.ENTITY, ResourceLocation.fromNamespaceAndPath("modid", "machine_entity"))
    .attach();
</pre>

<hr>

<h2>5. FootLibBindingRegistry (Data House)</h2>

<p>This registry stores all GLTF bindings.  
It is the authoritative FootLib binding database.</p>

<pre>
public final class FootLibBindingRegistry {

    private static final Map&lt;ResourceLocation, FootLibBinding&gt; BINDINGS = new HashMap<>();

    public static void register(FootLibBinding binding) {
        BINDINGS.put(binding.bindTarget(), binding);
    }

    public static FootLibBinding get(ResourceLocation target) {
        return BINDINGS.get(target);
    }

    public static Collection&lt;FootLibBinding&gt; all() {
        return BINDINGS.values();
    }
}
</pre>

<h3>5.1 Binding Record</h3>

<pre>
public record FootLibBinding(
    FootGltfBindType bindType,
    ResourceLocation bindTarget,
    ResourceLocation cfgLocation,
    FootGltfConfig config
) {}
</pre>

<h3>5.2 Internal Event Listener</h3>

<pre>
@SubscribeEvent
public static void onAttach(FootLibAttachGltfEvent event) {
    FootLibBindingRegistry.register(
        new FootLibBinding(
            event.getBindType(),
            event.getBindTarget(),
            event.getCfgLocation(),
            event.getConfig()
        )
    );
}
</pre>

<hr>

<h2>6. High‑Level Pipeline</h2>

<pre>
AttachmentBuilder
    → FootLibAttachGltfEvent
    → FootLibBindingRegistry
    → FootGltfConfigResolver
    → FootGltfIRBuilder
    → FootGltfRuntimeModel
</pre>

<h3>6.1 Typical Flow</h3>

<pre>
// 1. Attach config and fire event
FootGltfConfig cfg = FootGltfAttachmentBuilder.create(resourceManager)
    .config(cfgLocation)
    .bind(bindType, bindTarget)
    .attach();

// 2. FootLib stores binding
FootLibBinding binding = FootLibBindingRegistry.get(bindTarget);

// 3. Resolve config
FootGltfResolved resolved = FootGltfConfigResolver.resolve(binding.config());

// 4. Build IR
FootGltfIR ir = FootGltfIRBuilder.build(resolved);

// 5. Create runtime model
FootGltfRuntimeModel model = new FootGltfRuntimeModel(ir);
</pre>

<hr>

<h2>7. Design Principles</h2>

<ul>
<li><strong>API‑driven</strong>: No mixins, no registry hacks.</li>
<li><strong>Event‑based</strong>: Mods hook via FootLibAttachGltfEvent.</li>
<li><strong>Registry‑safe</strong>: Bindings use names, not objects.</li>
<li><strong>Version‑correct</strong>: Uses MC 1.21.1 ResourceLocation API.</li>
<li><strong>Modular</strong>: Each stage is independent and replaceable.</li>
</ul>

</body>
</html>
