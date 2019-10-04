package vip.hsq168.plugin.flutter_easemob.infrastructure

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.hyphenate.chat.Message
import com.hyphenate.helpdesk.easeui.UIProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import android.text.TextUtils

import com.hyphenate.helpdesk.model.MessageHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import vip.hsq168.plugin.flutter_easemob.EasemobService
import vip.hsq168.plugin.flutter_easemob.utils.UserProfile
import java.util.concurrent.ConcurrentHashMap


class UserProfileProvider(var context: Context   ) : UIProvider.UserProfileProvider {

    companion object {
        var userProfileMap: MutableMap<String, UserProfile> = ConcurrentHashMap()

    }


    override fun setNickAndAvatar(context: Context?, message: Message?, userAvatarView: ImageView?, usernickView: TextView?) {
        var hdDefaultAvatar = com.hyphenate.helpdesk.R.drawable.hd_default_avatar;
        val agentInfo = MessageHelper.getAgentInfo(message)
        if (usernickView != null) {
            usernickView.text = message!!.from()
            if (agentInfo != null) {
                if (!TextUtils.isEmpty(agentInfo.nickname)) {
                    usernickView.text = agentInfo.nickname
                }
            }
        }
        if (userAvatarView != null) {
            userAvatarView.setImageResource(hdDefaultAvatar)
            if (agentInfo != null) {
                if (!TextUtils.isEmpty(agentInfo.avatar)) {
                    var strUrl = agentInfo.avatar
                    if (!TextUtils.isEmpty(strUrl)) {
                        if (!strUrl.startsWith("http")) {
                            strUrl = "http:$strUrl"
                        }
                        Glide.with(context!!).load(strUrl).apply(RequestOptions.placeholderOf(hdDefaultAvatar).diskCacheStrategy(DiskCacheStrategy.ALL)).into(userAvatarView)
                    }
                }
            }

        }
//        if (agentInfo == null) {
//            GlobalScope.async {
//                var profile = getUserProfile(message!!.from())
//                if (profile.nickName != "") usernickView!!.text = profile.nickName
//                if (profile.avater != "") Glide.with(context!!).load(profile.avater).apply(RequestOptions.placeholderOf(hdDefaultAvatar).diskCacheStrategy(DiskCacheStrategy.ALL)).into(userAvatarView!!)
//            }
//        }
    }

//    private fun getUserProfile(userName: String): UserProfile {
//        return if (userProfileMap.containsKey(userName)) {
//            userProfileMap[userName]!!
//        } else {
//            var profile = service.getUserProfile(userName)
//            if (profile.nickName != "" && profile.avater != "")
//                userProfileMap[userName] = profile
//            profile
//        }
//    }
}