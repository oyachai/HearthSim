package com.hearthsim.model;

import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.MinionList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.MDC;

import java.util.ArrayList;

/**
 * A class that represents the current state of the board (game)
 *
 */
public class BoardModel implements DeepCopyable {

//    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final PlayerModel currentPlayer;
    private final PlayerModel waitingPlayer;

    int p0_deckPos_;
    int p1_deckPos_;
    byte p0_fatigueDamage_;
    byte p1_fatigueDamage_;

    IdentityLinkedList<MinionPlayerPair> allMinionsFIFOList_;

    public class MinionPlayerPair {
        private BaseEntity minion;
        private PlayerModel playerModel ;

        public MinionPlayerPair(BaseEntity minion, PlayerModel playerModel) {
            this.minion = minion;
            this.playerModel = playerModel;
        }

        public BaseEntity getMinion() {
            return minion;
        }

        public PlayerModel getPlayerModel() {
            return playerModel;
        }

        public void setPlayerModel(PlayerModel playerModel) {
            this.playerModel = playerModel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinionPlayerPair that = (MinionPlayerPair) o;

            if (minion != null ? !minion.equals(that.minion) : that.minion != null) return false;
            if (playerModel != null ? !playerModel.equals(that.playerModel) : that.playerModel != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = minion != null ? minion.hashCode() : 0;
            result = 31 * result + (playerModel != null ? playerModel.hashCode() : 0);
            return result;
        }
    }

    public BoardModel() {
    	this(new PlayerModel(0, "player0", new Hero("hero0", (byte)30), null),
    		 new PlayerModel(1, "player1", new Hero("hero1", (byte)30), null));
    }
    
    public BoardModel(Hero p0_hero, Hero p1_hero) {
    	this(new PlayerModel(0, "p0",p0_hero,null), new PlayerModel(1, "p1",p1_hero,null));
    }
    
    public BoardModel(PlayerModel currentPlayerModel, PlayerModel waitingPlayerModel) {

        this.currentPlayer = currentPlayerModel;
        this.waitingPlayer = waitingPlayerModel;
        buildModel();
    }



    public void buildModel() {
        p0_deckPos_ = 0;
        p1_deckPos_ = 0;
        p0_fatigueDamage_ = 1;
        p1_fatigueDamage_ = 1;

        allMinionsFIFOList_ = new IdentityLinkedList<MinionPlayerPair>();
    }

    public MinionList getMinions(PlayerSide side) throws HSInvalidPlayerIndexException {
        return modelForSide(side).getMinions();
    }


    public IdentityLinkedList<Card> getCurrentPlayerHand() {
        return currentPlayer.getHand();
    }

    public IdentityLinkedList<Card> getWaitingPlayerHand() {
        return waitingPlayer.getHand();
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

    public PlayerSide sideForModel(PlayerModel model){
        if (model.equals(currentPlayer)){
            return PlayerSide.CURRENT_PLAYER;
        }else if (model.equals(waitingPlayer)){
            return PlayerSide.WAITING_PLAYER;
        }else {
            throw new RuntimeException("unexpected player model");
        }
    }

    public BaseEntity getMinion(PlayerSide side, int index) throws HSInvalidPlayerIndexException {
        return modelForSide(side).getMinions().get(index);
    }

    public Card getCard_hand(PlayerSide playerSide, int index) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getHand().get(index);
    }

    public Card getCurrentPlayerCardHand(int index) {
        return currentPlayer.getHand().get(index);
    }

    public Card getWaitingPlayerCardHand(int index) {
        return waitingPlayer.getHand().get(index);
    }

    public BaseEntity getCharacter(PlayerSide playerSide, int index) throws HSInvalidPlayerIndexException {
        PlayerModel playerModel = modelForSide(playerSide);
        return (BaseEntity) (index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1));
    }

    public BaseEntity getCurrentPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.CURRENT_PLAYER, index);
    }

    public BaseEntity getWaitingPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.WAITING_PLAYER, index);
    }

    public BaseEntity getMinionForCharacter(PlayerSide playerSide, int index) {
        PlayerModel playerModel = modelForSide(playerSide);
        return (index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1));
    }

    //-----------------------------------------------------------------------------------
    // Various ways to put a minion onto board
    //-----------------------------------------------------------------------------------
    /**
     * Place a minion onto the board.  Does not trigger any events.
     *
     * This is a function to place a minion on the board.  Use this function only if you
     * want no events to be trigger upon placing the minion.
     *
     *
     *
     * @param playerSide
     * @param minion The minion to be placed on the board.
     * @param position The position to place the minion.  The new minion goes to the "left" (lower index) of the postinion index.
     * @throws HSInvalidPlayerIndexException
     */
