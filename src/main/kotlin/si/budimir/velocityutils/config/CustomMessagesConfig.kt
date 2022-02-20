package si.budimir.velocityutils.config

import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.config.data.CustomMessagesData

class CustomMessagesConfig(plugin: VelocityUtilsMain) :
    ConfigBase<CustomMessagesData>(plugin, "custom-messages.conf", CustomMessagesData::class.java)