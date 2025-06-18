package com.createlib.pic_lib.cache

import com.createlib.pic_lib.photo.model.Photo
import java.util.Collections
import java.util.LinkedHashMap
import javax.inject.Inject

class CacheManager @Inject constructor() {

    private val photoCache = Collections.synchronizedMap(
        object : LinkedHashMap<String, Photo>(16, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Photo>?): Boolean {
                return size > MAX_CACHE_SIZE || (eldest?.value?.timestamp
                    ?: 0) < System.currentTimeMillis() - CACHE_TTL
            }
        }
    )

    companion object {
        private const val MAX_CACHE_SIZE = 100 //max photo
        private const val CACHE_TTL = 24 * 60 * 60 * 1000 // 24 hours
    }

    fun cachePhoto(photo: Photo) {
        photoCache[photo.uri.toString()] = photo
    }

    fun getCachedPhoto(uri: String): Photo? = photoCache[uri]

    fun evictOldPhoto() =
        photoCache.entries.removeIf { it.value.timestamp < System.currentTimeMillis() - CACHE_TTL }
}