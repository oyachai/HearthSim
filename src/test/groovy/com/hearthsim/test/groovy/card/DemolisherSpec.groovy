package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.Demolisher
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class DemolisherSpec extends CardSpec {

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
				hand([Demolisher])
				field(commonField)
				mana(7)
				deck([TheCoin, TheCoin])
			}
			waitingPlayer {
				mana(4)
				deck([TheCoin, TheCoin])
			}
		}

		root = new HearthTreeNode(startingBoard)
	}
	
	def "playing Demolisher"() {
		def cards = [ new TheCoin(), new TheCoin() ]
		def deck = new Deck(cards)
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 1, root, deck, deck)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(Demolisher)
				mana(4)
			}
		}

		def retAfterStartTurn = new HearthTreeNode(Game.beginTurn(ret.data_))
		assertBoardDelta(copiedBoard, retAfterStartTurn.data_) {
			currentPlayer {
				playMinion(Demolisher)
				mana(8)
				maxMana(8)
				addCardToHand(TheCoin)
				updateMinion(1, [hasAttacked: false, hasBeenUsed: false])
				addDeckPos(1)
			}
			waitingPlayer {
				heroHealth(28)
			}
		}

	}
	
}
