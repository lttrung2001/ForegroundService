package com.ltbth.foregroundservice

import java.io.Serializable

data class Song(val title: String, val single: String, val image: Int, val resource: Int) :
    Serializable