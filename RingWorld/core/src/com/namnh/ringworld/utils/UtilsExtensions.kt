package com.namnh.ringworld.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

fun Color.randomColor(): Color = this.set(MathUtils.random(0.3f, 0.8f),
        MathUtils.random(0.3f, 0.8f), MathUtils.random(0.3f, 0.8f), 1f)

fun Color.randomColor1(): Color = this.set(MathUtils.random(), MathUtils.random(),
        MathUtils.random(), 1f)

