package com.kirillmesh.composition.domain.repository

import com.kirillmesh.composition.domain.entity.GameSettings
import com.kirillmesh.composition.domain.entity.Level
import com.kirillmesh.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}