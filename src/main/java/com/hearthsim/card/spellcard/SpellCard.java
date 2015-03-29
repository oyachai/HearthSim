package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.json.JSONObject;

import java.util.Collection;

public abstract class SpellCard extends Card {

    protected CharacterFilter characterFilter = CharacterFilterTargetedSpell.ALL;
    protected CardEffectCharacter effect;

    public SpellCard() {
        super();
    }

    // some cards require data from import so we have to use lazy loading
    protected abstract CardEffectCharacter getEffect();

    @Deprecated
    public SpellCard(byte mana, boolean hasBeenUsed) {
        super(mana, hasBeenUsed, true);
    }

    @Deprecated
    public SpellCard(byte mana) {
        this(mana, false);
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return this.characterFilter.targetMatches(PlayerSide.CURRENT_PLAYER, this, playerSide, minion, boardModel);
    }

    /**
     * Use the card on the given target
     * This is the core implementation of card's ability
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = boardState;

        CardEffectCharacter effect = this.getEffect();

        // Check to see if this card generates RNG children
        if (this instanceof SpellRandom) {
            int originIndex = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHand().indexOf(this);
            int targetIndex = boardState.data_.modelForSide(side).getIndexForCharacter(targetMinion);

            // TODO this is to workaround using super.use_core since we no longer have an accurate reference to the origin card (specifically, Soulfire messes things up)
            byte manaCost = this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState.data_);

            // create an RNG "base" that is untouched. This allows us to recreate the RNG children during history traversal.
            toRet = new RandomEffectNode(toRet, new HearthAction(HearthAction.Verb.USE_CARD, side, 0, side, 0));
            Collection<HearthTreeNode> children = ((SpellRandom)this).createChildren(PlayerSide.CURRENT_PLAYER, originIndex, toRet);

            // for each child, apply the effect and mana cost. we want to do as much as we can with the non-random effect portion (e.g., the damage part of Soulfire)
            for (HearthTreeNode child : children) {
                if (effect != null) {
                    child = this.getEffect().applyEffect(PlayerSide.CURRENT_PLAYER, null, side, targetIndex, child);
                }
                child.data_.modelForSide(PlayerSide.CURRENT_PLAYER).subtractMana(manaCost);
                toRet.addChild(child);
            }
        } else {
            if (effect != null) {
                if (this instanceof CardEffectAoeInterface) {
                    toRet = this.effectAllUsingFilter(((CardEffectAoeInterface)this).getAoeEffect(), ((CardEffectAoeInterface)this).getAoeFilter(), toRet);
                } else {
                    toRet = this.getEffect().applyEffect(PlayerSide.CURRENT_PLAYER, this, side, targetMinion, toRet);
                }
            }
            if (toRet != null) {
                toRet = super.use_core(side, targetMinion, toRet, singleRealizationOnly);
            }
        }

        return toRet;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellCard");
        return json;
    }
}
