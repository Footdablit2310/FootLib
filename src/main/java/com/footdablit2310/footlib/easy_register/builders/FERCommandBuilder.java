package com.footdablit2310.footlib.easy_register.builders;

import com.footdablit2310.footlib.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.easy_register.types.CommandExecutor;
import com.footdablit2310.footlib.easy_register.types.FERCommandEntry;
import com.footdablit2310.footlib.exceptions.IllegalNullValueException;

import com.mojang.brigadier.arguments.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.*;

public final class FERCommandBuilder {
	private final FootEasyRegisterSystem FER;
	private String namespace;
	private LiteralArgumentBuilder<CommandSourceStack> rootCommand;

	private int permissionLevel = 0;
	private CommandExecutor executor;

	private final CommandBuildContext context;

	private final List<ArgumentBuilder<CommandSourceStack, ?>> args = new ArrayList<>();
	private final Map<String, ArgumentValidator<?>> validators = new HashMap<>();
	private final Map<String, ArgumentType<?>> customArgs = new HashMap<>();
	private final Deque<LiteralArgumentBuilder<CommandSourceStack>> stack = new ArrayDeque<>();
	private LiteralArgumentBuilder<CommandSourceStack> topLiteral;




	public FERCommandBuilder(FootEasyRegisterSystem FER, CommandBuildContext context) {
		this.FER = FER;
		this.namespace = FER.getModId(); // default namespace
		this.context = context;
	}
	public FERCommandBuilder(FootEasyRegisterSystem FER, String namespace, CommandBuildContext context) {
		this.FER = FER;
		this.namespace = namespace;
		this.context = context;
	}

	public FERCommandBuilder namespace(String ns) {
		this.namespace = ns;
		return this;
	}

	public FERCommandBuilder sub(String name) {
		if (name.isBlank()) {
			throw new IllegalNullValueException("Blank String can not be a subcommand name");
		}
		LiteralArgumentBuilder<CommandSourceStack> sub = Commands.literal(name);
		stack.push(rootCommand);
		rootCommand.then(sub);
		rootCommand = sub;
		return this;
	}

	public FERCommandBuilder literal(String name) {
		if (name.isBlank()) {
			throw new IllegalNullValueException("Blank String can not be a command name");
		}
		LiteralArgumentBuilder<CommandSourceStack> lit = Commands.literal(name);

		if (rootCommand != null) {
			stack.push(rootCommand);
			rootCommand.then(lit);
		} else {
			topLiteral = lit; // store the root literal
		}

		rootCommand = lit;
		return this;
	}

	public FERCommandBuilder argumentPlayer(String name) {
		args.add(
			Commands.argument(name, EntityArgument.player())
		);
		return this;
	}
	// Boolean argument
	public FERCommandBuilder argumentBool(String name) {
		args.add(Commands.argument(name, BoolArgumentType.bool()));
		return this;
	}

	// Float argument
	public FERCommandBuilder argumentFloat(String name) {
		args.add(Commands.argument(name, FloatArgumentType.floatArg()));
		return this;
	}

	// Double argument
	public FERCommandBuilder argumentDouble(String name) {
		args.add(Commands.argument(name, DoubleArgumentType.doubleArg()));
		return this;
	}

	// Block position argument
	public FERCommandBuilder argumentBlockPos(String name) {
		args.add(Commands.argument(name, BlockPosArgument.blockPos()));
		return this;
	}

	// Item argument
	public FERCommandBuilder argumentItem(String name) {
		args.add(Commands.argument(name, ItemArgument.item(context)));
		return this;
	}

	// Entity argument
	public FERCommandBuilder argumentEntity(String name) {
		args.add(Commands.argument(name, EntityArgument.entity()));
		return this;
	}
	public FERCommandBuilder argumentCustom(String name, ArgumentType<?> type) {
		args.add(Commands.argument(name, type));
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

	public FERCommandBuilder goToParent() {
		if (!stack.isEmpty()) {
			rootCommand = stack.pop();
		}
		return this;
	}

	public FERCommandBuilder goToParent(int steps) {
		for (int i = 0; i < steps; i++) {
			if (!stack.isEmpty()) {
				rootCommand = stack.pop();
			} else {
				break; // no more parents
			}
		}
		return this;
	}

	public FERCommandBuilder root() {
		if (topLiteral != null) {
			rootCommand = topLiteral;
			stack.clear();
		}
		return this;
	}

	public void register() throws IllegalNullValueException {
		if (executor == null)
			throw new IllegalNullValueException("Command executor cannot be null.");

		for (ArgumentBuilder<CommandSourceStack, ?> arg : args) {
			rootCommand.then(arg.executes(ctx -> {
				try {
					// Run executor
					int result = executor.run(ctx);

					// Run validators
					for (var entry : validators.entrySet()) {
						String name = entry.getKey();
						ArgumentValidator validator = entry.getValue();
						Object value = ctx.getArgument(name, Object.class);
						validator.validate(value);
					}

					return result;

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}));

		}


		FER.addCommand(new FERCommandEntry(
			namespace,
			rootCommand,
			permissionLevel,
			executor
		));
	}
	public <T> FERCommandBuilder validate(String argName, ArgumentValidator<T> validator) {
		validators.put(argName, validator);
		return this;
	}

	@FunctionalInterface
	public interface ArgumentValidator<T> {
		void validate(T value) throws Exception;
	}

}
