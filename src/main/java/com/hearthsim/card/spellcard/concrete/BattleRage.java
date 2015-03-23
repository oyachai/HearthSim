package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class BattleRage extends SpellCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public BattleRage(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public BattleRage() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Draw a card for each damaged friendly character
     *
     * @param thisCardIndex The index (position) of the card in the hand
     * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
     * @param minionIndex The index of the target minion.
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(
            PlayerSide targetPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState,
            boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(targetPlayerSide, targetMinion, boardState, singleRealizationOnly);
        if (toRet != null) {
            PlayerModel playerModel = toRet.data_.modelForSide(targetPlayerSide);
            Hero hero = playerModel.getHero();
            IdentityLinkedList<Minion> minions = playerModel.getMinions();
            int numCardsToDraw = hero.getTotalHealth() < hero.getTotalMaxHealth() ? 1 : 0;
            for (Minion minion : minions) {
                numCardsToDraw += minion.getTotalHealth() < minion.getTotalMaxHealth() ? 1 : 0;
            }
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(numCardsToDraw);
            } else {
                toRet = new CardDrawNode(toRet, numCardsToDraw); //draw two cards
            }
        }
        return toRet;
    }
}
