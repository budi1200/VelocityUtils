package si.budimir.velocityutils.util.transformations

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.ParsingException
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver


@Suppress("UnstableApiUsage")
class RGBColorTagResolver : TagResolver, SerializableResolver.Single {
    companion object {
        private const val HEX = '#'
        private const val COLOR_3 = "c"
        private const val COLOR_2 = "colour"
        private const val COLOR = "color"
        val INSTANCE: TagResolver = RGBColorTagResolver()
        private val STYLE: StyleClaim<TextColor> = StyleClaim.claim(COLOR, Style::color) { color, emitter ->
            if (color is NamedTextColor) {
                emitter.tag(NamedTextColor.NAMES.key(color)!!)
            } else {
                emitter.tag(color.asHexString())
            }
        }

        private fun isColorOrAbbreviation(name: String): Boolean {
            return name == COLOR || name == COLOR_2 || name == COLOR_3
        }

        @Throws(ParsingException::class)
        fun resolveColor(colorName: String, ctx: Context): TextColor {
            val color: TextColor?
            color = if (colorName[0] == HEX) {
                TextColor.fromHexString(colorName)!!
            } else {
                null
            }

            if (color == null) {
                throw ctx.newException(
                    String.format(
                        "Unable to parse a color from '%s'. Please use named colours or hex (#RRGGBB) colors.",
                        colorName
                    )
                )
            }
            return color
        }
    }

    @Throws(ParsingException::class)
    override fun resolve(name: String, args: ArgumentQueue, ctx: Context): Tag? {
        if (!has(name)) {
            return null
        }
        val colorName: String
        colorName = if (isColorOrAbbreviation(name)) {
            args.popOr("Expected to find a color parameter: <name>|#RRGGBB").lowerValue()
        } else {
            name
        }
        val color = resolveColor(colorName, ctx)
        return Tag.styling(color)
    }

    override fun has(name: String): Boolean {
        return (isColorOrAbbreviation(name) || TextColor.fromHexString(name) != null)
    }

    override fun claimStyle(): StyleClaim<*> {
        return STYLE
    }
}