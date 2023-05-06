package com.example.n_meme.data.local.sharedPreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.n_meme.ui.home.intent.SortType

class PreferenceManager(val context: Context) {

    private fun getSharedPref() = context.getSharedPreferences("UserPref",MODE_PRIVATE)

    fun resetUserPref() = getSharedPref().edit().clear().apply()

    fun getSortPref() = getSharedPref().getString("sortPref","hot")

    fun updateSortPref(sortType: SortType) = getSharedPref().edit().putString("sortPref",sortType.name.lowercase()).apply()

    fun getSubredditPref() = getSharedPref().getString("subredditPref","wholesomememes")

    fun updateSubredditPref(subreddit: String) = getSharedPref().edit().putString("subredditPref",subreddit).apply()


}