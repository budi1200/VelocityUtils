package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.commands.VelocityUtilsCommand
import si.budimir.velocityutils.config.data.CustomMessage
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper

class JoinMessageCommand(private val plugin: VelocityUtilsMain) : SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val commandExecutor = invocation.source() as Player
        val args = invocation.arguments()

        if (args.size < 2) return

        // Clear custom join message
        if (args[1] == "clear") {
            try {
                val tmp = plugin.customMessagesConfig
                tmp.customMessages[commandExecutor.uniqueId]?.customJoinMessage = null
                plugin.customMessagesConfigObj.saveConfig(tmp)

                MessageHelper.sendMessage(commandExecutor, plugin.mainConfig.lang.joinMessageCleared)
            } catch (e: Exception) {
             MessageHelper.sendMessage(commandExecutor, plugin.mainConfig.lang.errorOccurred)
            }

            return
        }

        // Get message from arguments
        val inputMessage = args.drop(2).joinToString(" ")

        // Set custom join message
        val customMessage = plugin.customMessagesConfig.customMessages[commandExecutor.uniqueId] ?: CustomMessage(null, null)
        val placeholders = hashMapOf("player" to commandExecutor.username)

        // Parse message with formatting permissions
        val permissionParsedMessage = MessageHelper.parseWithPermissions(commandExecutor, inputMessage, placeholders)
        customMessage.customJoinMessage = permissionParsedMessage

        // Attempt to save changed message
        try {
            val tmp = plugin.customMessagesConfig
            tmp.customMessages[commandExecutor.uniqueId] = customMessage

            plugin.customMessagesConfigObj.saveConfig(tmp)

            // Inform player
            MessageHelper.sendMessage(commandExecutor, MessageHelper.parseString(plugin.mainConfig.lang.joinMessageChanged).append(permissionParsedMessage))
        } catch (e: Exception) {
         MessageHelper.sendMessage(commandExecutor, plugin.mainConfig.lang.errorOccurred)
        }
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