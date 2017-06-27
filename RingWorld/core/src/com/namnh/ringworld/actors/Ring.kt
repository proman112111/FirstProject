package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

abstract class Ring(val renderer: ShapeRenderer) : GameObject {
    protected var radius: Float = 0f
        set
        get() = radius

    protected var isStopped = false
    fun start() {
        isStopped = false
    }

    fun stop() {
        isStopped = true
    }
}