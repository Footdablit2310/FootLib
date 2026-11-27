# JEI Integration

FootLib provides a small JEI recipe-registration bridge for the FootEasyRegister item and block builders.

**This integration registers recipe data only.** Your mod remains responsible for creating its JEI recipe category, category layout, catalysts, and display logic.

JEI is declared as a required client-side dependency by FootLib.

## Recipe type IDs

Choose a unique `ResourceLocation` for each recipe type. The ID passed to `.jeiRecipe(...)` must match the ID used by your JEI category.

For example:

```java
public static final ResourceLocation PRESSING =
    ResourceLocation.fromNamespaceAndPath(MOD_ID, "pressing");
```

Your category's JEI type must use the `JEIRecipeWrapper.class` recipe class, because that is the wrapper FootLib submits to JEI.

FootLib also provides `JEICategoryBuilder.type(String, Class<T>)` as a small helper:

```java
public static final RecipeType<JEIRecipeWrapper> PRESSING_TYPE =
    JEICategoryBuilder.type("examplemod:pressing", JEIRecipeWrapper.class);
```

## Registering recipes with FER

Both `FERItemBuilder` and `FERBlockBuilder` provide two `.jeiRecipe(...)` overloads.

### Supplier overload (recommended for deferred holders)

```java
FER.item("steel_ingot", SteelIngotItem::new)
    .jeiRecipe(
        PRESSING,
        () -> List.of(new ItemStack(Items.IRON_INGOT)),
        () -> List.of(new ItemStack(Items.IRON_BLOCK))
    )
    .register();
```

The input and output suppliers are retained until JEI requests the recipes. This is useful when the stacks refer to items or blocks registered through deferred holders:

```java
FER.item("machine_part", MachinePartItem::new)
    .jeiRecipe(
        PRESSING,
        () -> List.of(new ItemStack(MY_INGOT.get())),
        () -> List.of(new ItemStack(MY_BLOCK_ITEM.get()))
    )
    .register();
```

The suppliers must return non-null lists containing non-null `ItemStack` values.

### Direct-list overload

If all stacks are already available, pass lists directly:

```java
FER.item("steel_ingot", SteelIngotItem::new)
    .jeiRecipe(
        PRESSING,
        List.of(new ItemStack(Items.IRON_INGOT)),
        List.of(new ItemStack(Items.IRON_BLOCK))
    )
    .register();
```

The direct-list overload is shorthand for the supplier overload. It does not defer the creation of the lists themselves.

### Block builder

The same API is available on block builders:

```java
FER.block("press", PressBlock::new)
    .simpleItem()
    .jeiRecipe(
        PRESSING,
        () -> List.of(new ItemStack(Items.COPPER_INGOT)),
        () -> List.of(new ItemStack(Items.COPPER_BLOCK))
    )
    .register();
```

## How registration works

1. Calling `.jeiRecipe(...)` adds a recipe-registration request to the builder.
2. Calling the builder's `.register()` forwards that request to FootLib's `JEIRecipeRegistry`.
3. The registry groups recipe suppliers by `ResourceLocation`.
4. When JEI invokes FootLib's `FootLibJEIPlugin`, the plugin resolves the suppliers and submits `JEIRecipeWrapper` instances to JEI.

The wrapper exposes two lists:

- `inputs()` — recipe input stacks.
- `outputs()` — recipe output stacks.

The wrapper copies the stacks when it is constructed, so the wrapper owns copies rather than the original stack objects.

## Implementing your category

Your own category must use the same recipe type ID and wrapper class:

```java
public static final RecipeType<JEIRecipeWrapper> PRESSING_TYPE =
    JEICategoryBuilder.type("examplemod:pressing", JEIRecipeWrapper.class);
```

In your category's recipe layout method, read the wrapper's `inputs()` and `outputs()` and add those stacks to the appropriate JEI slots. The layout and presentation are entirely controlled by your mod.

## Important limitations

- FootLib does not generate Minecraft recipes.
- FootLib does not create JEI categories or category layouts.
- FootLib does not infer recipes from registered items or blocks.
- A recipe is only submitted when its builder is registered with `.register()`.
- The category recipe type ID and wrapper class must match the values used by FootLib.
- Register recipe suppliers during normal mod registration. Do not register recipes repeatedly from gameplay events.

## Relevant classes

- `FERItemBuilder`
- `FERBlockBuilder`
- `JEIRecipeRegistry`
- `FootLibJEIPlugin`
- `JEIRecipeWrapper`
- `JEICategoryBuilder`
