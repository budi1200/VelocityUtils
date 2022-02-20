package si.budimir.velocityutils.enums

enum class Permissions(private val value: String) {
    HELP("command.help"),
    RELOAD("command.reload"),
    SILENT("silent"),
    USE_VANILLA_COLOR("color.vanilla"),
    USE_HEX_COLOR("color.hex"),
    USE_GRADIENT_COLOR("color.gradient"),
    USE_FORMATTING("color.formatting"),
    USE_CUSTOM_MESSAGES("messages.use"),
    SET_CUSTOM_MESSAGES("messages.set");

    fun getPermission(): String {
        return "velocityutils.$value"
    }
}