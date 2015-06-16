package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.basic.minion.HealingTotem;
import com.hearthsim.card.basic.minion.SearingTotem;
import com.hearthsim.card.basic.minion.StoneclawTotem;
import com.hearthsim.card.basic.minion.WrathOfAirTotem;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Shaman extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }
        if (playerSide != PlayerSide.CURRENT_PLAYER) {
            return false;
        }
        if (boardModel.modelForSide(playerSide).isBoardFull()) {
            return false;
        }

        return true;
    }

    /**
     * Use the hero ability on a given target
     *
     * Warlock: place random totem on the board
     *
     *
     *
     * @param targetPlayerSide
     * @param targetMinion The target minion
     * @param boardState
     *
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(
            PlayerSide targetPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState) {
        PlayerModel player = boardState.data_.modelForSide(targetPlayerSide);

        HearthTreeNode toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.HERO_ABILITY, PlayerSide.CURRENT_PLAYER, 0, targetPlayerSide, CharacterIndex.HERO));
        Minion[] totems = {new SearingTotem(), new StoneclawTotem(), new HealingTotem(), new WrathOfAirTotem()};
        boolean allTotemsNotSummonable = true;
        for (Minion totemToSummon : totems) {
            boolean totemAlreadySummoned = false;
            for (Minion minion : player.getMinions()) {
                if (minion.getClass().equals(totemToSummon.getClass())) {
                    totemAlreadySummoned = true;
                    break;
                }
            }
            if (!totemAlreadySummoned) {
                allTotemsNotSummonable = false;

                HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
                PlayerModel newCurrentPlayer = newState.data_.getCurrentPlayer();

                newCurrentPlayer.subtractMana(HERO_ABILITY_COST);
                newCurrentPlayer.getHero().hasBeenUsed(true);

                totemToSummon.summonMinionAtEnd(targetPlayerSide, newState, false);
            }
        }
        if (allTotemsNotSummonable)
            return null;

        return toRet;
    }
}
