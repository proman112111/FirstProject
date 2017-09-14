package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.FontsUtil

class DevelopersOverlay(private val renderer: ShapeRenderer,
                        private val spriteBatch: SpriteBatch) : RenderableObject {
    private val alphaBlack: Color = Color(0f, 0f, 0f, 0.8f)
    private val fontX: Float
    private val fontY: Float
    var githubBounds: Rectangle
    var facebookBounds: Rectangle
    var googleplusBounds: Rectangle

    init {
        val layout = GlyphLayout(FontsUtil.DEVELOPERS_SCREEN_FONT, DEVELOPERS)
        fontX = 0 + (Constants.WORLD_WIDTH - layout.width) / 2
        fontY = Constants.WORLD_HEIGHT / 4f + (Constants.WORLD_HEIGHT + layout.height) / 2

        facebookBounds = Rectangle(Constants.WORLD_WIDTH / 2 + 90, Constants.WORLD_HEIGHT / 2 - 65,
                50f, 50f)
        githubBounds = Rectangle(Constants.WORLD_WIDTH / 2 + 150, Constants.WORLD_HEIGHT / 2 - 65,
                50f, 50f)
        googleplusBounds = Rectangle(Constants.WORLD_WIDTH / 2 + 210,
                Constants.WORLD_HEIGHT / 2 - 65, 50f, 50f)

    }

    override fun render(delta: Float) {

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = alphaBlack
        renderer.rect(0f, 0f, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT)
        renderer.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)

        spriteBatch.begin()
        FontsUtil.DEVELOPERS_SCREEN_FONT.draw(spriteBatch, DEVELOPERS, fontX, fontY)
        FontsUtil.GO_TO_HOME_FONT.draw(spriteBatch, DEV_NAME, 140f, fontY - 140)
        FontsUtil.MODE_BUTTON_FONT.draw(spriteBatch, "(@betterclever)", 170f, fontY - 190)
        //        spriteBatch.draw(Constants.COPYRIGHT_IMAGE,Constants.WORLD_WIDTH/2-130,20,20,20);
        FontsUtil.COPYRIGHT_FONT.draw(spriteBatch, " CLEVERCORE LABS - 2017",
                Constants.WORLD_WIDTH / 2 - 110, 38f)
        //        spriteBatch.draw(Constants.FACEBOOK_IMAGE,Constants.WORLD_WIDTH/2 + 90,Constants.WORLD_HEIGHT/2 - 65,50,50);
        //        spriteBatch.draw(Constants.GITHUB_IMAGE,Constants.WORLD_WIDTH/2 + 150,Constants.WORLD_HEIGHT/2 - 65,50,50);
        //        spriteBatch.draw(Constants.GOOGLEPLUS_IMAGE,Constants.WORLD_WIDTH/2 + 210,Constants.WORLD_HEIGHT/2 - 65,50,50);
        //        spriteBatch.draw(Constants.CLOSE_IMAGE,Constants.WORLD_WIDTH-60,Constants.WORLD_HEIGHT-60,40,40);
        spriteBatch.end()
    }

    companion object {
        private val DEVELOPERS = "Developed By"
        private val DEV_NAME = "Pranjal Paliwal"
    }
}
