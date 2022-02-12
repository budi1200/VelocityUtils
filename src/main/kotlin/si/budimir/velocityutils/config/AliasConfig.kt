package si.budimir.velocityutils.config

import si.budimir.velocityutils.VelocityUtilsMain
import si.budimir.velocityutils.config.data.AliasConfigData

class AliasConfig(plugin: VelocityUtilsMain) :
    ConfigBase<AliasConfigData>(plugin, "alias.conf", AliasConfigData::class.java)