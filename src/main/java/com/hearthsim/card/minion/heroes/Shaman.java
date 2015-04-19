package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.basic.minion.HealingTotem;
import com.hearthsim.card.basic.minion.SearingTotem;
import com.hearthsim.card.basic.minion.StoneclawTotem;
import com.hearthsim.card.basic.minion.WrathOfAirTotem;
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
            HearthTreeNode boardState,
            boolean singleRealizationOnly) {
        PlayerModel player = boardState.data_.modelForSide(targetPlayerSide);

        if (singleRealizationOnly) {
            HearthTreeNode toRet = boardState;
            Minion minionToSummon = null;
            Minion[] allTotems = {new SearingTotem(), new StoneclawTotem(), new HealingTotem(), new WrathOfAirTotem()};
            for (int i = allTotems.length - 1; i > 0; --i) {
                int j = (int)(Math.random() * (i + 1));
                Minion ci = allTotems[i];
                allTotems[i] = allTotems[j];
                allTotems[j] = ci;
            }
            for (int index = 0; index < 4; ++index) {
                boolean totemAlreadySummoned = false;
                for (Minion minion : player.getMinions()) {
                    if (minion.getClass().equals(allTotems[index].getClass())) {
                        totemAlreadySummoned = true;
                    }
                }
                if (!totemAlreadySummoned) {
                    minionToSummon = allTotems[index];
                    break;
                }
            }
            if (minionToSummon == null)
                return null;
            this.hasBeenUsed = true;
            player.subtractMana(HERO_ABILITY_COST);
            toRet = minionToSummon.summonMinionAtEnd(targetPlayerSide, toRet, false, singleRealizationOnly);
            return toRet;
        }

        HearthTreeNode toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.HERO_ABILITY, PlayerSide.CURRENT_PLAYER, 0, targetPlayerSide, 0));
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

                totemToSummon.summonMinionAtEnd(targetPlayerSide, newState, false, singleRealizationOnly);
            }
        }
        if (allTotemsNotSummonable)
            return null;

        return toRet;
    }
}
