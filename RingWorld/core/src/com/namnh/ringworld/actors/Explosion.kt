package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.namnh.ringworld.utils.randomColor1

class Explosion(val renderer: ShapeRenderer, val position: Vector2, var angle: Float) : GameObject {
    var time = 0f
    private var speed = MathUtils.random(1f, 2f)
    private var sizeFactor = MathUtils.random(0.5f, 1.2f)
    private val color = Color().randomColor1()

    override fun render(delta: Float) {
        time += delta

        position.x += speed * 400 * delta * MathUtils.sinDeg(angle)
        position.y += speed * 400 * delta * MathUtils.cosDeg(angle)

        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = color
        renderer.identity()
        renderer.translate(position.x, position.y, 0f)
        renderer.rotate(0f, 0f, 1f, angle)
        renderer.triangle(-sizeFactor * 15f, -sizeFactor * 13f, sizeFactor * 15f, -sizeFactor * 13f,
                0f, sizeFactor * 13f)
        renderer.rotate(0f, 0f, 1f, -angle)
        renderer.translate(-position.x, -position.y, 0f)

        renderer.end()
    }
}