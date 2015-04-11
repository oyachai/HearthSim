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

public class TempleEnforcer extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a friendly minion +3 Health
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuffDelta(0, 3);

    public TempleEnforcer() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return TempleEnforcer.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return TempleEnforcer.battlecryAction;
    }
}
