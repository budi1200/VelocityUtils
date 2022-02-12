package si.budimir.velocityutils.config.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class AliasConfigData(
    val aliases: ArrayList<AliasEntry> = arrayListOf(AliasEntry("switchserver", "(\\S*)", "server", "$1"))
)

// TODO: Require permission option
@ConfigSerializable
data class AliasEntry(
    val name: String,
    val args: String,
    val command: String,
    val commandArgs: String
)