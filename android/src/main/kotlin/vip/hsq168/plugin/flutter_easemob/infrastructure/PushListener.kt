package vip.hsq168.plugin.flutter_easemob.infrastructure

import android.util.Log
import com.hyphenate.push.EMPushConfig
import com.hyphenate.push.EMPushType
import com.hyphenate.util.EMLog

class PushListener(var hasNotify: Boolean) : com.hyphenate.push.PushListener() {
    override fun onError(pushType: EMPushType?, errorCode: Long) {
        Log.e("PushClient", "Push client occur a error: $pushType - $errorCode")
    }

    override fun isSupportPush(p0: EMPushType?, p1: EMPushConfig?): Boolean {
        return if (hasNotify) super.isSupportPush(p0, p1) else hasNotify
    }
}