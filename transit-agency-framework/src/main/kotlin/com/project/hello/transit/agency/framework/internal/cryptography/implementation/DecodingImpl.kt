package com.project.hello.transit.agency.framework.internal.cryptography.implementation

import android.util.Base64
import com.project.hello.transit.agency.framework.internal.cryptography.Decoding
import javax.inject.Inject


internal class DecodingImpl @Inject constructor(): Decoding {

    override fun decode(encoded: String): String {
        val inputBytesShifted = encoded
            .toByteArray(Charsets.UTF_8)
            .map { byte: Byte -> (byte - 1).toByte() }
            .toByteArray()

        val inputDecoded = Base64.decode(inputBytesShifted, Base64.DEFAULT)
        return inputDecoded.toString(Charsets.UTF_8)
    }
}