package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SavageRoar extends SpellCard implements SpellAoeInterface {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffTemp(2);

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
    protected CardEffectCharacter getEffect() {
        return SavageRoar.effect;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return MinionFilter.ALL_FRIENDLIES;
    }
}
