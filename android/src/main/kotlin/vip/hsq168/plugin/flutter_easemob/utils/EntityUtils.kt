package vip.hsq168.plugin.flutter_easemob.utils

import java.lang.reflect.AccessibleObject.setAccessible
 
import android.os.Parcelable



class EntityUtils {


    object EntityUtils {
        //（一定要绑定，订阅成功才能进行对象转换）。
        /**
         * 实体类转Map
         *
         * @param object
         * @return
         */
        fun entityToMap(`object`: Parcelable): Map<String, Any> {
            //        stu.writeToParcel(p, 0);
            val map = HashMap<String,Any>()
            for (field in `object`.javaClass.declaredFields) {
                try {
                    val flag = field.isAccessible
                    field.isAccessible = true
                    val o = field.get(`object`)
                    map[field.name] = o
                    field.isAccessible = flag
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return map
        }

    }
}