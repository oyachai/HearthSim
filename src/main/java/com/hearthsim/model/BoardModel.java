package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.AuraTargetType;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithAura;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;

import org.json.JSONObject;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * A class that represents the current state of the board (game)
 *
 */
public class BoardModel implements DeepCopyable<BoardModel>, Iterable<BoardModel.CharacterLocation> {

//    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final PlayerModel currentPlayer;
    private final PlayerModel waitingPlayer;

    private IdentityLinkedList<MinionPlayerPair> allMinionsFIFOList_;

    public class MinionPlayerPair {
        private final Minion minion;

        private PlayerSide playerSide;

        public MinionPlayerPair(Minion minion, PlayerSide playerSide) {
            this.minion = minion;
            this.playerSide = playerSide;
        }

        public Minion getMinion() {
            return minion;
        }

        public PlayerSide getPlayerSide() {
            return playerSide;
        }

        public void setPlayerSide(PlayerSide playerSide) {
            this.playerSide = playerSide;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinionPlayerPair that = (MinionPlayerPair) o;

            if (minion != null ? !minion.equals(that.minion) : that.minion != null) return false;
            if (playerSide != null ? !playerSide.equals(that.playerSide) : that.playerSide != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = minion != null ? minion.hashCode() : 0;
            result = 31 * result + (playerSide != null ? playerSide.hashCode() : 0);
            return result;
        }
    }

    public class CharacterLocation {
        private final PlayerSide playerSide;
        private final int index;

        public CharacterLocation(PlayerSide playerSide, int index) {
            this.playerSide = playerSide;
            this.index = index;
        }

        public PlayerSide getPlayerSide() {
            return this.playerSide;
        }

        public int getIndex() {
            return this.index;
        }

        public JSONObject toJSON() {
            JSONObject json = new JSONObject();

            json.put("playerSide", playerSide);
            json.put("index", index);

            return json;
        }

        public String toString() {
            return playerSide.toString() + ":" + index;
        }
    }

    private class CharacterLocationIterator implements Iterator<CharacterLocation> {
        private PlayerSide playerSide;
        private PlayerModel.CharacterIterator characterIterator;

        private final BoardModel model;

        public CharacterLocationIterator(BoardModel model) {
            this.model = model;

            this.playerSide = PlayerSide.CURRENT_PLAYER;
            this.characterIterator = this.model.modelForSide(this.playerSide).iterator();
        }

        @Override
        public boolean hasNext() {
            // there is always at least one on the waiting player side
            if (this.playerSide == PlayerSide.CURRENT_PLAYER) {
                return true;
            }

            return this.characterIterator.hasNext();
        }

        @Override
        public CharacterLocation next() {
            if (this.characterIterator.hasNext()) {
                this.characterIterator.next();
            } else if (this.playerSide == PlayerSide.CURRENT_PLAYER) {
                this.playerSide = this.playerSide.getOtherPlayer();
                this.characterIterator = this.model.modelForSide(this.playerSide).iterator();
                this.characterIterator.next();
            }

            return new CharacterLocation(this.playerSide, this.characterIterator.getLocation());
        }
    }

    @Override
    public Iterator<CharacterLocation> iterator() {
        return new CharacterLocationIterator(this);
    }

    public BoardModel() {
        this(new PlayerModel((byte)0, "player0", new TestHero("hero0", (byte)30), new Deck()),
             new PlayerModel((byte)1, "player1", new TestHero("hero1", (byte)30), new Deck()));
    }

    public BoardModel(Deck deckPlayer0, Deck deckPlayer1) {
        this(new PlayerModel((byte)0, "player0", new TestHero("hero0", (byte)30), deckPlayer0),
             new PlayerModel((byte)1, "player1", new TestHero("hero1", (byte)30), deckPlayer1));
    }

    public BoardModel(Hero p0_hero, Hero p1_hero) {
        this(new PlayerModel((byte)0, "p0",p0_hero,new Deck()), new PlayerModel((byte)1, "p1",p1_hero,new Deck()));
    }

    public BoardModel(PlayerModel currentPlayerModel, PlayerModel waitingPlayerModel) {
        this.currentPlayer = currentPlayerModel;
        this.waitingPlayer = waitingPlayerModel;
        buildModel();
    }

    private void buildModel() {
        allMinionsFIFOList_ = new IdentityLinkedList<MinionPlayerPair>();
    }

