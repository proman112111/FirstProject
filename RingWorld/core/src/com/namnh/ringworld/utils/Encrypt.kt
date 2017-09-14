package com.namnh.ringworld.utils

import com.badlogic.gdx.utils.Base64Coder

object Encrypt {

    fun encrypt(score: Int): String {
        val sc = score.toString()
        return Base64Coder.encodeString(sc)
    }

    fun decrypt(encrypted: String): Int {
        val de = Base64Coder.decodeString(encrypted)
        return if (de == "") {
            0
        } else Integer.parseInt(de)
    }

}
