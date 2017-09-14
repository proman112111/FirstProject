package com.namnh.ringworld.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.namnh.ringworld.RingWorld
import com.namnh.ringworld.actors.*
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.FontsUtil

class HomeScreen(game: RingWorld) : BaseScreen(game) {

    private var viewport: ExtendViewport? = null

    private var playRippleButton: Ripple? = null

    private var developersOverlay: DevelopersOverlay? = null
    private var tutorialOverlay: TutorialOverlay? = null

    private var backgroundColor: Color? = null
    private var destColor: Color? = null
    private var colorAction: ColorAction? = null
    private var counter: Float = 0.toFloat()

    private var batch: SpriteBatch = SpriteBatch()
    private var renderer: ShapeRenderer = ShapeRenderer()

    private var scoreTrophy: Texture? = null
    private var achievementMedal: Texture? = null

    private var leaderBoard: CircleButton? = null
    private var firstButton: CircleButton? = null

    private lateinit var buttons: Array<Button?>
    private var zappers: Zappers? = null
    private var earnZapperButton: EarnZapperButton? = null
    private var soundOnOffButton: SoundOnOffButton? = null

    private var infoButtonBounds: Rectangle? = null
    private var infoCloseBounds: Rectangle? = null

    private var developerScreenShown = false

    private lateinit var preferences: Preferences

    private var playTapped = false
    private var hardLocked: Boolean = false
    private var mediumLocked: Boolean = false
    private var insaneLocked: Boolean = false

    override fun show() {

        Gdx.input.inputProcessor = this
        Gdx.input.isCatchBackKey = true

        infoButtonBounds = Rectangle(0f, 0f, 100f, 100f)
        infoCloseBounds = Rectangle(Constants.WORLD_WIDTH - 100, Constants.WORLD_HEIGHT - 100, 100f,
                100f)
        backgroundColor = Color(Color.TEAL)
        destColor = Color(Color.MAGENTA)
        colorAction = ColorAction()
        colorAction!!.color = backgroundColor
        colorAction!!.duration = 2f
        colorAction!!.endColor = destColor

        scoreTrophy = Texture("trophy.png")
        achievementMedal = Texture("medal.png")

        renderer = ShapeRenderer()
        playRippleButton = Ripple(renderer,
                Vector2(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 3), 60f, 100f)

        leaderBoard = CircleButton(renderer, 3 * Constants.WORLD_WIDTH / 4,
                Constants.WORLD_HEIGHT / 3, 60)
        firstButton = CircleButton(renderer, Constants.WORLD_WIDTH / 4, Constants.WORLD_HEIGHT / 3,
                60)

        batch = SpriteBatch()
        viewport = ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT)

        developersOverlay = DevelopersOverlay(renderer, batch)
        tutorialOverlay = TutorialOverlay(batch, renderer)

        preferences = Gdx.app.getPreferences(Constants.PREF_KEY)
        zappers = Zappers(batch, renderer, preferences, Vector2(10f, Constants.WORLD_HEIGHT - 70))
        earnZapperButton = EarnZapperButton(batch, renderer)


        soundOnOffButton = SoundOnOffButton(batch, preferences)

        initModeLockStates()

