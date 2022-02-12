package si.budimir.velocityutils.config

import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.config.data.MainConfigData

class MainConfig(plugin: VelocityUtilsMain) :
    ConfigBase<MainConfigData>(plugin, "config.conf", MainConfigData::class.java)