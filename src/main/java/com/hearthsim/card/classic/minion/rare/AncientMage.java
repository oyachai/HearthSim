package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientMage extends Minion implements MinionBattlecryInterface {

    public AncientMage() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {

            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

                int thisMinionIndex = currentPlayer.getIndexForCharacter(origin);
                for (Minion minion : currentPlayer.getMinionsAdjacentToCharacter(thisMinionIndex)) {
                    minion.addSpellDamage((byte)1);
                }
                return boardState;
            }
        };
    }
}
