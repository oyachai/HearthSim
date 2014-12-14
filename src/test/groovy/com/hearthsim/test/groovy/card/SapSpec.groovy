package com.hearthsim.test.groovy.card

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals

import com.hearthsim.card.minion.concrete.AncientBrewmaster
import com.hearthsim.card.minion.concrete.StormwindChampion
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.spellcard.concrete.Sap
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class SapSpec extends CardSpec {
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([Sap])
				mana(7)
			}
			waitingPlayer {
				field([[minion: WarGolem]])
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "sap enemy minion"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

		expect:
		assertEquals(root, ret)

		assertBoardDelta(copiedBoard, root.data_) {
			currentPlayer {
				removeCardFromHand(Sap)
				mana(5)
			}
			waitingPlayer {
				addCardToHand(WarGolem)
				removeMinion(0)
			}
		}
	}

	def "minion destroyed if hand full"() {
		for (int indx = 0; indx < 10; ++indx) {
			startingBoard.placeCardHandWaitingPlayer(new TheCoin());
		}

		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

		expect:
		assertEquals(root, ret)

		assertBoardDelta(copiedBoard, root.data_) {
			currentPlayer {
				removeCardFromHand(Sap)
				mana(5)
			}
			waitingPlayer { removeMinion(0) }
		}
	}
}
