package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Huffer;
import com.hearthsim.card.minion.concrete.Leokk;
import com.hearthsim.card.minion.concrete.Misha;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.card.spellcard.SpellRandom;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;
import java.util.Collection;

public class AnimalCompanion extends SpellCard implements SpellRandom {

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

        this.characterFilter = CharacterFilterTargetedSpell.SELF;
    }

    @Override
    protected CardEffectCharacter getEffect() {
        return null;
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
    public Collection<HearthTreeNode> createChildren(PlayerSide originSide, int originIndex, HearthTreeNode boardState) throws HSException {
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
