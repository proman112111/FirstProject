package com.namnh.ringworld

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.namnh.ringworld.screens.GameScreen
import com.namnh.ringworld.screens.HomeScreen
import com.namnh.ringworld.utils.Constants

class RingWorld(private val gameHelper: GameHelper) : Game() {

    private var gameScreen: GameScreen? = null
    private lateinit var music: Music
    private lateinit var homeScreen: HomeScreen

    override fun create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("sample.mp3"))
        music.isLooping = true
        music.volume = 0.8f
        homeScreen = HomeScreen(this)
        startGame(Constants.EASY_MODE)
    }

    fun startHomeScreen() {
        setScreen(homeScreen)
    }

    fun startGame(mode: Int) {
        if (gameScreen == null) {
            gameScreen = GameScreen(this, mode)
        }
        setScreen(gameScreen)
    }

    fun exitGame() {
        gameHelper.endGame()
    }
}
