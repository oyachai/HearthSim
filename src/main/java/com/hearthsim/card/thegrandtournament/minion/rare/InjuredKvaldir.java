package com.hearthsim.card.thegrandtournament.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class InjuredKvaldir extends Minion implements MinionBattlecryInterface {

    public InjuredKvaldir() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {

            @Override
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                HearthTreeNode toRet;
                toRet = InjuredKvaldir.this.takeDamageAndNotify((byte) 3, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, false, true);
                return toRet;
            }
        };
    }
}
