package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.QuestingAdventurer
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*


class QuestingAdventurerSpec extends CardSpec {

	def "playing QuestingAdventurer"() {
		
		def startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([QuestingAdventurer])
				mana(9)
			}
			waitingPlayer {
				mana(9)
			}
		}

		def root = new HearthTreeNode(startingBoard)

		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertFalse(ret == null);
		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(QuestingAdventurer)
				mana(6)
			}
		}

	}
	
	def "playing a card with a Questing Adventurer on the field"() {
		
		def startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([TheCoin])
				field([[minion: QuestingAdventurer]])
				mana(9)
			}
			waitingPlayer {
				field([[minion: QuestingAdventurer]]) //This Questing Adventurer should not be buffed
				mana(9)
			}
		}

		def root = new HearthTreeNode(startingBoard)

		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertFalse(ret == null);
		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				removeCardFromHand(TheCoin)
				mana(10)
				updateMinion(0, [deltaHealth: 1, deltaAttack: 1, deltaMaxHealth: 1])
			}
		}

	}

	
}