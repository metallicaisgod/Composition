package com.kirillmesh.composition.domain.usecases

import com.kirillmesh.composition.domain.entity.GameSettings
import com.kirillmesh.composition.domain.entity.Level
import com.kirillmesh.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}