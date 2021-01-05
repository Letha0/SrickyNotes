package sticky.notes

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent

class SharedPref(var context: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    companion object {
        const val PREF_NAME = "stickynotes"
        private var mInstance: SharedPref? = null

        @Synchronized
        fun getInstance(context: Context): SharedPref {
            if (mInstance == null) {
                mInstance = SharedPref(context)
            }
            return mInstance as SharedPref

        }
    }

    val title :String?
        get(){
            return pref.getString("title", null)
        }


    val content :String?
        get(){
            return pref.getString("content", null)
        }

    val idNote :String?
        get(){
            return pref.getString("idNote", null)
        }

    fun getNoteToEdit(title:String?, content:String?, idNote:String?){
        editor.putString("title",title)
        editor.putString("content",content)
        editor.putString("idNote",idNote)
        editor.commit()
    }

}