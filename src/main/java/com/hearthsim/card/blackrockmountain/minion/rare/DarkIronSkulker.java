package com.hearthsim.card.blackrockmountain.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkIronSkulker extends Minion implements MinionBattlecryInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterDamage<>(2);

    private static final FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }
           if (targetCharacter.getHealth() != targetCharacter.getMaxHealth()) {
                return false;
            }
            return true;
        }
    };

    public DarkIronSkulker() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide targetSide, CharacterIndex minionPlacementIndex, HearthTreeNode boardState) -> {
            return this.effectAllUsingFilter(DarkIronSkulker.effect, DarkIronSkulker.filter, boardState);
        };
    }
}
