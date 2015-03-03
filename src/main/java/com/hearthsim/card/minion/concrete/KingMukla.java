package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.spellcard.concrete.Bananas;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KingMukla extends Minion implements MinionUntargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;

    public KingMukla() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }

    /**
     * Battlecry: Give your opponent 2 bananas
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
        for (int index = 0; index < 2; ++index) {
            int numCards = PlayerSide.WAITING_PLAYER.getPlayer(toRet).getHand().size();
            if (numCards < 10) {
                PlayerSide.WAITING_PLAYER.getPlayer(toRet).placeCardHand(new Bananas());
            }
        }
        return toRet;
    }
}
