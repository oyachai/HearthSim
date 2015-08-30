package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DefenderOfArgus extends Minion implements MinionBattlecryInterface {

    public DefenderOfArgus() {
        super();
    }

    /**
     * Battlecry: Give adjacent minions +1/+1 and Taunt
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

                CharacterIndex thisMinionIndex = currentPlayer.getIndexForCharacter(DefenderOfArgus.this);
                for (Minion minion : currentPlayer.getMinionsAdjacentToCharacter(thisMinionIndex)) {
                    minion.addAttack(1);
                    minion.addHealth(1);
                    minion.setTaunt(true);
                }
                return boardState;
            }
        };
    }
}
