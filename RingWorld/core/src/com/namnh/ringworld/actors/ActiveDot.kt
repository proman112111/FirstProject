package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.randomColor

class ActiveDot(private val renderer: ShapeRenderer, var attachedRing: NormalRing?) : GameObject {
    private var radius: Float = 15f
    private var bound: Circle = Circle()
    private var colorAction: ColorAction = ColorAction()
    var position = Vector2()
    var rotatedRadius = 0f
    var angle = 0f
    private var time = 0f
    private var destColor = Color.YELLOW

    init {
        colorAction.color = Color.TEAL
        colorAction.duration = 2f
        colorAction.endColor = destColor
    }

    override fun render(delta: Float) {
        if (attachedRing == null) {
            normalRender(delta)
            return
        }
        attachedRender(delta)
    }

    private fun attachedRender(delta: Float) {
        colorAction.act(delta)
        time += delta
        if (time > 2f) {
            time -= 2
            destColor = destColor.randomColor()
            colorAction.reset()
            colorAction.color = Color.TEAL
            colorAction.endColor = destColor
        }
        angle -= 50 * delta
        angle %= 360f
        if (angle < 0) {
            angle += 360f
        }
        rotatedRadius = attachedRing?.radius!!
        position.x = Constants.GAME_WIDTH / 2 + (rotatedRadius - 2.5f) * MathUtils.cosDeg(angle)
        position.y = Constants.GAME_HEIGHT / 2 + (rotatedRadius - 2.5f) * MathUtils.sinDeg(angle)

        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = Color.BLACK
        renderer.circle(position.x, position.y, radius, 256)
        renderer.color = Color.TEAL
        renderer.circle(position.x, position.y, radius - 3, 256)

        bound.set(position.x, position.y, radius)

        renderer.end()
    }

    private fun normalRender(delta: Float) {
        position.x = Constants.GAME_WIDTH / 2 + (rotatedRadius - 2.5f) * MathUtils.cosDeg(angle)
        position.y = Constants.GAME_HEIGHT / 2 + (rotatedRadius - 2.5f) * MathUtils.sinDeg(angle)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = Color.CORAL
        renderer.circle(position.x, position.y, radius, 256)

        bound.set(position.x, position.y, radius)

        rotatedRadius += 300 * delta
        renderer.end()
    }

    fun detach(): Ring? {
        val r = attachedRing
        attachedRing = null
        return r
    }

    fun isDetached() = attachedRing == null
}