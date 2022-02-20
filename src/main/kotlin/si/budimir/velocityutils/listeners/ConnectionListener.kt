package si.budimir.velocityutils.listeners

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent.DisconnectPlayer
import com.velocitypowered.api.event.player.ServerPostConnectEvent
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.audience.Audience
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.MessageHelper
import java.util.*

@Suppress("UnstableApiUsage")
class ConnectionListener(private val plugin: VelocityUtilsMain) {
    private val connectedPlayers = hashMapOf<UUID, Boolean>()

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerConnected(event: ServerPostConnectEvent) {

        // Exit if player changed servers
        if (event.previousServer != null) return

        val player = event.player
        // Workaround to not broadcast random leave messages
        connectedPlayers[player.uniqueId] = true

        // Do not broadcast players with silent permission
        if (player.hasPermission(Permissions.SILENT.getPermission())) return

        // Custom message handler
        var message = plugin.mainConfig.defaultJoinMessage

        if (player.hasPermission(Permissions.USE_CUSTOM_MESSAGES.getPermission()))
            message = plugin.customMessagesConfig.customMessages[player.uniqueId]?.customJoinMessage ?: message

        // Message stuff
        val audience = Audience.audience(plugin.server.allPlayers)
        val placeholders = hashMapOf("player" to event.player.username)
        MessageHelper.sendMessage(audience, message, placeholders, false)
    }

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerKicked(event: KickedFromServerEvent) {
        if (event.result !is DisconnectPlayer) return

        handleDisconnect(event.player)
    }

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerDisconnected(event: DisconnectEvent) {
        if (event.loginStatus != DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) return

        handleDisconnect(event.player)
    }

    private fun handleDisconnect(player: Player) {
        // Workaround to not broadcast random leave messages
        if (connectedPlayers[player.uniqueId] == null) return
        connectedPlayers.remove(player.uniqueId)

        // Do not broadcast players with silent permission
        if (player.hasPermission(Permissions.SILENT.getPermission())) return

        // Custom message handler
        var message = plugin.mainConfig.defaultJoinMessage

        if (player.hasPermission(Permissions.USE_CUSTOM_MESSAGES.getPermission()))
            message = plugin.customMessagesConfig.customMessages[player.uniqueId]?.customLeaveMessage ?: message

        // Message stuff
        val audience = Audience.audience(plugin.server.allPlayers)
        val placeholders = hashMapOf("player" to player.username)
        MessageHelper.sendMessage(audience, message, placeholders, false)
    }
}