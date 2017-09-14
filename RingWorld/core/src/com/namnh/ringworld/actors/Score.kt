package com.namnh.ringworld.actors

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.FontsUtil

class Score(private var batch: SpriteBatch) : RenderableObject {

    var score = 0
        private set

    override fun render(delta: Float) {
        batch.begin()
        FontsUtil.SCORE_FONT.draw(batch, "" + score, 10f, Constants.WORLD_HEIGHT - 20)
        batch.end()
    }

    fun increase() {
        score++
    }

    fun reset() {
        score = 0
    }
}
