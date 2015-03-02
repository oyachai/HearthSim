package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwilightDrake extends Minion implements MinionUntargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;

    public TwilightDrake() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }

    /**
     * Battlecry: Gain +1 Health for each card in your hand.
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        this.addHealth((byte)PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHand().size());
        return toRet;
    }

}
