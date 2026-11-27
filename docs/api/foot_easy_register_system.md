# FootEasyRegister System

FootEasyRegister (FER) is FootLib's registration helper for common NeoForge registries. It wraps NeoForge's `DeferredRegister` instances and provides builders for registering items, blocks, block entities, menus, fluids, and sounds.

This page covers the system entry point, item/block registration, and creative tabs. For JEI recipe registration, see [jei.md](jei.md).

## Creating a FER instance

Create one `FootEasyRegisterSystem` for your mod and pass your mod ID, mod event bus, and display name:

```java
public static final FootEasyRegisterSystem FER =
    new FootEasyRegisterSystem(MOD_ID, modEventBus, "Example Mod");
```

The constructor creates and attaches deferred registers for:

- Blocks
- Items
- Block entities
- Creative mode tabs
- Fluid types and fluids
- Menus
- Sound events

It also subscribes to the creative-tab contents event. FER uses this event to add entries to tabs that already exist, such as vanilla tabs or tabs registered by another mod.

## Registering items

Use `item(name, factory)` to create an item builder:

```java
public static final DeferredHolder<Item, ExampleItem> EXAMPLE_ITEM =
    FER.item("example_item", ExampleItem::new)
        .register();
```

The builder accepts an item factory supplier. Call `.register()` to register it and receive its `DeferredHolder<Item, T>`.

## Registering blocks

Use `block(name, factory)` to create a block builder:

```java
public static final DeferredHolder<Block, ExampleBlock> EXAMPLE_BLOCK =
    FER.block("example_block", ExampleBlock::new)
        .register();
```

### Creating a simple BlockItem

A block does not automatically get an item form from FER. Call `.simpleItem()` to register a `BlockItem` using the same registry name as the block:

```java
FER.block("example_block", ExampleBlock::new)
    .simpleItem()
    .register();
```

If you want the block's item form to appear in a creative tab, use `.simpleItem()` together with `.creativeTab(...)`.

# Creative Tabs

FER supports two different creative-tab workflows:

1. **Custom tabs** — tabs registered by your mod through `registerCreativeTab(...)`.
2. **Existing tabs** — tabs that already exist, including vanilla tabs and tabs from other mods.

The two workflows use different `.creativeTab(...)` overloads. A `String` selects one of your custom tabs by its registry name; a `ResourceKey<CreativeModeTab>` selects an existing tab.

## Adding items to a custom tab

Pass the custom tab's registry name to `.creativeTab(String)`:

```java
public static final DeferredHolder<Item, ExampleItem> EXAMPLE_ITEM =
    FER.item("example_item", ExampleItem::new)
        .creativeTab("example_tab")
        .register();
```

Then register the custom tab using a `TabConfig`:

```java
FER.registerCreativeTab(new TabConfig(
    "example_tab",
    Component.translatable("itemGroup.examplemod.example_tab"),
    Optional.of(() -> new ItemStack(Items.DIAMOND)),
    false,
    89,
    Optional.empty(),
    Optional.empty(),
    Optional.empty()
));
```

**Registration order matters:** register all entries for a custom tab before calling `registerCreativeTab()` for that tab. FER collects the entries by tab name and captures that list when the tab is registered. Registering an item for a tab after the tab itself has been registered will not add that item to the already-created tab.

The tab name used by `.creativeTab("example_tab")` must exactly match the `name` passed to `TabConfig`.

### TabConfig parameters

The `TabConfig` constructor accepts these values, in this order:

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | Registry path for the tab, such as `"example_tab"`. Must not be blank. |
| `title` | `Component` | Display title. Usually use `Component.translatable(...)`. |
| `icon` | `Optional<Supplier<ItemStack>>` | Supplies the tab icon. If empty, FER uses a copper ingot. |
| `searchBar` | `boolean` | Whether the tab has a search bar. |
| `searchBarWidth` | `int` | Search-bar width. Values at or below zero are replaced with the default width, 89. |
| `beforeTabs` | `Optional<List<ResourceLocation>>` | Tab IDs to position this tab before. |
| `afterTabs` | `Optional<List<ResourceLocation>>` | Tab IDs to position this tab after. |
| `displayGenerator` | `Optional<CreativeModeTab.DisplayItemsGenerator>` | Optional callback for adding entries directly while the tab contents are built. |

For example, a tab with a search bar and a custom display generator:

```java
FER.registerCreativeTab(new TabConfig(
    "example_tab",
    Component.translatable("itemGroup.examplemod.example_tab"),
    Optional.of(() -> new ItemStack(Items.DIAMOND)),
    true,
    100,
    Optional.empty(),
    Optional.empty(),
    Optional.of((parameters, output) -> {
        output.accept(Items.DIAMOND);
        output.accept(Items.EMERALD);
    })
));
```

The display generator is useful for entries that you want to add directly rather than registering through an item or block builder. Entries added through FER's `.creativeTab("example_tab")` builder option are appended to the tab as well.

### Custom tab localization

When the tab title is a translatable component, FER automatically adds its translation key to the creative-tab language map.

The generated value is the FER instance's `visualName` if it is non-null and non-blank. Otherwise, FER derives a readable value from the tab registry name by replacing underscores with spaces and capitalizing each word.

You can also add a translation manually:

```java
FER.registerCreativeTabLang(
    "itemGroup.examplemod.example_tab",
    "Example Items"
);
```

The values can be retrieved with `FER.getCreativeTabLang()`, for example by your language/datagen integration. This method stores language entries; it does not itself write a language JSON file.

## Adding items to an existing tab

Use the `ResourceKey<CreativeModeTab>` overload when targeting a tab that is already registered:

```java
FER.item("example_item", ExampleItem::new)
    .creativeTab(CreativeModeTabs.INGREDIENTS)
    .register();
```

This works for vanilla tabs and for another mod's tab, provided you have its tab key.

FER stores these entries and adds them when NeoForge fires `BuildCreativeModeTabContentsEvent` for the matching tab. You do not need to register a custom `TabConfig` for an existing tab.

## Block creative-tab entries

For a block, enable `.simpleItem()` if you want its `BlockItem` to be placed in the tab:

```java
FER.block("example_block", ExampleBlock::new)
    .simpleItem()
    .creativeTab("example_tab")
    .register();
```

The block builder also exposes the `creativeTab(ResourceKey<CreativeModeTab>)` overload for existing tabs. The tab entry represents the block itself if no simple item is created; for normal inventory use, create the `BlockItem` with `.simpleItem()`.

## Builder chaining

Builder methods return the builder, so options can be combined before the final `.register()` call:

```java
FER.item("example_item", ExampleItem::new)
    .creativeTab("example_tab")
    .jeiRecipe(/* see jei.md */)
    .register();
```

## Other registration helpers

The system exposes additional builder entry points:

| Method | Purpose |
| --- | --- |
| `blockEntity(name, factory)` | Creates a block-entity builder |
| `menu(name)` | Creates a menu builder |
| `fluid(name)` | Creates a fluid builder |
| `soundEvent()` | Creates a sound-event builder |
| `command(context)` | Creates a command builder |
| `screen(menuType)` | Creates a menu-linked screen builder |

Consult the corresponding builder classes for the complete method set and specialized configuration options.

## Notes

- Use the returned deferred holders when referencing registered objects elsewhere.
- Registration methods should be called during mod setup/registration, not in gameplay callbacks.
- FER is a convenience layer over NeoForge deferred registration; it does not replace Minecraft or NeoForge registry lifecycle rules.
