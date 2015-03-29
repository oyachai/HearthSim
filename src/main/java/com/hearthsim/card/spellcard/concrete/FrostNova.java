package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostNova extends SpellCard implements SpellAoeInterface {
    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public FrostNova(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public FrostNova() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
    }

    /**
     *
     * Use the card on the given target
     *
     * This freeze all enemy minions
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
        return CardEffectCharacter.FREEZE;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return MinionFilter.ENEMY_MINIONS;
    }
}
