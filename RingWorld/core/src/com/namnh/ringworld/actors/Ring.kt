package com.namnh.ringworld.actors

abstract class Ring : RenderableObject {

    var radius: Float = 0f
        set(value) {
            field = value
        }
        get() = field

    var isStopped = false
        set(value) {
            field = value
        }
        get() = field

    abstract override fun render(delta: Float)

    fun stop() {
        isStopped = true
    }

    fun start() {
        isStopped = false
    }
}
