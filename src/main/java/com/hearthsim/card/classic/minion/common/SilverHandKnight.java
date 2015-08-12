package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SilverHandKnight extends Minion implements MinionBattlecryInterface {

    public SilverHandKnight() {
        super();
    }

    /**
     * Battlecry: Summon a Squire
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState)->{
            HearthTreeNode toRet = boardState;
            if (toRet != null) {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                if (!currentPlayer.isBoardFull()) {
                    Minion mdragon = new Squire();
                    toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, false);
                }
            }
            return toRet;
        };
    }
}
