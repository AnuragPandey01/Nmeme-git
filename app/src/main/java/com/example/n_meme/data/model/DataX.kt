package com.example.n_meme.data.model

import android.media.MediaMetadata

data class DataX(
    val author: String,
    val content_categories: Any,
    val gallery_data: GalleryData,
    val id: String,
    val is_reddit_media_domain: Boolean,
    val is_robot_indexable: Boolean,
    val is_video: Boolean,
    val locked: Boolean,
    val media_metadata: MediaMetadata,
    val media_only: Boolean,
    val over_18: Boolean,
    val pinned: Boolean,
    val preview: Preview,
    val spoiler: Boolean,
    val subreddit: String,
    val subreddit_id: String,
    val thumbnail: String,
    val thumbnail_height: Int,
    val thumbnail_width: Int,
    val title: String,
    val ups: Int,
    val upvote_ratio: Double,
    val url: String,
    val url_overridden_by_dest: String,
)