package vip.hsq168.plugin.flutter_easemob.infrastructure

import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMMessage

class EMClientMessageListener: EMMessageListener {
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
}