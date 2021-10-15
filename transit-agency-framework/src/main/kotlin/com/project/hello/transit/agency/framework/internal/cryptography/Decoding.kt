package com.project.hello.transit.agency.framework.internal.cryptography

internal interface Decoding {
    fun decode(encoded: String): String
}