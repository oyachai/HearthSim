package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class DarkCultist extends Minion {

    public DarkCultist() {
        super();
        deathrattleAction_ = new DarkCultistDeathrattle(3);
    }

    private class DarkCultistEffect extends EffectMinionAction {
        private byte effect;

        public DarkCultistEffect(int effect) {
            this.effect = (byte)effect;
        }

        public boolean canEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter) {
            if (targetSide != originSide) {
                return false;
            }

            if (targetCharacter == origin) {
                return false;
            }

            if (targetCharacter.isHero()) {
                return false;
            }

            return true;
        }

        public void applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter) {
            targetCharacter.addHealth(this.effect);
            targetCharacter.addMaxHealth(this.effect);
        }
    }


    private class DarkCultistDeathrattle extends DeathrattleAction {
        private EffectMinionAction effect;

        public DarkCultistDeathrattle(int effect) {
            this.effect = new DarkCultistEffect(effect);
        }

        public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
            if (!(origin instanceof Minion)) {
                return null;
            }
            PlayerModel owner = boardState.data_.modelForSide(playerSide);
            int originIndex = owner.getIndexForCharacter((Minion)origin);

            // TODO could probably be faster and belongs in a more common location
            List<Minion> possibleTargets = new ArrayList<>();
            for (Minion minion : owner.getMinions()) {
                if (this.effect.canEffect(playerSide, origin, playerSide, minion)) {
                    possibleTargets.add(minion);
                }
            }

            Minion targetMinion;
            switch (possibleTargets.size()) {
                case 0: // no targets, do nothing
                    break;
                case 1: // one target, no RNG needed
                    targetMinion = possibleTargets.get(0);
                    this.effect.applyEffect(playerSide, origin, playerSide, targetMinion);
                    break;
                default: // more than 1 option, generate all possible futures
                    RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
                    for (Minion possibleTarget : possibleTargets) {
                        int targetIndex = owner.getIndexForCharacter(possibleTarget);

                        HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                        this.effect.applyEffectToBoard(playerSide, origin, playerSide, targetIndex, newState.data_);

                        // TODO need to do this manually for now. we should handle this in the death handler
                        newState.data_.removeMinion(playerSide, originIndex - 1);
                        rngNode.addChild(newState);
                    }

                    boardState = rngNode;
                    break;
            }
            return boardState;
        }
    }
}
