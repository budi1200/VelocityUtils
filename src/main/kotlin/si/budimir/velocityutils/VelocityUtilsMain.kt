package si.budimir.velocityutils

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import si.budimir.velocityutils.commands.VelocityUtilsCommand
import si.budimir.velocityutils.config.AliasConfig
import si.budimir.velocityutils.config.MainConfig
import si.budimir.velocityutils.config.data.AliasConfigData
import si.budimir.velocityutils.config.data.MainConfigData
import si.budimir.velocityutils.enums.Constants
import si.budimir.velocityutils.util.MessageHelper
import java.nio.file.Path
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Plugin(
    id = "velocityutils",
    name = "VelocityUtils",
    version = Constants.VERSION,
    description = "A collection of useful features for Velocity proxy",
    authors = ["budi1200"]
)
class VelocityUtilsMain {
    @Inject
    lateinit var server: ProxyServer

    @Inject
    lateinit var logger: Logger

    @Inject
    @DataDirectory
    lateinit var dataDirectory: Path

    @Inject
    lateinit var commandManager: CommandManager

    // Config
    lateinit var mainConfigObj: MainConfig
    lateinit var mainConfig: MainConfigData
    lateinit var aliasConfigObj: AliasConfig
    lateinit var aliasConfig: AliasConfigData

    // Alias Manager
    lateinit var aliasManager: AliasManager

    // Commands
    lateinit var velocityUtilsCommand: VelocityUtilsCommand

    companion object {
        lateinit var instance: VelocityUtilsMain
    }

    init {
        instance = this
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
        val startTime = Instant.now()

        // Main Config
        mainConfigObj = MainConfig(this)
        mainConfig = mainConfigObj.getConfig()

        // Load message helper
        MessageHelper.load(this)

        // Alias Config
        aliasConfigObj = AliasConfig(this)
        aliasConfig = aliasConfigObj.getConfig()

        // Init alias manager
        aliasManager = AliasManager(this)
        aliasManager.registerCommands()

        // Register command(s)
        velocityUtilsCommand = VelocityUtilsCommand(this)
        commandManager.register("vutils", velocityUtilsCommand)

        val loadTime = Duration.between(startTime, Instant.now())
        logger.info("VelocityUtils loaded (took ${loadTime.toMillis()}ms)")
    }
}