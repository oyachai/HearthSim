package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.minion.Hero
import com.hearthsim.card.minion.Minion
import com.hearthsim.card.weapon.WeaponCard
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.IdentityLinkedList
import spock.lang.Specification

class CardSpec extends Specification {

    void assertBoardDelta(BoardModel oldBoard, BoardModel newBoard, Closure deltaClosure) {
        def oldBoardCopy = oldBoard.deepCopy()
        def expectedBoard = new BoardModelBuilder(boardModel: oldBoardCopy).make(deltaClosure)
        assertBoardEquals(expectedBoard, newBoard)
    }

    //Trying to improve the legibility of output when the test fails
    void assertBoardEquals(BoardModel oldBoard, BoardModel newBoard) {
        assertPlayerEquals(oldBoard.currentPlayer, newBoard.currentPlayer)
        assertPlayerEquals(oldBoard.waitingPlayer, newBoard.waitingPlayer)
		assert oldBoard.allMinionsFIFOList_.size() == newBoard.allMinionsFIFOList_.size()
		for (int indx = 0; indx < oldBoard.allMinionsFIFOList_.size(); ++indx) {
			assertPlayerEquals(oldBoard.allMinionsFIFOList_.get(indx).getPlayerSide().getPlayer(oldBoard), newBoard.allMinionsFIFOList_.get(indx).getPlayerSide().getPlayer(newBoard))
			assertMinionEquals(oldBoard.allMinionsFIFOList_.get(indx).getMinion(), newBoard.allMinionsFIFOList_.get(indx).getMinion())
		}
        assert oldBoard == newBoard //for now, a catch all at the end
    }

    void assertPlayerEquals(PlayerModel oldPlayerModel, PlayerModel newPlayerModel) {
        assert oldPlayerModel.mana == newPlayerModel.mana
        assert oldPlayerModel.maxMana == newPlayerModel.maxMana
        assert oldPlayerModel.overload == newPlayerModel.overload
        assert oldPlayerModel.spellDamage == newPlayerModel.spellDamage
        assert oldPlayerModel.deckPos == newPlayerModel.deckPos
        assert oldPlayerModel.fatigueDamage == newPlayerModel.fatigueDamage
        assertHandEquals(oldPlayerModel.hand, newPlayerModel.hand)
        assertHeroEquals(oldPlayerModel.hero, newPlayerModel.hero)
        assertMinionsEqual(oldPlayerModel.minions, newPlayerModel.minions)
        assert oldPlayerModel.numCardsUsed == newPlayerModel.numCardsUsed
        assert oldPlayerModel == newPlayerModel //catch all
    }

    void assertHeroEquals(Hero oldHero, Hero newHero) {
        assert oldHero.health == newHero.health
        assert oldHero.armor == newHero.armor
        assert oldHero.attack == newHero.attack
        assert oldHero.frozen == newHero.frozen
        assert oldHero.windfury == newHero.windfury
        assertWeaponEquals(oldHero.weapon, newHero.weapon)
        assert oldHero == newHero //catch all
    }

    void assertHandEquals(IdentityLinkedList<Card> oldHand, IdentityLinkedList<Card> newHand) {
        assert oldHand.size() == newHand.size()
        for (int indx = 0; indx < oldHand.size(); ++indx) {
            assert oldHand.get(indx).baseManaCost == newHand.get(indx).baseManaCost
            assert oldHand.get(indx).hasBeenUsed == newHand.get(indx).hasBeenUsed
            assert oldHand.get(indx).inHand == newHand.get(indx).inHand
            assert oldHand.get(indx).manaDelta == newHand.get(indx).manaDelta
            if (oldHand.get(indx) instanceof Minion && newHand.get(indx) instanceof Minion) {
                assertMinionEquals((Minion)oldHand.get(indx), (Minion)newHand.get(indx))
            }

            assert oldHand.get(indx) == newHand.get(indx)
        }
    }

    void assertWeaponEquals(WeaponCard oldWeapon, WeaponCard newWeapon) {
        assert oldWeapon?.weaponCharge == newWeapon?.weaponCharge
        assert oldWeapon?.weaponDamage == newWeapon?.weaponDamage
        assert oldWeapon?.hasBeenUsed() == newWeapon?.hasBeenUsed()
        assert oldWeapon == newWeapon //catch all
    }

    void assertMinionsEqual(IdentityLinkedList<Minion> oldMinions, IdentityLinkedList<Minion> newMinions) {
        assert oldMinions.size() == newMinions.size()
        for (int indx = 0; indx < oldMinions.size(); ++indx) {
            Minion oldMinion = oldMinions.get(indx)
            Minion newMinion = newMinions.get(indx)
            assertMinionEquals(oldMinion, newMinion)
        }
    }
    
    void assertMinionEquals(Minion oldMinion, Minion newMinion) {
        assert oldMinion.attack == newMinion.attack
        assert oldMinion.baseAttack == newMinion.baseAttack
        assert oldMinion.auraAttack == newMinion.auraAttack
        assert oldMinion.health == newMinion.health
        assert oldMinion.maxHealth == newMinion.maxHealth
        assert oldMinion.auraHealth == newMinion.auraHealth
        assert oldMinion.charge == newMinion.charge
        assert oldMinion.taunt == newMinion.taunt
        assert oldMinion.divineShield == newMinion.divineShield
        assert oldMinion.frozen == newMinion.frozen
        assert oldMinion.hasAttacked() == newMinion.hasAttacked()
        assert oldMinion.heroTargetable == newMinion.heroTargetable
        assert oldMinion.silenced == newMinion.silenced
        assert oldMinion.stealthedUntilRevealed == newMinion.stealthedUntilRevealed
        assert oldMinion.stealthedUntilNextTurn == newMinion.stealthedUntilNextTurn
        assert oldMinion.hasBeenUsed == newMinion.hasBeenUsed
        assert oldMinion.cantAttack == newMinion.cantAttack
        assert oldMinion == newMinion //catch all
    }
}
