package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class SavageRoar extends SpellCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SavageRoar(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public SavageRoar() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gives all friendly characters +2 attack for this turn
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
                    PlayerModel currentPlayer = boardState.data_.modelForSide(originSide);
                    currentPlayer.getHero().setExtraAttackUntilTurnEnd((byte)2);
                    for (Minion minion : currentPlayer.getMinions())
                        minion.setExtraAttackUntilTurnEnd((byte)2);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
