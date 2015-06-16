package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.MurlocRaider
import com.hearthsim.card.basic.minion.MurlocScout
import com.hearthsim.card.basic.minion.MurlocTidehunter
import com.hearthsim.card.classic.minion.rare.MurlocTidecaller
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertNotNull


class MurlocTidecallerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion: MurlocTidecaller]])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "playing murlocs adds attack"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new MurlocRaider()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MurlocRaider)
                mana(9)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1])
            }
        }
    }

    def "playing non-murlocs does nothing"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new GoldshireFootman()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(GoldshireFootman)
                mana(9)
                numCardsUsed(1)
            }
        }
    }

    def "enemy playing murloc adds attack"() {
        root.data_.placeMinion(WAITING_PLAYER, new MurlocTidecaller())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new MurlocRaider()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MurlocRaider)
                mana(9)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1])
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1])
            }
        }
    }

    def "buffs after battlecry"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new MurlocTidehunter()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(MurlocTidehunter)
                addMinionToField(MurlocScout)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2])
            }
        }
    }
}
