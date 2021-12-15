package com.project.hello.commons.livedata

class Event<T>(val content: T) {

    var consumed = false
        private set

    fun consumeAndReturnOrNull(): T? =
        if (consumed) {
            null
        } else {
            consumed = true
            content
        }
}