package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwilightDrake extends Minion implements MinionBattlecryInterface {

    public TwilightDrake() {
        super();
    }

    /**
     * Battlecry: Gain +1 Health for each card in your hand.
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide,int minionPlacementIndex, HearthTreeNode boardState) -> {
            this.addHealth((byte) boardState.data_.getCurrentPlayer().getHand().size());
            return boardState;
        };
    }
}
