package mcp.mobius.waila.command;

import com.mojang.brigadier.CommandDispatcher;
import mcp.mobius.waila.api.impl.DumpGenerator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommandDumpHandlers {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("dumpHandlers").requires(source -> source.hasPermission(2)).executes(context -> {
			File file = new File("waila_handlers.md");
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(DumpGenerator.generateInfoDump());
				context.getSource().sendSuccess(new TranslationTextComponent("command.dump.success"), false);
				return 1;
			} catch (IOException e) {
				context.getSource().sendFailure(new StringTextComponent(e.getClass().getSimpleName() + ": " + e.getMessage()));
				return 0;
			}
		}));
	}
}
