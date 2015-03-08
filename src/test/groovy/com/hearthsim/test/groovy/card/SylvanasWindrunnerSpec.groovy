package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.SylvanasWindrunner
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.spellcard.concrete.ShadowWordDeath
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class SylvanasWindrunnerSpec extends CardSpec {

    
    HearthTreeNode root
    BoardModel startingBoard

    def "playing Sylvanas"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SylvanasWindrunner])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SylvanasWindrunner)
                mana(1)
                numCardsUsed(1)
            }
        }
    }
    
    def "with two enemy minion, playing Sylvanas and killing it"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SylvanasWindrunner, ShadowWordDeath])
                mana(9)
            }
            waitingPlayer {
                field([[minion: WarGolem],
                    [minion: GoldshireFootman]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        def swd = ret.data_.getCurrentPlayer().getHand().get(0)
        def sylvanas = ret.data_.getCharacter(CURRENT_PLAYER, 1)
        def ret2 = swd.useOn(CURRENT_PLAYER, sylvanas, ret, null, null)
        
        expect:
        assertFalse(ret2 == null);

        assertBoardDelta(copiedBoard, ret2.data_) {
            currentPlayer {
                playMinion(SylvanasWindrunner)
                removeMinion(0)
                removeCardFromHand(ShadowWordDeath)
                mana(0)
                numCardsUsed(2)
            }
        }
        
        assert ret2.numChildren() == 2
        
        HearthTreeNode child0 = ret2.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                playMinion(WarGolem)
                mana(0)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
        
        HearthTreeNode child1 = ret2.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                playMinion(GoldshireFootman)
                mana(0)
            }
            waitingPlayer {
                removeMinion(1)
            }
        }

    }
    
    def "enemy has Sylvanas on board, kill it with a War Golem"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion: WarGolem],
                    [minion: GoldshireFootman]])
                mana(9)
            }
            waitingPlayer {
                field([[minion: SylvanasWindrunner]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def attacker = root.data_.getCharacter(CURRENT_PLAYER, 1)
        def attacked = root.data_.getCharacter(WAITING_PLAYER, 1)
        def ret = attacker.attack(WAITING_PLAYER, attacked, root, null, null, false)
        
        expect:
        assertFalse(ret == null);
        assert ret.numChildren() == 2
        
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaHealth: -5, hasAttacked: true])
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                removeMinion(0)
            }
            waitingPlayer {
                addMinionToField(WarGolem)
                updateMinion(0, [deltaHealth: -5])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                removeMinion(1)
            }
            waitingPlayer {
                addMinionToField(GoldshireFootman)
            }
        }
    }
    
    def "with two enemy minion, playing Sylvanas and killing it, and then resolve the play"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SylvanasWindrunner, ShadowWordDeath])
                mana(9)
            }
            waitingPlayer {
                field([[minion: WarGolem],
                    [minion: GoldshireFootman]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.getCharacter(CURRENT_PLAYER, 0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        def swd = ret.data_.getCurrentPlayer().getHand().get(0)
        def sylvanas = ret.data_.getCharacter(CURRENT_PLAYER, 1)
        def ret2 = swd.useOn(CURRENT_PLAYER, sylvanas, ret, null, null)
        
        expect:
        assertTrue(ret2 instanceof RandomEffectNode);
        
        def ret3 = ret2.finishAllEffects(null, null);
        if (ret3.data_.getMinion(CURRENT_PLAYER, 0) instanceof WarGolem) {
            assertBoardDelta(copiedBoard, ret3.data_) {
                currentPlayer {
                    playMinion(SylvanasWindrunner)
                    removeMinion(0)
                    removeCardFromHand(ShadowWordDeath)
                    mana(0)
                    playMinion(WarGolem)
                    numCardsUsed(2)
                }
                waitingPlayer {
                    removeMinion(0)
                }
            }
        } else {
            assertBoardDelta(copiedBoard, ret3.data_) {
                currentPlayer {
                    playMinion(SylvanasWindrunner)
                    removeMinion(0)
                    removeCardFromHand(ShadowWordDeath)
                    mana(0)
                    playMinion(GoldshireFootman)
                    numCardsUsed(2)
                }
                waitingPlayer {
                    removeMinion(1)
                }
            }
        }
    }
}
