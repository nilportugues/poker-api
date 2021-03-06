package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.PlayerRepository
import com.lambtors.poker_api.module.poker.domain.error.PlayerNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, PlayerId}

final class PlayerCardsFinder(playerRepository: PlayerRepository)(implicit ec: ExecutionContext) {
  def find(playerId: PlayerId): Future[(Card, Card)] = playerRepository.search(playerId).flatMap { playerOption =>
    if (playerOption.isDefined) {
      Future.successful((playerOption.get.firstCard, playerOption.get.secondCard))
    } else {
      Future.failed(PlayerNotFound(playerId))
    }
  }
}
