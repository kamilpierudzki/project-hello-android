package com.project.hello.commons.domain

fun <T> MutableList<T>.addElementIfNotContains(element: T) {
    if (!contains(element)) {
        add(element)
    }
}