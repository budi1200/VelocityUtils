package si.budimir.velocityutils.config.data

import net.kyori.adventure.text.Component
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@ConfigSerializable
data class CustomMessagesData(
    val customMessages: HashMap<UUID, CustomMessage> = hashMapOf()
)

@ConfigSerializable
data class CustomMessage(
    var customJoinMessage: Component?,
    var customLeaveMessage: Component?
)