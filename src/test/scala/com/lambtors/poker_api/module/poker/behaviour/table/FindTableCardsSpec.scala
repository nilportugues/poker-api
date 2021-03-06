package com.lambtors.poker_api.module.poker.behaviour.table

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.table.find.{FindTableCardsQueryHandler, TableCardsFinder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{InvalidGameId, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.infrastructure.stub.{
  FindTableCardsQueryStub,
  GameIdStub,
  PokerGameStub,
  TableCardsResponseStub
}

class FindTableCardsSpec extends PokerBehaviourSpec {
  private val queryHandler = new FindTableCardsQueryHandler(new TableCardsFinder(pokerGameRepository))

  "Find table cards query" should {
    "return cards of the table" in {
      val query         = FindTableCardsQueryStub.random()
      val gameId        = GameIdStub.create(UUID.fromString(query.gameId))
      val pokerGame     = PokerGameStub.create(gameId)
      val tableResponse = TableCardsResponseStub.create(pokerGame.tableCards)

      shouldFindPokerGame(gameId, pokerGame)

      queryHandler.handle(query).futureValue should ===(tableResponse)
    }

    "return a failed future in case a game does not exists with the given id" in {
      val query = FindTableCardsQueryStub.random()

      val gameId = GameIdStub.create(UUID.fromString(query.gameId))

      shouldNotFindPokerGame(gameId)

      queryHandler.handle(query).failed.futureValue should ===(PokerGameNotFound(gameId))
    }

    "return a validation error on invalid game id" in {
      val query = FindTableCardsQueryStub.create(gameId = GameIdStub.invalid())

      queryHandler.handle(query).failed.futureValue should ===(InvalidGameId(query.gameId))
    }
  }
}
