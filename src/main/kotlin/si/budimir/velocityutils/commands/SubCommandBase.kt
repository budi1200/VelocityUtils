package si.budimir.velocityutils.commands

import com.velocitypowered.api.command.SimpleCommand

interface SubCommandBase {
    fun execute(invocation: SimpleCommand.Invocation)

    fun suggestAsync(invocation: SimpleCommand.Invocation): MutableList<String>

    fun getPermission(): String

    fun getDesc(): String

    fun canConsoleUse(): Boolean
}