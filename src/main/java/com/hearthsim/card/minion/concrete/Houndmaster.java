package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Houndmaster extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly beast +2/+2 and Taunt
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
        protected MinionTribe tribeFilter() { return MinionTribe.BEAST; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuffDelta(2, 2, true);

    public Houndmaster() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return Houndmaster.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return Houndmaster.battlecryAction;
    }
}
