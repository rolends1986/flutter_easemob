package vip.hsq168.plugin.flutter_easemob.push.mi

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log

import com.hyphenate.push.EMPushType
import com.hyphenate.push.EMPushHelper
import com.hyphenate.util.EMLog
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
//
//import com.xiaomi.mipush.sdk.*
//
//
//class MIMessageReceiver: PushMessageReceiver() {
//
//
//    override fun onNotificationMessageClicked(context: Context, message: MiPushMessage) {
//        Log.i("EMMiMsgReceiver", "onNotificationMessageClicked is called. $message")
//        val localIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
//        localIntent.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
//        context.startActivity(localIntent)
//
//
//    }
//
//    override fun onNotificationMessageArrived(context: Context, message: MiPushMessage) {
//        Log.i("EMMiMsgReceiver", "onNotificationMessageArrived is called. $message")
//    }
//
//
//    override fun onReceiveRegisterResult(context: Context, message: MiPushCommandMessage) {
//        val str1 = message.command
//        val localList = message.commandArguments
//        val str2 = if (localList != null && localList!!.size > 0) localList!!.get(0) else null
//        val str3 = if (localList != null && localList!!.size > 1) localList!!.get(1) else null
//        Log.i("EMMiMsgReceiver", "onReceiveRegisterResult. cmdArg1: $str2; cmdArg2:$str3")
//        if ("register" == str1) {
//            if (message.resultCode === 0L) {
//                EMPushHelper.getInstance().onReceiveToken(EMPushType.MIPUSH, str2)
//            } else {
//                EMPushHelper.getInstance().onErrorResponse(EMPushType.MIPUSH, message.resultCode)
//            }
//        }
//    }
//}