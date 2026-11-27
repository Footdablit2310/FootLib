# FootEasyRegister System

FootEasyRegister (FER) is FootLib's registration helper for common NeoForge registries. It wraps NeoForge's `DeferredRegister` instances and provides small builders for registering items, blocks, block entities, menus, fluids, and sounds.

This page covers the system entry point and the item/block builder workflow. For JEI recipe registration, see [jei.md](jei.md).

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

The system also subscribes to the creative-tab contents event so entries can be added to existing tabs.

## Registering items

Use `item(name, factory)` to create an item builder:

```java
public static final DeferredHolder<Item, ExampleItem> EXAMPLE_ITEM =
    FER.item("example_item", ExampleItem::new)
        .register();
```

The builder accepts an item factory supplier. Call `.register()` to register it and receive its `DeferredHolder<Item, T>`.

### Item creative tabs

Add an item to a custom FER tab by its registration name:

```java
FER.item("example_item", ExampleItem::new)
    .creativeTab("example_tab")
    .register();
```

Or add it to an existing tab using its `ResourceKey<CreativeModeTab>`:

```java
FER.item("example_item", ExampleItem::new)
    .creativeTab(CreativeModeTabs.INGREDIENTS)
    .register();
```

For custom tabs, call `addToTab(...)` through the builder before registering the custom tab with `registerCreativeTab(...)`. The custom tab collects entries registered for its name when it is created.

## Registering blocks

Use `block(name, factory)` to create a block builder:

```java
public static final DeferredHolder<Block, ExampleBlock> EXAMPLE_BLOCK =
    FER.block("example_block", ExampleBlock::new)
        .register();
```

### Creating a simple BlockItem

Call `.simpleItem()` to register a `BlockItem` with the same registry name:

```java
FER.block("example_block", ExampleBlock::new)
    .simpleItem()
    .register();
```

### Block creative tabs

The block builder also supports `.creativeTab(...)`. If you want the block's item form in a creative tab, enable `.simpleItem()` as well.

```java
FER.block("example_block", ExampleBlock::new)
    .simpleItem()
    .creativeTab("example_tab")
    .register();
```

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
