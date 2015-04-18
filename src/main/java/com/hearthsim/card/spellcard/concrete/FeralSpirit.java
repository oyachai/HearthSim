package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.minion.concrete.SpiritWolf;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummon;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerModel;

public class FeralSpirit extends SpellTargetableCard {

    public FeralSpirit() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
            PlayerModel currentPlayer = boardState.data_.modelForSide(targetSide);
            if (currentPlayer.isBoardFull()) {
                return null;
            }

            Minion mi0 = new SpiritWolf();
            boardState = mi0.summonMinionAtEnd(targetSide, boardState, false, false);

            if (!currentPlayer.isBoardFull()) {
                Minion mi1 = new SpiritWolf();
                boardState = mi1.summonMinionAtEnd(targetSide, boardState, false, false);
            }
            return boardState;
        };
    }
}
