package com.hearthsim.test.groovy.card

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.PatientAssassin
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class PatientAssassinSpec extends CardSpec {
	
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health = 3;

		def commonField = [
			[mana: minionMana, attack: attack, maxHealth: health],
		]

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([PatientAssassin])
				field([[minion: PatientAssassin]])
				mana(7)
			}
			waitingPlayer {
				field(commonField)
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Patient Assassin and attacking the Hero with it"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 1)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		def patientAssassin = ret.data_.getCharacter(CURRENT_PLAYER, 1)
		def ret2 = patientAssassin.attack(WAITING_PLAYER, ret.data_.getCharacter(WAITING_PLAYER, 0), ret, null, null, false)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret2.data_) {
			currentPlayer {
				playMinion(PatientAssassin)
				mana(5)
				updateMinion(0, [hasAttacked: true, stealthed: false])
			}
			waitingPlayer {
				heroHealth(29)
			}
		}
	}
	
	def "playing Patient Assassin and attacking a minion with it"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 1)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		def patientAssassin = ret.data_.getCharacter(CURRENT_PLAYER, 1)
		def ret2 = patientAssassin.attack(WAITING_PLAYER, ret.data_.getCharacter(WAITING_PLAYER, 1), ret, null, null, false)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret2.data_) {
			currentPlayer {
				playMinion(PatientAssassin)
				mana(5)
				removeMinion(0)
			}
			waitingPlayer {
				removeMinion(0)
			}
		}
	}
}
