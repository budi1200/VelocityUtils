package si.budimir.velocityutils.util.transformations

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.examination.ExaminableProperty
import java.util.*
import java.util.stream.Stream

class RGBColorTransformation(private val color: TextColor) : Transformation() {

    companion object {
        private fun isColorOrAbbreviation(name: String): Boolean {
            return name == Tokens.COLOR || name == Tokens.COLOR_2 || name == Tokens.COLOR_3
        }

        fun canParse(name: String?): Boolean {
            if (name == null) return false

            return (isColorOrAbbreviation(name) || TextColor.fromHexString(name) != null)
        }

        fun create(name: String, args: List<TagPart>): RGBColorTransformation {
            val colorName: String = if (isColorOrAbbreviation(name)) {
                if (args.size == 1) {
                    args[0].value().lowercase()
                } else {
                    throw ParsingException("Expected to find a color parameter, but found $args", args)
                }
            } else {
                name
            }

            val color: TextColor? = if (colorName[0] == '#') {
                TextColor.fromHexString(colorName)
            } else {
                null
            }

            if (color == null) {
                throw ParsingException("Don't know how to turn '$name' into a color", args)
            }

            return RGBColorTransformation(color)
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
        val that = other as RGBColorTransformation
        return color == that.color
    }

    override fun hashCode(): Int {
        return Objects.hash(color)
    }
}