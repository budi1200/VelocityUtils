package si.budimir.velocityutils.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ConsoleCommandSource
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.commands.subcommands.HelpCommand
import si.budimir.velocityutils.commands.subcommands.ReloadCommand
import si.budimir.velocityutils.util.MessageHelper
import java.util.concurrent.CompletableFuture

class VelocityUtilsCommand(private val plugin: VelocityUtilsMain) : SimpleCommand {
    private val subCommands: MutableMap<String, SubCommandBase> = HashMap()
    private var subCommandsList: List<String> = emptyList()

    init {
        subCommands["help"] = HelpCommand()
        subCommands["reload"] = ReloadCommand()

        subCommandsList = subCommands.keys.toList()
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            MessageHelper.sendMessage(sender, plugin.mainConfig.lang.invalidUsage)
            return
        }

        val sc: SubCommandBase? = subCommands[args[0]]
        if (sc == null) {
            MessageHelper.sendMessage(sender, plugin.mainConfig.lang.invalidUsage)
            return
        }

        val reqPerm: String = sc.getPermission()
        if (reqPerm != "" && !sender.hasPermission(reqPerm)) {
            MessageHelper.sendMessage(sender, plugin.mainConfig.lang.missingPermission)
            return
        }

        // Check if console can use the sub command
        if (invocation.source() is ConsoleCommandSource && !sc.canConsoleUse()) {
            MessageHelper.sendMessage(invocation.source(), "This command cannot be used from console.")
            return
        }

        sc.execute(invocation)
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<MutableList<String>> {
        val args = invocation.arguments()

        return CompletableFuture.supplyAsync {
            return@supplyAsync when {
                args.isEmpty() -> {
                    subCommandsList.toMutableList()
                }
                args.size == 1 -> {
                    subCommandsList.filter { it.contains(args[0], ignoreCase = true) }.toMutableList()
                }
                else -> {
                    val sc: SubCommandBase = subCommands[args[0]] ?: return@supplyAsync mutableListOf()
                    sc.suggestAsync(invocation)
                }
            }
        }
    }

    fun getSubCommands(): MutableMap<String, SubCommandBase> {
        return subCommands
    }
}