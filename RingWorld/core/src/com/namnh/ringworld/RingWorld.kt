package com.namnh.ringworld

import com.badlogic.gdx.Game
import com.badlogic.gdx.audio.Music
import com.namnh.ringworld.screens.HomeScreen
import com.namnh.ringworld.screens.PlayScreen
import com.namnh.ringworld.utils.Constants

class RingWorld(private val platformHelper: GameHelper) : Game() {

    private var playScreen: PlayScreen? = null
    private lateinit var music: Music
    private lateinit var homeScreen: HomeScreen

    override fun create() {
        music = Constants.MUSIC
        music.isLooping = true
        music.volume = 0.8f
        homeScreen = HomeScreen(this)
        setScreen(homeScreen)
    }

    fun startPlay(mode: Int) {
        if (playScreen == null) {
            playScreen = PlayScreen(this, mode)
        }
        playScreen!!.setPlayMode(mode)
        setScreen(playScreen)
    }

    fun setHomeScreen() {
        setScreen(homeScreen)
    }

    fun exit() {
        platformHelper.endGame()
    }
}
