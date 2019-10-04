package vip.hsq168.plugin.flutter_easemob

import android.app.Application
import com.hyphenate.chat.*
import com.hyphenate.push.EMPushConfig
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.hyphenate.push.EMPushHelper
import com.hyphenate.chat.EMClient
import com.hyphenate.EMCallBack
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.hyphenate.chat.EMMessage
import com.hyphenate.EMMessageListener
import com.hyphenate.helpdesk.easeui.UIProvider
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.hyphenate.helpdesk.model.VisitorInfo

import io.flutter.plugin.common.JSONUtil
import org.json.JSONObject


//import com.xiaomi.channel.commonutils.logger.LoggerInterface
//import com.xiaomi.mipush.sdk.MiPushClient
//import pub.devrel.easypermissions.EasyPermissions
//import pub.devrel.easypermissions.PermissionRequest

import java.util.*
import java.util.concurrent.CountDownLatch


class FlutterEasemobPlugin(var registrar: Registrar) : MethodCallHandler {

    companion object {
        @JvmStatic
        private val sCacheLock = Object()
        @JvmStatic
        val SHARED_PREFERENCES_KEY = "fullter_easemob_plugin_cache"
        @JvmStatic
        val CALLBACK_DISPATCHER_HANDLE_KEY = "fullter_easemob_callback_dispatch_handler"
        private var channel: MethodChannel? = null

        @JvmStatic
        fun registerWith(registrar: Registrar) {

//            com.xiaomi.mipush.sdk.Logger.setLogger(registrar.context(), object : LoggerInterface {
//                override fun setTag(p0: String?) {
//
//                }
//
//                override fun log(content: String?) {
//                    Log.d("MIPUSH", content)
//                }
//
//                override fun log(content: String?, t: Throwable?) {
//                    Log.d("MIPUSH", content, t)
//                }
//            })
//
//
//            var regId = MiPushClient.getRegId(registrar.activity().application)
//            Log.d("MIPUSH", "regId:${regId}")
            //  MiPushClient.registerPush(registrar.context(), "2882303761518149924", "5291814984924")

            channel = MethodChannel(registrar.messenger(), "vip.hsq168.plugin.flutter_easemob")
            channel!!.setMethodCallHandler(FlutterEasemobPlugin(registrar))


        }


        fun init(application: Application, appkey: String, tenantId: String) {

            var builder: EMPushConfig.Builder = EMPushConfig.Builder(application)

            var options = EMOptions()
            options.pushConfig = builder.build()
            // 默认添加好友时，是不需要验证的，改成需要验证
            options.acceptInvitationAlways = false
            // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
            options.autoTransferMessageAttachments = true
            // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
            options.setAutoDownloadThumbnail(true)
            options.autoLogin = true
            //初始化
            EMClient.getInstance().init(application, options)

            val chatOptions = ChatClient.Options()
            chatOptions.setAppkey(appkey)//必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
            chatOptions.setTenantId(tenantId)//必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”

            // Kefu SDK 初始化
            if (!ChatClient.getInstance().init(application, chatOptions)) {
                return
            }
            // Kefu EaseUI的初始化
            UIProvider.getInstance().init(application)
            //修改通知
//            var providerClass = UIProvider::class.java
//            var notifierField = providerClass.getDeclaredField("notifier")
//            notifierField.isAccessible = true
//            var notifier = vip.hsq168.plugin.flutter_easemob.infrastructure.Notifier(service)
//            notifier.init(application)
//            notifierField.set(UIProvider.getInstance(), notifier)

            UIProvider.getInstance().userProfileProvider = vip.hsq168.plugin.flutter_easemob.infrastructure.UserProfileProvider(application)
//            EMPushHelper.getInstance().setPushListener(vip.hsq168.plugin.flutter_easemob.infrastructure.PushListener(hasNotify))
            ChatClient.getInstance().addConnectionListener(vip.hsq168.plugin.flutter_easemob.infrastructure.ConnectionListener())
            ChatClient.getInstance().chatManager().addMessageListener(vip.hsq168.plugin.flutter_easemob.infrastructure.ChatClientMessageListener())
            EMClient.getInstance().chatManager().addMessageListener(vip.hsq168.plugin.flutter_easemob.infrastructure.EMClientMessageListener())
        }


    }


    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "login" -> {
                login(call.argument("userName"), call.argument("password"))
                result.success(true)
            }
            "loadAllConversations" -> {
                EMClient.getInstance().chatManager().loadAllConversations()
                result.success(true)
            }
            "getAllMessages" -> {
                var conversation = EMClient.getInstance().chatManager().getConversation(call.argument("userName"))
                var messages = conversation.allMessages
                var convertedMsgs: List<Map<String, Any>> = messages.map {
                    mapOf(
                            "from" to it.from,
                            "to" to it.to,
                            "conversationId" to it.conversationId(),
                            "msgTime" to it.msgTime,
                            "body" to it.body.toString(),
                            "isAcked" to it.isAcked,
                            "isUnread" to it.isUnread,
                            "isDelivered" to it.isDelivered
                    )
                }
                result.success(convertedMsgs)
            }
            "getUnreadMsgCount" -> {
                val conversation = EMClient.getInstance().chatManager().getConversation(call.argument("userName"))
                result.success(conversation.unreadMsgCount)

            }
            "startServiceIM" -> {
                var canLogin = true
                if (!ChatClient.getInstance().isLoggedInBefore) {
                    val latch = CountDownLatch(1)
                    var userName = "guest_${Random().nextLong()}"
                    ChatClient.getInstance().register(userName, userName, object : com.hyphenate.helpdesk.callback.Callback {
                        override fun onSuccess() {
                            latch.countDown()
                        }

                        override fun onProgress(p0: Int, p1: String?) {
                            latch.countDown()
                        }

                        override fun onError(p0: Int, p1: String?) {
                            canLogin = false
                            latch.countDown()
                        }
                    })
                    latch.await()
                    if (canLogin) {
                        val latch2 = CountDownLatch(1)
                        ChatClient.getInstance().login(userName, userName, object : com.hyphenate.helpdesk.callback.Callback {
                            override fun onSuccess() {
                                latch2.countDown()
                            }

                            override fun onProgress(p0: Int, p1: String?) {
                                latch2.countDown()
                            }

                            override fun onError(p0: Int, p1: String?) {
                                canLogin = false
                                latch2.countDown()
                            }
                        })
                        latch2.await()
                    }
                }
                if (canLogin) {
                    var intent = IntentBuilder(registrar.context())
                            .setServiceIMNumber(call.argument("serviceNumber")) //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
                            .setTitleName("在线客服")
                            .setShowUserNick(true).setVisitorInfo(VisitorInfo().name("老王"))
                            .build()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    registrar.context().startActivity(intent)
                    result.success(true)
                } else {
                    Toast.makeText(registrar.context(), "开启客服失败", LENGTH_SHORT)
                }
            }
            "setHandler" -> {
                setHandler(call.argument<String>("name"), call.argument<String>("callback"))
                result.success(true)
            }
        }

    }

    private fun setHandler(name: String?, callbackHandler: String?) {
        synchronized(sCacheLock) {
            registrar.context().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(name, callbackHandler!!.toLong())
                    .apply()
        }
    }

    private fun login(userName: String?, password: String?) {
        EMClient.getInstance().login(userName, password, object : EMCallBack {
            //回调
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
            }

            override fun onProgress(progress: Int, status: String) {

            }

            override fun onError(code: Int, message: String) {

            }
        })
    }

