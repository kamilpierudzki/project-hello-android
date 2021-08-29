package com.project.hello.commons.framework.livedata

class Event<T>(val content: T) {

    var consumed = false
        private set

    fun consumeAndReturn(): T? =
        if (consumed) {
            null
        } else {
            consumed = true
            content
        }
}