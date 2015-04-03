package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterBuff extends CardEffectCharacter {
    private final byte attack;
    private final byte health;

    public CardEffectCharacterBuff(int attack, int health) {
        this.attack = (byte) attack;
        this.health = (byte) health;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (this.attack > 0) {
            targetCharacter.setAttack(this.attack);
        }

        if (this.health > 0) {
            targetCharacter.setHealth(this.health);

            // don't set hero's max health
            if (!(targetCharacter instanceof Hero)) {
                targetCharacter.setMaxHealth(this.health);
            }
        }
        return boardState;
    }
}
