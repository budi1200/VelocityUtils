package si.budimir.velocityutils.util.transformations

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.examination.ExaminableProperty
import java.util.*
import java.util.stream.Stream

class BasicColorTransformation(private val color: TextColor) : Transformation() {

    companion object {
        private val COLOR_ALIASES: MutableMap<String, TextColor> =
            hashMapOf("dark_grey" to NamedTextColor.DARK_GRAY, "grey" to NamedTextColor.GRAY)

        private fun isColorOrAbbreviation(name: String): Boolean {
            return name == Tokens.COLOR || name == Tokens.COLOR_2 || name == Tokens.COLOR_3
        }

        fun canParse(name: String?): Boolean {
            if (name == null) return false

            return (isColorOrAbbreviation(name) || NamedTextColor.NAMES.value(name) != null || COLOR_ALIASES.containsKey(
                name
            ))
        }

        fun create(name: String, args: List<TagPart>): BasicColorTransformation {
            val colorName: String = if (isColorOrAbbreviation(name)) {
                if (args.size == 1) {
                    args[0].value().lowercase()
                } else {
                    throw ParsingException("Expected to find a color parameter, but found $args", args)
                }
            } else {
                name
            }

            val color: TextColor? = if (COLOR_ALIASES.containsKey(colorName)) {
                COLOR_ALIASES[colorName]
            } else {
                NamedTextColor.NAMES.value(colorName)
            }

            if (color == null) {
                throw ParsingException("Don't know how to turn '$name' into a color", args)
            }

            return BasicColorTransformation(color)
        }
    }

    override fun apply(): Component {
        return Component.empty().color(color)
    }

    override fun examinableProperties(): Stream<out ExaminableProperty> {
        return Stream.of(ExaminableProperty.of("color", color))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        val that = other as BasicColorTransformation
        return color == that.color
    }

    override fun hashCode(): Int {
        return Objects.hash(color)
    }
}