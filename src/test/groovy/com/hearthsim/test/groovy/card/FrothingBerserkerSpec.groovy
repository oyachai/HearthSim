package com.hearthsim.test.groovy.card
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.FrothingBerserker
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class FrothingBerserkerSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		def commonField = [
				[mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
				[mana: minionMana, attack: attack, health: health1 - 1, maxHealth: health1]
		]

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([FrothingBerserker])
				field(commonField)
				mana(7)
			}
			waitingPlayer {
				field(commonField)
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}


	def "playing Frothing Berserker and attacking"() {
		def minionPlayedBoard = startingBoard.deepCopy()
		def copiedRoot = new HearthTreeNode(minionPlayedBoard)
		def target = minionPlayedBoard.getCharacter(CURRENT_PLAYER, 2);
		def theCard = minionPlayedBoard.getCurrentPlayerCardHand(0);
		def ret = theCard.useOn(CURRENT_PLAYER, target, copiedRoot, null, null);

		def attacker = minionPlayedBoard.getCharacter(CURRENT_PLAYER, 1)
		def attacked = minionPlayedBoard.getCharacter(WAITING_PLAYER, 1)
		ret =  attacker.attack(WAITING_PLAYER, attacked, copiedRoot, null, null)
		
		expect:
		assertFalse(ret == null);

		assertBoardDelta(startingBoard, minionPlayedBoard) {
			currentPlayer {
				playMinion(FrothingBerserker)
				removeMinion(0)
				updateMinion(1, [deltaAttack: 2])
				mana(4)
			}
			waitingPlayer {
				removeMinion(0)
			}
		}
	}
}