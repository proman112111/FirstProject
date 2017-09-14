package com.namnh.ringworld.actors

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.namnh.ringworld.utils.Constants

class SoundOnOffButton(private var batch: SpriteBatch,
                       private var preferences: Preferences) : RenderableObject {

    private var soundOnImage: Texture = Texture("speaker.png")
    private var soundOffImage: Texture = Texture("silent.png")
    private var soundOn: Boolean = false
    private var bounds: Rectangle = Rectangle(Constants.WORLD_WIDTH - 70,
            Constants.WORLD_HEIGHT - 70, 70f, 70f)

    init {
        soundOn = preferences.getBoolean(Constants.SOUND_ON, true)
        preferences.putBoolean(Constants.SOUND_ON, soundOn).flush()
        initSound()
    }

    override fun render(delta: Float) {
        batch.begin()
        batch.draw(if (soundOn) soundOnImage else soundOffImage, bounds.x, bounds.y,
                bounds.width - 20, bounds.height - 20)
        batch.end()
    }

    fun toggle() {
        soundOn = !soundOn
        initSound()
        preferences.putBoolean(Constants.SOUND_ON, soundOn).flush()
    }

    private fun initSound() {
        if (soundOn) {
            Constants.MUSIC.play()
        } else {
            Constants.MUSIC.pause()
        }
    }

    fun isTouched(position: Vector2): Boolean {
        return bounds.contains(position)
    }
}
