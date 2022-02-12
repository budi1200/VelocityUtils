package si.budimir.velocityutils

import com.velocitypowered.api.command.SimpleCommand

class AliasManager(private val plugin: VelocityUtilsMain) {
    // TODO: Maybe ingame command that lists all current active aliases
    private val registeredCommands: MutableList<String> = mutableListOf()

    fun registerCommands() {
        plugin.aliasConfig.aliases.forEach { alias ->
            // Build brigadier command
            val command = SimpleCommand { invocation ->
                val source = invocation.source()
                val args = invocation.arguments()

                val joinedArgs = args.joinToString(" ")

                val commandArgs = if (args.isNotEmpty()) {
                    joinedArgs.replace(alias.args.toRegex(), alias.commandArgs)
                } else {
                    ""
                }

                plugin.commandManager.executeAsync(source, "${alias.command} $commandArgs")
            }

            plugin.commandManager.register(alias.name, command)
            registeredCommands.add(alias.name)
            plugin.logger.info("Registered alias command \"${alias.name}\"")
        }
    }

    private fun unregisterCommands() {
        registeredCommands.forEach { alias: String ->
            plugin.commandManager.unregister(alias)
        }
        registeredCommands.clear()
    }

    fun reloadCommands() {
        unregisterCommands()
        registerCommands()
    }
}