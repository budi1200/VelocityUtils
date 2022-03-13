package si.budimir.velocityutils.util

import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.transformations.BasicColorTagResolver
import si.budimir.velocityutils.util.transformations.RGBColorTagResolver


abstract class MessageHelper {
    companion object {
        private lateinit var plugin: VelocityUtilsMain
        private lateinit var pluginPrefix: Component
        private val miniMessage = MiniMessage.builder().build()

        fun load(plugin: VelocityUtilsMain) {
            this.plugin = plugin
            pluginPrefix = parseString(plugin.mainConfig.pluginPrefix)
        }

        fun reloadPrefix() {
            pluginPrefix = parseString(plugin.mainConfig.pluginPrefix)
        }

        fun sendMessage(
            commandSource: CommandSource,
            rawMessage: String,
            placeholders: MutableMap<String, String> = hashMapOf(),
            prefix: Boolean = true
        ) {
            var output = Component.text("")

            // Add prefix if required
            if (prefix)
                output = output.append(pluginPrefix)

            output = output.append(parseString(rawMessage, placeholders))

            commandSource.sendMessage(output)
        }

        fun sendMessage(
            commandSource: CommandSource,
            component: Component,
            prefix: Boolean = true
        ) {
            var output = Component.text("")

            // Add prefix if required
            if (prefix)
                output = output.append(pluginPrefix)

            output = output.append(component)

            commandSource.sendMessage(output)
        }

        fun sendMessage(
            audience: ForwardingAudience,
            rawMessage: Component,
            prefix: Boolean = true
        ) {
            var output = Component.text("")

            // Add prefix if required
            if (prefix)
                output = output.append(pluginPrefix)

            output = output.append(rawMessage)

            audience.sendMessage(output)
        }

        fun parseString(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            val resolver = TagResolver.resolver(placeholders.map { Placeholder.parsed(it.key, it.value) })

            return Component
                .text("")
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    miniMessage.deserialize(key, resolver)
                )
        }

        fun parseWithPermissions(
            player: CommandSource,
            key: String,
            placeholders: Map<String, String> = hashMapOf()
        ): Component {
            val miniMessageBuilder = MiniMessage.builder()

            val allowedTags = TagResolver.builder()

            if (player.hasPermission(Permissions.USE_VANILLA_COLOR.getPermission())) {
//                plugin.logger.info("Player has ${Permissions.USE_VANILLA_COLOR.getPermission()}")
                allowedTags.resolver(BasicColorTagResolver.INSTANCE)
            }

            if (player.hasPermission(Permissions.USE_HEX_COLOR.getPermission())) {
//                plugin.logger.info("Player has ${Permissions.USE_HEX_COLOR.getPermission()}")
                allowedTags.resolver(RGBColorTagResolver.INSTANCE)
            }

            if (player.hasPermission(Permissions.USE_GRADIENT_COLOR.getPermission())) {
//                plugin.logger.info("Player has ${Permissions.USE_GRADIENT_COLOR.getPermission()}")
                allowedTags.resolver(StandardTags.gradient())
            }

            if (player.hasPermission(Permissions.USE_FORMATTING.getPermission())) {
//                plugin.logger.info("Player has ${Permissions.USE_FORMATTING.getPermission()}")
                allowedTags.resolver(StandardTags.decorations(TextDecoration.BOLD))
                allowedTags.resolver(StandardTags.decorations(TextDecoration.ITALIC))
                allowedTags.resolver(StandardTags.decorations(TextDecoration.UNDERLINED))
                allowedTags.resolver(StandardTags.decorations(TextDecoration.STRIKETHROUGH))
            }

            val gatedMiniMessage = miniMessageBuilder.tags(allowedTags.build()).build()
            plugin.logger.info(placeholders.toString())
            val resolver = TagResolver.resolver(placeholders.map { Placeholder.parsed(it.key, it.value) })

            return gatedMiniMessage.deserialize(key, resolver)
        }
    }
}