/*    public void placeMinion(PlayerSide playerSide, BaseEntity minion, int position) throws HSInvalidPlayerIndexException {
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.getMinions().add(position, minion);

        this.allMinionsFIFOList_.add(new MinionPlayerPair(minion, playerModel));
        minion.isInHand(false);*/
    public void placeMinion(PlayerSide playerSide, BaseEntity minion, int position) throws HSInvalidPlayerIndexException {
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.getMinions().add(position, minion);

        this.allMinionsFIFOList_.add(new MinionPlayerPair(minion, playerModel));
        minion.isInHand(false);       
    }


    /**
     * Place a minion onto the board.  Does not trigger any events.
     *
     * This is a function to place a minion on the board.  Use this function only if you
     * want no events to be trigger upon placing the minion.
     *
     *
     * @param playerSide
     * @param minion The minion to be placed on the board.  The minion is placed on the right-most space.
     * @throws HSInvalidPlayerIndexException
     */
    public void placeMinion(PlayerSide playerSide, BaseEntity minion) throws HSInvalidPlayerIndexException {
        minion.isInHand(false);
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.getMinions().add(minion);
        this.allMinionsFIFOList_.add(new MinionPlayerPair(minion, playerModel));
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------

    public void placeCard_hand(PlayerSide playerSide, Card card) throws HSInvalidPlayerIndexException {
        card.isInHand(true);
        modelForSide(playerSide).getHand().add(card);
    }

    public void placeCardHandCurrentPlayer(int cardIndex) {
        currentPlayer.placeCardHand(cardIndex);
    }

    public void placeCardHandCurrentPlayer(Card card) {
        currentPlayer.placeCardHand(card);
    }

    public void placeCardHandWaitingPlayer(int cardIndex) {
        waitingPlayer.placeCardHand(cardIndex);
    }

    public void placeCardHandWaitingPlayer(Card card) {
        waitingPlayer.placeCardHand(card);
    }

    public void removeCard_hand(Card card) {
        currentPlayer.getHand().remove(card);
    }

    public void placeCardHand(PlayerSide side, Card card){
        modelForSide(side).placeCardHand(card);
    }

    public void removeCardFromHand(Card card, PlayerSide playerSide){
        modelForSide(playerSide).getHand().remove(card);
    }

    public int getNumCards_hand() {
        return currentPlayer.getHand().size();
    }

    public int getNumCards_hand(PlayerSide playerSide) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getHand().size();
    }

    public int getNumCardsHandCurrentPlayer() {
        return currentPlayer.getHand().size();
    }

    public int getNumCardsHandWaitingPlayer() {
        return waitingPlayer.getHand().size();
    }

    public Hero getHero(PlayerSide playerSide) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getHero();
    }

    public Hero getCurrentPlayerHero() {
        return currentPlayer.getHero();
    }

    public Hero getWaitingPlayerHero() {
        return waitingPlayer.getHero();
    }

    public void setFatigueDamage(PlayerSide playerSide, byte fatigueDamage) {
        if (playerSide == PlayerSide.CURRENT_PLAYER){
            p0_fatigueDamage_= fatigueDamage;
        }else{
            p1_fatigueDamage_= fatigueDamage;
        }
    }

    public int getFatigueDamage(PlayerSide playerSide){
        if (playerSide==PlayerSide.CURRENT_PLAYER){
            return p0_fatigueDamage_;
        }else{
            return p1_fatigueDamage_;
        }
    }

    /**
     * Index of the next card in deck
     *
     * Returns the index of the next card to draw from the deck.
     * @return
     */
    public int getDeckPos(int playerIndex) throws HSInvalidPlayerIndexException {
        if (playerIndex == 0)
            return p0_deckPos_;
        else if (playerIndex == 1)
            return p1_deckPos_;
        else
            throw new HSInvalidPlayerIndexException();
    }

    public void setDeckPos(int playerIndex, int position) throws HSInvalidPlayerIndexException {
        if (playerIndex == 0)
            p0_deckPos_ = position;
        else if (playerIndex == 1)
            p1_deckPos_ = position;
        else
            throw new HSInvalidPlayerIndexException();
    }

    /**
     * Index of the next card in deck
     *
     * Returns the index of the next card to draw from the deck.
     * @return
     */
    public int getDeckPos_p0() {
        return p0_deckPos_;
    }

    public void setDeckPos_p0(int position) {
        p0_deckPos_ = position;
    }

    /**
     * Index of the next card in deck
     *
     * Returns the index of the next card to draw from the deck.
     * @return
     */
    public int getDeckPos_p1() {
        return p1_deckPos_;
    }

    public void setDeckPos_p1(int position) {
        p1_deckPos_ = position;
    }

    /**
     * Draw a card from a deck and place it in the hand
     *
     * This function is intentionally only implemented for Player1.
     * For Player0, it is almost always correct to user CardDrawNode instead.
     *
     * @param deck Deck from which to draw.
     * @param numCards Number of cards to draw.
     * @throws HSInvalidPlayerIndexException
     */
    public void drawCardFromWaitingPlayerDeck(Deck deck, int numCards) throws HSInvalidPlayerIndexException {
        //This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
        for (int indx = 0; indx < numCards; ++indx) {
            Card card = deck.drawCard(this.getDeckPos(1));
            if (card == null) {
                byte fatigueDamage = this.getFatigueDamage(1);
                this.setFatigueDamage(1, (byte)(fatigueDamage + 1));
                Hero waitingPlayerHero = this.getWaitingPlayer().getHero();
                waitingPlayerHero.setHealth((byte) (waitingPlayerHero.getHealth() - fatigueDamage));
            } else {
                if (this.getNumCardsHandWaitingPlayer() < 10) {
                    this.placeCard_hand(PlayerSide.WAITING_PLAYER, card);
                }
                this.setDeckPos(1, this.getDeckPos(1) + 1);
            }
        }
    }

    /**
     * Get the fatigue damage
     *
     * Returns the fatigue damage taken when a card draw fails next.
     * @return
     */
    public byte getFatigueDamage(int playerIndex) throws HSInvalidPlayerIndexException {
        if (playerIndex == 0)
            return p0_fatigueDamage_;
        else if (playerIndex == 1)
            return p1_fatigueDamage_;
        else
            throw new HSInvalidPlayerIndexException();
    }

    public void setFatigueDamage(int playerIndex, byte damage) throws HSInvalidPlayerIndexException {
        if (playerIndex == 0)
            p0_fatigueDamage_ = damage;
        else if (playerIndex == 1)
            p1_fatigueDamage_ = damage;
        else
            throw new HSInvalidPlayerIndexException();
    }

    /**
     * Get the fatigue damage for player 0
     *
     * Returns the fatigue damage taken when a card draw fails next.
     * @return
     */
    public byte getFatigueDamage_p0() {
        return p0_fatigueDamage_;
    }

    public void setFatigueDamage_p0(byte damage) {
        p0_fatigueDamage_ = damage;
    }


    /**
     * Get the spell damage
     *
     * Returns the additional spell damage provided by buffs
     * @return
     * @param playerSide
     */
    public byte getSpellDamage(PlayerSide playerSide) throws HSInvalidPlayerIndexException {
        return modelForSide(playerSide).getSpellDamage();
    }

    /**
     * Set the spell damage
     *
     * @return
     */
    public void setSpellDamage(PlayerSide playerSide, byte damage) throws HSInvalidPlayerIndexException {
        modelForSide(playerSide).setSpellDamage(damage);
    }

    /**
     * Add spell damage
     *
     * @return
     */
    public void addSpellDamage(PlayerSide playerSide, byte damage) throws HSInvalidPlayerIndexException {
        modelForSide(playerSide).addSpellDamage(damage);
    }

    /**
     * Add spell damage
     *
     * @return
     */
    public void subtractSpellDamage(PlayerSide playerSide, byte damage) throws HSInvalidPlayerIndexException {
        modelForSide(playerSide).subtractSpellDamage(damage);
    }

    public boolean removeMinion(MinionPlayerPair minionIdPair) throws HSInvalidPlayerIndexException {
        this.allMinionsFIFOList_.remove(minionIdPair);
        if (minionIdPair.getPlayerModel() == currentPlayer)
        	minionIdPair.minion.silenced(PlayerSide.CURRENT_PLAYER, this);
        else
        	minionIdPair.minion.silenced(PlayerSide.WAITING_PLAYER, this);
        return minionIdPair.getPlayerModel().getMinions().remove(minionIdPair.minion);
    }

    public boolean removeMinion(BaseEntity minion) throws HSException {
        MinionPlayerPair mP = null;
        for (MinionPlayerPair minionIdPair : this.allMinionsFIFOList_) {
            if (minionIdPair.minion == minion) {
                mP = minionIdPair;
                break;
            }
        }
        return this.removeMinion(mP);
    }

    public boolean removeMinion(PlayerSide side, int minionIndex) throws HSException {
        return removeMinion(getMinion(side, minionIndex));
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    // Overload support
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------

    public void addOverload(PlayerSide playerSide, byte overloadToAdd) throws HSException {
        PlayerModel playerModel = modelForSide(playerSide);
        playerModel.setOverload((byte) (playerModel.getOverload() + overloadToAdd));
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------


    public boolean isAlive(PlayerSide playerSide) {
        return modelForSide(playerSide).getHero().getHealth() > 0;
    }

    public ArrayList<Integer> getAttackableMinions() {
        ArrayList<Integer> toRet = new ArrayList<Integer>();
        boolean hasTaunt = false;
        for (final BaseEntity minion : waitingPlayer.getMinions()) {
            hasTaunt = hasTaunt || minion.getTaunt();
        }
        if (!hasTaunt) {
            toRet.add(0);
            int counter = 1;
            for (BaseEntity ignored : waitingPlayer.getMinions()) {
                toRet.add(counter);
                counter++;
            }
            return toRet;
        } else {
            int counter = 1;
            for (BaseEntity aP1_minions_ : waitingPlayer.getMinions()) {
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
        for (BaseEntity minion : currentPlayer.getMinions()) {
            if (minion.getTotalHealth() <= 0)
                return true;
        }
        for (BaseEntity minion : getWaitingPlayer().getMinions()) {
            if (minion.getTotalHealth() <= 0) {
                return true;
            }
        }
        return false;
    }

    public IdentityLinkedList<MinionPlayerPair> getAllMinionsFIFOList() {
        return allMinionsFIFOList_;
    }

    public void resetMana() {
    	currentPlayer.resetMana();
    	waitingPlayer.resetMana();
    }


    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        BoardModel bOther = (BoardModel)other;

        if (p0_deckPos_ != bOther.p0_deckPos_) return false;
        if (p1_deckPos_ != bOther.p1_deckPos_) return false;
        if (p0_fatigueDamage_ != bOther.p0_fatigueDamage_) return false;
        if (p1_fatigueDamage_ != bOther.p1_fatigueDamage_) return false;

        if (!currentPlayer.equals(bOther.currentPlayer)) return false;
        if (!waitingPlayer.equals(bOther.waitingPlayer)) return false;
     
        return true;
    }

    @Override
    public int hashCode() {
    	int hash = 1;
    	hash = hash * 31 + currentPlayer.hashCode();
    	hash = hash * 31 + waitingPlayer.hashCode();
    	hash = hash * 31 + p0_deckPos_;
    	hash = hash * 31 + p1_deckPos_;
    	hash = hash * 31 + p0_fatigueDamage_;
    	hash = hash * 31 + p1_fatigueDamage_;
    	
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
        for (BaseEntity minion : currentPlayer.getMinions()) {
            minion.hasAttacked(false);
            minion.hasBeenUsed(false);
        }
        for (BaseEntity minion : waitingPlayer.getMinions()) {
            minion.hasAttacked(false);
            minion.hasBeenUsed(false);
        }
    }

    /**
     * Reset the has_been_used state of the cards in hand
     */
    public void resetHand() {
        for(Card card : currentPlayer.getHand()) {
            card.hasBeenUsed(false);
        }
    }

    public BoardModel flipPlayers() {

        BoardModel newBoard = new BoardModel((PlayerModel) waitingPlayer.deepCopy(), (PlayerModel) currentPlayer.deepCopy());

        newBoard.p0_deckPos_ = this.p1_deckPos_;
        newBoard.p1_deckPos_ = this.p0_deckPos_;
        newBoard.p0_fatigueDamage_ = this.p1_fatigueDamage_;
        newBoard.p1_fatigueDamage_ = this.p0_fatigueDamage_;

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = minionPlayerPair.getPlayerModel();
            BaseEntity oldMinion = minionPlayerPair.getMinion();
            int indexOfOldMinion = oldPlayerModel.getMinions().indexOf(oldMinion);

            PlayerModel newPlayerModel;
            PlayerModel newWaitingPlayer = newBoard.getWaitingPlayer();
            PlayerModel newCurrentPlayer = newBoard.getCurrentPlayer();
            if (oldPlayerModel.equals(currentPlayer)) {
                newPlayerModel = newWaitingPlayer;
            } else if (oldPlayerModel.equals(waitingPlayer)) {
                newPlayerModel = newCurrentPlayer;
            } else {
                throw new RuntimeException("unexpected player");
            }

            newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newPlayerModel.getMinions().get(indexOfOldMinion), newPlayerModel));

        }
        
        return newBoard;
    }

    public Object deepCopy() {
        BoardModel newBoard = new BoardModel((PlayerModel) currentPlayer.deepCopy(), (PlayerModel) waitingPlayer.deepCopy());

        newBoard.p0_deckPos_ = this.p0_deckPos_;
        newBoard.p1_deckPos_ = this.p1_deckPos_;
        newBoard.p0_fatigueDamage_ = this.p0_fatigueDamage_;
        newBoard.p1_fatigueDamage_ = this.p1_fatigueDamage_;

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = minionPlayerPair.getPlayerModel();
            BaseEntity oldMinion = minionPlayerPair.getMinion();
            int indexOfOldMinion = oldPlayerModel.getMinions().indexOf(oldMinion);

            PlayerModel newPlayerModel;
            PlayerModel newWaitingPlayer = newBoard.getWaitingPlayer();
            PlayerModel newCurrentPlayer = newBoard.getCurrentPlayer();
            if (oldPlayerModel.equals(currentPlayer)) {
                newPlayerModel = newCurrentPlayer;
            } else if (oldPlayerModel.equals(waitingPlayer)) {
                newPlayerModel = newWaitingPlayer;
            } else {
                throw new RuntimeException("unexpected player");
            }

            newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newPlayerModel.getMinions().get(indexOfOldMinion), newPlayerModel));

        }

        return newBoard;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("p0_deckPos", p0_deckPos_);
        json.put("p1_deckPos", p1_deckPos_);

        JSONArray p0_minions = new JSONArray();
        for (BaseEntity minion : currentPlayer.getMinions())
            p0_minions.put(minion.toJSON());
        json.put("p0_minions", p0_minions);

        JSONArray p1_minions = new JSONArray();
        for (BaseEntity minion : waitingPlayer.getMinions())
            p1_minions.put(minion.toJSON());
        json.put("p1_minions", p1_minions);

        JSONArray p0_hand = new JSONArray();
        for (Card card : currentPlayer.getHand())
            p0_hand.put(card.toJSON());
        json.put("p0_hand", p0_hand);

        JSONArray p1_hand = new JSONArray();
        for (Card card : waitingPlayer.getHand())
            p1_hand.put(card.toJSON());
        json.put("p1_hand", p1_hand);

        json.put("p0_hero", currentPlayer.getHero().toJSON());
        json.put("p1_hero", waitingPlayer.getHero().toJSON());

        json.put("p0_fatigue", p0_fatigueDamage_);
        json.put("p1_fatigue", p1_fatigueDamage_);
        json.put("p0_spellDamage", currentPlayer.getSpellDamage());
        json.put("p1_spellDamage", waitingPlayer.getSpellDamage());
        return json;
    }

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

    // todo: remove asap, simply to aid in refactoring
    public int getIndexOfPlayer(PlayerSide playerSide) {
        if (playerSide == PlayerSide.CURRENT_PLAYER){
            return 0;
        }else{
            return 1;
        }
    }

    // todo: remove asap, simply to aid in refactoring
    public PlayerSide getPlayerByIndex(int index) {
        if (index == 0){
            return PlayerSide.CURRENT_PLAYER;
        }else{
            return PlayerSide.WAITING_PLAYER;
        }
    }

}