package com.namnh.ringworld.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object Constants {

    val WORLD_WIDTH = 800f
    val WORLD_HEIGHT = 450f
    val EASY_MODE = 0
    val MEDIUM_MODE = 1
    val HARD_MODE = 2
    val INSANE_MODE = 3
    val PREF_KEY = "ZapTapPrefs"
    val PLAY_COUNT = "playcount"
    val ZAPPER_COUNT = "zappercount"
    val MEDIUM_LOCKED = "medium_lock"
    val HARD_LOCKED = "hard_lock"
    val INSANE_LOCKED = "insane_lock"
    val SOUND_ON = "sound_on"
    val MUSIC: Music = Gdx.audio.newMusic(Gdx.files.internal("background-skyline.mp3"))
    //    public static final Texture COPYRIGHT_IMAGE = new Texture("copyright.png");
    //    public static final Texture FACEBOOK_IMAGE = new Texture("facebook.png");
    //    public static final Texture GOOGLEPLUS_IMAGE = new Texture("google-plus.png");
    //    public static final Texture GITHUB_IMAGE = new Texture("github-logo.png");
    //    public static final Texture CLOSE_IMAGE = new Texture("cancel.png");
    //    public static final Texture INFORMATION_IMAGE = new Texture("information.png");
}
