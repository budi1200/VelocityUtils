package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.enums.Constants
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper

class HelpCommand : SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val player = invocation.source()
        val subCommands: MutableMap<String, SubCommandBase> =
            VelocityUtilsMain.instance.velocityUtilsCommand.getSubCommands()
        val finalMessage: StringBuilder = StringBuilder()

        finalMessage.append("=== VelocityUtils Help ===")
        finalMessage.append("\nAuthor: <green>budi1200<reset>")
        finalMessage.append("\nVersion: <green>${Constants.VERSION}<reset>")
        finalMessage.append("\nAvailable commands:")

        for (i in subCommands) {
            val msg = "\n• <green>${i.key} <white>- <bold>${i.value.getDesc()}<reset>"
            finalMessage.append(msg)
        }

        MessageHelper.sendMessage(player, finalMessage.toString(), mutableMapOf(), false)
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String> {
        return mutableListOf()
    }

    override fun getPermission(): String {
        return Permissions.HELP.getPermission()
    }

    override fun getDesc(): String {
        return "Get help"
    }

    override fun canConsoleUse(): Boolean {
        return true
    }

}