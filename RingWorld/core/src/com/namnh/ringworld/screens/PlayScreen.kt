package com.namnh.ringworld.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.DelayedRemovalArray
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.namnh.ringworld.RingWorld
import com.namnh.ringworld.actors.*
import com.namnh.ringworld.utils.Constants
import com.namnh.ringworld.utils.Encrypt
import com.namnh.ringworld.utils.FontsUtil

class PlayScreen(game: RingWorld, private var playMode: Int) : BaseScreen(game) {

    private var extendViewport: ExtendViewport? = null
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private var spriteBatch: SpriteBatch = SpriteBatch()

    private var sw = 1
    private var time: Float = 0.toFloat()

    private var timer: Float = 0.toFloat()

    private var rings: DelayedRemovalArray<Ring>? = null
    private var explosionTriangles: DelayedRemovalArray<ExplosionTriangle>? = null

    private var playBall: PlayBall? = null

    private var lastAttachedRing: Ring? = null
    private var score: Score? = null

    private var stopped = false
    private var paused = false
    private var gameOver = false

    private var bannerColor: Color? = null

    private var pauseButton: Texture? = null
    private var zapperImage: Texture? = null
    private var sadImage: Texture? = null
    private var restartImage: Texture? = null

    private var bounds: Rectangle? = null
    private var pauseButtonBounds: Rectangle? = null
    private var goToHomeBounds: Rectangle? = null
    private var continueZapperButtonBounds: Rectangle? = null
    private var restartButtonBounds: Rectangle? = null

    private var zappers: Zappers? = null

    private var chancesLeft: Int = 0
    private lateinit var preferences: Preferences

    override fun show() {

        Gdx.input.inputProcessor = this
        Gdx.input.isCatchBackKey = true

        shapeRenderer = ShapeRenderer()
        spriteBatch = SpriteBatch()
        shapeRenderer.setAutoShapeType(true)
        extendViewport = ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT)

        rings = DelayedRemovalArray()
        explosionTriangles = DelayedRemovalArray()

        bounds = Rectangle()
        time = 0f
        timer = 0f

        preferences = Gdx.app.getPreferences(Constants.PREF_KEY)
        zappers = Zappers(spriteBatch, shapeRenderer, preferences,
                Vector2(Constants.WORLD_WIDTH - 150, Constants.WORLD_HEIGHT - 70))

        zappers!!.giveInitZappers()

        bannerColor = Color(0f, 0f, 0f, 0.8f)
        score = Score(spriteBatch)
        playBall = null

        pauseButton = Texture("pause.png")
        zapperImage = Texture("zapper.png")
        sadImage = Texture("sad.png")
        restartImage = Texture("replay.png")

        pauseButtonBounds = Rectangle(Constants.WORLD_WIDTH - 100, 0f, 100f, 100f)
        goToHomeBounds = Rectangle(Constants.WORLD_WIDTH / 2 - 150,
                Constants.WORLD_HEIGHT / 6 - 100, 300f, 100f)
        continueZapperButtonBounds = Rectangle()
        restartButtonBounds = Rectangle(Constants.WORLD_WIDTH - 100, 0f, 100f, 100f)

