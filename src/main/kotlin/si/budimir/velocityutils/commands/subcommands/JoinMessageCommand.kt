package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.commands.VelocityUtilsCommand
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper

class JoinMessageCommand(private val plugin: VelocityUtilsMain) : SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val commandExecutor = invocation.source()
        val args = invocation.arguments()

        if (args.size < 2) return

        // Check for clear arg
        if (args[1] == "clear") {
            // TODO: Clear custom join message
            println("Clearing custom join message")
            return
        }

        // TODO: Set custom join message

        // Inform player
        val permissionParsedMessage = MessageHelper.parseWithPermissions(commandExecutor, args[2])
        MessageHelper.sendMessage(commandExecutor, MessageHelper.parseString(plugin.mainConfig.lang.joinMessageChanged).append(permissionParsedMessage))
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