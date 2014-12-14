package com.hearthsim.model;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.MinionList;
import org.json.JSONObject;
import org.slf4j.MDC;

import java.util.ArrayList;

/**
 * A class that represents the current state of the board (game)
 *
 */
public class BoardModel implements DeepCopyable<BoardModel> {

//    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final PlayerModel currentPlayer;
    private final PlayerModel waitingPlayer;

    IdentityLinkedList<MinionPlayerPair> allMinionsFIFOList_;

    public class MinionPlayerPair {
        private Minion minion;
        private PlayerModel playerModel ;

        public MinionPlayerPair(Minion minion, PlayerModel playerModel) {
            this.minion = minion;
            this.playerModel = playerModel;
        }

        public Minion getMinion() {
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



    public void buildModel() {
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

    public Minion getMinion(PlayerSide side, int index) throws HSInvalidPlayerIndexException {
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

    public Minion getCharacter(PlayerSide playerSide, int index) throws HSInvalidPlayerIndexException {
        PlayerModel playerModel = modelForSide(playerSide);
        return index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1);
    }

    public Minion getCurrentPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.CURRENT_PLAYER, index);
    }

    public Minion getWaitingPlayerCharacter(int index) {
        return getMinionForCharacter(PlayerSide.WAITING_PLAYER, index);
    }

    public Minion getMinionForCharacter(PlayerSide playerSide, int index) {
        PlayerModel playerModel = modelForSide(playerSide);
        return index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1);
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
    public void placeMinion(PlayerSide playerSide, Minion minion, int position) throws HSInvalidPlayerIndexException {
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
    public void placeMinion(PlayerSide playerSide, Minion minion) throws HSInvalidPlayerIndexException {
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

    public void placeCardDeck(PlayerSide side, Card card){
        modelForSide(side).placeCardDeck(card);
    }

    public Hero getHero(PlayerSide playerSide) {
        return modelForSide(playerSide).getHero();
    }

    public Hero getCurrentPlayerHero() {
        return currentPlayer.getHero();
    }

    public Hero getWaitingPlayerHero() {
        return waitingPlayer.getHero();
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

    public boolean removeMinion(MinionPlayerPair minionIdPair) throws HSInvalidPlayerIndexException {
        this.allMinionsFIFOList_.remove(minionIdPair);
        if (minionIdPair.getPlayerModel() == currentPlayer)
        	minionIdPair.minion.silenced(PlayerSide.CURRENT_PLAYER, this);
        else
        	minionIdPair.minion.silenced(PlayerSide.WAITING_PLAYER, this);
        return minionIdPair.getPlayerModel().getMinions().remove(minionIdPair.minion);
    }

    public boolean removeMinion(Minion minion) throws HSException {
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
        for (Minion minion : currentPlayer.getMinions()) {
            if (minion.getTotalHealth() <= 0)
                return true;
        }
        for (Minion minion : getWaitingPlayer().getMinions()) {
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
        for (Minion minion : currentPlayer.getMinions()) {
            minion.hasAttacked(false);
            minion.hasBeenUsed(false);
        }
        for (Minion minion : waitingPlayer.getMinions()) {
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

        BoardModel newBoard = new BoardModel(waitingPlayer.deepCopy(), currentPlayer.deepCopy());

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = minionPlayerPair.getPlayerModel();
            Minion oldMinion = minionPlayerPair.getMinion();
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

    @Override
	public BoardModel deepCopy() {
        BoardModel newBoard = new BoardModel(currentPlayer.deepCopy(), waitingPlayer.deepCopy());

        for (MinionPlayerPair minionPlayerPair : allMinionsFIFOList_) {

            PlayerModel oldPlayerModel = minionPlayerPair.getPlayerModel();
            Minion oldMinion = minionPlayerPair.getMinion();
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

    // TODO: remove asap, simply to aid in refactoring
    public int getIndexOfPlayer(PlayerSide playerSide) {
        if (playerSide == PlayerSide.CURRENT_PLAYER){
            return 0;
        }else{
            return 1;
        }
    }

    // TODO: remove asap, simply to aid in refactoring
    public PlayerSide getPlayerByIndex(int index) {
        if (index == 0){
            return PlayerSide.CURRENT_PLAYER;
        }else{
            return PlayerSide.WAITING_PLAYER;
        }
    }

}