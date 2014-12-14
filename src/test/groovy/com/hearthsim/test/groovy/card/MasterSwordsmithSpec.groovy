package com.hearthsim.test.groovy.card


import com.hearthsim.card.minion.concrete.MasterSwordsmith
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class MasterSwordsmithSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		def commonField = [
				[mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
		]

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([MasterSwordsmith])
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
	
	def "playing Master Swordsmith"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(MasterSwordsmith)
				mana(5)
			}
		}

		def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
		assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
			currentPlayer {
				playMinion(MasterSwordsmith)
				mana(5)
				updateMinion(0, [deltaAttack: 1])
			}
		}

	}
	
}