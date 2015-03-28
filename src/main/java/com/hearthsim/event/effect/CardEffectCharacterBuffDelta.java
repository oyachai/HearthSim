package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterBuffDelta extends CardEffectCharacter {
    private byte attackDelta;
    private byte healthDelta;
    private boolean addTaunt;

    public CardEffectCharacterBuffDelta(int attackDelta, int healthDelta) {
        this(attackDelta, healthDelta, false);
    }

    public CardEffectCharacterBuffDelta(int attackDelta, int healthDelta, boolean addTaunt) {
        this.attackDelta = (byte) attackDelta;
        this.healthDelta = (byte) healthDelta;
        this.addTaunt = addTaunt;
    }


    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.addAttack(this.attackDelta);
        targetCharacter.addHealth(this.healthDelta);
        targetCharacter.addMaxHealth(this.healthDelta);

        if (addTaunt) {
            targetCharacter.setTaunt(true);
        }
        return boardState;
    }
}
