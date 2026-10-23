package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import com.footdablit2310.footlib.api.easy_register.types.CommandExecutor;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;

public final class FERCommandBuilder {
	private final FootEasyRegisterSystem FER;
	private final CommandBuildContext context;
	private String namespace;

	private CommandInfoList tree;

	public FERCommandBuilder(FootEasyRegisterSystem fer, String namespace, CommandBuildContext context) {
		this.FER = fer;
		this.context = context;
		this.namespace = namespace;
	}

	public FERCommandBuilder(FootEasyRegisterSystem fer, CommandBuildContext context) {
		this.FER = fer;
		this.context = context;
		this.namespace = fer.getModId();
	}

	public FERCommandBuilder literal(String name) {
		if (tree == null)
			tree = new CommandInfoList(name);
		else
			tree.addChild(name);
		return this;
	}

	public FERCommandBuilder sub(String name) {
		tree.addChild(name);
		return this;
	}

	public FERCommandBuilder goToParent() {
		tree.goToParent();
		return this;
	}

	public FERCommandBuilder root() {
		tree.goToRoot();
		return this;
	}

	private static final class CommandInfo {
		final String name;
		final List<CommandInfo> children = new ArrayList<>();
		final List<ArgumentBuilder<CommandSourceStack, ?>> arguments = new ArrayList<>();

		CommandExecutor executor;
		int permissionLevel = 0;

		CommandInfo parent;

		CommandInfo(String name) {
			this.name = name;
		}

		LiteralArgumentBuilder<CommandSourceStack> build() {
			LiteralArgumentBuilder<CommandSourceStack> lit = Commands.literal(name);

			if (permissionLevel > 0)
				lit.requires(src -> src.hasPermission(permissionLevel));

			if (executor != null)
				lit.executes(ctx -> {
					try {
						return executor.run(ctx);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

			for (var arg : arguments)
				lit.then(arg);

			for (var child : children)
				lit.then(child.build());

			return lit;
		}
	}


	private static final class CommandInfoList {
		private final CommandInfo root;
		private CommandInfo current;

		CommandInfoList(String rootName) {
			this.root = new CommandInfo(rootName);
			this.current = root;
		}

		public CommandInfo root() {
			return root;
		}

		public CommandInfo current() {
			return current;
		}

		public CommandInfo addChild(String name) {
			CommandInfo child = new CommandInfo(name);
			child.parent = current;
			current.children.add(child);
			current = child;
			return child;
		}

		public void goToParent() {
			if (current.parent != null)
				current = current.parent;
		}

		public void goToRoot() {
			current = root;
		}
	}


}