package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

abstract class Ring(val renderer: ShapeRenderer) : GameObject {
    var radius: Float = 0f
    var isStopped = false

    fun start() {
        isStopped = false
    }

    fun stop() {
        isStopped = true
    }
}