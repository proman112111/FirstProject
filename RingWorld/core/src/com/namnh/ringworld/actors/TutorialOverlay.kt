package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.FontsUtil

class TutorialOverlay(private var spriteBatch: SpriteBatch,
                      internal var renderer: ShapeRenderer) : RenderableObject {
    private var alphaBlack: Color = Color(0f, 0f, 0f, 0.8f)

    override fun render(delta: Float) {

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = alphaBlack
        renderer.rect(0f, 0f, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT)
        renderer.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)

        spriteBatch.begin()
        FontsUtil.DEVELOPERS_SCREEN_FONT.draw(spriteBatch, "How To Play?",
                Constants.WORLD_WIDTH / 2 - 150, Constants.WORLD_HEIGHT / 2 + 130)

        spriteBatch.end()
    }
}
