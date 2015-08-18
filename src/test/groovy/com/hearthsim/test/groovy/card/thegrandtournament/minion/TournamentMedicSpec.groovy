package com.hearthsim.test.groovy.card.thegrandtournament.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.minion.heroes.Priest
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.thegrandtournament.minion.common.TournamentMedic
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

/**
 * Created by oyachai on 8/17/15.
 */
class TournamentMedicSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make(new Priest(), new TestHero(), {
            currentPlayer {
                heroHealth(10)
                field([[minion:TournamentMedic], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:TournamentMedic], [minion: WarGolem]])
            }
        })

        root = new HearthTreeNode(startingBoard)
    }

    def "using Hero ability buffs the minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def priest = root.data_.getCurrentPlayer().getHero()

        def ret = priest.useHeroAbility(CURRENT_PLAYER, CharacterIndex.HERO, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(8)
                heroHealth(14)
                heroHasBeenUsed(true)
            }
        }
    }
}
