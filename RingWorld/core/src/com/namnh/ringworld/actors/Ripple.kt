package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2

class Ripple(internal var renderer: ShapeRenderer, private var position: Vector2,
             private var minRadius: Float, private var maxRadius: Float) : RenderableObject {

    private var curRadius: Float = 0.toFloat()
    private var speedFactor: Float = 0.toFloat()
    private var alphaWhite: Color
    private var circle: Circle = Circle(position, maxRadius)

    init {
        curRadius = minRadius
        speedFactor = (maxRadius - minRadius) / 40
        alphaWhite = Color(1f, 1f, 1f, 0.5f)
    }

    override fun render(delta: Float) {

        curRadius += speedFactor * 60f * delta
        if (curRadius > maxRadius) {
            curRadius = minRadius
        }

        alphaWhite.a = 1 - curRadius * 0.4f / (maxRadius - minRadius) / speedFactor

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = alphaWhite
        renderer.circle(position.x, position.y, curRadius, 128)
        renderer.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)

    }

    fun isTouched(position: Vector2): Boolean {
        return circle.contains(position)
    }
}
