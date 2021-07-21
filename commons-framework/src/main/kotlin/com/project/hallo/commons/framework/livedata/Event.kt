package com.project.hallo.commons.framework.livedata

class Event<T>(val content: T) {

    var consumed = false
        private set

    fun getContentOrNull(): T? =
        if (consumed) {
            null
        } else {
            consumed = true
            content
        }
}