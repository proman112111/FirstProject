package com.namnh.ringworld.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object FontsUtil {

    var HOME_SCREEN_LOGO_FONT: BitmapFont
    var DEVELOPERS_SCREEN_FONT: BitmapFont
    var GAME_OVER_FONT: BitmapFont
    var SCORE_FONT: BitmapFont
    var ZAPPER_FONT: BitmapFont
    var PENDING_ZAPPER_FONT: BitmapFont
    var PLAY_BUTTON_FONT: BitmapFont
    var MODE_BUTTON_FONT: BitmapFont
    var GO_TO_HOME_FONT: BitmapFont
    var RESUME_BUTTON_FONT: BitmapFont
    var COPYRIGHT_FONT: BitmapFont

    init {
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()

        parameter.minFilter = Texture.TextureFilter.Linear
        parameter.magFilter = Texture.TextureFilter.Linear

        var generator = FreeTypeFontGenerator(Gdx.files.internal("Teacher_a.ttf"))
        parameter.size = 100
        parameter.color = Color.WHITE
        HOME_SCREEN_LOGO_FONT = generator.generateFont(parameter)
        generator.dispose()

        generator = FreeTypeFontGenerator(Gdx.files.internal("Quantify-Bold.ttf"))
        parameter.size = 50
        DEVELOPERS_SCREEN_FONT = generator.generateFont(parameter)
        parameter.size = 30
        PLAY_BUTTON_FONT = generator.generateFont(parameter)
        parameter.size = 30
        parameter.color = Color.BLACK
        RESUME_BUTTON_FONT = generator.generateFont(parameter)
        parameter.size = 20
        parameter.color = Color.WHITE
        MODE_BUTTON_FONT = generator.generateFont(parameter)
        parameter.size = 15
        COPYRIGHT_FONT = generator.generateFont(parameter)
        parameter.size = 30
        parameter.color = Color(0.9f, 0.9f, 0.9f, 1f)
        GO_TO_HOME_FONT = generator.generateFont(parameter)
        generator.dispose()

        generator = FreeTypeFontGenerator(Gdx.files.internal("Track.ttf"))
        parameter.size = 100
        parameter.color = Color.WHITE
        SCORE_FONT = generator.generateFont(parameter)
        parameter.size = 45
        ZAPPER_FONT = generator.generateFont(parameter)
        parameter.size = 30
        PENDING_ZAPPER_FONT = generator.generateFont(parameter)
        generator.dispose()

        generator = FreeTypeFontGenerator(Gdx.files.internal("VikingHell.ttf"))
        parameter.size = 100
        parameter.color = Color.WHITE
        GAME_OVER_FONT = generator.generateFont(parameter)
        generator.dispose()
    }
}
