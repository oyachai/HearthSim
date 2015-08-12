package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.goblinsvsgnomes.spell.spareparts.*;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ListIterator;

/**
 * Created by oyachai on 8/11/15.
 */
public class EffectHeroAddSparePart<T extends Card> implements EffectHero<T> {

    public EffectHeroAddSparePart(){}

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
        if (boardState.numChildren() > 0) {
            ListIterator<HearthTreeNode> iter = boardState.getChildren().listIterator();
            while (iter.hasNext()) {
                HearthTreeNode newNode = this.applyEffect(targetSide, iter.next());
                if (newNode != null) {
                    iter.set(newNode);
                }
            }
        }

        if (!boardState.data_.modelForSide(targetSide).isHandFull()) {
            boardState = new RandomEffectNode(boardState, null);
            for (Card sparePart : new Card[]{new ArmorPlating(), new EmergencyCoolant(), new FinickyCloakfield(),
                new ReversingSwitch(), new RustyHorn(), new TimeRewinder(), new WhirlingBlades()}) {

                HearthTreeNode ch = new HearthTreeNode(boardState.data_.deepCopy());
                ch.data_.modelForSide(targetSide).getHand().add(sparePart);
                boardState.addChild(ch);
            }
        }
        return boardState;
    }

}
