package com.namnh.ringworld.screens

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.namnh.ringworld.RingWorld

open class BaseScreen(val game: RingWorld) : InputAdapter(), Screen {
    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}