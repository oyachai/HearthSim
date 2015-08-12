package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 3/21/15.
 */
public class EdwinVanCleef extends Minion implements MinionBattlecryInterface {

    public EdwinVanCleef() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {

            @Override
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                byte buff = (byte) ((boardState.data_.getCurrentPlayer().getNumCardsUsed() - 1) * 2);
                EdwinVanCleef.this.addAttack(buff);
                EdwinVanCleef.this.addHealth(buff);
                EdwinVanCleef.this.addMaxHealth(buff);
                return boardState;
            }
        };
    }
}
