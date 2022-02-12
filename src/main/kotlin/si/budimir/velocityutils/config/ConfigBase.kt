package si.budimir.velocityutils.config

import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.objectMapperFactory
import si.budimir.velocityutils.VelocityUtilsMain
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists


open class ConfigBase<T>(private val plugin: VelocityUtilsMain, fileName: String, private val clazz: Class<T>) {
    private val logger = plugin.logger
    private val dataDirectory = plugin.dataDirectory
    private val configPath = Path.of("$dataDirectory/$fileName")

    private val loader = HoconConfigurationLoader.builder()
        .path(configPath)
        .prettyPrinting(true)
        .defaultOptions { options ->
            options.shouldCopyDefaults(true)
            options.serializers { builder ->
                builder.registerAnnotatedObjects(objectMapperFactory())
            }
        }
        .build()

    private lateinit var root: CommentedConfigurationNode
    private var config: T? = null

    fun reloadConfig(): Boolean {
        root = try {
            loader.load()
        } catch (e: IOException) {
            logger.error("An error occurred while loading configuration: ${e.message}")
            if (e.cause != null) {
                e.cause!!.printStackTrace()
            }
            return false
        }

        val tmp = root.get(clazz)

        if (tmp == null) {
            logger.error("An error occurred while parsing configuration")
            return false
        }

        config = tmp

        return true
    }

    fun getConfig(): T {
        if (config != null) reloadConfig()
        return config!!
    }

    private fun saveDefaultConfig() {
        if (!dataDirectory.exists())
            dataDirectory.createDirectory();

        try {
            val node: CommentedConfigurationNode = loader.load()
            config = node[clazz]!!
            node[clazz] = config
            loader.save(node)
        } catch (exception: ConfigurateException) {
            logger.error("Could not load configuration: {}", exception.message)
        }
    }

    init {
        saveDefaultConfig()
    }
}