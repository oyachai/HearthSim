package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.DreadCorsair
import com.hearthsim.card.weapon.concrete.FieryWarAxe
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class DreadCorsairSpec extends CardSpec {
	HearthTreeNode root
	BoardModel startingBoard

	def setup() {

		def minionMana = 2;
		def attack = 5;
		def health0 = 3;
		def health1 = 7;

		startingBoard = new BoardModelBuilder().make {
			currentPlayer {
				hand([DreadCorsair, FieryWarAxe])
				mana(8)
			}
			waitingPlayer {
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}
	
	def "playing Dread Corsair with no weapon should use up 4 mana"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(DreadCorsair)
				mana(4)
			}
		}
	}
	
	def "playing Dread Corsair with Fiery War Axe should use up 1 mana"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(1)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)
		
		def dreadCorsair = root.data_.getCurrentPlayerCardHand(0)
		def ret2 = dreadCorsair.useOn(CURRENT_PLAYER, target, root, null, null)
		
		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(DreadCorsair)
                weapon(FieryWarAxe) {
                    weaponCharge(2)
                }
                removeCardFromHand(FieryWarAxe)
				mana(5)
			}
		}
	}
}
