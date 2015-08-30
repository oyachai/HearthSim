package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@FunctionalInterface
public interface EffectCharacter<T extends Card> {

    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState);

    public default HearthTreeNode applyEffect(PlayerSide targetSide, Card targetCharacter, HearthTreeNode boardState) {
        // MrHen: I have no idea why this (U) conversion is necessary but I get a weird compile assert if I set up the generics to use CardEffectInterface<T, Minion>
        CharacterIndex index = boardState.data_.modelForSide(targetSide).getIndexForCharacter((Minion)targetCharacter);
        return this.applyEffect(targetSide, index, boardState);
    }

    public final static EffectCharacter BOUNCE = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (boardState.data_.modelForSide(targetSide).getHand().size() < 10) {
            Minion copy = (Minion)targetCharacter.createResetCopy();
            boardState.data_.modelForSide(targetSide).placeCardHand(copy);
            boardState.data_.removeMinion(targetCharacter);
        } else {
            targetCharacter.setHealth((byte) -99);
        }
        return boardState;
    };

    public final static EffectCharacter DESTROY = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.setHealth((byte) -99);
        return boardState;
    };

    public final static EffectCharacter FREEZE = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setFrozen(true);
        return boardState;
    };

    public final static EffectCharacter<Card> GIVE_CHARGE = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetCharacter.setCharge(true);
        return boardState;
    };

    public final static EffectCharacter<Card> SWAP_ATTACK_HEALTH = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        byte attack = targetCharacter.getTotalAttack(boardState.data_, targetSide);
        targetCharacter.setAttack(targetCharacter.getTotalHealth());
        targetCharacter.setHealth(attack);
        targetCharacter.setMaxHealth(attack);
        return boardState;
    };

    public final static EffectCharacter SILENCE = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.silenced(targetSide, boardState.data_);
        return boardState;
    };

    public final static EffectCharacter STEALTH_UNTIL_NEXT_TURN = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        targetMinion.setStealthedUntilNextTurn(true);
        return boardState;
    };

    public final static EffectCharacter MIND_CONTROL = (targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);

        boardState.data_.removeMinion(targetSide, targetCharacterIndex);
        boardState.data_.placeMinion(targetSide.getOtherPlayer(), targetMinion);

        if (targetMinion.getCharge()) {
            if (!targetMinion.canAttack(boardState.data_, targetSide.getOtherPlayer())) {
                targetMinion.hasAttacked(false);
            }
        } else {
            targetMinion.hasAttacked(true);
        }
        targetMinion.hasBeenUsed(true);
        return boardState;
    };
}