        buttons = arrayOfNulls(4)
        buttons[0] = Button(renderer, 10f, 10f, 180f, 60f, "Easy", false,
                FontsUtil.MODE_BUTTON_FONT, batch)
        buttons[1] = Button(renderer, 10 + 200f, 10f, 180f, 60f, "Medium", mediumLocked,
                FontsUtil.MODE_BUTTON_FONT, batch)
        buttons[2] = Button(renderer, 10 + 400f, 10f, 180f, 60f, "Hard", hardLocked,
                FontsUtil.MODE_BUTTON_FONT, batch)
        buttons[3] = Button(renderer, 10 + 600f, 10f, 180f, 60f, "Insane", insaneLocked,
                FontsUtil.MODE_BUTTON_FONT, batch)

    }

    private fun initModeLockStates() {
        if (preferences.getBoolean(Constants.MEDIUM_LOCKED, true)) {
            mediumLocked = true
            preferences.putBoolean(Constants.MEDIUM_LOCKED, true).flush()
        } else {
            mediumLocked = false
        }
        if (preferences.getBoolean(Constants.HARD_LOCKED, true)) {
            hardLocked = true
            preferences.putBoolean(Constants.HARD_LOCKED, true).flush()
        } else {
            hardLocked = false
        }
        if (preferences.getBoolean(Constants.INSANE_LOCKED, true)) {
            insaneLocked = true
            preferences.putBoolean(Constants.INSANE_LOCKED, true).flush()
        } else {
            insaneLocked = false
        }
    }

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(backgroundColor!!.r, backgroundColor!!.g, backgroundColor!!.b,
                backgroundColor!!.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport!!.apply()
        batch.projectionMatrix = viewport!!.camera.combined
        renderer.projectionMatrix = viewport!!.camera.combined

        playRippleButton!!.render(delta)

        firstButton!!.render(delta)
        leaderBoard!!.render(delta)

        chameleonizeTheBackground(delta)
        writeLogo()

        soundOnOffButton!!.render(delta)

        batch.begin()
        batch.draw(scoreTrophy, 3 * Constants.WORLD_WIDTH / 4 - 30, Constants.WORLD_HEIGHT / 4 + 10,
                60f, 60f)
        batch.draw(achievementMedal, Constants.WORLD_WIDTH / 4 - 30,
                Constants.WORLD_HEIGHT / 4 + 10, 60f, 60f)
        batch.end()


        if (playTapped) {
            for (i in buttons.indices) {
                buttons[i]?.render(delta)
            }
        }

        zappers!!.render(delta)

        if (developerScreenShown) {
            developersOverlay!!.render(delta)
            tutorialOverlay!!.render(delta)
        }
        if (!playTapped && !developerScreenShown) {
            batch.begin()
            //            batch.draw(Constants.INFORMATION_IMAGE, 20, 10, 50, 50);
            batch.end()
        }

    }

    private fun writeLogo() {
        batch.begin()
        FontsUtil.HOME_SCREEN_LOGO_FONT.draw(batch, "Zap Tap", Constants.WORLD_WIDTH / 4,
                5 * Constants.WORLD_HEIGHT / 7)
        if (playTapped) {
            FontsUtil.PLAY_BUTTON_FONT.draw(batch, "MODE", Constants.WORLD_WIDTH / 2 - 58,
                    Constants.WORLD_HEIGHT / 3 + 10)
        } else {
            FontsUtil.PLAY_BUTTON_FONT.draw(batch, "PLAY", Constants.WORLD_WIDTH / 2 - 55,
                    Constants.WORLD_HEIGHT / 3 + 10)
        }
        batch.end()
    }

    private fun chameleonizeTheBackground(delta: Float) {
        counter += delta

        colorAction!!.act(delta)

        if (counter > 2) {
            counter -= 2f
            destColor = destColor!!.set(MathUtils.random(0.3f, 0.8f), MathUtils.random(0.3f, 0.8f),
                    MathUtils.random(0.3f, 0.8f), 1f)
            colorAction!!.reset()
            colorAction!!.color = backgroundColor
            colorAction!!.duration = 2f
            colorAction!!.endColor = destColor
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height, true)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val value = super.touchDown(screenX, screenY, pointer, button)

        val position = viewport!!.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (!playTapped) {
            if (infoButtonBounds!!.contains(position)) {
                developerScreenShown = true
                return value
            }
            if (developersOverlay!!.githubBounds.contains(position)) {
                Gdx.net.openURI("https://github.com/betterclever")
            }
            if (developersOverlay!!.facebookBounds.contains(position)) {
                Gdx.net.openURI("https://facebook.com/betterclever")
            }
            if (developersOverlay!!.googleplusBounds.contains(position)) {
                Gdx.net.openURI("https://plus.google.com/u/0/104046348939694351090")
            }
        }

        if (developerScreenShown) {
            if (infoCloseBounds!!.contains(position)) {
                developerScreenShown = false
            }
            return value
        }

        if (playRippleButton!!.isTouched(position)) {
            playTapped = !playTapped
            initModeLockStates()
        }

        if (soundOnOffButton!!.isTouched(position)) {
            soundOnOffButton!!.toggle()
        }

        if (firstButton!!.isTouched(position)) {
            //            game.getPlatformHelper().showAchievement();
            //            game.getPlatformHelper().submitAllScores();
        }

        if (leaderBoard!!.isTouched(position)) {
            //            game.getPlatformHelper().showScore();
        }

        if (playTapped) {
            for (i in buttons.indices) {
                if (buttons[i]!!.isTouched(position)) {
                    playTapped = false
                    game.startPlay(i)
                }
            }
        }

        return value
    }

    override fun keyDown(keycode: Int): Boolean {
        val s = super.keyDown(keycode)
        if (keycode == Input.Keys.BACK) {
            game.exit()
        }
        return s
    }
}
