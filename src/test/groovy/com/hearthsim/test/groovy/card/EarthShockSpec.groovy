package com.hearthsim.test.groovy.card

import com.hearthsim.card.spellcard.concrete.EarthShock
import com.hearthsim.card.minion.concrete.Boar
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class EarthShockSpec extends CardSpec {
	
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([EarthShock])
				mana(7)
			}
			waitingPlayer {
				mana(4)
				field([[minion: Boar, health: 2, maxHealth: 2]])
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Earth Shock on a buffed health 1 target"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				mana(6)
				removeCardFromHand(EarthShock)
			}
			waitingPlayer {
				removeMinion(0);
			}
		}

	}
}
