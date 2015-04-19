package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;

public class HungryCrab extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Destroy a murloc and gain +2/+2
     */
    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
        protected MinionTribe tribeFilter() {
            return MinionTribe.MURLOC;
        }
    };

    private final static EffectCharacter battlecryAction = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
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
    public FilterCharacter getBattlecryFilter() {
        return HungryCrab.filter;
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return HungryCrab.battlecryAction;
    }
}
