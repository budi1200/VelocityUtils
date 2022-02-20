package si.budimir.velocityutils.config.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@ConfigSerializable
data class CustomMessagesData(
    val customMessages: HashMap<UUID, CustomMessage> = hashMapOf()
)

@ConfigSerializable
data class CustomMessage(
    val customJoinMessage: String?,
    val customLeaveMessage: String?
)