//    private fun checkPermission(block: (Boolean) -> Unit) {
//        var permissions = arrayOf(android.Manifest.permission.READ_PHONE_STATE)
//        if (EasyPermissions.hasPermissions(registrar.activity(), *permissions)) {
//            block(true)
//        } else {
//            var requestCode = UUID.randomUUID().mostSignificantBits.toInt()
//            var deniedResult = arrayOf(PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED)
//            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, deniedResult.toIntArray(), object : EasyPermissions.PermissionCallbacks {
//                override fun onPermissionsDenied(rc: Int, perms: MutableList<String>) {
//                    if (requestCode == rc)
//                        block(false)
//                }
//
//                override fun onPermissionsGranted(rc: Int, perms: MutableList<String>) {
//                    if (requestCode == rc)
//                        block(true)
//                }
//
//
//                override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//                }
//            })
//
//
//            var request = PermissionRequest.Builder(registrar.activity(), requestCode, *permissions)
//                    .setPositiveButtonText(android.R.string.ok)
//                    .setNegativeButtonText(android.R.string.cancel)
//                    .setRationale(android.R.string.unknownName)
//                    .setTheme(R.style.Theme_AppCompat)
//                    .build()
//            EasyPermissions.requestPermissions(request)
//
//
//        }
//    }

    private fun init(appkey: String?, tenantId: String?, callback: Long?) {
        //不在支持推送,推送由mob部分处理
        doInit(false, appkey, tenantId, callback)
    }

    private fun doInit(hasNotify: Boolean, appkey: String?, tenantId: String?, callback: Long?) {
        synchronized(sCacheLock) {
            registrar.context().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(CALLBACK_DISPATCHER_HANDLE_KEY, callback!!)
                    .apply()
        }
        //  service.startService()

    }
}
