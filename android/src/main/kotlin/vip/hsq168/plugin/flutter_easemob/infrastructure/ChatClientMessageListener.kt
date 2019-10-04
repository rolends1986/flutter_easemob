package vip.hsq168.plugin.flutter_easemob.infrastructure

import com.hyphenate.chat.ChatManager
import com.hyphenate.chat.Message

class ChatClientMessageListener: ChatManager.MessageListener {
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
}