package si.budimir.velocityutils.util.transformations

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.ParsingException
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

@Suppress("UnstableApiUsage")
internal class BasicColorTagResolver : TagResolver, SerializableResolver.Single {

    companion object {
        private const val COLOR_3 = "c"
        private const val COLOR_2 = "colour"
        private const val COLOR = "color"
        val INSTANCE: TagResolver = BasicColorTagResolver()

        private val STYLE: StyleClaim<TextColor> = StyleClaim.claim(COLOR, Style::color) { color, emitter ->
            if (color is NamedTextColor) {
                emitter.tag(NamedTextColor.NAMES.key(color)!!)
            } else {
                emitter.tag(color.asHexString())
            }
        }

        private val COLOR_ALIASES: MutableMap<String, TextColor> = HashMap()

        init {
            COLOR_ALIASES["dark_grey"] = NamedTextColor.DARK_GRAY
            COLOR_ALIASES["grey"] = NamedTextColor.GRAY
        }

        private fun isColorOrAbbreviation(name: String): Boolean {
            return name == COLOR || name == COLOR_2 || name == COLOR_3
        }

        @Throws(ParsingException::class)
        fun resolveColor(colorName: String, ctx: Context): TextColor {
            val color: TextColor
            color = if (COLOR_ALIASES.containsKey(colorName)) {
                COLOR_ALIASES[colorName]!!
            } else {
                NamedTextColor.NAMES.value(colorName)!!
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
        return (isColorOrAbbreviation(name)
                || NamedTextColor.NAMES.value(name) != null || COLOR_ALIASES.containsKey(
            name
        ))
    }

    override fun claimStyle(): StyleClaim<*> {
        return STYLE
    }
}