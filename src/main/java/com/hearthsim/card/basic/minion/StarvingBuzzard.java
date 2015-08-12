package com.hearthsim.card.basic.minion;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class StarvingBuzzard extends Minion implements MinionSummonedInterface {

    private static final EffectCharacter<Minion> effect = new EffectHeroDraw<>(1);

    public StarvingBuzzard() {
        super();
    }

    /**
     *
     * Called whenever a minion is summoned on the board
     *
     * The buzzard draws a card whenever a Beast is placed on the battlefield
     *
     *
     * @param thisMinionPlayerSide
     * @param summonedMinionPlayerSide
     * @param summonedMinion The summoned minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * */
    @Override
    public HearthTreeNode minionSummonEvent(
            PlayerSide thisMinionPlayerSide,
            PlayerSide summonedMinionPlayerSide,
            Minion summonedMinion,
            HearthTreeNode boardState) {
        if (summonedMinionPlayerSide == PlayerSide.WAITING_PLAYER || thisMinionPlayerSide == PlayerSide.WAITING_PLAYER)
            return boardState;

        HearthTreeNode toRet = boardState;
        if (summonedMinion.getTribe() == MinionTribe.BEAST) { //TODO: this might be wrong..
            toRet = StarvingBuzzard.effect.applyEffect(thisMinionPlayerSide, CharacterIndex.HERO, boardState);
        }

        return toRet;
    }
}
