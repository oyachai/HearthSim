package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.classic.minion.common.Whelp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LeeroyJenkins extends Minion implements MinionBattlecryInterface {

    public LeeroyJenkins() {
        super();
    }

    /**
     * Battlecry: summon two 1/1 whelps for your opponent
     */
    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            HearthTreeNode toRet = boardState;
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            for (int index = 0; index < 2; ++index) {
                if (!waitingPlayer.isBoardFull()) {
                    Minion newMinion = new Whelp();
                    toRet = newMinion.summonMinionAtEnd(PlayerSide.WAITING_PLAYER, toRet, false);
                }
            }
            return toRet;
        };
    }
}
