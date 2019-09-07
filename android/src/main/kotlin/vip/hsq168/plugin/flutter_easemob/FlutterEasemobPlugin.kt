package vip.hsq168.plugin.flutter_easemob

import com.hyphenate.chat.*
import com.hyphenate.push.EMPushConfig
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.hyphenate.push.EMPushType
import com.hyphenate.util.EMLog
import com.hyphenate.push.PushListener
import com.hyphenate.push.EMPushHelper
import com.hyphenate.chat.EMClient
import com.hyphenate.EMCallBack
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.hyphenate.chat.EMMessage
import com.hyphenate.EMMessageListener
import com.hyphenate.helpdesk.easeui.UIProvider
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.easeui.Notifier
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.hyphenate.helpdesk.model.VisitorInfo
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.MiPushClient
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

import java.util.*


class FlutterEasemobPlugin(var registrar: Registrar) : MethodCallHandler {

    companion object {
        @JvmStatic
        private val sCacheLock = Object()
        @JvmStatic
        val SHARED_PREFERENCES_KEY = "fullter_easemob_plugin_cache"
        @JvmStatic
        val CALLBACK_DISPATCHER_HANDLE_KEY = "fullter_easemob_callback_dispatch_handler"

        val msg_ch = arrayOf("发来一条消息", "发来一张图片", "发来一段语音", "发来位置信息", "发来一个视频", "发来一个文件", "%1个联系人发来%2条消息")
        private var channel: MethodChannel? = null

        @JvmStatic
        fun registerWith(registrar: Registrar) {

            com.xiaomi.mipush.sdk.Logger.setLogger(registrar.context(), object : LoggerInterface {
                override fun setTag(p0: String?) {

                }

                override fun log(content: String?) {
                    Log.d("MIPUSH", content)
                }

                override fun log(content: String?, t: Throwable?) {
                    Log.d("MIPUSH", content, t)
                }
            })


            var regId = MiPushClient.getRegId(registrar.activity().application)
            Log.d("MIPUSH", "regId:${regId}")
            //  MiPushClient.registerPush(registrar.context(), "2882303761518149924", "5291814984924")

            channel = MethodChannel(registrar.messenger(), "vip.hsq168.plugin.flutter_easemob")
            channel!!.setMethodCallHandler(FlutterEasemobPlugin(registrar))


        }


    }