    public PlayerModel modelForSide(PlayerSide side){
        PlayerModel model;
        switch (side) {
            case CURRENT_PLAYER: model = currentPlayer;
                break;
            case WAITING_PLAYER: model = waitingPlayer;
                break;
            default:
                throw new RuntimeException("unexpected player side");
        }

        return model;
    }

    /**
     * Count all minions in play regardless of player side
     * @return The number of all minions in play
     */
    public int getTotalMinionCount() {
        return this.allMinionsFIFOList_.size();
    }

    public Card getCard_hand(PlayerSide playerSide, int index) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getHand().get(index);
    }

    public Minion getCharacter(CharacterLocation location) {
        return this.getCharacter(location.getPlayerSide(), location.getIndex());
    }

    public Minion getCharacter(PlayerSide playerSide, int index) {
        PlayerModel playerModel = modelForSide(playerSide);
        return playerModel.getCharacter(index);
    }

    //-----------------------------------------------------------------------------------
    // Various ways to put a minion onto board
    //-----------------------------------------------------------------------------------
    /**
     * Place a minion onto the board.  Does not trigger any events, but applies all auras.
     *
     * This is a function to place a minion on the board.  Use this function only if you
     * want no events to be trigger upon placing the minion.
     *
     *
     * @param playerSide
     * @param minion The minion to be placed on the board.
     * @param position The position to place the minion.  The new minion goes to the "left" (lower index) of the postinion index.
     * @throws HSInvalidPlayerIndexException
     */
    public void placeMinion(PlayerSide playerSide, Minion minion, int position) {
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.getMinions().add(position, minion);
        this.allMinionsFIFOList_.add(new MinionPlayerPair(minion, playerSide));
        minion.isInHand(false);

        //Apply the aura if any
        applyAuraOfMinion(playerSide, minion);
        applyOtherMinionsAura(playerSide, minion);
    }

    /**
     * Place a minion onto the board.  Does not trigger any events, but applies all auras.
     *
     * This is a function to place a minion on the board.  Use this function only if you
     * want no events to be trigger upon placing the minion.
     *
     *
     * @param playerSide
     * @param minion The minion to be placed on the board.  The minion is placed on the right-most space.
     * @throws HSInvalidPlayerIndexException
     */
    public void placeMinion(PlayerSide playerSide, Minion minion) {
        this.placeMinion(playerSide, minion, this.modelForSide(playerSide).getMinions().size());
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------

    public void placeCardHand(PlayerSide side, Card card){
        modelForSide(side).placeCardHand(card);
    }

    public void removeCardFromHand(Card card, PlayerSide playerSide){
        modelForSide(playerSide).getHand().remove(card);
    }

    public int getNumCards_hand(PlayerSide playerSide) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getHand().size();
    }

    public void placeCardDeck(PlayerSide side, Card card){
        modelForSide(side).placeCardDeck(card);
    }

    /**
     * Draw a card from a deck and place it in the hand
     *
     * @param deck Deck from which to draw.
     * @param numCards Number of cards to draw.
     * @throws HSInvalidPlayerIndexException
     */
    public void drawCardFromWaitingPlayerDeck(int numCards) {
        //This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
        for (int indx = 0; indx < numCards; ++indx) {
            this.getWaitingPlayer().drawNextCardFromDeck();
        }
    }

    /**
     * Draw a card from a deck and place it in the hand without using a CardDrawNode
     *
     * Note: It is almost always correct to use CardDrawNode instead of this function!!!!
     *
     * @param deck Deck from which to draw.
     * @param numCards Number of cards to draw.
     * @throws HSInvalidPlayerIndexException
     */
    public void drawCardFromCurrentPlayerDeck(int numCards) throws HSInvalidPlayerIndexException {
        for (int indx = 0; indx < numCards; ++indx) {
            this.getCurrentPlayer().drawNextCardFromDeck();
        }
    }

    public boolean removeMinion(MinionPlayerPair minionIdPair) {
        this.allMinionsFIFOList_.remove(minionIdPair);
        removeAuraOfMinion(minionIdPair.getPlayerSide(), minionIdPair.minion);
        return this.modelForSide(minionIdPair.getPlayerSide()).getMinions().remove(minionIdPair.minion);
    }

