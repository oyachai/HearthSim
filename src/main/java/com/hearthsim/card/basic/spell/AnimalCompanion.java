package com.hearthsim.card.basic.spell;

import com.hearthsim.card.basic.minion.Huffer;
import com.hearthsim.card.basic.minion.Leokk;
import com.hearthsim.card.basic.minion.Misha;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellRandomInterface;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterSummon;
import com.hearthsim.model.BoardModel;
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
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterSummon.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return null;
    }

    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel))
            return false;
        if (boardModel.modelForSide(PlayerSide.CURRENT_PLAYER).isBoardFull()) {
            return false;
        }
        return true;
    }

    /**
     * Use the card on the given target
     * Summons either Huffer, Leokk, or Misha
     *
     * @param originSide
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
            newState = minion.summonMinionAtEnd(originSide, newState, false);
            if (newState != null)
                children.add(newState);
        }

        return children;
    }
}
