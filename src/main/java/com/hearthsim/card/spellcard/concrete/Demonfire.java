package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Demonfire extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Demonfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Demonfire() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 2 damage to a minion.  If it's a friendly Demon, give it +2/+2 instead.
     *
     * @param side
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected CardEffectCharacter getEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    if (isCurrentPlayer(targetSide) && targetCharacter.getTribe() == MinionTribe.DEMON) {
                        targetCharacter.setAttack((byte)(targetCharacter.getAttack() + 2));
                        targetCharacter.setHealth((byte)(targetCharacter.getHealth() + 2));
                    } else {
                        boardState = targetCharacter.takeDamageAndNotify((byte) 2, originSide, targetSide, boardState, true, false);
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
