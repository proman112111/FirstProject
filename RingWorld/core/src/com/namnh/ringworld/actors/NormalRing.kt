package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.namnh.ringworld.utils.Constants

class NormalRing(renderer: ShapeRenderer) : Ring(renderer) {
    private var color: Color = Color(MathUtils.random(0.3f, 0.8f), MathUtils.random(0.3f, 0.8f),
            MathUtils.random(0.3f, 0.8f), 1f)

    init {
        radius = 200f
    }

    override fun render(delta: Float) {
        if (!isStopped) {
            radius -= 60 * delta
        }
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = this.color
        renderer.circle(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius, 256)
        renderer.color = Color.BLACK
        renderer.circle(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius - 3, 256)
        renderer.color = this.color
        renderer.circle(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius - 4, 256)
        renderer.color = Color.BLACK
        renderer.circle(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius - 7, 256)

        renderer.end()
    }
}