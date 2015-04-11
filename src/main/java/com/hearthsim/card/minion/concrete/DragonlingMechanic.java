package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DragonlingMechanic extends Minion implements MinionBattlecryInterface {

    public DragonlingMechanic() {
        super();
    }

    /**
     * Battlecry: Summons a Mechanical Dragonling
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            HearthTreeNode toRet = boardState;
            if (toRet != null) {
                PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                if (!currentPlayer.isBoardFull()) {
                    Minion mdragon = new MechanicalDragonling();
                    toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, false, false);
                }
            }
            return toRet;
        };
    }
}
