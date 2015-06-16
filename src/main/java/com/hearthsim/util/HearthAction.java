package com.hearthsim.util;

import com.hearthsim.Game;
import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;


/**
 * A class representing an action that a player can take
 *
 *
 */
public class HearthAction {

    // TODO the DO_NOT_ verbs are used for history tracking but we can probably optimize them away in the future.
    public enum Verb {
        USE_CARD, HERO_ABILITY, ATTACK, UNTARGETABLE_BATTLECRY, TARGETABLE_BATTLECRY, START_TURN, END_TURN, DO_NOT_USE_CARD, DO_NOT_ATTACK, DO_NOT_USE_HEROPOWER, RNG, DRAW_CARDS
    }

    public final Verb verb_;

    private final PlayerSide actionPerformerPlayerSide;
    private final int cardOrCharacterIndex_;

    private final PlayerSide targetPlayerSide;
    public final CharacterIndex targetCharacterIndex;

    public HearthAction(Verb verb) {
        this(verb, PlayerSide.CURRENT_PLAYER, -1, null, CharacterIndex.UNKNOWN);
    }

    public HearthAction(Verb verb, PlayerSide actionPerformerPlayerSide, int cardOrCharacterIndex) {
        this(verb, actionPerformerPlayerSide, cardOrCharacterIndex, null, CharacterIndex.UNKNOWN);
    }

    public HearthAction(Verb verb, PlayerSide actionPerformerPlayerSide, int cardOrCharacterIndex, PlayerSide targetPlayerSide, CharacterIndex targetCharacterIndex) {
        verb_ = verb;
        this.actionPerformerPlayerSide = actionPerformerPlayerSide;
        cardOrCharacterIndex_ = cardOrCharacterIndex;

        this.targetPlayerSide = targetPlayerSide;
        this.targetCharacterIndex = targetCharacterIndex;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("verb_", verb_);
        json.put("actionPerformerPlayerSide", actionPerformerPlayerSide);
        json.put("cardOrCharacterIndex_", cardOrCharacterIndex_);
        json.put("targetPlayerSide", targetPlayerSide);
        json.put("targetCharacterIndex", targetCharacterIndex);

        return json;
    }

    @Deprecated
    public HearthTreeNode perform(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.perform(boardState);
    }

    public HearthTreeNode perform(HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel actingPlayer = actionPerformerPlayerSide != null ? boardState.data_.modelForSide(actionPerformerPlayerSide) : null;
        PlayerModel targetPlayer = targetPlayerSide != null ? boardState.data_.modelForSide(targetPlayerSide) : null;

        switch(verb_) {
            case USE_CARD: {
                Card card = actingPlayer.getHand().get(cardOrCharacterIndex_);
                toRet = card.useOn(targetPlayerSide, targetCharacterIndex, toRet);
            }
            break;
            case HERO_ABILITY: {
                Hero hero = actingPlayer.getHero();
                Minion target = targetPlayer.getCharacter(targetCharacterIndex);
                toRet = hero.useHeroAbility(targetPlayerSide, target, toRet);
            }
            break;
            case ATTACK: {
                Minion attacker = actingPlayer.getCharacter(CharacterIndex.fromInteger(cardOrCharacterIndex_));
                toRet = attacker.attack(targetPlayerSide, targetCharacterIndex, toRet);
            }
            break;
            case UNTARGETABLE_BATTLECRY: {
                Minion minion = actingPlayer.getCharacter(CharacterIndex.fromInteger(cardOrCharacterIndex_));
                toRet = minion.useUntargetableBattlecry(targetCharacterIndex, toRet);
                break;
            }
            case TARGETABLE_BATTLECRY: {
                Minion minion = actingPlayer.getCharacter(CharacterIndex.fromInteger(cardOrCharacterIndex_));
                toRet = minion.useTargetableBattlecry(targetPlayerSide, targetCharacterIndex, toRet);
                break;
            }
            case START_TURN: {
                toRet = new HearthTreeNode(Game.beginTurn(boardState.data_.deepCopy()));
                break;
            }
            case END_TURN: {
                toRet = new HearthTreeNode(Game.endTurn(boardState.data_.deepCopy()).flipPlayers());
                break;
            }
            case DO_NOT_USE_CARD: {
                for (Card c : actingPlayer.getHand()) {
                    c.hasBeenUsed(true);
                }
                break;
            }
            case DO_NOT_ATTACK: {
                for (Minion minion : actingPlayer.getMinions()) {
                    minion.hasAttacked(true);
                }
                actingPlayer.getHero().hasAttacked(true);
                break;
            }
            case DO_NOT_USE_HEROPOWER: {
                actingPlayer.getHero().hasBeenUsed(true);
                break;
            }
            case RNG: {
                // We need to perform the current state again if the children don't exist yet. This can happen in certain replay scenarios.
                // Do not do this if the previous action was *also* RNG or we will end up in an infinite loop.
                if (toRet.isLeaf() && boardState.getAction().verb_ != Verb.RNG) {
                    boardState.data_.getCurrentPlayer().addNumCardsUsed((byte)-1); //do not double count
                    toRet = boardState.getAction().perform(boardState);
                }
                // RNG has declared this child happened
                toRet = toRet.getChildren().get(cardOrCharacterIndex_);
                break;
            }
            case DRAW_CARDS: {
                // Note, this action only supports drawing cards from the deck. Cards like Ysera or Webspinner need to be implemented using RNG children.
                for (int indx = 0; indx < cardOrCharacterIndex_; ++indx) {
                    actingPlayer.drawNextCardFromDeck();
                }
                break;
            }
        }
        return toRet;
    }
}
