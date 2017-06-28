package com.namnh.ringworld.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.DelayedRemovalArray
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.namnh.ringworld.RingWorld
import com.namnh.ringworld.actors.*
import com.namnh.ringworld.utils.Constants

class GameScreen(game: RingWorld, private var gameMode: Int) : BaseScreen(game) {
    private val spriteBatch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val viewport = ExtendViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT)

    private var rings = DelayedRemovalArray<Ring>()
    private var explosions = DelayedRemovalArray<Explosion>()
    private var isGameOver = false
    private var isStopped = false
    private var time = 0f
    private var timer = 0f
    private var activeDot: ActiveDot? = null
    private var sw = 1
    private lateinit var lastAttachedRing: Ring

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
        Gdx.input.isCatchBackKey = true

        shapeRenderer.setAutoShapeType(true)

        reset(gameMode)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        if (!isStopped) {
            time += 2 * delta
            timer += delta
        }
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        shapeRenderer.projectionMatrix = viewport.camera.combined
        spriteBatch.projectionMatrix = viewport.camera.combined

        if (!isStopped) {
            if (timer >= 0.5f) {
                timer -= 0.5f

                sw = -sw
                if (sw == 1) {
                    rings.add(DiscreteRing(shapeRenderer, gameMode))
                } else {
                    val ring = NormalRing(shapeRenderer)
                    rings.add(ring)
                    if (activeDot == null) {
                        activeDot = ActiveDot(shapeRenderer, ring)
                    }
                }
            }
        }

        renderRings(delta)

        renderExplosion(delta)

        if (!isStopped) {
            handleActiveDot(delta)
        }

        // score.render(delta);

        if (activeDot != null) {
            checkAndHandleGameOver()
        }

        if (isStopped) {
            handleStopped()
        }
    }

    private fun renderRings(delta: Float) {
        val iterate = rings.iterator()
        rings.begin()
        while (iterate.hasNext()) {
            if (iterate.next().radius < 10f) {
                iterate.remove()
            }
        }
        rings.end()

        for (i in rings.size - 1 downTo 0) {
            rings.get(i).render(delta)
        }
    }

    private fun renderExplosion(delta: Float) {
        for (exp in explosions) {
            exp.render(delta)
        }

        val iterate = explosions.iterator()
        explosions.begin()
        while (iterate.hasNext()) {
            if (iterate.next().time > 8f) {
                iterate.remove()
            }
        }

        explosions.end()
    }

    private fun handleActiveDot(delta: Float) {
        if (activeDot == null) {
            return
        }

        activeDot?.render(delta)

        if (!activeDot?.isDetached()!!) {
            return
        }

        val size = rings.size - 1

        loop@ for (i in size downTo 0) {
            val ring = rings.get(i)
            if (ring == lastAttachedRing) {
                continue@loop
            }

            val ringRadius = ring.radius
            if (activeDot == null) {
                return
            }

            val diff = ringRadius - activeDot?.rotatedRadius!!

            if (diff <= -15f || diff >= 7.5f) {
                return
            }

            when (ring) {
                is NormalRing -> {
                    activeDot?.attachedRing = ring
                    break@loop
                    // TODO("increase score here")
                }
                is DiscreteRing -> {
                    val dotAngle = activeDot?.angle!!
                    val arcNum = ring.arcNum
                    val rotation = ring.rotation
                    for (j in 0 until arcNum) {
                        val q1 = 360f / arcNum * j + rotation
                        val q2 = q1 + 180f / arcNum
                        if ((dotAngle >= (q1 - 5f) && dotAngle <= (q2 + 5f)) || (dotAngle + 360 >= (q1 - 5) && dotAngle + 360 <= (q2 + 5))) {
                            makeGameOver1()
                        }
                    }
                }
            }
        }
    }

    private fun checkAndHandleGameOver() {
        val rotatedRadius = activeDot?.rotatedRadius
        if (activeDot != null && (rotatedRadius!! > 300f || rotatedRadius < 12f)) {
            makeGameOver()
        }
    }

    private fun makeGameOver() {
        isStopped = true
        isGameOver = true

        for (ring in rings) {
            ring.stop()
        }

        createExplosions()
        activeDot = null
    }

    private fun makeGameOver1() {
        createExplosions()

        for (ring in rings) {
            ring.stop()
        }

        isStopped = true
        isGameOver = true
        activeDot = null
    }

    private fun handleStopped() {
        if (isGameOver) {
            handleGameOver()
        } else {
            // TODO("Just pause game")
        }
    }

    private fun handleGameOver() {
        // TODO("Just over game")
    }

    private fun createExplosions() {
        val num = MathUtils.random(40, 50)

        (0 until num).map {
            Explosion(shapeRenderer, activeDot?.position!!, (it * 360 / num).toFloat())
        }.forEach {
            explosions.add(it)
        }
    }

    private fun reset(gameMode: Int) {
        this.gameMode = gameMode
        resetGame()
    }

    private fun resetGame() {
        rings.clear()
        explosions.clear()
        time = 0f
        timer = 0f
        sw = 1
        activeDot = null
        isStopped = false
        isGameOver = false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        super.touchDown(screenX, screenY, pointer, button)
        //        val touchPos = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        if (activeDot != null) {
            lastAttachedRing = activeDot?.detach()!!
        }
        return true
    }

    override fun dispose() {
        super.dispose()
        spriteBatch.dispose()
        shapeRenderer.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.SPACE -> {
                if (activeDot != null) {
                    lastAttachedRing = activeDot!!.detach()!!
                }
            }
            Input.Keys.ESCAPE -> {
                if (!isStopped) {
                    pause()
                }
            }
            Input.Keys.BACK -> {
                if (!isStopped) {
                    pause()
                    return false
                }
                if (isStopped) {
                    game.startHomeScreen()
                }
            }
        }
        return false
    }
}