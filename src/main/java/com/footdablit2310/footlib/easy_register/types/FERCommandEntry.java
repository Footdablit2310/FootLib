package com.footdablit2310.footlib.easy_register.types;

import com.footdablit2310.footlib.FootLib;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public record FERCommandEntry(
	String namespace,
	LiteralArgumentBuilder<CommandSourceStack> rootCommand,
	int permissionLevel,
	CommandExecutor executor
) {

	public LiteralArgumentBuilder<CommandSourceStack> build() {

		if (!namespace.isBlank()) {
			return Commands.literal(namespace)
				.requires(src -> src.hasPermission(permissionLevel))
				.then(rootCommand);
		} else {
			FootLib.LOGGER.warn("Running without namespace, this may cause mod conflicts.");
			return rootCommand
				.requires(src -> src.hasPermission(permissionLevel));
		}
	}
}
