package com.example.bookcourt.utils

import java.security.MessageDigest

object Hashing {
    fun getHash(inByteArray: ByteArray,type:String):String{
        var digestedBytes = MessageDigest.getInstance(type).digest(inByteArray)
        return  with(StringBuilder()){
            digestedBytes.forEach{ b->
                append(String.format("%02X",b)) }
            toString().toLowerCase()
        }
    }
}