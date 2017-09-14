package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class ExplosionTriangle(internal var renderer: ShapeRenderer, position: Vector2,
                        private val angle: Float) : RenderableObject {
    private val position: Vector2 = Vector2(position)
    private var f = 0.0f
    internal var color: Color
    var time: Float = 0.toFloat()
        private set
    private val speedFactor: Float

    init {
        f = MathUtils.random(0.5f, 1.2f)
        color = Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f)
        speedFactor = MathUtils.random(1f, 2f)
    }

    override fun render(delta: Float) {

        time += delta

        position.x += speedFactor * 400f * delta * MathUtils.sinDeg(angle)
        position.y += speedFactor * 400f * delta * MathUtils.cosDeg(angle)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = color
        renderer.identity()
        renderer.translate(position.x, position.y, 0f)
        renderer.rotate(0f, 0f, 1f, angle)
        renderer.triangle(0f - f * 15f, 0f - f * 13f, 0 + f * 15f, 0f - f * 13f, 0f, 0f + f * 13f)
        renderer.rotate(0f, 0f, 1f, -angle)
        renderer.translate(-position.x, -position.y, 0f)
        renderer.end()
    }
}