    var builder: EMPushConfig.Builder = EMPushConfig.Builder(registrar.context())
    var service: EasemobService = EasemobService(registrar.context())

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "enableVivoPush" -> {
                builder.enableVivoPush()
            }
            "enableMeiZuPush" -> {
                builder.enableMeiZuPush(call.argument("appId"), call.argument("appKey"))
            }
            "enableMiPush" -> {
                builder.enableMiPush(call.argument("appId"), call.argument("appKey"))
            }
            "enableOppoPush" -> {
                builder.enableOppoPush(call.argument("appKey"), call.argument("appSecret"))
            }
            "enableHWPush" -> {
                builder.enableHWPush()
            }
            "init" -> {
                init(call.argument("appKey"), call.argument("tenantId"), call.argument("callback"))
            }
            "setDebugMode" -> {
                EMClient.getInstance().setDebugMode(call.arguments())
            }
            "login" -> {
                login(call.argument("userName"), call.argument("password"))
            }
            "loadAllConversations" -> {
                EMClient.getInstance().chatManager().loadAllConversations()
            }
            "getUnreadMsgCount" -> {
                val conversation = EMClient.getInstance().chatManager().getConversation(call.argument("userName"))
                result.success(conversation.unreadMsgCount)
            }
            "startServiceIM" -> {
                var intent = IntentBuilder(registrar.context())
                        .setServiceIMNumber(call.argument("serviceNumber")) //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
                        .setTitleName("在线客服")
                        .setShowUserNick(true).setVisitorInfo(VisitorInfo().name("老王"))
                        .build()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                registrar.context().startActivity(intent)
            }
            "setHandler" -> {
                setHandler(call.argument<String>("name"), call.argument<Long>("callback"))
            }
        }

    }

    private fun setHandler(name: String?, callbackHandler: Long?) {
        synchronized(sCacheLock) {
            registrar.context().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(name, callbackHandler!!)
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

    private fun checkPermission(block: (Boolean) -> Unit) {
        var permissions = arrayOf(android.Manifest.permission.READ_PHONE_STATE)
        if (EasyPermissions.hasPermissions(registrar.activity(), *permissions)) {
            block(true)
        } else {
            var requestCode = UUID.randomUUID().mostSignificantBits.toInt()
            var deniedResult = arrayOf(PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED)
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, deniedResult.toIntArray(), object : EasyPermissions.PermissionCallbacks {
                override fun onPermissionsDenied(rc: Int, perms: MutableList<String>) {
                    if (requestCode == rc)
                        block(false)
                }

                override fun onPermissionsGranted(rc: Int, perms: MutableList<String>) {
                    if (requestCode == rc)
                        block(true)
                }


                override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

                }
            })


            var request = PermissionRequest.Builder(registrar.activity(), requestCode, *permissions)
                    .setPositiveButtonText(android.R.string.ok)
                    .setNegativeButtonText(android.R.string.cancel)
                    .setRationale(android.R.string.unknownName)
                    .setTheme(R.style.Theme_AppCompat)
                    .build()
            EasyPermissions.requestPermissions(request)


        }
    }

    private fun init(appkey: String?, tenantId: String?, callback: Long?) {
        doInit(true, appkey, tenantId, callback)
    }

    private fun doInit(hasNotify: Boolean, appkey: String?, tenantId: String?, callback: Long?) {


        synchronized(sCacheLock) {
            registrar.context().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(CALLBACK_DISPATCHER_HANDLE_KEY, callback!!)
                    .apply()
        }
        service.startService()
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
        EMClient.getInstance().init(registrar.context(), options)

        val chatOptions = ChatClient.Options()
        chatOptions.setAppkey(appkey)//必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
        chatOptions.setTenantId(tenantId)//必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”

        // Kefu SDK 初始化
        if (!ChatClient.getInstance().init(registrar.context(), chatOptions)) {
            return
        }
        // Kefu EaseUI的初始化
        UIProvider.getInstance().init(registrar.context())
        //修改通知
        var providerClass = UIProvider::class.java
        var notifierField = providerClass.getDeclaredField("notifier")
        notifierField.isAccessible = true
        var notifier = vip.hsq168.plugin.flutter_easemob.infrastructure.Notifier(service)
        notifier.init(registrar.context())
        notifierField.set(UIProvider.getInstance(), notifier)

        UIProvider.getInstance().setUserProfileProvider(vip.hsq168.plugin.flutter_easemob.infrastructure.UserProfileProvider(registrar.context(), service))



        EMPushHelper.getInstance().setPushListener(
                object : PushListener() {
                    override fun onError(pushType: EMPushType, errorCode: Long) {
                        EMLog.e("PushClient", "Push client occur a error: $pushType - $errorCode")
                    }

                    override fun isSupportPush(pushType: EMPushType, pushConfig: EMPushConfig?): Boolean {
                        return if (hasNotify) super.isSupportPush(pushType, pushConfig) else false
                    }
                })

        ChatClient.getInstance().addConnectionListener(
                object : ChatClient.ConnectionListener {
                    override fun onConnected() {
                        //成功连接到服务器
                    }

                    override fun onDisconnected(errorcode: Int) {
                        //errorcode的值
                        //Error.USER_REMOVED 账号移除
                        //Error.USER_LOGIN_ANOTHER_DEVICE 账号在其他地方登录
                        //Error.USER_AUTHENTICATION_FAILED 账号密码错误
                        //Error.USER_NOT_FOUND  账号找不到

                    }
                })

        ChatClient.getInstance().chatManager().addMessageListener(
                object : ChatManager.MessageListener {
                    override fun onMessage(list: List<Message>) {
                        //收到普通消息
                    }

                    override fun onCmdMessage(list: List<Message>) {
                        //收到命令消息，命令消息不存数据库，一般用来作为系统通知，例如留言评论更新，
                        //会话被客服接入，被转接，被关闭提醒
                    }

                    override fun onMessageStatusUpdate() {
                        //消息的状态修改，一般可以用来刷新列表，显示最新的状态
                    }

                    override fun onMessageSent() {
                        //发送消息后，会调用，可以在此刷新列表，显示最新的消息
                    }
                })

        EMClient.getInstance().chatManager().addMessageListener(
                object : EMMessageListener {
                    override fun onMessageReceived(messages: List<EMMessage>) {
                        //收到消息
                    }

                    override fun onCmdMessageReceived(messages: List<EMMessage>) {
                        //收到透传消息
                    }

                    override fun onMessageRead(messages: List<EMMessage>) {
                        //收到已读回执
                    }

                    override fun onMessageDelivered(message: List<EMMessage>) {
                        //收到已送达回执
                    }

                    override fun onMessageRecalled(messages: List<EMMessage>) {
                        //消息被撤回
                    }

                    override fun onMessageChanged(message: EMMessage, change: Any) {
                        //消息状态变动
                    }
                })


    }
}
