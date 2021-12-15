package com.project.hello.commons

fun <T> MutableList<T>.addElementIfNotContains(element: T) {
    if (!contains(element)) {
        add(element)
    }
}