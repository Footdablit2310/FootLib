package com.footdablit2310.footlib.easy_register.types;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface CommandExecutor {
	<T> T run(CommandContext<CommandSourceStack> ctx) throws Exception;
}
