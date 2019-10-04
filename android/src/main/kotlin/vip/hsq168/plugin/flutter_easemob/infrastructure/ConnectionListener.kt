package vip.hsq168.plugin.flutter_easemob.infrastructure

import android.util.Log
import com.hyphenate.chat.ChatClient

class ConnectionListener : ChatClient.ConnectionListener {
    override fun onConnected() {
        Log.d("ChatClient", "聊天服务器连接成功")
    }

    override fun onDisconnected(errorcode: Int) {
        Log.d("ChatClient", "聊天服务器连接断开errorcode: $errorcode")
    }
}