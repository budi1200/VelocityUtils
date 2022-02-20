package si.budimir.velocityutils.util.transformations

import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.TransformationType

class CustomTransformationTypes {
    companion object {
        val BASIC_COLOR: TransformationType<BasicColorTransformation> = TransformationType.transformationType(
            { name: String? ->
                BasicColorTransformation.canParse(
                    name
                )
            },
            { name: String, args: MutableList<TagPart> ->
                BasicColorTransformation.create(
                    name,
                    args
                )
            })

        val HEX_COLOR = TransformationType.transformationType(
            { name: String? ->
                RGBColorTransformation.canParse(
                    name
                )
            },
            { name: String, args: MutableList<TagPart> ->
                RGBColorTransformation.create(
                    name,
                    args
                )
            })
    }
}