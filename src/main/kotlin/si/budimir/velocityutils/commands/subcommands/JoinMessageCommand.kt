package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.commands.VelocityUtilsCommand
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper

class JoinMessageCommand : SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val args = invocation.arguments()

        if (args.size < 2) return

        // Check for clear arg
        if (args[1] == "clear") {
            // TODO: Clear custom join message
            println("Clearing custom join message")
            return
        }

        // Set custom join message
        println("Setting custom message to ${args[2]}")
        invocation.source().sendMessage(MessageHelper.parseWithPermissions(invocation.source() as Player, args[2]))
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String> {
        return VelocityUtilsCommand.getJoinLeaveTabSuggestions(invocation.arguments())
    }

    override fun getPermission(): String {
        return Permissions.SET_CUSTOM_MESSAGES.getPermission()
    }

    override fun getDesc(): String {
        return "Set custom join message"
    }

    override fun canConsoleUse(): Boolean {
        return false
    }
}