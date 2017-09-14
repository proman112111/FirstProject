package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.namnh.ringworld.utils.Constants

class PlayBall(private val renderer: ShapeRenderer,
               private var attachedRing: NormalRing?) : RenderableObject {

    private val radius: Int = 15
    private val bound: Circle = Circle()
    var rotateRadius = 0f
        private set
    var angle: Float = 0.toFloat()
        private set
    private var time: Float = 0.toFloat()
    val position: Vector2
    private val color: Color
    private var destColor: Color? = null
    private val colorAction: ColorAction

    init {
        angle = 0f
        position = Vector2()
        color = Color.TEAL
        destColor = Color.YELLOW
        colorAction = ColorAction()
        colorAction.color = color
        colorAction.duration = 2f
        colorAction.endColor = destColor
    }

    override fun render(delta: Float) {

        if (attachedRing == null) {
            renderNotAttached(delta)
            return
        }

        colorAction.act(delta)

        time += delta

        if (time > 2) {
            time -= 2f
            destColor = destColor!!.set(MathUtils.random(0.3f, 0.8f), MathUtils.random(0.3f, 0.8f),
                    MathUtils.random(0.3f, 0.8f), 1f)
            colorAction.reset()
            colorAction.color = color
            colorAction.endColor = destColor
            Gdx.app.log("destcolor", destColor.toString())
        }

        angle -= 50 * delta
        angle %= 360f
        if (angle < 0) {
            angle += 360f
        }
        rotateRadius = attachedRing!!.radius

        position.x = Constants.WORLD_WIDTH / 2 + (rotateRadius - 2.5f) * MathUtils.cosDeg(angle)
        position.y = Constants.WORLD_HEIGHT / 2 + (rotateRadius - 2.5f) * MathUtils.sinDeg(angle)

        // Gdx.app.log("arr", String.valueOf(rotateRadius));

        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = Color.BLACK
        renderer.circle(position.x, position.y, radius.toFloat(), 256)
        renderer.color = color
        renderer.circle(position.x, position.y, (radius - 3).toFloat(), 256)

        bound.set(position.x, position.y, radius.toFloat())

        //Gdx.app.log("angle of ball", String.valueOf(angle));
        renderer.end()

    }

    private fun renderNotAttached(delta: Float) {

        //Gdx.app.log("rr", String.valueOf(rotateRadius));

        position.x = Constants.WORLD_WIDTH / 2 + (rotateRadius - 2.5f) * MathUtils.cosDeg(angle)
        position.y = Constants.WORLD_HEIGHT / 2 + (rotateRadius - 2.5f) * MathUtils.sinDeg(angle)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = Color.CORAL
        renderer.circle(position.x, position.y, radius.toFloat(), 256)

        bound.set(position.x, position.y, radius.toFloat())

        rotateRadius += 300 * delta
        renderer.end()

    }

    fun detach(): Ring? {
        Gdx.app.log("ball", "detached")
        val r = attachedRing
        attachedRing = null
        return r
    }

    val isDetached: Boolean
        get() = attachedRing == null

    fun setAttachedRing(attachedRing: NormalRing) {
        Gdx.app.log("ball", "attached")
        this.attachedRing = attachedRing
    }
}
