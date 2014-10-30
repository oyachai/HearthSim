package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode
import com.hearthsim.card.spellcard.concrete.Soulfire
import com.hearthsim.card.spellcard.concrete.Polymorph
import com.hearthsim.card.minion.concrete.WarGolem

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class SoulfireSpec extends CardSpec {
	
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
				hand([Soulfire, Polymorph, WarGolem])
				mana(7)
			}
			waitingPlayer {
				field(commonField)
				mana(4)
			}
		}

		
		root = new HearthTreeNode(startingBoard)
	}

	def "playing Soulfire"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null);

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				mana(7)
			}
		}

		assertTrue(ret instanceof RandomEffectNode)
		assertEquals(ret.numChildren(), 2)
		
		HearthTreeNode child0 = ret.getChildren().get(0);
		assertBoardDelta(copiedBoard, child0.data_) {
			currentPlayer {
				heroHealth(26)
				removeCardFromHand(Soulfire)
				removeCardFromHand(Polymorph)
			}
		}

		HearthTreeNode child1 = ret.getChildren().get(1);
		assertBoardDelta(copiedBoard, child1.data_) {
			currentPlayer {
				heroHealth(26)
				removeCardFromHand(Soulfire)
				removeCardFromHand(WarGolem)
			}
		}

	}
}
