package si.budimir.velocityutils.util

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.template.TemplateResolver
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry
import net.kyori.adventure.text.minimessage.transformation.TransformationType
import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.enums.Permissions
import si.budimir.velocityutils.util.transformations.CustomTransformationTypes


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
            audience: ForwardingAudience,
            rawMessage: String,
            placeholders: Map<String, String>,
            prefix: Boolean = true
        ) {
            var output = Component.text("")

            // Add prefix if required
            if (prefix)
                output = output.append(pluginPrefix)

            output = output.append(parseString(rawMessage, placeholders))

            audience.sendMessage(output)
        }

        fun parseString(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            val resolver = TemplateResolver.pairs(placeholders)

            return Component
                .text("")
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    miniMessage.deserialize(key, resolver)
                )
        }

        fun getParsedString(key: String, placeholders: Map<String, String> = hashMapOf()): Component {
            val resolver = TemplateResolver.pairs(placeholders)

            return Component
                .text("")
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    miniMessage.deserialize(key, resolver)
                )
        }

        fun parseWithPermissions(
            player: Player,
            key: String,
            placeholders: Map<String, String> = hashMapOf()
        ): Component {
            val miniMessageBuilder = MiniMessage.builder()

            val allowedTransformations = TransformationRegistry.builder().clear()

            if (player.hasPermission(Permissions.USE_VANILLA_COLOR.getPermission())) {
                plugin.logger.info("Player has ${Permissions.USE_VANILLA_COLOR.getPermission()}")
                allowedTransformations.add(CustomTransformationTypes.BASIC_COLOR)
            }

            if (player.hasPermission(Permissions.USE_HEX_COLOR.getPermission())) {
                plugin.logger.info("Player has ${Permissions.USE_HEX_COLOR.getPermission()}")
                allowedTransformations.add(CustomTransformationTypes.HEX_COLOR)
            }

            if (player.hasPermission(Permissions.USE_GRADIENT_COLOR.getPermission())) {
                plugin.logger.info("Player has ${Permissions.USE_GRADIENT_COLOR.getPermission()}")
                allowedTransformations.add(TransformationType.GRADIENT)
            }

            val gatedMiniMessage = miniMessageBuilder.transformations(allowedTransformations.build()).build()
            val resolver = TemplateResolver.pairs(placeholders)

            return gatedMiniMessage.deserialize(key, resolver)
        }
    }
}
