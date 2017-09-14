package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2

class CircleButton(internal var renderer: ShapeRenderer, scrX: Float, scrY: Float,
                   radius: Int) : RenderableObject {
    private var bounds: Circle = Circle(scrX, scrY, radius.toFloat())
    internal var color: Color = Color(1f, 1f, 1f, 0.3f)

    override fun render(delta: Float) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = color
        renderer.circle(bounds.x, bounds.y, bounds.radius, 128)
        renderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    fun isTouched(position: Vector2): Boolean {
        return bounds.contains(position)
    }
}
