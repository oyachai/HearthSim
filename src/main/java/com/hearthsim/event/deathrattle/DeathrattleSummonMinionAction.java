package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
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
    public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) {

        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, singleRealizationOnly);

        PlayerModel targetPlayer = toRet.data_.modelForSide(playerSide);
        PlayerSide targetPlayerSide = playerSide;
        if (sideToSummon == PlayerSide.WAITING_PLAYER) {
            targetPlayer = toRet.data_.modelForSide(playerSide.getOtherPlayer());
            targetPlayerSide = playerSide.getOtherPlayer();
        }

        int targetIndex = targetPlayer.getNumMinions();
        if (origin instanceof Minion && targetPlayerSide == playerSide) {
            targetIndex = targetPlayer.getIndexForCharacter((Minion)origin) - 1;
            toRet.data_.removeMinion((Minion) origin);
        }

        int numMinionsToActuallySummon = numMinions_;
        if (targetPlayer.getNumEmptyBoardSpace() < numMinionsToActuallySummon) {
            numMinionsToActuallySummon = targetPlayer.getNumEmptyBoardSpace();
        }

        for (int index = 0; index < numMinionsToActuallySummon; ++index) {
            try {
                Minion newMinion = minionClass_.newInstance();
                toRet = newMinion.summonMinion(targetPlayerSide, targetIndex, toRet, false, true);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate card.");
            }
        }
        return toRet;
    }
}
