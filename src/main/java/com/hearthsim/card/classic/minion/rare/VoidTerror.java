package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class VoidTerror extends Minion implements MinionBattlecryInterface {

    public VoidTerror() {
        super();
    }

    /**
     * Battlecry: Destroy the minions on either side of this minion and gain their Attack and Health.
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return new EffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

                int thisMinionIndex = currentPlayer.getIndexForCharacter(origin);
                for (Minion minion : currentPlayer.getMinionsAdjacentToCharacter(thisMinionIndex)) {
                    origin.addAttack(minion.getTotalAttack());
                    origin.addHealth(minion.getTotalHealth());
                    origin.addMaxHealth(minion.getTotalHealth());
                    minion.setHealth((byte) -99);
                }
                return boardState;
            }
        };
    }
}
