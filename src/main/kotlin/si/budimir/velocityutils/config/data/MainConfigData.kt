package si.budimir.velocityutils.config.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfigData(
    val pluginPrefix: String = "<bold><blue>VelocityUtils »<reset> ",
    val lang: Lang = Lang()
)

@ConfigSerializable
data class Lang(
    val missingPermission: String = "<red>Missing permission!",
    val invalidUsage: String = "<red>Invalid usage!"
)