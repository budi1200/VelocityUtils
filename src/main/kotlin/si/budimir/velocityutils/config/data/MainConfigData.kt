package si.budimir.velocityutils.config.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfigData(
    val pluginPrefix: String = "<bold><blue>VelocityUtils »<reset> ",
)