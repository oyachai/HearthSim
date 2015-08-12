package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectCharacterBuff<T extends Card> implements EffectCharacter<T> {
    private final byte attack;
    private final byte health;

    public EffectCharacterBuff(int attack, int health) {
        this.attack = (byte) attack;
        this.health = (byte) health;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (this.attack > 0) {
            targetCharacter.setAttack(this.attack);
            targetCharacter.setExtraAttackUntilTurnEnd((byte) 0);
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
