package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class TheBlackKnight extends Minion implements MinionTargetableBattlecry {

    public TheBlackKnight() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Destroy a minion with an Attack of 7 or more
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1
        ) throws HSException {
        if (targetMinion.getTaunt()) {
            targetMinion.setHealth((byte)-99);
            return boardState;
        } else {
            return null;
        }
    }
}
