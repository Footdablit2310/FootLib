package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.easy_register.types.CommandExecutor;
import com.footdablit2310.footlib.easy_register.types.FERCommandEntry;
import com.footdablit2310.footlib.exceptions.IllegalNullValueException;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class FERCommandBuilder {

	private final FootEasyRegisterSystem FER;
	private String namespace;
	private LiteralArgumentBuilder<CommandSourceStack> rootCommand;

	private int permissionLevel = 0;
	private CommandExecutor executor;

	public FERCommandBuilder(FootEasyRegisterSystem FER) {
		this.FER = FER;
		this.namespace = FER.getModId(); // default namespace
	}
	public FERCommandBuilder(FootEasyRegisterSystem FER, String namespace) {
		this.FER = FER;
		this.namespace = namespace;
	}

	public FERCommandBuilder namespace(String ns) {
		this.namespace = ns;
		return this;
	}

	public FERCommandBuilder literal(String name) {
		this.rootCommand = Commands.literal(name);
		return this;
	}

	public FERCommandBuilder requires(int level) {
		this.permissionLevel = level;
		return this;
	}

	public FERCommandBuilder executes(CommandExecutor exec) {
		this.executor = exec;
		return this;
	}

	public void register() throws IllegalNullValueException {
		if (rootCommand == null)
			throw new IllegalNullValueException("Command literal cannot be null.");

		if (executor == null)
			throw new IllegalNullValueException("Command executor cannot be null.");

		FER.addCommand(new FERCommandEntry(
			namespace,
			rootCommand,
			permissionLevel,
			executor
		));
	}
}
