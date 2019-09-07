package vip.hsq168.plugin.flutter_easemob_example


import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant
import vip.hsq168.plugin.flutter_easemob.EasemobService
 



class Application: FlutterApplication(), PluginRegistry.PluginRegistrantCallback {
    override fun onCreate() {
        super.onCreate()
        EasemobService.setPluginRegistrant(this)


    }

    fun  miLog(){


    }

    override fun registerWith(registry: PluginRegistry) {
        GeneratedPluginRegistrant.registerWith(registry)
    }
    
    
}