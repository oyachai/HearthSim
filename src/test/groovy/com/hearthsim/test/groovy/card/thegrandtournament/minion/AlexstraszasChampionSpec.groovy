package com.hearthsim.test.groovy.card.thegrandtournament.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.minion.common.TwilightWhelp
import com.hearthsim.card.thegrandtournament.minion.rare.AlexstraszasChampion
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class AlexstraszasChampionSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AlexstraszasChampion])
                field([[minion:AxeFlinger]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "no buffs when not holding a dragon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AlexstraszasChampion, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "triggers buffs when holding a dragon"() {
        startingBoard.placeCardHand(CURRENT_PLAYER, new TwilightWhelp())
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AlexstraszasChampion, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1, charge: true])
            }
        }
    }
}
