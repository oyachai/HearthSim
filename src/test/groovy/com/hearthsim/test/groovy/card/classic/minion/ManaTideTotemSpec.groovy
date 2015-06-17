package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.rare.ManaTideTotem
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class ManaTideTotemSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([ManaTideTotem])
				deck([TheCoin])
				mana(7)
			}
			waitingPlayer {
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Master Swordsmith"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayer().getHand().get(0)
		def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(ManaTideTotem)
				mana(4)
                numCardsUsed(1)
			}
		}

		def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
		assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
			currentPlayer {
				playMinion(ManaTideTotem)
				mana(4)
				addCardToHand(TheCoin)
				addDeckPos(1)
                numCardsUsed(1)
			}
		}

	}

}
