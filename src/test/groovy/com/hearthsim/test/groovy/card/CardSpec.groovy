package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.Hero
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerModel
import com.hearthsim.test.helpers.BoardModelBuilder
import org.junit.Assert
import spock.lang.Specification

class CardSpec extends Specification{

    void assertBoardDelta(BoardModel oldBoard, BoardModel newBoard, Closure deltaClosure) {
        def oldBoardCopy = oldBoard.deepCopy()
        def expectedBoard = new BoardModelBuilder(boardModel: oldBoardCopy).make(deltaClosure)
		assertBoardEquals(expectedBoard, newBoard)
    }
	
	
	//Trying to improve the legibility of output when the test fails
	void assertBoardEquals(BoardModel oldBoard, BoardModel newBoard) {
		assertPlayerEquals(oldBoard.currentPlayer, newBoard.currentPlayer)
		assertPlayerEquals(oldBoard.waitingPlayer, newBoard.waitingPlayer)
		Assert.assertEquals(oldBoard, newBoard) //for now, a catch all at the end
	}
	
	void assertPlayerEquals(PlayerModel oldPlayerModel, PlayerModel newPlayerModel) {
		Assert.assertEquals(oldPlayerModel.mana, newPlayerModel.mana)
		Assert.assertEquals(oldPlayerModel.maxMana, newPlayerModel.maxMana)
		Assert.assertEquals(oldPlayerModel.overload, newPlayerModel.overload)
		assertHeroEquals(oldPlayerModel.hero, newPlayerModel.hero)
		Assert.assertEquals(oldPlayerModel.minions.size(), newPlayerModel.minions.size())
		Assert.assertEquals(oldPlayerModel, newPlayerModel) //catch all
	}

	void assertHeroEquals(Hero oldHero, Hero newHero) {
		Assert.assertEquals(oldHero.health, newHero.health)
		Assert.assertEquals(oldHero.armor, newHero.armor)
		Assert.assertEquals(oldHero.attack, newHero.attack)
		Assert.assertEquals(oldHero.frozen, newHero.frozen)
		Assert.assertEquals(oldHero, newHero) //catch all
	}
}
