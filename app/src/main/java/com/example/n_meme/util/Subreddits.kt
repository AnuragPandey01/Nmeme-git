package com.example.n_meme.util

object Subreddits {
    val list = listOf(
        "wholesomemes",
        "memes",
        "me_irl",
        "dankmemes",
        "funny",
        "terriblefacebookmemes",
        "adviceanimals",
        "comics",
        "comicsandmemes",
        "funnyanimals",
        "aww",
        "funnysigns",
        "meirl",
        "memes_of_the_dank",
        "highqualitygifs",
        "deepfriedmemes",
        "bonehurtingjuice",
        "surrealmemes",
        "antimeme",
        "okbuddyretard",
        "comedyheaven",
        "memeswithoutwords",
        "2meirl4meirl",
        "wholesomegreentext",
        "wholesomecomics",
        "wholesome_memes",
        "memes_of_the_dankest",
        "funny_memes",
        "memes_daily",
        "memes_for_days",
        "best_memes",
        "dankest_memes",
        "wholesome_memes_animals",
        "wholesome_memes_comics",
        "wholesome_memes_doggos",
        "wholesome_memes_cats",
        "wholesome_memes_humans",
        "wholesome_memes_other",
        "animememes",
        "hentaimemes"
    )

    //sort by keyword
    fun sort(keyword: String): List<String> {
        return list.filter { it.contains(keyword, ignoreCase = true) }
    }

}