package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.namnh.ringworld.utils.Constants

class EarnZapperButton(private val spriteBatch: SpriteBatch,
                       private val renderer: ShapeRenderer) : RenderableObject {
    private val bitmapFont: BitmapFont
    private val getImage: Texture
    private val position: Vector2 = Vector2(Constants.WORLD_WIDTH - 250, Constants.WORLD_HEIGHT - 70)
    private val bounds: Rectangle = Rectangle(Constants.WORLD_WIDTH - 250, Constants.WORLD_HEIGHT - 70, 250f, 70f)

    init {

        val generator = FreeTypeFontGenerator(Gdx.files.internal("Quantify-Bold.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 20
        parameter.color = Color.WHITE
        parameter.minFilter = Texture.TextureFilter.Linear
        parameter.magFilter = Texture.TextureFilter.Linear
        bitmapFont = generator.generateFont(parameter)
        getImage = Texture("get-money.png")
    }

    override fun render(delta: Float) {
        spriteBatch.begin()
        spriteBatch.draw(getImage, position.x, position.y, 50f, 50f)
        bitmapFont.draw(spriteBatch, "Earn Free", position.x + 80, position.y + 50)
        bitmapFont.draw(spriteBatch, "Zappers", position.x + 80, position.y + 20)
        spriteBatch.end()
    }

    fun isTouched(position: Vector2): Boolean {
        if (bounds.contains(position)) {
            Gdx.app.log("Tapped", "earnzapper button")
            return true
        }
        return false
    }
}
