package com.hearthsim.test.groovy.card

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.EmperorCobra
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class EmperorCobraSpec extends CardSpec {

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
				hand([EmperorCobra])
				field(commonField + [minion: EmperorCobra])
				mana(7)
			}
			waitingPlayer {
				field(commonField)
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Emperor Cobra and attacking the Hero with it"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 2)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		def theCobra = ret.data_.getCharacter(CURRENT_PLAYER, 2)
		def ret2 = theCobra.attack(WAITING_PLAYER, ret.data_.getCharacter(WAITING_PLAYER, 0), ret, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret2.data_) {
			currentPlayer {
				playMinion(EmperorCobra)
				mana(4)
				updateMinion(1, [hasAttacked: true])
			}
			waitingPlayer {
				heroHealth(28)
			}
		}
	}
	
	def "playing Emperor Cobra and attacking a minion with it"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 2)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		def theCobra = ret.data_.getCharacter(CURRENT_PLAYER, 2)
		def ret2 = theCobra.attack(WAITING_PLAYER, ret.data_.getCharacter(WAITING_PLAYER, 1), ret, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret2.data_) {
			currentPlayer {
				playMinion(EmperorCobra)
				mana(4)
				removeMinion(1)
			}
			waitingPlayer {
				removeMinion(0)
			}
		}
	}
}
