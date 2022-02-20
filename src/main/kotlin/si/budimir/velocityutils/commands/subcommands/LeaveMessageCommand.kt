package si.budimir.velocityutils.commands.subcommands

import com.velocitypowered.api.command.SimpleCommand
import si.budimir.velocityutils.commands.SubCommandBase
import si.budimir.velocityutils.commands.VelocityUtilsCommand
import si.budimir.velocityutils.enums.Permissions

class LeaveMessageCommand : SubCommandBase {

    override fun execute(invocation: SimpleCommand.Invocation) {
        TODO("Not yet implemented")
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String> {
        return VelocityUtilsCommand.getJoinLeaveTabSuggestions(invocation.arguments())
    }

    override fun getPermission(): String {
        return Permissions.SET_CUSTOM_MESSAGES.getPermission()
    }

    override fun getDesc(): String {
        return "Set custom leave message"
    }

    override fun canConsoleUse(): Boolean {
        return false
    }
}