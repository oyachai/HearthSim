package com.hearthsim.test.groovy.card


import com.hearthsim.card.minion.concrete.BloodsailCorsair
import com.hearthsim.card.weapon.concrete.FieryWarAxe
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class BloodsailCorsairSpec extends CardSpec {

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
				hand([BloodsailCorsair])
				field(commonField)
				mana(7)
			}
			waitingPlayer {
				weapon(FieryWarAxe) {
                    weaponCharge(2)
				}
				field(commonField)
				mana(4)
			}
		}

		root = new HearthTreeNode(startingBoard)
	}

	def "playing Bloodsail Corsair with weapon"() {
		def copiedBoard = startingBoard.deepCopy()
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(BloodsailCorsair)
				mana(6)
			}
			waitingPlayer {
				weaponCharge(1)
			}
		}
	}
}
