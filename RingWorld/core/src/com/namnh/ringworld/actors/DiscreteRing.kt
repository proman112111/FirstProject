package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.namnh.ringworld.utils.Constants

class DiscreteRing(renderer: ShapeRenderer, val mode: Int) : Ring(renderer) {
    private var arcNum = 0
        get() = arcNum

    private var rotation = 0f
        get() = rotation

    private var rotateSpeed = 0
    private var ringColor: Color = Color.WHITE
    private var colorAction: ColorAction = ColorAction()

    init {
        radius = 200f
        colorAction.color = ringColor
        colorAction.duration = 2f
        colorAction.endColor = Color(0.7f, 0.3f, 0.3f, 1f)
        initRing()
    }

    private fun initRing() {
        when (mode) {
            Constants.EASY_MODE -> {
                rotateSpeed = 40
                arcNum = MathUtils.random(2, 5)
            }
            Constants.NORMAL_MODE -> {
                rotateSpeed = 60
                arcNum = MathUtils.random(2, 6)
            }
            Constants.HARD_MODE -> {
                rotateSpeed = MathUtils.random(50, 80)
                arcNum = MathUtils.random(3, 6)
            }
        }
    }

    override fun render(delta: Float) {
        if (!isStopped) {
            radius -= 60 * delta
            rotation += rotateSpeed * delta
            rotation %= 360f

            colorAction.act(delta)
        }
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = ringColor
        for (i in 0..arcNum) {
            renderer.arc(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius,
                    ((180f / arcNum * 2 * i) + rotation), 180f / arcNum, 256)
        }
        renderer.color = Color.BLACK
        renderer.circle(Constants.GAME_WIDTH / 2, Constants.GAME_HEIGHT / 2, radius - 7.5f, 256)

        renderer.end()
    }
}