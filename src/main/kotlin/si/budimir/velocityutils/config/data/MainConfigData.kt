package si.budimir.velocityutils.config.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MainConfigData(
    val pluginPrefix: String = "<bold><blue>VelocityUtils »<reset> ",
    val defaultJoinMessage: String = "<yellow><player> joined the game",
    val defaultLeaveMessage: String = "<yellow><player> left the game",
    val lang: Lang = Lang()
)

@ConfigSerializable
data class Lang(
    val joinMessageCleared: String = "<green>Join message removed",
    val joinMessageChanged: String = "<white>Changed join message to: ",
    val leaveMessageCleared: String = "<green>Leave message removed",
    val leaveMessageChanged: String = "<white>Changed leave message to: ",
    val errorOccurred: String = "<white>An error occurred! Contact an admin",
    val missingPermission: String = "<red>Missing permission!",
    val invalidUsage: String = "<red>Invalid usage!"
)