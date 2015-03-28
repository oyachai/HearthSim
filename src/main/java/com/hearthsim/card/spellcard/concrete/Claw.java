package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Claw extends SpellCard {

    private static final byte DAMAGE_AMOUNT = 2;
    private static final byte ARMOR_AMOUNT = 2;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Claw(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Claw() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     * Claw
     *
     * Gives the hero +2 attack for this turn and +2 armor
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
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setExtraAttackUntilTurnEnd((byte)(DAMAGE_AMOUNT + targetCharacter.getExtraAttackUntilTurnEnd()));
                    ((Hero)targetCharacter).setArmor(ARMOR_AMOUNT);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
