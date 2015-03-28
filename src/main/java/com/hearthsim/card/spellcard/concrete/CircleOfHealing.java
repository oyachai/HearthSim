package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

    private static final byte HEAL_AMOUNT = 4;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public CircleOfHealing(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public CircleOfHealing() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     *
     * Circle of Healing
     *
     * Heals all minions for 4
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected EffectMinionAction getEffect() {
        if (this.effect == null) {
            this.effect = new EffectMinionAction() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                    PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
                    for (Minion minion : currentPlayer.getMinions()) {
                        boardState = minion.takeHealAndNotify(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, boardState);
                    }

                    for (Minion minion : waitingPlayer.getMinions()) {
                        boardState = minion.takeHealAndNotify(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, boardState);
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
