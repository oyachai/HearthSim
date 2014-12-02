package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.Hero
import com.hearthsim.card.minion.Minion
import com.hearthsim.entity.BaseEntity
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.MinionList
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
		Assert.assertEquals(oldPlayerModel.spellDamage, newPlayerModel.spellDamage)
		assertHeroEquals(oldPlayerModel.hero, newPlayerModel.hero)
		assertMinionsEqual(oldPlayerModel.minions, newPlayerModel.minions)
		Assert.assertEquals(oldPlayerModel, newPlayerModel) //catch all
	}

	void assertHeroEquals(Hero oldHero, Hero newHero) {
		Assert.assertEquals(oldHero.health, newHero.health)
		Assert.assertEquals(oldHero.armor, newHero.armor)
		Assert.assertEquals(oldHero.attack, newHero.attack)
		Assert.assertEquals(oldHero.frozen, newHero.frozen)
		Assert.assertEquals(oldHero, newHero) //catch all
	}
	
	void assertMinionsEqual(MinionList oldMinions, MinionList newMinions) {
		Assert.assertEquals(oldMinions.size(), newMinions.size())
		for (int indx = 0; indx < oldMinions.size(); ++indx) {
			BaseEntity oldMinion = oldMinions.get(indx)
			BaseEntity newMinion = newMinions.get(indx)
			Assert.assertEquals(oldMinion.attack, newMinion.attack)
			Assert.assertEquals(oldMinion.baseAttack_, newMinion.baseAttack_)
			Assert.assertEquals(oldMinion.auraAttack, newMinion.auraAttack)
			Assert.assertEquals(oldMinion.health, newMinion.health)
			Assert.assertEquals(oldMinion.baseHealth, newMinion.baseHealth)
			Assert.assertEquals(oldMinion.auraHealth, newMinion.auraHealth)
			Assert.assertEquals(oldMinion.charge, newMinion.charge)
			Assert.assertEquals(oldMinion.taunt, newMinion.taunt)
			Assert.assertEquals(oldMinion.divineShield, newMinion.divineShield)
			Assert.assertEquals(oldMinion.frozen, newMinion.frozen)
			Assert.assertEquals(oldMinion.hasAttacked(), newMinion.hasAttacked())
			Assert.assertEquals(oldMinion.heroTargetable, newMinion.heroTargetable)
			Assert.assertEquals(oldMinion.silenced, newMinion.silenced)
			Assert.assertEquals(oldMinion.stealthed, newMinion.stealthed)
			Assert.assertEquals(oldMinion, newMinion) //catch all
		}
		
	}
}
