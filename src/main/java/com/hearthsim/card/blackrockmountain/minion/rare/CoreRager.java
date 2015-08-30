package com.hearthsim.card.blackrockmountain.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CoreRager extends Minion implements MinionBattlecryInterface {

    public CoreRager() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                if (currentPlayer.getHand().size() == 0) {
                    CoreRager.this.addAttack(3);
                    CoreRager.this.addHealth(3);
                    CoreRager.this.addMaxHealth(3);
                }
                return boardState;
            }
        };
    }

}
