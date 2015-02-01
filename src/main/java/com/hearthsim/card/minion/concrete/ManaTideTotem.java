package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ManaTideTotem extends Minion {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public ManaTideTotem() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.TOTEM;
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
        if (isWaitingPlayer(thisMinionPlayerIndex))
            return tmpState;

        tmpState.data_.drawCardFromCurrentPlayerDeck(1);
        return tmpState;
    }

}
