package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.goblinsvsgnomes.minion.common.Cogmaster
import com.hearthsim.card.goblinsvsgnomes.minion.common.SpiderTank
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class CogmasterSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ShadowWordPain])
                field([[minion: Cogmaster], [minion: SpiderTank]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "With a mech on the board, attack is 3"() {

        def cogmaster = root.data_.getCharacter(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1)

        expect:
        cogmaster.getTotalAttack(root, PlayerSide.CURRENT_PLAYER) == (byte)3

    }

    def "Without a mech on the board, attack is 1"() {

        def cogmaster = root.data_.getCharacter(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_1)
        def card = root.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHand().get(0)
        def ret = card.useOn(PlayerSide.CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        cogmaster.getTotalAttack(ret, PlayerSide.CURRENT_PLAYER) == (byte)1

    }
}
