package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

import com.hearthsim.card.spellcard.concrete.UnleashTheHounds
import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.Hound
import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class UnleashTheHoundsSpec extends CardSpec {
	
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		def commonField = [
			[mana: minionMana, attack: attack, maxHealth: health0], //TODO: attack may be irrelevant here
		]

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([UnleashTheHounds])
				mana(7)
			}
			waitingPlayer {
				field(commonField)
				mana(4)
			}
		}

		
		root = new HearthTreeNode(startingBoard)
	}

	def "playing UnleashTheHounds with 1 enemy minion"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				removeCardFromHand(UnleashTheHounds)
				addMinionToField(Hound, false, true)
				mana(4)
			}
		}

	}

	def "playing UnleashTheHounds with not enough room"() {
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
		startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor());

		def copiedBoard = startingBoard.deepCopy()

		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

		expect:
		assertNotNull(ret);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				removeCardFromHand(UnleashTheHounds)
				addMinionToField(Hound, false, true)
				mana(4)
			}
		}

	}
}
