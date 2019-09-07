package vip.hsq168.plugin.flutter_easemob.kefu

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONException
import org.json.JSONObject


class KeFuOfficialAccount {

    var id: String? = null
    var name: String? = null
    var type: String? = null
    var img: String? = null


    companion object {
        val KEY_OFFICIAL_ID = "official_account_id"
        val KEY_OFFICIAL_NAME = "name"
        val KEY_OFFICIAL_TYPE = "type"
        val KEY_OFFICIAL_IMG = "img"
        val KEY_OFFICIAL_ACCOUNT = "official_account"
        val SEPARATOR = "#|"
        val KEY_MARKETING_TASK_ID = "marketing_task_id"
        val KEY_MARKETING = "marketing"
        val KEY_AGENT_ACCOUNT = "agent"
        val KEY_AGENT_ID = "userId"
        val KEY_AGENT_NAME = "userNickname"
        val KEY_AGENT_IMG = "avatar"
    }

}
