package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper

class ReloadCommand : SubCommandBase {
    private val plugin = VelocityUtilsMain.instance

    override fun execute(invocation: SimpleCommand.Invocation) {
        val reloadResults = hashMapOf<String, Boolean>()

        reloadResults["main.conf"] = plugin.mainConfigObj.reloadConfig()
        reloadResults["alias.conf"] = plugin.aliasConfigObj.reloadConfig()

        val failedReloads = reloadResults.filterValues { !it }
        if (failedReloads.isNotEmpty()) {
            MessageHelper.sendMessage(
                invocation.source(),
                "<red>Failed to reload configs! ${failedReloads.keys.joinToString(", ")}"
            )
            return
        }

        MessageHelper.reloadPrefix()
        MessageHelper.sendMessage(invocation.source(), "<green>Plugin Reloaded!")
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String> {
        return mutableListOf()
    }

    override fun getPermission(): String {
        return Permissions.RELOAD.getPermission()
    }

    override fun getDesc(): String {
        return "Reloads the plugin"
    }

    override fun canConsoleUse(): Boolean {
        return true
    }
}