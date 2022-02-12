package si.budimir.velocityutils

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import si.budimir.velocityutils.config.MainConfig
import si.budimir.velocityutils.config.data.MainConfigData
import si.budimir.velocityutils.enums.Constants
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
    private val startTime = Instant.now()

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

    companion object {
        lateinit var instance: VelocityUtilsMain
    }

    init {
        instance = this
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {

        // Config
        mainConfigObj = MainConfig(this)
        mainConfig = mainConfigObj.getConfig()

        val loadTime = Duration.between(startTime, Instant.now())
        logger.info("VelocityUtils loaded (took ${loadTime.toMillis()}ms)")
    }
}