    public boolean removeMinion(Minion minion) {
        MinionPlayerPair mP = null;
        for (MinionPlayerPair minionIdPair : this.allMinionsFIFOList_) {
            if (minionIdPair.minion == minion) {
                mP = minionIdPair;
                break;
            }
        }
        return this.removeMinion(mP);
    }

    public boolean removeMinion(PlayerSide side, int minionIndex) {
        return removeMinion(getMinion(side, minionIndex));
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------


    //----------------------------------------------------------------------------
    // Aura Handling
    //
    // Aura handling is delegated to BoardModel rather than Minion class
    //----------------------------------------------------------------------------

    /**
     * Applies any aura that the given minion has
     *
     * @param side The PlayerSide of the minion
     * @param minion
     */
    private void applyAuraOfMinion(PlayerSide side, Minion minion) {
        if (minion instanceof MinionWithAura && !minion.isSilenced()) {
            EnumSet<AuraTargetType> targetTypes = ((MinionWithAura)minion).getAuraTargets();
            for (AuraTargetType targetType : targetTypes) {
                switch (targetType) {
                case AURA_FRIENDLY_MINIONS:
                    for (Minion targetMinion : this.modelForSide(side).getMinions()) {
                        if (targetMinion != minion)
                            ((MinionWithAura) minion).applyAura(side, targetMinion, this);
                    }
                    break;
                case AURA_ENEMY_MINIONS:
                    for (Minion targetMinion : this.modelForSide(side.getOtherPlayer()).getMinions()) {
                        ((MinionWithAura) minion).applyAura(side, targetMinion, this);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    /**
     * Applies any aura effects that other minions on the board might have
     *
     * @param side The PlayerSide of the minion to apply the auras to
     * @param minion
     */
    private void applyOtherMinionsAura(PlayerSide side, Minion minion) {
        for (Minion otherMinion : this.modelForSide(side).getMinions()) {
            if (otherMinion instanceof MinionWithAura &&
                    minion != otherMinion &&
                    !otherMinion.isSilenced() &&
                    ((MinionWithAura)otherMinion).getAuraTargets().contains(AuraTargetType.AURA_FRIENDLY_MINIONS)) {
                ((MinionWithAura)otherMinion).applyAura(side, minion, this);
            }
        }
        for (Minion otherMinion : this.modelForSide(side.getOtherPlayer()).getMinions()) {
            if (otherMinion instanceof MinionWithAura &&
                    minion != otherMinion &&
                    !otherMinion.isSilenced() &&
                    ((MinionWithAura)otherMinion).getAuraTargets().contains(AuraTargetType.AURA_ENEMY_MINIONS)) {
                ((MinionWithAura)otherMinion).applyAura(side, minion, this);
            }
        }
    }

    /**
     * Revomes any aura that the given minion might have
     *
     * @param side The PlayerSide of the minion
     * @param minion
     */
    public void removeAuraOfMinion(PlayerSide side, Minion minion) {
        if (minion instanceof MinionWithAura && !minion.isSilenced()) {
            EnumSet<AuraTargetType> targetTypes = ((MinionWithAura)minion).getAuraTargets();
            for (AuraTargetType targetType : targetTypes) {
                switch (targetType) {
                case AURA_FRIENDLY_MINIONS:
                    for (Minion targetMinion : this.modelForSide(side).getMinions()) {
                        if (targetMinion != minion)
                            ((MinionWithAura) minion).removeAura(side, targetMinion, this);
                    }
                    break;
                case AURA_ENEMY_MINIONS:
                    for (Minion targetMinion : this.modelForSide(side.getOtherPlayer()).getMinions()) {
                        ((MinionWithAura) minion).removeAura(side, targetMinion, this);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }


    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------

    public boolean isAlive(PlayerSide playerSide) {
        return modelForSide(playerSide).getHero().getHealth() > 0;
    }

    public boolean isLethalState() {
        return !this.isAlive(PlayerSide.CURRENT_PLAYER) || !this.isAlive(PlayerSide.WAITING_PLAYER);
    }

    @SuppressWarnings("unused")
    public ArrayList<Integer> getAttackableMinions() {
        ArrayList<Integer> toRet = new ArrayList<Integer>();
        boolean hasTaunt = false;
        for (final Minion minion : waitingPlayer.getMinions()) {
            hasTaunt = hasTaunt || minion.getTaunt();
        }
        if (!hasTaunt) {
            toRet.add(0);
            int counter = 1;
            for (Minion ignored : waitingPlayer.getMinions()) {
                toRet.add(counter);
                counter++;
            }
            return toRet;
        } else {
            int counter = 1;
            for (Minion aP1_minions_ : waitingPlayer.getMinions()) {
                if (aP1_minions_.getTaunt())
                    toRet.add(counter);
                counter++;
            }
            return toRet;
        }
    }


    //Dead minion check

    /**
     * Checks to see if there are dead minions
     * @return
     */
    public boolean hasDeadMinions() {
        for (Minion minion : this.getAllMinions()) {
            if (minion.getTotalHealth() <= 0)
                return true;
        }
        return false;
    }

    public Collection<Minion> getAllMinions() {
        ArrayList<Minion> minions = new ArrayList<>();
        minions.addAll(this.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions());
        minions.addAll(this.modelForSide(PlayerSide.WAITING_PLAYER).getMinions());
        return minions;
    }

    public IdentityLinkedList<MinionPlayerPair> getAllMinionsFIFOList() {
        return allMinionsFIFOList_;
    }

    public void resetMana() {
        currentPlayer.resetMana();
        waitingPlayer.resetMana();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        BoardModel bOther = (BoardModel)other;

        if (!currentPlayer.equals(bOther.currentPlayer)) return false;
        if (!waitingPlayer.equals(bOther.waitingPlayer)) return false;
        if (!allMinionsFIFOList_.equals(bOther.allMinionsFIFOList_)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + currentPlayer.hashCode();
        hash = hash * 31 + waitingPlayer.hashCode();
        hash = hash * 31 + allMinionsFIFOList_.hashCode();
        return hash;
    }

    /**
     * Reset all minions to clear their has_attacked state.
     *
     * Call this function at the beginning of each turn
     *
     */
    public void resetMinions() {
        currentPlayer.getHero().hasAttacked(false);
        currentPlayer.getHero().hasBeenUsed(false);
        waitingPlayer.getHero().hasAttacked(false);
        waitingPlayer.getHero().hasBeenUsed(false);
        for (Minion minion : this.getAllMinions()) {
            minion.hasAttacked(false);
            minion.hasBeenUsed(false);
        }
    }

    /**
     * Reset the has_been_used state of the cards in hand
     */
    public void resetHand() {
        for (Card card : currentPlayer.getHand()) {
            card.hasBeenUsed(false);
        }
    }

    public BoardModel flipPlayers() {

        BoardModel newBoard = new BoardModel(waitingPlayer.deepCopy(), currentPlayer.deepCopy());

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = this.modelForSide(minionPlayerPair.getPlayerSide());
            Minion oldMinion = minionPlayerPair.getMinion();
            int indexOfOldMinion = oldPlayerModel.getMinions().indexOf(oldMinion);

            PlayerModel newPlayerModel = newBoard.modelForSide(minionPlayerPair.getPlayerSide().getOtherPlayer());
            newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newPlayerModel.getMinions().get(indexOfOldMinion), minionPlayerPair.getPlayerSide().getOtherPlayer()));
        }

        return newBoard;
    }

    @Override
    public BoardModel deepCopy() {
        BoardModel newBoard = new BoardModel(currentPlayer.deepCopy(), waitingPlayer.deepCopy());

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = this.modelForSide(minionPlayerPair.getPlayerSide());
            Minion oldMinion = minionPlayerPair.getMinion();
            int indexOfOldMinion = oldPlayerModel.getMinions().indexOf(oldMinion);

            PlayerModel newPlayerModel = newBoard.modelForSide(minionPlayerPair.getPlayerSide());
            newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newPlayerModel.getMinions().get(indexOfOldMinion), minionPlayerPair.getPlayerSide()));
        }

        return newBoard;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("currentPlayer", currentPlayer.toJSON());
        json.put("waitingPlayer", waitingPlayer.toJSON());

        return json;
    }

    @Override
    public String toString() {
        String boardFormat = MDC.get("board_format");
        if (boardFormat != null && boardFormat.equals("simple")) {
            return simpleString();
        } else {
            return jsonString();
        }
    }

    private String simpleString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append("P0_health:").append(currentPlayer.getHero().getHealth()).append(", ");
        stringBuffer.append("P0_mana:").append(currentPlayer.getMana()).append(", ");
        stringBuffer.append("P1_health:").append(waitingPlayer.getHero().getHealth()).append(", ");
        stringBuffer.append("P1_mana:").append(waitingPlayer.getMana()).append(", ");
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private String jsonString() {
        return this.toJSON().toString();
    }

    public PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerModel getWaitingPlayer() {
        return waitingPlayer;
    }

    @Deprecated
    public int getIndexOfPlayer(PlayerSide playerSide) {
        if (playerSide == PlayerSide.CURRENT_PLAYER){
            return 0;
        } else {
            return 1;
        }
    }

    @Deprecated
    public PlayerSide getPlayerByIndex(int index) {
        if (index == 0){
            return PlayerSide.CURRENT_PLAYER;
        } else {
            return PlayerSide.WAITING_PLAYER;
        }
    }

    @Deprecated
    public IdentityLinkedList<Card> getCurrentPlayerHand() {
        return currentPlayer.getHand();
    }

    @Deprecated
    public IdentityLinkedList<Card> getWaitingPlayerHand() {
        return waitingPlayer.getHand();
    }

    @Deprecated
    public PlayerSide sideForModel(PlayerModel model) {
        if (model.equals(currentPlayer)) {
            return PlayerSide.CURRENT_PLAYER;
        } else if (model.equals(waitingPlayer)) {
            return PlayerSide.WAITING_PLAYER;
        } else {
            throw new RuntimeException("unexpected player model");
        }
    }

    @Deprecated
    public Card getCurrentPlayerCardHand(int index) {
        return currentPlayer.getHand().get(index);
    }

    @Deprecated
    public Card getWaitingPlayerCardHand(int index) {
        return waitingPlayer.getHand().get(index);
    }

    @Deprecated
    public Minion getCurrentPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.CURRENT_PLAYER, index);
    }

    @Deprecated
    public Minion getWaitingPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.WAITING_PLAYER, index);
    }

    @Deprecated
    public void placeCard_hand(PlayerSide playerSide, Card card) throws HSInvalidPlayerIndexException {
        card.isInHand(true);
        modelForSide(playerSide).getHand().add(card);
    }

    @Deprecated
    public void placeCardHandCurrentPlayer(int cardIndex) {
        currentPlayer.placeCardHand(cardIndex);
    }

    @Deprecated
    public void placeCardHandCurrentPlayer(Card card) {
        currentPlayer.placeCardHand(card);
    }

    @Deprecated
    public void placeCardHandWaitingPlayer(int cardIndex) {
        waitingPlayer.placeCardHand(cardIndex);
    }

    @Deprecated
    public void placeCardHandWaitingPlayer(Card card) {
        waitingPlayer.placeCardHand(card);
    }

    @Deprecated
    public void removeCard_hand(Card card) {
        currentPlayer.getHand().remove(card);
    }

    @Deprecated
    public int getNumCards_hand() {
        return currentPlayer.getHand().size();
    }

    @Deprecated
    public int getNumCardsHandCurrentPlayer() {
        return currentPlayer.getHand().size();
    }

    @Deprecated
    public int getNumCardsHandWaitingPlayer() {
        return waitingPlayer.getHand().size();
    }

    @Deprecated
    public Hero getCurrentPlayerHero() {
        return currentPlayer.getHero();
    }

    @Deprecated
    public Hero getWaitingPlayerHero() {
        return waitingPlayer.getHero();
    }

    @Deprecated
    public Hero getHero(PlayerSide playerSide) {
        return modelForSide(playerSide).getHero();
    }

    @Deprecated
    public IdentityLinkedList<Minion> getMinions(PlayerSide side) throws HSInvalidPlayerIndexException {
        return modelForSide(side).getMinions();
    }

    @Deprecated
    private Minion getMinionForCharacter(PlayerSide playerSide, int index) {
        PlayerModel playerModel = modelForSide(playerSide);
        return index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1);
    }

    /**
     * Get the spell damage
     *
     * Returns the additional spell damage provided by buffs
     * @return
     * @param playerSide
     */
    @Deprecated
    public byte getSpellDamage(PlayerSide playerSide) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getSpellDamage();
    }

    @Deprecated
    public void addOverload(PlayerSide playerSide, byte overloadToAdd) throws HSException {
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.setOverload((byte) (playerModel.getOverload() + overloadToAdd));
    }

    @Deprecated // use PlayerModel.getCharacter instead
    public Minion getMinion(PlayerSide side, int index) {
        return modelForSide(side).getMinions().get(index);
    }
}
