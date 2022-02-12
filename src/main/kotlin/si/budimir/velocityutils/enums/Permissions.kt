package si.budimir.velocityutils.enums

enum class Permissions(private val value: String) {
    HELP("command.help"),
    RELOAD("command.reload");

    fun getPermission(): String {
        return "velocityutils.$value"
    }
}