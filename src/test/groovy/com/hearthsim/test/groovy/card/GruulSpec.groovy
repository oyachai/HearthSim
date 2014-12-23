package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.Gruul
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class GruulSpec extends CardSpec {
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([Gruul])
				mana(8)
			}
			waitingPlayer {
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}
	
	def "playing Gruul"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(Gruul)
				mana(0)
			}
		}

		def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
		assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
			currentPlayer {
				playMinion(Gruul)
				mana(0)
				updateMinion(0, [deltaHealth: 1, deltaAttack: 1, deltaMaxHealth: 1])
			}
		}

	}
	
}
