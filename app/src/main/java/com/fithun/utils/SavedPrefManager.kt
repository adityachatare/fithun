package com.fithun.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SavedPrefManager(var context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("WalkAndEarn", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as boolean.
     */
    private fun getBooleanValue(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private fun setBooleanValue(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private fun getStringValue(key: String): String? {
        return preferences.getString(key, "")
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private fun setStringValue(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private fun getIntValue(key: String): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private fun setIntValue(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    fun getLongValue(key: String?): Long {
        return preferences.getLong(key, 0L)
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    fun setLongValue(key: String?, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * Remove the preference for the particular key
     *
     * @param key : Key for which the preference to be cleared.
     */
    fun removeFromPreference(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    companion object {
        //preferences variables
        const val AccessToken = "AccessToken"
        const val isLogin = "isLogin"
        const val userId = "userId"
        const val email = "email"
        const val Password = "Password"
        const val isRemember = "isRemembe"
        private const val PREF_NAME = "MyAppPreferences"
        private const val REMEMBER_ME_KEY = "rememberMe"
        private const val EMAIL_KEY = "email"
        private const val PASSWORD_KEY = "password"


        const val stepCount = "stepCount"
        const val totalDistance = "totalDistance"
        const val Speed = "Speed"
        const val Unit = "Unit"


        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"


        const val UPI_ID = "UPI_ID"


        const val isContestStart = "isContestStart"
        const val totalDistanceForContest = "totalDistanceForContest"
        const val unitForContest = "unitForContest"
        const val CONTEST_ID = "CONTEST_ID"





        fun setRememberMeState(context: Context, rememberMe: Boolean) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(REMEMBER_ME_KEY, rememberMe)
            editor.apply()
        }

        fun getRememberMeState(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(REMEMBER_ME_KEY, false)
        }

        fun saveLoginCredentials(context: Context, email: String, password: String) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(EMAIL_KEY, email)
            editor.putString(PASSWORD_KEY, password)
            editor.apply()
        }

        fun getLoginCredentials(context: Context): Pair<String, String> {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val email = sharedPreferences.getString(EMAIL_KEY, "") ?: ""
            val password = sharedPreferences.getString(PASSWORD_KEY, "") ?: ""
            return Pair(email, password)
        }

        private var instance: SavedPrefManager? = null
        private const val PREF_HIGH_QUALITY = "pref_high_quality"



        fun getInstance(context: Context): SavedPrefManager? {
            if (instance == null) {
                synchronized(SavedPrefManager::class.java) {
                    if (instance == null) {
                        instance = SavedPrefManager(context)
                    }
                }
            }
            return instance
        }


        fun saveStringPreferences(context: Context?, key: String, value: String?): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
            return key
        }

        fun saveIntPreferences(context: Context?, key: String?, value: Int?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putInt(key, value)
            }
            editor.apply()
        }

        fun saveFloatPreferences(context: Context?, key: String?, value: Float) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        /*
  This method is used to get string values from shared preferences.
   */
        fun getStringPreferences(context: Context?, key: String?): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(key, "")
        }




        /*
     This method is used to get string values from shared preferences.
      */
        fun getIntPreferences(context: Context?, key: String?): Int {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getInt(key, 0)
        }

        fun savePreferenceBoolean(context: Context?, key: String?, b: Boolean) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, b)
            editor.commit()
        }

        /*
      This method is used to get string values from shared preferences.
       */
        fun getBooleanPreferences(context: Context?, key: String?): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(key, false)
        }

        fun getFloatPreferences(context: Context?, key: String?): Float {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getFloat(key, 0f)
        }

        /**
         * Removes all the fields from SharedPrefs
         */
        fun clearPrefs(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }



        fun clearPrefsForNextDay(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.remove("stepCount")
            editor.remove("totalDistance")
            editor.remove("Speed")
            editor.remove("Unit")
            editor.apply()
        }


        fun deleteStepsCounts(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()


            editor.remove("Speed")
            editor.apply()
        }



        fun deleteAllData(context: Context?){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()


            editor.remove("stepCount")
            editor.remove("AccessToken")
            editor.remove("totalDistance")
            editor.remove("Speed")
            editor.remove("Unit")
            editor.apply()
        }



        fun stepCountDeleteOfContest(context: Context?){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()


            editor.remove("isContestStart")
            editor.remove("totalDistanceForContest")
            editor.remove("unitForContest")
            editor.remove("CONTEST_ID")
            editor.remove("START_DATE")
            editor.remove("END_DATE")
            editor.apply()
        }




    }


}