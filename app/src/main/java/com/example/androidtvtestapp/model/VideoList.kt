package com.example.androidtvtestapp.model

data class VideoList(
    val error : Int,
    val videos : List<Video>,
    val count : Int
)