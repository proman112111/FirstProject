package com.namnh.ringworld

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.namnh.ringworld.screens.GameScreen
import com.namnh.ringworld.screens.HomeScreen

class RingWorld(val gameHelper: GameHelper) : Game() {

    internal var gameScreen: GameScreen? = null
    internal lateinit var music: Music
    internal lateinit var homeScreen: HomeScreen

    override fun create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("background-skyline.mp3"))
        music.isLooping = true
        music.volume = 0.8f
        homeScreen = HomeScreen(this)
        startHomeScreen()
    }

    fun startHomeScreen() {
        setScreen(homeScreen)
    }

    fun startGame(mode: Int) {
        if (gameScreen == null) {
            gameScreen = GameScreen(this, mode)
        }
        gameScreen.setPlayMode(mode)
        setScreen(gameScreen)
    }

    fun exitGame() {
        gameHelper.endGame()
    }
}
