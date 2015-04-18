package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleSummonMinionAction extends DeathrattleAction {

    private final int numMinions_;
    private final Class<? extends Minion> minionClass_;

    public DeathrattleSummonMinionAction(Class<? extends Minion> minionClass, int numMnions) {
        numMinions_ = numMnions;
        minionClass_ = minionClass;
    }

    @Override
    public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) {

        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, singleRealizationOnly);
        PlayerModel targetPlayer = toRet.data_.modelForSide(playerSide);

        int targetIndex = targetPlayer.getNumMinions();
        if (origin instanceof Minion) {
            targetIndex = targetPlayer.getIndexForCharacter((Minion)origin) - 1;
            toRet.data_.removeMinion((Minion) origin);
        }

        int numMinionsToActuallySummon = numMinions_;
        if (targetPlayer.isBoardFull()) {
            numMinionsToActuallySummon = 7 - targetPlayer.getNumMinions();
        }

        for (int index = 0; index < numMinionsToActuallySummon; ++index) {
            try {
                Minion newMinion = minionClass_.newInstance();
                toRet = newMinion.summonMinion(playerSide, targetIndex, toRet, false, true);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate card.");
            }
        }
        return toRet;
    }
}
