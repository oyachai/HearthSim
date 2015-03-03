package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionHealedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Lightwarden extends Minion implements MinionHealedInterface {

    private static final boolean HERO_TARGETABLE = true;

    public Lightwarden() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
    }

    @Override
    public HearthTreeNode minionHealedEvent(
            PlayerSide thisMinionPlayerSide,
            PlayerSide healedMinionPlayerSide,
            Minion healedMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1) {
        this.addAttack((byte)2);
        return boardState;
    }
}
