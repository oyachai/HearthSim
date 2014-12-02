package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.card.minion.concrete.AncientBrewmaster
import com.hearthsim.card.minion.concrete.StormwindChampion

import spock.lang.Specification;
import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class AncientBrewmasterSpec extends CardSpec {

	HearthTreeNode root
	BoardModel startingBoard

	def setup() {
		
				startingBoard = new BoardModelBuilder().make {
					currentPlayer {
						hand([AncientBrewmaster])
						mana(7)
					}
					waitingPlayer {
						mana(7)
					}
				}
		
				root = new HearthTreeNode(startingBoard)
				
			}
	
	def "playing Ancient Brewmaster while there are no other minions no board"() {
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null)

		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(AncientBrewmaster)
				mana(3)
			}
		}
		assertEquals(ret.numChildren(), 0)
	}
	
	def "playing Ancient Brewmaster with one friendly minion on board"() {
		startingBoard.placeMinion(CURRENT_PLAYER, new StormwindChampion())
		def copiedBoard = startingBoard.deepCopy()
		def target = root.data_.getCharacter(CURRENT_PLAYER, 1)
		def theCard = root.data_.getCurrentPlayerCardHand(0)
		def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, root, null, null)

		expect:
		assertFalse(ret == null)
		assertEquals(ret.numChildren(), 1)
		
		assertBoardDelta(copiedBoard, ret.data_) {
			currentPlayer {
				playMinion(AncientBrewmaster)
				mana(3)
				updateMinion(1, [deltaAuraHealth: 1])
				updateMinion(1, [deltaAuraAttack: 1])
			}
		}
		
		HearthTreeNode child0 = ret.getChildren().get(0);
		assertBoardDelta(ret.data_, child0.data_) {
			currentPlayer {
				addCardToHand(StormwindChampion)
				removeMinion(0)
			}
		}


	}
}
