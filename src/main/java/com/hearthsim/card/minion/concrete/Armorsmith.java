package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.event.effect.CardEffectHero;
import com.hearthsim.event.effect.CardEffectHeroBuff;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Armorsmith extends Minion implements MinionDamagedInterface {

    private static final CardEffectHero effect = new CardEffectHeroBuff(0, 1);

    public Armorsmith() {
        super();
    }

    /**
     *
     * Whenever a friendly minion takes damage, gain 1 Armor
     *
     *
     * @param thisMinionPlayerSide
     * @param damagedPlayerSide
     * @param damagedMinion The damaged minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * */
    @Override
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        HearthTreeNode toRet = boardState;
        if (thisMinionPlayerSide == damagedPlayerSide) {
            toRet = Armorsmith.effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, toRet);
        }
        return toRet;
    }
}
