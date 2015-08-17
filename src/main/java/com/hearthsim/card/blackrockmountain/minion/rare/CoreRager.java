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
                    CoreRager.this.setAttack((byte) (CoreRager.this.getAttack() + 3));
                    CoreRager.this.setHealth((byte) (CoreRager.this.getHealth() + 3));
                    CoreRager.this.setMaxHealth((byte) (CoreRager.this.getMaxHealth() + 3));
                }
                return boardState;
            }
        };
    }

}
