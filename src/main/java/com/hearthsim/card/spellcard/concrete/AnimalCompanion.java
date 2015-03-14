package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class AnimalCompanion extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public AnimalCompanion(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     * Defaults to hasBeenUsed = false
     */
    public AnimalCompanion() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetEnemyMinions = false;
        this.canTargetOwnMinions = false;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (boardModel.modelForSide(playerSide).isBoardFull()) {
            return false;
        }

        return true;
    }

    /**
     * Use the card on the given target
     * Summons either Huffer, Leokk, or Misha
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel targetPlayer = toRet.data_.modelForSide(side);

        if (singleRealizationOnly) {
            toRet = super.use_core(side, targetMinion, toRet, singleRealizationOnly);
            if (toRet != null) {
                double rnd = Math.random();
                Minion minion = null;
                if (rnd < 0.333333333333333333333333333) {
                    minion = new Huffer();
                } else if (rnd > 0.66666666666666666666666666666) {
                    minion = new Leokk();
                } else {
                    minion = new Misha();
                }
                toRet = minion.summonMinionAtEnd(side, toRet, null, null, false, singleRealizationOnly);
            }
        } else {
            toRet = new RandomEffectNode(toRet, new HearthAction(HearthAction.Verb.USE_CARD, side, 0, side, 0));

            int thisCardIndex = targetPlayer.getHand().indexOf(this);
            for (Minion minion : new Minion[]{new Huffer(), new Leokk(), new Misha()}) {
                HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));

                newState = super.use_core(side, targetPlayer.getHero(), newState, singleRealizationOnly);
                newState = minion.summonMinionAtEnd(side, newState, null, null, false, singleRealizationOnly);
                newState.data_.modelForSide(side).getHand().remove(thisCardIndex);
            }
        }
        return toRet;
    }
}
