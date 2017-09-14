package com.namnh.ringworld.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Button(internal var renderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float,
             private var text: String, b: Boolean, private var font: BitmapFont,
             private var batch: SpriteBatch) : RenderableObject {
    private var bounds: Rectangle = Rectangle(x, y, width, height)
    private var buttonColor: Color = Color(1f, 1f, 1f, 0.5f)
    private var lockedButtonColor: Color = Color(1f, 1f, 1f, 0.3f)
    private var layout: GlyphLayout
    private var lock: Texture
    private var fontX: Float = 0.toFloat()
    private var fontY: Float = 0.toFloat()
    private var locked = false

    init {
        locked = b
        layout = GlyphLayout(font, text)
        lock = Texture("padlock.png")
        fontX = x + (width - layout.width) / 2
        fontY = y + (height + layout.height) / 2
    }

    override fun render(delta: Float) {
        if (locked) {
            drawText()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

            renderer.begin(ShapeRenderer.ShapeType.Filled)
            renderer.color = lockedButtonColor
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renderer.end()

            Gdx.gl.glDisable(GL20.GL_BLEND)

            batch.begin()
            batch.draw(lock, bounds.x + bounds.width / 2 - 20, bounds.y + 10, 40f, 40f)
            batch.end()
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

            renderer.begin(ShapeRenderer.ShapeType.Filled)
            renderer.color = buttonColor
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renderer.end()
            drawText()

            Gdx.gl.glDisable(GL20.GL_BLEND)
        }
    }

    private fun drawText() {
        batch.begin()
        font.draw(batch, text, fontX, fontY)
        batch.end()
    }

    fun isTouched(position: Vector2): Boolean {
        if (!locked) {
            if (bounds.contains(position)) {
                return true
            }
        }
        return false
    }

    fun unlock() {
        locked = false
    }
}
