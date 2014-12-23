package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.FlameImp
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class FlameImpSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		def commonField = [
			[mana: minionMana, attack: attack, maxHealth: health0],
			//TODO: attack may be irrelevant here
			[mana: minionMana, attack: attack, health: health1 - 1, maxHealth: health1]
		]

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([FlameImp])
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

	def "playing FlameImp damages the hero"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 2, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(FlameImp)
				mana(6)
				heroHealth(27)
			}
		}

	}

	def "playing FlameImp can kill own hero"() {
		def copiedBoard = startingBoard.deepCopy()
		root.data_.getCharacter(CURRENT_PLAYER, 0).setHealth((byte)2)

		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 2, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(FlameImp)
				mana(6)
				heroHealth(-1)
			}
		}
		
		assertTrue(ret.data_.isLethalState())
	}
}
