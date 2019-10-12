package vip.hsq168.plugin.flutter_easemob

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.flutter.app.FlutterPluginRegistry
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterRunArguments
import vip.hsq168.plugin.flutter_easemob.utils.UserProfile
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


class EasemobService(var context: Context) : MethodChannel.MethodCallHandler {


    private lateinit var mBackgroundChannel: MethodChannel

    companion object {
        @JvmStatic
        private val TAG = "EasemobService"

//        @JvmStatic
//        private lateinit var sPluginRegistrantCallback: PluginRegistry.PluginRegistrantCallback
        @JvmStatic
        private var sBackgroundFlutterView: FlutterNativeView? = null
        @JvmStatic
        private val sServiceStarted = AtomicBoolean(false)


//        fun setPluginRegistrant(callback: PluginRegistry.PluginRegistrantCallback) {
//            sPluginRegistrantCallback = callback
//        }
    }


    fun startService() {
        synchronized(sServiceStarted) {
            if (sBackgroundFlutterView == null) {
                val callbackHandle = context.getSharedPreferences(
                        FlutterEasemobPlugin.SHARED_PREFERENCES_KEY,
                        Context.MODE_PRIVATE)
                        .getLong(FlutterEasemobPlugin.CALLBACK_DISPATCHER_HANDLE_KEY, 0)
                if (callbackHandle == 0L) {
                    Log.e(TAG, "Fatal: failed to find callbackHandle")
                    return
                }
                val callbackInfo = FlutterCallbackInformation.lookupCallbackInformation(callbackHandle)
                if (callbackInfo.callbackName==null) {
                    Log.e(TAG, "Fatal: failed to find callback")
                    return
                }
                Log.i(TAG, "Starting GeofencingService...")
                sBackgroundFlutterView = FlutterNativeView(context, true)
                // var registry = initRegistry ?: sBackgroundFlutterView!!.pluginRegistry
//                if (initRegistry == null)
//                    sPluginRegistrantCallback.registerWith(registry)
                val args = FlutterRunArguments()
                args.bundlePath = FlutterMain.findAppBundlePath(context)
                args.entrypoint = callbackInfo.callbackName
                args.libraryPath = callbackInfo.callbackLibraryPath

                sBackgroundFlutterView!!.runFromBundle(args)

            }
        }
        mBackgroundChannel = MethodChannel(sBackgroundFlutterView,
                "vip.hsq168.plugin.flutter_easemob/background")
        mBackgroundChannel.setMethodCallHandler(this)
    }

    private fun runOnUIThread(block: () -> Unit) {
        var mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post { block() }
    }

    private fun invokeMethod(method: String, arguments: Any?): Any? {
        var callback = context.getSharedPreferences(FlutterEasemobPlugin.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).getLong(method, 0)
        if (callback == 0L) {
            return null
        }
        val latch = CountDownLatch(1)
        var result: Any? = null
        runOnUIThread {
            mBackgroundChannel.invokeMethod("do", mapOf("callback" to callback.toString(), "arguments" to arguments), object : MethodChannel.Result {
                override fun notImplemented() {
                    latch.countDown()
                }

                override fun error(p0: String?, p1: String?, p2: Any?) {
                    latch.countDown()
                }

                override fun success(p: Any?) {
                    try {
                        result = p
                    } finally {
                        latch.countDown()
                    }
                }
            })
        }
        latch.await()
        return result
    }


    fun getUserProfile(userName: String): UserProfile {
        var profile = UserProfile()
        var result = invokeMethod("getUserInfo", userName)
        if (result != null && result is Map<*, *>) {
            var map = result as Map<String, String>
            profile.userName = map["userName"] ?: error("")
            profile.nickName = map["nickName"] ?: error("")
            profile.avater = map["avater"] ?: error("")
        }


        return profile
    }

    override fun onMethodCall(p0: MethodCall, p1: Result) {
         
    }
}