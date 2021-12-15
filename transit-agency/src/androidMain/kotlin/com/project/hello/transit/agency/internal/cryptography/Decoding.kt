package com.project.hello.transit.agency.internal.cryptography

internal interface Decoding {
    fun decode(encoded: String): String
}