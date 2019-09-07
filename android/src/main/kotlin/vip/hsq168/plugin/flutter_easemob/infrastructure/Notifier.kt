package vip.hsq168.plugin.flutter_easemob.infrastructure

import android.app.Notification
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.media.AudioManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import android.os.Vibrator
import com.hyphenate.chat.Message
import java.util.*
import android.media.RingtoneManager
import android.media.AudioAttributes
import android.net.Uri
import com.hyphenate.helpdesk.model.MessageHelper
import vip.hsq168.plugin.flutter_easemob.EasemobService
 


class Notifier (var service: EasemobService): com.hyphenate.helpdesk.easeui.Notifier() {
    private var manager: NotificationManager? = null
    val id = "flutter_easemob"
    val name = "客服对话"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(soundUri: Uri?) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        if (soundUri != null)
            channel.setSound(soundUri, AudioAttributes.Builder().build())
        getManager()!!.createNotificationChannel(channel)
    }

    private fun getManager(): NotificationManager? {
        if (manager == null) {
            manager = getSystemService(appContext, NotificationManager::class.java) as NotificationManager?
        }
        return manager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChannelNotification(): Notification.Builder {
        return Notification.Builder(appContext, id)

    }

    private fun getNotification25(): NotificationCompat.Builder {
        return NotificationCompat.Builder(appContext)
    }

    private fun createNotification(title: String, ticker: String, content: String, intent: PendingIntent, smallIcon: Int, soundUri: Uri?): Notification {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(soundUri)
            var builder = getChannelNotification()
                    .setContentTitle(title)
                    .setTicker(ticker)
                    .setContentText(content)
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
            if (smallIcon != 0)
                builder = builder.setSmallIcon(smallIcon)
            return builder.build()

        } else {
            var builder = getNotification25()
                    .setContentTitle(title)
                    .setTicker(ticker)
                    .setContentText(content)
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
            if (smallIcon != 0)
                builder = builder.setSmallIcon(smallIcon)
            if (soundUri != null)
                builder = builder.setSound(soundUri)
            return builder.build()
        }
    }


    override fun init(context: Context): Notifier {
        appContext = context
        notificationManager = getManager()
        packageName = appContext.applicationInfo.packageName
        audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return this
    }


    /**
     * 发送通知栏提示
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    override fun sendNotification(message: Message, isForeground: Boolean, numIncrease: Boolean) {
        if (Locale.getDefault().language.contains("zh")) {
            msgs = msg_ch
        } else {
            msgs = msg_eng
        }
        try {
            var nickName: String?
            val agentInfo = MessageHelper.getAgentInfo(message)
            nickName = if (agentInfo != null) {
                agentInfo.nickname
            } else {
                var profile = service.getUserProfile(message.from())
                profile.nickName
            }
            var notifyText = "$nickName "
            when (message.type) {
                Message.Type.TXT -> notifyText += msgs[0]
                Message.Type.IMAGE -> notifyText += msgs[1]
                Message.Type.VOICE -> notifyText += msgs[2]
                Message.Type.LOCATION -> notifyText += msgs[3]
                Message.Type.VIDEO -> notifyText += msgs[4]
                Message.Type.FILE -> notifyText += msgs[5]
                else -> {
                }
            }
            val packageManager = appContext.packageManager
            // notification titile
            var contentTitle = packageManager.getApplicationLabel(appContext.applicationInfo) as String
            var msgIntent = appContext.packageManager.getLaunchIntentForPackage(packageName)
            notifyID = Random().nextInt(100)
            val pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (numIncrease) {
                // prepare latest event info section
                if (!isForeground) {
                    notificationNum++
                    fromUsers.add(message.from())
                }
            }
            val fromUsersNum = fromUsers.size
            var summaryBody = msgs[6].replaceFirst("%1".toRegex(), Integer.toString(fromUsersNum)).replaceFirst("%2".toRegex(), Integer.toString(notificationNum))
            var smallIcon = appContext.applicationInfo.icon
            val defaultSoundUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notification = createNotification(contentTitle, notifyText, summaryBody, pendingIntent, smallIcon, defaultSoundUri)
            if (isForeground) {
                notificationManager.notify(foregroundNotifyID, notification)
                notificationManager.cancel(foregroundNotifyID)
            } else {
                notificationManager.cancel(oldNotifyID)
                notificationManager.notify(notifyID, notification)
                oldNotifyID = notifyID
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun sendNotification(message: String) {
        try {
            val packageManager = appContext.packageManager
            // notification title
            val contentTitle = packageManager
                    .getApplicationLabel(appContext.applicationInfo) as String
            val packageName = appContext.applicationInfo.packageName

            val defaultSoundUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val msgIntent = appContext.packageManager
                    .getLaunchIntentForPackage(packageName)
            val pendingIntent = PendingIntent.getActivity(appContext,
                    notifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            var smallIcon = appContext.applicationInfo.icon
            // create and send notification
            val notification = createNotification(contentTitle, message, message, pendingIntent, smallIcon, defaultSoundUri)
            notificationManager.notify(notifyID, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}