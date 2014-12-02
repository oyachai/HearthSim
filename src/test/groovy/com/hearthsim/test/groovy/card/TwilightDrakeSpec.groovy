package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.card.minion.concrete.FlameImp
import com.hearthsim.card.minion.concrete.TwilightDrake
import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class TwilightDrakeSpec extends CardSpec {
	
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([TwilightDrake, FlameImp, FlameImp, FlameImp])
				mana(7)
			}
			waitingPlayer {
				mana(4)
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

	def "playing TwighlightDrake with 3 other cards in hand"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(TwilightDrake)
				mana(3)
                updateMinion(0, [deltaHealth: 3])
			}
		}

	}
}
