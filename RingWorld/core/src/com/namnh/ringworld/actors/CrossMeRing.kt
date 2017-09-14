package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.namnh.ringworld.utils.Constants

class CrossMeRing(internal var renderer: ShapeRenderer, mode: Int) : Ring() {
    var arcNum: Int = 0
        internal set
    var rot = 0f
        internal set
    private var speed: Int = 0
    private var white: Color
    private var red: Color
    private var ringColor: Color
    private var time: Float = 0f
    private var colorAction: ColorAction

    init {
        radius = 200f
        white = Color.WHITE.cpy()
        setParamsByMode(mode)

        red = Color(0.7f, 0.3f, 0.3f, 1f)

        ringColor = white
        colorAction = ColorAction()
        colorAction.color = ringColor
        colorAction.duration = 2f
        colorAction.endColor = red

    }

    private fun setParamsByMode(mode: Int) {

        when (mode) {
            Constants.EASY_MODE -> {
                speed = 40
                arcNum = MathUtils.random(2, 5)
            }
            Constants.MEDIUM_MODE -> {
                speed = 60
                arcNum = MathUtils.random(2, 6)
            }
            Constants.HARD_MODE -> {
                speed = MathUtils.random(50, 80)
                arcNum = MathUtils.random(3, 6)
            }
            Constants.INSANE_MODE -> {
                speed = MathUtils.random(40, 120)
                arcNum = MathUtils.random(3, 6)
            }
        }

    }

    override fun render(delta: Float) {
        if (!isStopped) {
            radius -= 60 * delta
            rot += speed * delta
            rot %= 360f

            time += delta
            colorAction.act(delta)

        }

        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = ringColor
        for (i in 0 until arcNum) {
            renderer.arc(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, radius,
                    180 / arcNum * 2 * i + rot, (180 / arcNum).toFloat(), 256)
        }

        renderer.color = Color.BLACK
        renderer.circle(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, radius - 7.5f, 256)

        renderer.end()

    }
}