        reset(playMode)
    }

    override fun render(delta: Float) {

        if (!stopped) {
            time += 2 * delta
            timer += delta
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        extendViewport!!.apply()
        shapeRenderer.projectionMatrix = extendViewport!!.camera.combined
        spriteBatch.projectionMatrix = extendViewport!!.camera.combined

        if (!stopped) {
            if (timer >= 0.5) {
                timer -= 0.5f

                // dirty hack
                sw = -sw
                if (sw == 1) {
                    rings!!.add(CrossMeRing(shapeRenderer, playMode))
                } else {
                    val q = NormalRing(shapeRenderer)
                    rings!!.add(q)
                    if (playBall == null) {
                        playBall = PlayBall(shapeRenderer, q)
                    }
                }
            }
        }

        rings!!.begin()
        (0 until rings!!.size).filter { rings!!.get(it).radius < 10 }.forEach {
            rings!!.removeIndex(it)
        }
        rings!!.end()

        for (i in rings!!.size - 1 downTo 0) {
            rings!!.get(i).render(delta)
        }

        for (tri in explosionTriangles!!) {
            tri.render(delta)
        }

        explosionTriangles!!.begin()
        (0 until explosionTriangles!!.size).filter {
            explosionTriangles!!.get(it).time > 8f
        }.forEach { explosionTriangles!!.removeIndex(it) }
        explosionTriangles!!.end()

        if (!stopped) {
            handleBall(delta)
        }

        score!!.render(delta)

        if (playBall != null) {
            if (playBall!!.rotateRadius > 300) {
                stopped = true
                gameOver = true
                for (r in rings!!) {
                    r.stop()
                }
                createExplosion()
                playBall = null
            } else if (playBall!!.rotateRadius < 12) {
                stopped = true
                gameOver = true
                for (r in rings!!) {
                    r.stop()
                }
                createExplosion()
                playBall = null

            }
        }

        zappers!!.render(delta)


        if (!stopped) drawPauseButton()

        if (stopped) handleStoppedCase()

    }

    private fun handleStoppedCase() {

        drawBanner()
        writeText("Go to Home", Constants.WORLD_WIDTH / 2 - 130, Constants.WORLD_HEIGHT / 8,
                FontsUtil.GO_TO_HOME_FONT)

        if (gameOver) {
            handleGameOver()
        } else if (paused) {
            writeText("   Paused", Constants.WORLD_WIDTH / 4.5f, Constants.WORLD_HEIGHT / 2 + 50,
                    FontsUtil.GAME_OVER_FONT)
            drawButtonWithText("    RESUME", 0)
        }
    }

    private fun handleGameOver() {
        writeText("Game Over", Constants.WORLD_WIDTH / 4.5f, Constants.WORLD_HEIGHT / 2 + 100,
                FontsUtil.GAME_OVER_FONT)

        val zapperCount = Encrypt.decrypt(preferences.getString(Constants.ZAPPER_COUNT))
        if (chancesLeft <= 0) {
            writeText("No Chances Left", Constants.WORLD_WIDTH / 4.5f, Constants.WORLD_HEIGHT / 2,
                    FontsUtil.GO_TO_HOME_FONT)
            spriteBatch.begin()
            spriteBatch.draw(sadImage, 3 * Constants.WORLD_WIDTH / 4 - 40,
                    Constants.WORLD_HEIGHT / 2 - 35, 40f, 40f)
            spriteBatch.end()
        } else if (zapperCount < 13) {
            writeText("Not enough Zappers", Constants.WORLD_WIDTH / 4.5f - 50,
                    Constants.WORLD_HEIGHT / 2, FontsUtil.GO_TO_HOME_FONT)
            spriteBatch.begin()
            spriteBatch.draw(sadImage, 3 * Constants.WORLD_WIDTH / 4 - 30,
                    Constants.WORLD_HEIGHT / 2 - 40, 40f, 40f)
            spriteBatch.end()
        } else {
            writeText("Continue using 100 ", Constants.WORLD_WIDTH / 4.5f - 30,
                    Constants.WORLD_HEIGHT / 2, FontsUtil.GO_TO_HOME_FONT)
            continueZapperButtonBounds!!.set(Constants.WORLD_WIDTH / 4.5f - 30,
                    Constants.WORLD_HEIGHT / 2 - 50, 800f, 100f)
            spriteBatch.begin()
            spriteBatch.draw(zapperImage, 3 * Constants.WORLD_WIDTH / 4 - 40,
                    Constants.WORLD_HEIGHT / 2 - 40, 40f, 40f)
            spriteBatch.end()
        }
        if (zappers!!.pendingZappers > 0) {
            drawButtonWithText("Claim Zappers", -20)
        } else {
            drawButtonWithText("    Claimed  ", -20)
        }
        drawRestartButton()
    }

    private fun drawRestartButton() {
        spriteBatch.begin()
        spriteBatch.draw(restartImage, restartButtonBounds!!.x + 10, restartButtonBounds!!.y + 20,
                80f, 80f)
        spriteBatch.end()
    }

    private fun drawPauseButton() {
        spriteBatch.begin()
        spriteBatch.draw(pauseButton, Constants.WORLD_WIDTH - 90, 20f, 80f, 80f)
        spriteBatch.end()
    }

    private fun drawButtonWithText(text: String, displacementY: Int) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(Constants.WORLD_WIDTH / 2 - 155,
                Constants.WORLD_HEIGHT / 3 - 25 + displacementY, 298f, 50f)
        shapeRenderer.end()

        bounds!!.set(Constants.WORLD_WIDTH / 2 - 155,
                Constants.WORLD_HEIGHT / 3 - 25 + displacementY, 295f, 50f)

        spriteBatch.begin()
        FontsUtil.RESUME_BUTTON_FONT.draw(spriteBatch, text, Constants.WORLD_WIDTH / 2 - 155,
                Constants.WORLD_HEIGHT / 3 + 15 + displacementY)
        spriteBatch.end()

    }

    private fun writeText(text: String, scrX: Float, scrY: Float, font: BitmapFont) {
        spriteBatch.begin()
        font.draw(spriteBatch, text, scrX, scrY)
        spriteBatch.end()
    }

    private fun drawBanner() {

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = bannerColor
        shapeRenderer.rect(0f, 0f, Constants.WORLD_WIDTH, 3 * Constants.WORLD_HEIGHT / 4)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

    }

    private fun handleBall(delta: Float) {

        if (playBall == null) {
            Gdx.app.log("ball", "null")
            return
        }

        playBall!!.render(delta)

        if (playBall!!.isDetached) {

            for (i in rings!!.size - 1 downTo 0) {

                val ring = rings!!.get(i)

                if (ring == lastAttachedRing) {
                    continue
                }

                val r = ring.radius

                if (playBall == null) {
                    return
                }
                val diff = r - playBall!!.rotateRadius

                if (diff < 7.5f && diff > -15) {
                    if (ring.javaClass == NormalRing::class.java) {
                        playBall!!.setAttachedRing(ring as NormalRing)
                        score!!.increase()
                        if (score!!.score % 20 == 0 && score!!.score > 0) {
                            zappers!!.increasePendingZappers()
                        }
                        break
                    } else {

                        val cmr = ring as CrossMeRing
                        val ballAngle = playBall!!.angle

                        val arcCount = cmr.arcNum
                        val rot = cmr.rot


                        for (j in 0 until arcCount) {

                            val q1 = 360 / arcCount * j + rot
                            val q2 = q1 + 180 / arcCount

                            if (ballAngle >= q1 - 5 && ballAngle <= q2 + 5) {

                                createExplosion()

                                for (mRing in rings!!) {
                                    mRing.stop()
                                }
                                stopped = true
                                gameOver = true
                                playBall = null
                            } else if (ballAngle + 360 >= q1 - 5 && ballAngle + 360 <= q2 + 5) {

                                createExplosion()

                                for (mRing in rings!!) {
                                    mRing.stop()
                                }

                                stopped = true
                                gameOver = true
                                playBall = null
                            }
                        }
                    }
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        //Gdx.app.log("width", String.valueOf(width));
        extendViewport!!.update(width, height, true)
    }

    private fun createExplosion() {

        val n = MathUtils.random(20, 30)
        (0 until n).map {
            ExplosionTriangle(shapeRenderer, playBall!!.position, it * 360f / n)
        }.forEach { explosionTriangles!!.add(it) }
        //        game.getPlatformHelper().submitScore(score.getScore(),playMode);
        //        Gdx.input.vibrate(500)

    }

    override fun pause() {
        paused = true
        stopped = true
        for (r in rings!!) {
            r.stop()
        }
    }

    override fun resume() {
        paused = false
        if (!gameOver) {
            stopped = false
        }
        for (r in rings!!) {
            r.start()
        }
    }

    override fun hide() {

    }

    private fun reset(playMode: Int) {

        this.playMode = playMode
        reset()
    }

    private fun reset() {

        explosionTriangles!!.clear()
        rings!!.clear()
        time = 0f
        timer = 0f
        sw = 1
        playBall = null
        stopped = false
        gameOver = false
        zappers!!.resetPendingZappers()
        score!!.reset()
        zappers!!.giveInitZappers()

        var playNum = preferences!!.getInteger(Constants.PLAY_COUNT)
        playNum++
        Gdx.app.log("play_Count", playNum.toString())
        preferences.putInteger(Constants.PLAY_COUNT, playNum).flush()

        //        if(playNum >= 50){
        //            game.getPlatformHelper().unlockAchievement();
        //        }

        chancesLeft = 2
    }

    override fun dispose() {
        spriteBatch.dispose()
        shapeRenderer.dispose()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        super.touchDown(screenX, screenY, pointer, button)

        val touchPos = extendViewport!!.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (restartButtonBounds!!.contains(touchPos)) {
            if (gameOver) {
                reset()
                return true
            }
        }

        if (gameOver) {
            val zapperCount = Encrypt.decrypt(preferences!!.getString(Constants.ZAPPER_COUNT))
            if (zapperCount >= 100 && chancesLeft > 0) {
                if (continueZapperButtonBounds!!.contains(touchPos)) {
                    continueGameWithSameScore()
                }
            }
        }

        if (!paused) {
            if (pauseButtonBounds!!.contains(touchPos)) {
                pause()
                return true
            }
        }


        if (stopped) {
            if (bounds!!.contains(touchPos)) {
                if (paused) {
                    resume()
                } else if (gameOver) {
                    if (zappers!!.pendingZappers > 0) {
                        //game.getPlatformHelper().claimTheZappers(zappers.getPendingZappers());
                        zappers!!.resetPendingZappers()
                    }
                }
            } else if (goToHomeBounds!!.contains(touchPos)) {
                game.setHomeScreen()
            }
            return true
        }


        if (playBall != null) {
            lastAttachedRing = playBall!!.detach()
            Gdx.app.log("Hi", "touchDown")
        }

        return true
    }

    private fun continueGameWithSameScore() {
        explosionTriangles!!.clear()
        rings!!.clear()
        time = 0f
        timer = 0f
        sw = 1
        playBall = null
        stopped = false
        gameOver = false

        chancesLeft--

        var currentZapperCount = Encrypt.decrypt(preferences.getString(Constants.ZAPPER_COUNT))
        currentZapperCount -= 100
        preferences.putString(Constants.ZAPPER_COUNT, Encrypt.encrypt(currentZapperCount)).flush()

    }

    override fun keyDown(keycode: Int): Boolean {

        if (keycode == Input.Keys.SPACE) {
            if (playBall != null) {
                lastAttachedRing = playBall!!.detach()
            }
        }

        if (keycode == Input.Keys.ESCAPE) {
            if (!stopped) {
                pause()
            }
        }

        if (keycode == Input.Keys.BACK) {
            if (!stopped) {
                pause()
                return false
            }
            if (stopped) {
                game.setHomeScreen()
            }
        }

        return false
    }

    fun setPlayMode(mode: Int) {
        playMode = mode
    }
}
