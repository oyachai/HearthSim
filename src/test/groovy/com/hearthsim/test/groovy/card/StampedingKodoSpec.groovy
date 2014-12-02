package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode
import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.minion.concrete.RiverCrocolisk
import com.hearthsim.card.minion.concrete.StampedingKodo

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class StampedingKodoSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([StampedingKodo])
				mana(7)
			}
			waitingPlayer {
				mana(7)
			}
		}

		root = new HearthTreeNode(startingBoard)
		
	}
	
	def "cannot play for waiting player's side"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(WAITING_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(WAITING_PLAYER, target, root, null, null)

		expect:

		assertTrue(ret == null)
		assertEquals(copiedBoard, startingBoard)
	}

	def "playing Stampeding Kodo while there are no other minions no board"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null)

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(StampedingKodo)
				mana(2)
			}
		}
		assertEquals(ret.numChildren(), 0)		
	}
	
	def "playing Stampeding Kodo with 2 qualified target"() {
		startingBoard.placeMinion(WAITING_PLAYER, new GoldshireFootman())
		startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor())
		startingBoard.placeMinion(WAITING_PLAYER, new RiverCrocolisk())
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null)

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(StampedingKodo)
				mana(2)
			}
		}
		
		assertTrue(ret instanceof RandomEffectNode)
		assertEquals(ret.numChildren(), 2)
		
		HearthTreeNode child0 = ret.getChildren().get(0);
		assertBoardDelta(startingBoard, child0.data_) {
			waitingPlayer {
				removeMinion(0)
			}
		}

		HearthTreeNode child1 = ret.getChildren().get(1);
		assertBoardDelta(startingBoard, child1.data_) {
			waitingPlayer {
				removeMinion(2)
			}
		}

	}	
}
