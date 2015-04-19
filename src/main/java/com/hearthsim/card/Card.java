package com.hearthsim.card;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.card.spellcard.SpellRandomInterface;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.event.effect.*;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterHand;
import com.hearthsim.event.filter.FilterInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Card implements DeepCopyable<Card> {
    private static final Logger log = LoggerFactory.getLogger(Card.class);

    protected boolean hasBeenUsed;
    protected boolean isInHand_;

    protected DeathrattleAction deathrattleAction_;

    protected final ImplementedCardList.ImplementedCard implementedCard;

    /**
     * Constructor
     */
    public Card() {
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        this.implementedCard = implementedCard;
        this.initFromImplementedCard(implementedCard);
    }

    protected void initFromImplementedCard(ImplementedCardList.ImplementedCard implementedCard) {
        this.hasBeenUsed = false;
        this.isInHand_ = true;
    }

    /**
     * Get the name of the card
     *
     * @return Name of the card
     */
    public String getName() {
        return this.implementedCard != null ? this.implementedCard.name_ : null;
    }

    /**
     * Get the mana cost of the card
     *
     * @param side The PlayerSide of the card for which you want the mana cost
     * @param board The BoardModel representing the current board state
     *
     * @return Mana cost of the card
     */
    public byte getManaCost(PlayerSide side, BoardModel board) {
        return this.getBaseManaCost();
    }

    /**
     * Get the base mana cost of the card
     *
     * @return Mana cost of the card
     */
    public byte getBaseManaCost() {
        if (this.implementedCard == null) {
            return 0;
        }
        return (byte) this.implementedCard.mana_;
    }


    /**
     * Returns whether the card has been used or not
     *
     * @return
     */
    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }

    /**
     * Sets whether the card has been used or not
     *
     * @param value The new hasBeenUsed value
     */
    public void hasBeenUsed(boolean value) {
        hasBeenUsed = value;
    }

    public void isInHand(boolean value) {
        isInHand_ = value;
    }

    protected boolean isInHand() {
        return isInHand_;
    }

    // Use for bounce (e.g., Brewmaster) or recreate (e.g., Reincarnate)
    public Card createResetCopy() {
        try {
            Constructor<? extends Card> ctor = this.getClass().getConstructor();
            return ctor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // This deepCopy pattern is required because we use the class of each card to recreate it under certain circumstances
    @Override
    public Card deepCopy() {
        Card copy = null;
        try {
            copy = getClass().newInstance();
        } catch(InstantiationException e) {
            Card.log.error("instantiation error", e);
        } catch(IllegalAccessException e) {
            Card.log.error("illegal access error", e);
        }
        if (copy == null) {
            throw new RuntimeException("unable to instantiate card.");
        }

        copy.hasBeenUsed = this.hasBeenUsed;
        copy.isInHand_ = this.isInHand_;

        return copy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        // More logic here to be discuss below...
        if (this.getBaseManaCost() != ((Card)other).getBaseManaCost())
            return false;

        if (hasBeenUsed != ((Card)other).hasBeenUsed)
            return false;

        if (isInHand_ != ((Card)other).isInHand_)
            return false;

        if (this.getName() == null) {
            if (((Card)other).getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(((Card)other).getName())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.getName() != null ? this.getName().hashCode() : 0;
        result = 31 * result + this.getBaseManaCost();
        result = 31 * result + (hasBeenUsed ? 1 : 0);
        result = 31 * result + (isInHand_ ? 1 : 0);
        return result;
    }

    /**
     * Returns whether this card can be used on the given target or not
     *
     * This function is an optional optimization feature. Some cards in Hearthstone have limited targets; Shadow Bolt cannot be used on heroes, Mind Blast can only target enemy heroes, etc. Even in this situation though, BoardStateFactory
     * will still try to play non- sensical moves because it doesn't know that such moves are invalid until it tries to play them. The problem is, the BoardStateFactory has to go and make a deep copy of the current BoardState before it can
     * go and try to play the invalid move, and it turns out that 98% of execution time in HearthSim is BoardStateFactory calling BoardState.deepCopy(). By overriding this function and returning false on appropriate occasions, we can save
     * some calls to deepCopy and get better performance out of the code.
     *
     * By default, this function only checks mana cost.
     *
     * @param playerSide
     * @param boardModel
     * @return
     */
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!(this.getManaCost(PlayerSide.CURRENT_PLAYER, boardModel) <= boardModel.getCurrentPlayer().getMana())) {
            return false;
        }

        if (this instanceof EffectOnResolveTargetable) {
            if (!((EffectOnResolveTargetable)this).getTargetableFilter().targetMatches(PlayerSide.CURRENT_PLAYER, this, playerSide, minion, boardModel)) {
                return false;
            }
        } else if (this instanceof SpellCard && playerSide != PlayerSide.CURRENT_PLAYER || !minion.isHero()) { // TODO ignore minion cards for now
            return false;
        }

        return true;
    }

    public boolean canBeUsedOn(PlayerSide playerSide, int targetIndex, BoardModel boardModel) {
        Minion targetMinion = boardModel.modelForSide(playerSide).getCharacter(targetIndex);
        return this.canBeUsedOn(playerSide, targetMinion, boardModel);
    }

    public final HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        return this.useOn(side, targetMinion, boardState, false);
    }

    public HearthTreeNode useOn(PlayerSide side, int targetIndex, HearthTreeNode boardState) throws HSException {
        return this.useOn(side, targetIndex, boardState, false);
    }

    public HearthTreeNode useOn(PlayerSide side, int targetIndex, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        Minion target = boardState.data_.modelForSide(side).getCharacter(targetIndex);
        return this.useOn(side, target, boardState, singleRealizationOnly);
    }

    /**
     * Use the card on the given target
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero)
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @param singleRealizationOnly For cards with random effects, setting this to true will return only a single realization of the random event.
     *
     * @return The boardState is manipulated and returned
     */
    private HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        if (!this.canBeUsedOn(side, targetMinion, boardState.data_))
            return null;

        PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
        PlayerModel targetPlayer = boardState.data_.modelForSide(side);

        // Need to record card and target index *before* the board state changes
        int cardIndex = currentPlayer.getHand().indexOf(this);
        int targetIndex = targetPlayer.getIndexForCharacter(targetMinion);

        currentPlayer.addNumCardsUsed((byte)1);

        HearthTreeNode toRet = this.notifyCardPlayBegin(boardState, singleRealizationOnly);
        if (toRet != null) {
            toRet = this.use_core(side, targetMinion, toRet, singleRealizationOnly);
        }

        if (toRet != null) {
            // we need to resolve each RNG child separately
            if (toRet instanceof RandomEffectNode && toRet.numChildren() > 0) {
                for (HearthTreeNode child : toRet.getChildren()) {
                    this.resolveCardPlayedAndNotify(child, singleRealizationOnly); // TODO deal with null return
                }
            } else {
                toRet = this.resolveCardPlayedAndNotify(toRet, singleRealizationOnly);
            }
        }

        if (toRet != null) {
            toRet.setAction(new HearthAction(Verb.USE_CARD, PlayerSide.CURRENT_PLAYER, cardIndex, side, targetIndex));
        }

        return toRet;
    }

    private HearthTreeNode resolveCardPlayedAndNotify(HearthTreeNode boardState, boolean singleRealizationOnly) {
        if (boardState != null && this.triggersOverload()) {
            boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).addOverload(this.getOverload());
        }

        if (boardState != null) {
            boardState = this.notifyCardPlayResolve(boardState, singleRealizationOnly);
        }

        return boardState;
    }

    /**
     * Use the card on the given target
     * <p>
     * This is the core implementation of card's ability
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    protected HearthTreeNode use_core(
        PlayerSide side,
        Minion targetMinion,
        HearthTreeNode boardState,
        boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = boardState;
        int originIndex = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHand().indexOf(this);
        int targetIndex = boardState.data_.modelForSide(side).getIndexForCharacter(targetMinion);

        EffectCharacter<Card> targetableEffect = null;
        if (this instanceof EffectOnResolveTargetable) {
            targetableEffect = ((EffectOnResolveTargetable) this).getTargetableEffect();
        }

        // TODO this is to workaround using super.use_core since we no longer have an accurate reference to the origin card (specifically, Soulfire messes things up)
        byte manaCost = this.getManaCost(PlayerSide.CURRENT_PLAYER, boardState.data_);
        Collection<HearthTreeNode> rngChildren = null;

        // different interfaces have different usage patterns
        if (this instanceof SpellRandomInterface) {
            rngChildren = ((SpellRandomInterface) this).createChildren(PlayerSide.CURRENT_PLAYER, originIndex, toRet);
        } else if (this instanceof EffectOnResolveRandom) {
            rngChildren = this.effectRandomUsingFilter((EffectOnResolveRandom) this, toRet);
        } else if (this instanceof EffectOnResolveAoe) {
            toRet = this.effectAllUsingFilter(((EffectOnResolveAoe) this).getAoeEffect(), ((EffectOnResolveAoe) this).getAoeFilter(), toRet);
        }

        // if we expected rngChildren but none were created, don't let this card be played if it was the only effect
        if (rngChildren != null && rngChildren.size() == 0 && targetableEffect == null) {
            toRet = null;
        }

        if (toRet == null) {
            return null;
        }

        toRet = this.createRngNodeWithChildren(toRet, rngChildren);

        if (toRet != null && toRet instanceof RandomEffectNode) {
            // create an RNG "base" that is untouched. This allows us to recreate the RNG children during history traversal.
            this.hasBeenUsed(false); // revert back to unused for the purposes of replays

            // for each child, apply the effect and mana cost. we want to do as much as we can with the non-random effect portion (e.g., the damage part of Soulfire)
            for (HearthTreeNode child : toRet.getChildren()) {
                if (targetableEffect != null) {
                    child = targetableEffect.applyEffect(PlayerSide.CURRENT_PLAYER, null, side, targetIndex, child);
                }
                child.data_.modelForSide(PlayerSide.CURRENT_PLAYER).subtractMana(manaCost);
            }
        } else {
            if (toRet != null) {
                // apply standard card played effects
                PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
                toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).subtractMana(manaCost);
                this.hasBeenUsed(true);
                currentPlayer.getHand().remove(this);

                if (targetableEffect != null) {
                    toRet = targetableEffect.applyEffect(PlayerSide.CURRENT_PLAYER, this, side, targetMinion, toRet);
                }
            }
        }

        return toRet;
    }

    // ======================================================================================
    // Various notifications
    // ======================================================================================
    private HearthTreeNode notifyCardPlayBegin(HearthTreeNode boardState, boolean singleRealizationOnly) {
        PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = boardState.data_.getWaitingPlayer();

        HearthTreeNode toRet = boardState;
        ArrayList<CardPlayBeginInterface> matches = new ArrayList<>();

        for (Card card : currentPlayer.getHand()) {
            if (card instanceof CardPlayBeginInterface) {
                matches.add((CardPlayBeginInterface)card);
            }
        }

        Card hero = currentPlayer.getHero();
        if (hero instanceof CardPlayBeginInterface) {
            matches.add((CardPlayBeginInterface)hero);
        }

        for (Minion minion : currentPlayer.getMinions()) {
            if (!minion.isSilenced() && minion instanceof CardPlayBeginInterface) {
                matches.add((CardPlayBeginInterface)minion);
            }
        }

        for (CardPlayBeginInterface match : matches) {
            toRet = match.onCardPlayBegin(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, singleRealizationOnly);
        }
        matches.clear();

        for (Card card : waitingPlayer.getHand()) {
            if (card instanceof CardPlayBeginInterface) {
                matches.add((CardPlayBeginInterface)card);
            }
        }

        hero = waitingPlayer.getHero();
        if (hero instanceof CardPlayBeginInterface) {
            matches.add((CardPlayBeginInterface)hero);
        }

        for (Minion minion : waitingPlayer.getMinions()) {
            if (!minion.isSilenced() && minion instanceof CardPlayBeginInterface) {
                matches.add((CardPlayBeginInterface)minion);
            }
        }

        for (CardPlayBeginInterface match : matches) {
            toRet = match.onCardPlayBegin(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, singleRealizationOnly);
        }

        // check for and remove dead minions
        toRet = BoardStateFactoryBase.handleDeadMinions(toRet, singleRealizationOnly);
        return toRet;
    }

    private HearthTreeNode notifyCardPlayResolve(HearthTreeNode boardState, boolean singleRealizationOnly) {
        PlayerModel currentPlayer = boardState.data_.getCurrentPlayer();
        PlayerModel waitingPlayer = boardState.data_.getWaitingPlayer();

        HearthTreeNode toRet = boardState;
        ArrayList<CardPlayAfterInterface> matches = new ArrayList<>();

        for (Card card : currentPlayer.getHand()) {
            if (card instanceof CardPlayAfterInterface) {
                matches.add((CardPlayAfterInterface)card);
            }
        }

        Card hero = currentPlayer.getHero();
        if (hero instanceof CardPlayAfterInterface) {
            matches.add((CardPlayAfterInterface)hero);
        }

        for (Minion minion : currentPlayer.getMinions()) {
            if (!minion.isSilenced() && minion instanceof CardPlayAfterInterface) {
                matches.add((CardPlayAfterInterface)minion);
            }
        }

        for (CardPlayAfterInterface match : matches) {
            toRet = match.onCardPlayResolve(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, singleRealizationOnly);
        }
        matches.clear();

        for (Card card : waitingPlayer.getHand()) {
            if (card instanceof CardPlayAfterInterface) {
                matches.add((CardPlayAfterInterface)card);
            }
        }

        hero = waitingPlayer.getHero();
        if (hero instanceof CardPlayAfterInterface) {
            matches.add((CardPlayAfterInterface)hero);
        }

        for (Minion minion : waitingPlayer.getMinions()) {
            if (!minion.isSilenced() && minion instanceof CardPlayAfterInterface) {
                matches.add((CardPlayAfterInterface)minion);
            }
        }

        for (CardPlayAfterInterface match : matches) {
            toRet = match.onCardPlayResolve(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this, toRet, singleRealizationOnly);
        }

        // check for and remove dead minions
        toRet = BoardStateFactoryBase.handleDeadMinions(toRet, singleRealizationOnly);
        return toRet;
    }

    protected final HearthTreeNode effectAllUsingFilter(EffectCharacter<Card> effect, FilterCharacter filter, HearthTreeNode boardState) {
        if (boardState != null && filter != null) {
            for (BoardModel.CharacterLocation location : boardState.data_) {
                Minion character = boardState.data_.getCharacter(location);
                if (filter.targetMatches(PlayerSide.CURRENT_PLAYER, this, location.getPlayerSide(), character, boardState.data_)) {
                    boardState = effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, location.getPlayerSide(), character, boardState);
                }
            }
        }
        return boardState;
    }

    protected final Collection<HearthTreeNode> effectRandomUsingFilter(EffectOnResolveRandom randomEffect, HearthTreeNode boardState) {
        if (randomEffect instanceof EffectOnResolveRandomCharacter) {
            EffectOnResolveRandomCharacter character = (EffectOnResolveRandomCharacter)randomEffect;
            return this.effectRandomCharacterUsingFilter(character.getRandomTargetEffect(), character.getRandomTargetSecondaryEffect(), character.getRandomTargetFilter(), boardState);
        } else if (randomEffect instanceof EffectOnResolveRandomHand) {
            EffectOnResolveRandomHand hand = (EffectOnResolveRandomHand)randomEffect;
            return this.effectRandomHandUsingFilter(hand.getRandomTargetEffect(), hand.getRandomTargetSecondaryEffect(), hand.getRandomTargetFilter(), PlayerSide.CURRENT_PLAYER, boardState);
        }

        return null;
    }

    protected final Collection<HearthTreeNode> effectRandomCharacterUsingFilter(EffectCharacter<Card> effect, EffectCharacter<Card> effectOthers, FilterCharacter filter, HearthTreeNode boardState) {
        return this.effectRandomCharacterUsingFilter(effect, effectOthers, filter, PlayerSide.CURRENT_PLAYER, boardState);
    }

    protected final Collection<HearthTreeNode> effectRandomCharacterUsingFilter(EffectCharacter<Card> effect, EffectCharacter<Card> effectOthers, FilterCharacter filter, PlayerSide originSide, HearthTreeNode boardState) {
        return this.iterateAndEffectRandom(effect, effectOthers, filter, originSide, boardState, boardState.data_.iterator());
    }

    protected final Collection<HearthTreeNode> effectRandomHandUsingFilter(EffectHand effect, EffectHand effectOthers, FilterHand filter, PlayerSide originSide, HearthTreeNode boardState) {
        Iterator<BoardModel.CharacterLocation> handIterator = boardState.data_.handIterator();
        return this.iterateAndEffectRandom(effect, effectOthers, filter, originSide, boardState, handIterator);
    }

    protected final Collection<HearthTreeNode> iterateAndEffectRandom(EffectInterface<Card> effect, EffectInterface<Card> effectOthers, FilterInterface<Card> filter, PlayerSide originSide, HearthTreeNode boardState, Iterator<BoardModel.CharacterLocation> targetIterator) {
        int originIndex = boardState.data_.modelForSide(originSide).getHand().indexOf(this);
        boolean originInHand = originIndex >= 0;
        if (!originInHand) {
            originIndex = boardState.data_.modelForSide(originSide).getIndexForCharacter((Minion)this);
        }

        ArrayList<HearthTreeNode> children = new ArrayList<>();
        while (targetIterator.hasNext()) {
            BoardModel.CharacterLocation location = targetIterator.next();
            if (filter.targetMatches(originSide, this, location.getPlayerSide(), location.getIndex(), boardState.data_)) {
                boolean somethingHappened = false;
                HearthTreeNode newState = new HearthTreeNode(boardState.data_.deepCopy());
                Card origin;
                if (originInHand) {
                    origin = newState.data_.modelForSide(originSide).getHand().get(originIndex);
                } else {
                    origin = newState.data_.modelForSide(originSide).getCharacter(originIndex);
                }
                if (effect != null) {
                    newState = effect.applyEffect(originSide, origin, location.getPlayerSide(), location.getIndex(), newState);
                    somethingHappened = newState != null;
                }

                if (effectOthers != null && newState != null) {
                    for (BoardModel.CharacterLocation childLocation : newState.data_) {
                        if (location.equals(childLocation)) {
                            continue;
                        }
                        if (filter.targetMatches(originSide, origin, childLocation.getPlayerSide(), childLocation.getIndex(), boardState.data_)) {
                            newState = effectOthers.applyEffect(originSide, origin, childLocation.getPlayerSide(), childLocation.getIndex(), newState);
                            somethingHappened = newState != null;
                        }
                    }
                }

                if (somethingHappened) {
                    if (originInHand) {
                        newState.data_.modelForSide(originSide).getHand().remove(origin);
                    }
                    children.add(newState);
                }
            }
        }
        return children;
    }

    protected HearthTreeNode createRngNodeWithChildren(HearthTreeNode boardState, Collection<HearthTreeNode> rngChildren) {
        if (rngChildren != null && rngChildren.size() > 0) {
            RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
            boardState = this.createNodeWithChildren(rngNode, rngChildren);
        }
        return boardState;
    }

    protected HearthTreeNode createNodeWithChildren(HearthTreeNode boardState, Collection<HearthTreeNode> children) {
        if (children != null) {
            if (children.size() == 1) {
                boardState = children.stream().findAny().get();
            } else if (children.size() > 1) {
                boardState.addChildren(children);
            }
        }
        return boardState;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.getName());
        json.put("mana", this.getBaseManaCost());
        if (hasBeenUsed) json.put("hasBeenUsed", hasBeenUsed);
        return json;
    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }

    protected boolean isWaitingPlayer(PlayerSide side) {
        return PlayerSide.WAITING_PLAYER == side;
    }

    @Deprecated
    protected boolean isNotHero(Minion targetMinion) {
        return !isHero(targetMinion);
    }

    protected boolean isCurrentPlayer(PlayerSide side) {
        return PlayerSide.CURRENT_PLAYER == side;
    }

    protected byte getOverload() {
        if (this.implementedCard == null) {
            return 0;
        }
        return (byte) this.implementedCard.overload;
    }

    public boolean triggersOverload() {
        return this.getOverload() > 0;
    }

    public boolean hasDeathrattle() {
        return deathrattleAction_ != null;
    }

    public DeathrattleAction getDeathrattle() {
        return deathrattleAction_;
    }

    public void setDeathrattle(DeathrattleAction action) {
        deathrattleAction_ = action;
    }


    @Deprecated
    public Card(String name, byte baseManaCost, boolean hasBeenUsed, boolean isInHand, byte overload) {
        this.hasBeenUsed = hasBeenUsed;
        isInHand_ = isInHand;
        this.implementedCard = null;
    }

    @Deprecated
    public Card(byte baseManaCost, boolean hasBeenUsed, boolean isInHand) {
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        this.hasBeenUsed = hasBeenUsed;
        isInHand_ = isInHand;
        this.implementedCard = implementedCard;
    }

    @Deprecated
    public final HearthTreeNode useOn(PlayerSide side, Minion targetMinion, HearthTreeNode boardState,
                                      Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.useOn(side, targetMinion, boardState, false);
    }

    @Deprecated
    public HearthTreeNode useOn(PlayerSide side, int targetIndex, HearthTreeNode boardState, Deck deckPlayer0,
                                Deck deckPlayer1) throws HSException {
        return this.useOn(side, targetIndex, boardState, false);
    }

    @Deprecated
    protected boolean isHero(Minion targetMinion) {
        return targetMinion instanceof Hero;
    }

}
