package com.hearthsim.event.deathrattle;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleSummonMinionAction extends DeathrattleAction {

    private final int numMinions_;
    private final Class<? extends Minion> minionClass_;
    private final PlayerSide sideToSummon;

    public DeathrattleSummonMinionAction(Class<? extends Minion> minionClass, int numMinions) {
        this(minionClass, numMinions, PlayerSide.CURRENT_PLAYER);
    }

    public DeathrattleSummonMinionAction(Class<? extends Minion> minionClass, int numMnions, PlayerSide sideToSummon) {
        numMinions_ = numMnions;
        minionClass_ = minionClass;
        this.sideToSummon = sideToSummon;
    }

    @Override
    public HearthTreeNode performAction(CharacterIndex originIndex, PlayerSide playerSide, HearthTreeNode boardState) {

        HearthTreeNode toRet = super.performAction(originIndex, playerSide, boardState);
        if (toRet == null)
            return boardState;
        PlayerModel targetPlayer = toRet.data_.modelForSide(playerSide);
        PlayerSide targetPlayerSide = playerSide;
        if (sideToSummon == PlayerSide.WAITING_PLAYER) {
            targetPlayer = toRet.data_.modelForSide(playerSide.getOtherPlayer());
            targetPlayerSide = playerSide.getOtherPlayer();
        }

        int numMinionsToActuallySummon = numMinions_;
        if (targetPlayer.getNumEmptyBoardSpace() < numMinionsToActuallySummon) {
            numMinionsToActuallySummon = targetPlayer.getNumEmptyBoardSpace();
        }

        for (int index = 0; index < numMinionsToActuallySummon; ++index) {
            try {
                Minion newMinion = minionClass_.newInstance();
                if (sideToSummon == PlayerSide.WAITING_PLAYER)
                    toRet = newMinion.summonMinion(targetPlayerSide, CharacterIndex.fromInteger(targetPlayer.getNumMinions()), toRet, false);
                else
                    toRet = newMinion.summonMinion(targetPlayerSide, originIndex.indexToLeft(), toRet, false);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate card.");
            }
        }
        return toRet;
    }
}
