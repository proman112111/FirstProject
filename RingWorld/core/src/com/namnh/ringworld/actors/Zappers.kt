package com.namnh.ringworld.actors

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.Encrypt
import com.namnh.ringworld.utils.FontsUtil

class Zappers(private val spriteBatch: SpriteBatch, private val renderer: ShapeRenderer,
              private val preferences: Preferences,
              private val position: Vector2) : RenderableObject {
    private val zapperImage: Texture
    private var zapperCount: Int = 0
    var pendingZappers: Int = 0
        private set

    private var timePassed: Float = 0.toFloat()

    init {
        pendingZappers = 0
        zapperImage = Texture("zapper.png")
        zapperCount = Encrypt.decrypt(preferences.getString(Constants.ZAPPER_COUNT))
    }

    override fun render(delta: Float) {

        timePassed += delta

        if (timePassed > 2) {
            timePassed -= 2f
            updateZapperCount()
        }

        spriteBatch.begin()
        spriteBatch.draw(zapperImage, position.x, position.y, 50f, 50f)
        FontsUtil.ZAPPER_FONT.draw(spriteBatch, zapperCount.toString(), position.x + 50,
                position.y + 50)
        if (pendingZappers > 0) {
            FontsUtil.PENDING_ZAPPER_FONT.draw(spriteBatch, "+ " + pendingZappers,
                    position.x + 50, position.y + 5)
        }
        spriteBatch.end()

    }

    private fun updateZapperCount() {
        zapperCount = Encrypt.decrypt(preferences.getString(Constants.ZAPPER_COUNT))
    }

    fun increasePendingZappers() {
        pendingZappers += 10
    }

    fun giveInitZappers() {
        pendingZappers += 3
    }

    fun resetPendingZappers() {
        pendingZappers = 0
    }
}
