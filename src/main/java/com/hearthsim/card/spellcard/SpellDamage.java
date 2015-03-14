package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class SpellDamage extends SpellCard {

    protected byte damage_;

    public SpellDamage() {
        super();
    }

    @Deprecated
    public SpellDamage(byte baseManaCost, byte damage, boolean hasBeenUsed) {
        super(baseManaCost, hasBeenUsed);
        damage_ = damage;
    }

    @Override
    public void initFromImplementedCard(ImplementedCardList.ImplementedCard implementedCard) {
        super.initFromImplementedCard(implementedCard);

        this.damage_ = (byte) implementedCard.spellEffect;
    }

    public byte getAttack() {
        return damage_;
    }

    @Override
    public boolean equals(Object other) {

        if (!super.equals(other)) {
            return false;
        }

        if (this.damage_ != ((SpellDamage)other).damage_) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + damage_;
        return result;
    }

    /**
     * Attack using this spell
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        return targetMinion.takeDamage(damage_, PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, boardState, true, false);
    }

    @Deprecated
    public HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState,
                                 Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.attack(targetMinionPlayerSide, targetMinion, boardState);
    }

    @Deprecated
    public HearthTreeNode attackAllMinionsOnSide(PlayerSide targetMinionPlayerSide, HearthTreeNode boardState,
                                                 Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.attackAllMinionsOnSide(targetMinionPlayerSide, boardState);
    }

    public HearthTreeNode attackAllMinionsOnSide(PlayerSide targetMinionPlayerSide, HearthTreeNode boardState) throws HSException {
        if (boardState != null) {
            PlayerModel targetPlayer = boardState.data_.modelForSide(targetMinionPlayerSide);
            for (Minion minion : targetPlayer.getMinions()) {
                boardState = this.attack(targetMinionPlayerSide, minion, boardState);
            }
        }
        return boardState;
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
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        toRet = this.attack(side, targetMinion, toRet);
        return toRet;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellDamage");
        return json;
    }
}
