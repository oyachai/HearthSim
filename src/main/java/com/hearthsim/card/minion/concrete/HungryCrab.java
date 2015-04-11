package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HungryCrab extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a murloc and gain +2/+2
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
        protected MinionTribe tribeFilter() { return MinionTribe.MURLOC; }
    };

    private final static CardEffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        if (targetMinion.getTribe() == MinionTribe.MURLOC) {
            targetMinion.setHealth((byte) -99);
            ((Minion)origin).addAttack((byte) 2);
            ((Minion)origin).addHealth((byte) 2);
            ((Minion)origin).addMaxHealth((byte) 2);
            return boardState;
        } else {
            return null;
        }
    };

    public HungryCrab() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return HungryCrab.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return HungryCrab.battlecryAction;
    }
}
