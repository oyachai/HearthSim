package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

import com.hearthsim.card.minion.concrete.LeeroyJenkins
import com.hearthsim.card.minion.concrete.Whelp

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class LeeroyJenkinsSpec extends CardSpec {
	
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([LeeroyJenkins])
				mana(7)
			}
			waitingPlayer {
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Leeroy Jenkins"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:

		assertFalse(ret == null)
		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinionWithCharge(LeeroyJenkins)
				mana(2)
			}
			waitingPlayer {
				playMinion(Whelp)
				playMinion(Whelp)
			}
		}
	}
}
