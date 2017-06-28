package com.namnh.ringworld.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.namnh.ringworld.RingWorld
import com.namnh.ringworld.utils.Constants

class HomeScreen(game: RingWorld) : BaseScreen(game) {
    val TAG = "HomeScreen"
    private var viewport: Viewport = ExtendViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT)
    private val renderer = ShapeRenderer()
    init {

        // handle touch event
        Gdx.input.inputProcessor = this
        Gdx.input.isCatchBackKey = true
    }
}