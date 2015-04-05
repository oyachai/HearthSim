package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.SpellRandomInterface;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterSummon;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;
import java.util.Collection;

public class AnimalCompanion extends SpellTargetableCard implements SpellRandomInterface {

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
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterSummon.SELF;
    }

    @Override
    public CardEffectCharacter getTargetableEffect() {
        return null;
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
    public Collection<HearthTreeNode> createChildren(PlayerSide originSide, int originIndex, HearthTreeNode boardState) {
        HearthTreeNode newState;
        ArrayList<HearthTreeNode> children = new ArrayList<>();
        for (Minion minion : new Minion[]{new Huffer(), new Leokk(), new Misha()}) {
            newState = new HearthTreeNode(boardState.data_.deepCopy());
            newState.data_.modelForSide(originSide).getHand().remove(originIndex);
            newState = minion.summonMinionAtEnd(originSide, newState, false, false);
            children.add(newState);
        }

        return children;
    }
}
