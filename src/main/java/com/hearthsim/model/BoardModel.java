
package com.hearthsim.model;

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

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    int p0_mana_;
    int p1_mana_;
    int p0_maxMana_;
    int p1_maxMana_;

    int p0_deckPos_;
    int p1_deckPos_;
    byte p0_fatigueDamage_;
    byte p1_fatigueDamage_;

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
        Hero hero0 = new Hero("hero0", (byte) 30);
        Hero hero1 = new Hero("hero1", (byte) 30);

        this.currentPlayer = new PlayerModel("player0", hero0, null);
        this.waitingPlayer = new PlayerModel("player1", hero1, null);

        buildModel();
    }

    public BoardModel(PlayerModel playerModel0, PlayerModel playerModel1, PlayerModel firstPlayer) {

        if (firstPlayer == playerModel0){
            this.currentPlayer = playerModel0;
            this.waitingPlayer = playerModel1;
        }else{
            this.currentPlayer = playerModel1;
            this.waitingPlayer = playerModel0;
        }

        buildModel();
    }

    public BoardModel(Hero p0_hero, Hero p1_hero) {
        this.currentPlayer = new PlayerModel("p0",p0_hero,null);
        this.waitingPlayer = new PlayerModel("p1",p1_hero,null);
        buildModel();
    }

    public void buildModel() {
        p0_mana_ = 0;
        p1_mana_ = 0;
        p0_maxMana_ = 0;
        p1_maxMana_ = 0;
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

    public Minion getMinion(PlayerSide side, int index) throws HSInvalidPlayerIndexException {
        return modelForSide(side).getMinions().get(index);
    }

    public Card getCard_hand(PlayerModel playerModel, int index) throws HSInvalidPlayerIndexException {
        return playerModel.getHand().get(index);
    }

    public Card getCurrentPlayerCardHand(int index) {
        return currentPlayer.getHand().get(index);
    }

    public Card getWaitingPlayerCardHand(int index) {
        return waitingPlayer.getHand().get(index);
    }

    public Minion getCharacter(PlayerModel playerModel, int index) throws HSInvalidPlayerIndexException {
        return index == 0 ? playerModel.getHero() : playerModel.getMinions().get(index - 1);
    }

    public Minion getCurrentPlayerCharacter(int index) {
        return getMinionForCharacter(currentPlayer, index);
    }

    public Minion getWaitingPlayerCharacter(int index) {
        return getMinionForCharacter(waitingPlayer, index);
    }

    public Minion getMinionForCharacter(PlayerModel playerModel, int index) {
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

    public int getMana_p0() {
        return p0_mana_;
    }

    public void setMana_p0(int mana) {
        p0_mana_ = mana;
    }

    public int getMana_p1() {
        return p1_mana_;
    }

    public void setMana_p1(int mana) {
        p1_mana_ = mana;
    }

    public int getMaxMana_p0() {
        return p0_maxMana_;
    }

    public void setMaxMana_p0(int mana) {
        p0_maxMana_ = mana;
    }

    public int getMaxMana_p1() {
        return p1_maxMana_;
    }

    public void setMaxMana_p1(int mana) {
        p1_maxMana_ = mana;
    }

    public void addMaxMana_p0(int mana) {
        p0_maxMana_ += mana;
    }

    public void addMaxMana_p1(int mana) {
        p1_maxMana_ += mana;
    }

    public void addMana_p1(int mana) {
        p1_mana_ += mana;
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
     * Returns the additional spell damage provided by buffs
     * @return
     */
    public void setSpellDamage(PlayerSide playerSide, byte damage) throws HSInvalidPlayerIndexException {
        modelForSide(playerSide).setSpellDamage(damage);
    }


    public boolean removeMinion(MinionPlayerPair minionIdPair) throws HSInvalidPlayerIndexException {
        this.allMinionsFIFOList_.remove(minionIdPair);
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
        this.allMinionsFIFOList_.remove(mP);
        return mP.getPlayerModel().getMinions().remove(mP.minion);
    }

    public boolean removeMinion(PlayerSide side, int minionIndex) throws HSException {
        return removeMinion(getMinion(side, minionIndex));
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    // Overload support
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------

    public void addOverload(PlayerModel playerModel, byte overloadToAdd) throws HSException {
        playerModel.setOverload((byte) (currentPlayer.getOverload() + overloadToAdd));
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------


    public boolean isAlive(PlayerModel playerModel) {
        return playerModel.getHero().getHealth() > 0;
    }

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
        p0_mana_ = p0_maxMana_;
        p1_mana_ = p1_maxMana_;

        //handle the overload
        p0_mana_ -= currentPlayer.getOverload();
        p1_mana_ -= waitingPlayer.getOverload();

        currentPlayer.setOverload((byte) 0);
        waitingPlayer.setOverload((byte) 0);
    }

    public void endTurn(Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
        currentPlayer.getHero().endTurn(PlayerSide.CURRENT_PLAYER, this, deckPlayer0, deckPlayer1);
        waitingPlayer.getHero().endTurn(PlayerSide.WAITING_PLAYER, this, deckPlayer0, deckPlayer1);
        for (int index = 0; index < currentPlayer.getMinions().size(); ++index) {
            Minion targetMinion = currentPlayer.getMinions().get(index);
            try {
                targetMinion.endTurn(PlayerSide.CURRENT_PLAYER, this, deckPlayer0, deckPlayer1);
            } catch (HSException e) {
                e.printStackTrace();
            }
        }
        for (int index = 0; index < waitingPlayer.getMinions().size(); ++index) {
            Minion targetMinion = waitingPlayer.getMinions().get(index);
            try {
                targetMinion.endTurn(PlayerSide.WAITING_PLAYER, this, deckPlayer0, deckPlayer1);
            } catch (HSException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Minion> toRemove = new ArrayList<Minion>();
        for (Minion targetMinion : currentPlayer.getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
            currentPlayer.getMinions().remove(minion);

        toRemove.clear();
        for (Minion targetMinion : waitingPlayer.getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
            waitingPlayer.getMinions().remove(minion);
    }

    public void startTurn() throws HSException {
        this.resetHand();
        this.resetMinions();

        for (Minion targetMinion : currentPlayer.getMinions()) {
            try {
                targetMinion.startTurn(PlayerSide.CURRENT_PLAYER, this, currentPlayer.getDeck(), waitingPlayer.getDeck());
            } catch (HSInvalidPlayerIndexException e) {
                e.printStackTrace();
            }
        }
        for (Minion targetMinion : waitingPlayer.getMinions()) {
            try {
                targetMinion.startTurn(PlayerSide.WAITING_PLAYER, this, currentPlayer.getDeck(), waitingPlayer.getDeck());
            } catch (HSInvalidPlayerIndexException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Minion> toRemove = new ArrayList<Minion>();
        for (Minion targetMinion : currentPlayer.getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
            currentPlayer.getMinions().remove(minion);

        toRemove.clear();
        for (Minion targetMinion : waitingPlayer.getMinions()) {
            if (targetMinion.getTotalHealth() <= 0)
                toRemove.add(targetMinion);
        }
        for (Minion minion : toRemove)
            waitingPlayer.getMinions().remove(minion);
    }

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

        if (p0_mana_ != ((BoardModel)other).p0_mana_)
            return false;
        if (p1_mana_ != ((BoardModel)other).p1_mana_)
            return false;
        if (p0_maxMana_ != ((BoardModel)other).p0_maxMana_)
            return false;
        if (p1_maxMana_ != ((BoardModel)other).p1_maxMana_)
            return false;

        if (!currentPlayer.getHero().equals(((BoardModel)other).currentPlayer.getHero())) {
            return false;
        }

        if (!waitingPlayer.getHero().equals(((BoardModel)other).waitingPlayer.getHero())) {
            return false;
        }

        if (p0_deckPos_ != ((BoardModel)other).p0_deckPos_)
            return false;

        if (p1_deckPos_ != ((BoardModel)other).p1_deckPos_)
            return false;

        if (p0_fatigueDamage_ != ((BoardModel)other).p0_fatigueDamage_)
            return false;

        if (p1_fatigueDamage_ != ((BoardModel)other).p1_fatigueDamage_)
            return false;

        if (currentPlayer.getSpellDamage() != ((BoardModel)other).getCurrentPlayer().getSpellDamage())
            return false;

        if (getWaitingPlayer().getSpellDamage() != ((BoardModel)other).getWaitingPlayer().getSpellDamage())
            return false;

        if (currentPlayer.getMinions().size() != ((BoardModel)other).getCurrentPlayer().getMinions().size())
            return false;
        if (waitingPlayer.getMinions().size() != ((BoardModel)other).getWaitingPlayer().getMinions().size())
            return false;
        if (currentPlayer.getHand().size() != ((BoardModel)other).getCurrentPlayer().getHand().size())
            return false;

        for (int i = 0; i < currentPlayer.getMinions().size(); ++i) {
            if (!currentPlayer.getMinions().get(i).equals(((BoardModel)other).getCurrentPlayer().getMinions().get(i))) {
                return false;
            }
        }

        for (int i = 0; i < waitingPlayer.getMinions().size(); ++i) {
            if (!waitingPlayer.getMinions().get(i).equals(((BoardModel)other).getWaitingPlayer().getMinions().get(i))) {
                return false;
            }
        }

        for (int i = 0; i < currentPlayer.getHand().size(); ++i) {
            if (!currentPlayer.getHand().get(i).equals(((BoardModel) other).getCurrentPlayer().getHand().get(i))) {
                return false;
            }
        }

        for (int i = 0; i < waitingPlayer.getHand().size(); ++i) {
            if (!waitingPlayer.getHand().get(i).equals(((BoardModel)other).waitingPlayer.getHand().get(i))) {
                return false;
            }
        }

        // More logic here to be discuss below...
        return true;
    }

    @Override
    public int hashCode() {
        int hs = currentPlayer.getHand().size();
        if (hs < 0) hs = 0;
        int res = hs + getCurrentPlayer().getMinions().size() * 10 + waitingPlayer.getMinions().size() * 100;
        res += (p0_mana_ <= 0 ? 0 : (p0_mana_ - 1) * 1000);
        res += ((currentPlayer.getHero().getHealth() + waitingPlayer.getHero().getHealth()) % 100) * 10000;
        int th = 0;
        if (hs > 0) {
            Card cc = currentPlayer.getHand().get(0);
            try {
                Minion mm = (Minion)cc;
                th += (cc.hasBeenUsed() ? 1 : 0) + mm.getAttack() + mm.getHealth() + cc.getMana();
            } catch (ClassCastException e) {
                th += (cc.hasBeenUsed() ? 1 : 0) + cc.getMana();
            }
        }
        if (hs > 1) {
            Card cc = currentPlayer.getHand().get(1);
            try {
                Minion mm = (Minion)cc;
                th += (cc.hasBeenUsed() ? 1 : 0) + mm.getAttack() + mm.getHealth() + cc.getMana();
            } catch (ClassCastException e) {
                th += (cc.hasBeenUsed() ? 1 : 0) + cc.getMana();
            }
        }
        res += (th % 10) * 1000000;
        int mh0 = 0;
        if (currentPlayer.getMinions().size() > 0) {
            mh0 += currentPlayer.getMinions().get(0).getHealth();
        }
        if (currentPlayer.getMinions().size() > 1) {
            mh0 += currentPlayer.getMinions().get(1).getHealth();
        }
        res += (mh0 % 100) * 10000000;
        int mh1 = 0;
        if (waitingPlayer.getMinions().size() > 0) {
            mh1 += waitingPlayer.getMinions().get(0).getHealth();
        }
        if (waitingPlayer.getMinions().size() > 1) {
            mh1 += waitingPlayer.getMinions().get(1).getHealth();
        }
        res += (mh1 % 20) * 100000000;
        return res;
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

        BoardModel newState = (BoardModel)this.deepCopy();
        int p0_mana = newState.getMana_p0();
        int p1_mana = newState.getMana_p1();
        int p0_maxMana = newState.getMaxMana_p0();
        int p1_maxMana = newState.getMaxMana_p1();

        PlayerModel tmpPlayer = newState.getCurrentPlayer();
        newState.setCurrentPlayer(newState.getWaitingPlayer());
        newState.setWaitingPlayer(tmpPlayer);
        newState.setMana_p0(p1_mana);
        newState.setMana_p1(p0_mana);
        newState.setMaxMana_p0(p1_maxMana);
        newState.setMaxMana_p1(p0_maxMana);
        newState.p0_deckPos_ = p1_deckPos_;
        newState.p1_deckPos_ = p0_deckPos_;
        newState.p0_fatigueDamage_ = p1_fatigueDamage_;
        newState.p1_fatigueDamage_ = p0_fatigueDamage_;

        return newState;
    }

    public Object deepCopy() {
        BoardModel newBoard = new BoardModel();

        newBoard.setCurrentPlayer((PlayerModel) currentPlayer.deepCopy());
        newBoard.setWaitingPlayer((PlayerModel) waitingPlayer.deepCopy());

        newBoard.p0_deckPos_ = this.p0_deckPos_;
        newBoard.p1_deckPos_ = this.p1_deckPos_;
        newBoard.p0_fatigueDamage_ = this.p0_fatigueDamage_;
        newBoard.p1_fatigueDamage_ = this.p1_fatigueDamage_;
        newBoard.p0_mana_ = this.p0_mana_;
        newBoard.p1_mana_ = this.p1_mana_;
        newBoard.p0_maxMana_ = this.p0_maxMana_;
        newBoard.p1_maxMana_ = this.p1_maxMana_;

//        for (MinionPlayerIDPair minionIdPair : allMinionsFIFOList_) {
//            if (minionIdPair.playerIndex_ == 0) {
//                int minionIndex = p0_minions_.indexOf(minionIdPair.minion_);
//                newBoard.allMinionsFIFOList_.add(new MinionPlayerIDPair(newBoard.p0_minions_.get(minionIndex), 0));
//            } else if (minionIdPair.playerIndex_ == 1) {
//                int minionIndex = p1_minions_.indexOf(minionIdPair.minion_);
//                newBoard.allMinionsFIFOList_.add(new MinionPlayerIDPair(newBoard.p1_minions_.get(minionIndex), 1));
//            }
//        }

        // todo: need to get the minion from the new board and update it to the new player
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
                System.out.println(System.identityHashCode(oldPlayerModel));
                throw new RuntimeException("WHAHAHAA"); // how is this possible? where does the missing player get in?
            }

            newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newPlayerModel.getMinions().get(indexOfOldMinion), newPlayerModel));

        }

        //todo: the problem here is that player references are not updated to new players..
//        for (MinionPlayerPair minionIdPair : allMinionsFIFOList_) {
//
//            if (minionIdPair.playerModel == currentPlayer) {
//                int minionIndex = currentPlayer.getMinions().indexOf(minionIdPair.minion);
//                newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newBoard.getCurrentPlayer().getMinions().get(minionIndex), minionIdPair.playerModel));
//            } else {
//                int minionIndex = waitingPlayer.getMinions().indexOf(minionIdPair.minion);
//                newBoard.allMinionsFIFOList_.add(new MinionPlayerPair(newBoard.getWaitingPlayer().getMinions().get(minionIndex), minionIdPair.playerModel));
//            }
//        }
        return newBoard;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("p0_mana", p0_mana_);
        json.put("p1_mana", p1_mana_);
        json.put("p0_maxMana", p0_maxMana_);
        json.put("p1_maxMana", p0_maxMana_);
        json.put("p0_deckPos", p0_deckPos_);
        json.put("p1_deckPos", p1_deckPos_);

        JSONArray p0_minions = new JSONArray();
        for (Minion minion : currentPlayer.getMinions())
            p0_minions.put(minion.toJSON());
        json.put("p0_minions", p0_minions);

        JSONArray p1_minions = new JSONArray();
        for (Minion minion : waitingPlayer.getMinions())
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
        stringBuffer.append("P0_mana:").append(p0_mana_).append(", ");
        stringBuffer.append("P1_health:").append(waitingPlayer.getHero().getHealth()).append(", ");
        stringBuffer.append("P1_mana:").append(p1_mana_).append(", ");
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private String jsonString() {
        return this.toJSON().toString();
    }

    public PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerModel getWaitingPlayer() {
        return waitingPlayer;
    }

    public void setWaitingPlayer(PlayerModel waitingPlayer) {
        this.waitingPlayer = waitingPlayer;
    }

    public PlayerModel getOtherPlayer(PlayerModel playerModel){
        PlayerModel otherPlayer;
        if (playerModel == currentPlayer){
            otherPlayer = waitingPlayer;
        }else{
            otherPlayer = currentPlayer;
        }
        return otherPlayer;
    }

    // todo: remove asap, simply to aid in refactoring
    public int getIndexOfPlayer(PlayerModel playerModel) {
        if (playerModel == currentPlayer){
            return 0;
        }else{
            return 1;
        }
    }

    // todo: remove asap, simply to aid in refactoring
    public PlayerModel getPlayerByIndex(int index) {
        if (index == 0){
            return currentPlayer;
        }else{
            return waitingPlayer;
        }
    }

}