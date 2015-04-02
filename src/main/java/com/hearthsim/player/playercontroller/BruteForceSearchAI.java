package com.hearthsim.player.playercontroller;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.ParamFile;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.CardFactory;
import com.hearthsim.util.HearthActionBoardPair;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.factory.SparseBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.StopNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BruteForceSearchAI implements ArtificialPlayer {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private final static int MAX_THINK_TIME = 20000;

    private boolean useSparseBoardStateFactory_ = true;
    private boolean useDuplicateNodePruning = true;

    public WeightedScorer scorer = new WeightedScorer();

    protected BruteForceSearchAI() {
    }

    // TODO: come up with more meaningful names for these different AI 'styles'
    public static BruteForceSearchAI buildStandardAI2() {
        BruteForceSearchAI artificialPlayer = buildStandardAI1();
        artificialPlayer.scorer.setTauntWeight(0);
        artificialPlayer.scorer.setSpellDamageAddWeight(0.9);
        artificialPlayer.scorer.setSpellDamageMultiplierWeight(1);
        artificialPlayer.scorer.setMyDivineShieldWeight(1);
        artificialPlayer.scorer.setEnemyDivineShieldWeight(1);

        return artificialPlayer;
    }

    public static BruteForceSearchAI buildStandardAI1() {
        BruteForceSearchAI artificialPlayer = new BruteForceSearchAI();

        artificialPlayer.scorer.setMyAttackWeight(0.9);
        artificialPlayer.scorer.setMyHealthWeight(0.9);
        artificialPlayer.scorer.setEnemyAttackWeight(1.0);
        artificialPlayer.scorer.setEnemyHealthWeight(1.0);
        artificialPlayer.scorer.setTauntWeight(1.0);
        artificialPlayer.scorer.setMyHeroHealthWeight(0.1);
        artificialPlayer.scorer.setEnemyHeroHealthWeight(0.1);
        artificialPlayer.scorer.setManaWeight(0.1);
        artificialPlayer.scorer.setMyNumMinionsWeight(0.5);
        artificialPlayer.scorer.setEnemyNumMinionsWeight(0.5);
        artificialPlayer.scorer.setSpellDamageAddWeight(0.0);
        artificialPlayer.scorer.setSpellDamageMultiplierWeight(0.5);
        artificialPlayer.scorer.setMyDivineShieldWeight(0.0);
        artificialPlayer.scorer.setEnemyDivineShieldWeight(0.0);

        artificialPlayer.scorer.setMyWeaponWeight(0.5);
        artificialPlayer.scorer.setEnemyWeaponWeight(0.5);

        return artificialPlayer;
    }

    /**
     * Constructor
     * This is the preferred (non-deprecated) way to instantiate this class
     *
     * @param aiParamFile The path to the input parameter file
     * @throws IOException
     * @throws HSInvalidParamFileException
     */
    public BruteForceSearchAI(Path aiParamFile) throws IOException, HSInvalidParamFileException {
        ParamFile pFile = new ParamFile(aiParamFile);
        try {
            this.scorer.setMyAttackWeight(pFile.getDouble("w_a"));
            this.scorer.setMyHealthWeight(pFile.getDouble("w_h"));
            this.scorer.setEnemyAttackWeight(pFile.getDouble("wt_a"));
            this.scorer.setEnemyHealthWeight(pFile.getDouble("wt_h"));
            this.scorer.setTauntWeight(pFile.getDouble("w_taunt"));
            this.scorer.setMyHeroHealthWeight(pFile.getDouble("w_health"));
            this.scorer.setEnemyHeroHealthWeight(pFile.getDouble("wt_health"));

            this.scorer.setMyNumMinionsWeight(pFile.getDouble("w_num_minions"));
            this.scorer.setEnemyNumMinionsWeight(pFile.getDouble("wt_num_minions"));

            // The following two have default values for now...
            // These are rather arcane parameters, so please understand
            // them before attempting to change them.
            this.scorer.setSpellDamageMultiplierWeight(pFile.getDouble("w_sd_mult", 1.0));
            this.scorer.setSpellDamageAddWeight(pFile.getDouble("w_sd_add", 0.9));

            // Divine Shield defualts to 0 for now
            this.scorer.setMyDivineShieldWeight(pFile.getDouble("w_divine_shield", 0.0));
            this.scorer.setEnemyDivineShieldWeight(pFile.getDouble("wt_divine_shield", 0.0));

            // weapon score for the hero
            this.scorer.setMyWeaponWeight(pFile.getDouble("w_weapon", 0.5));
            this.scorer.setEnemyWeaponWeight(pFile.getDouble("wt_weapon", 0.5));

            // charge model score
            this.scorer.setMyChargeWeight(pFile.getDouble("w_charge", 0.0));

            this.scorer.setManaWeight(pFile.getDouble("w_mana", 0.1));

            useSparseBoardStateFactory_ = pFile.getBoolean("use_sparse_board_state_factory", true);
            useDuplicateNodePruning = pFile.getBoolean("use_duplicate_node_pruning", true);

            // Look for a pattern: card_in_hand_value_*
            Set<String> keys = pFile.getKeysContaining("card_in_hand_score_");
            for (String key : keys) {
                String cardName = key.substring(19);
                try {
                    this.scorer.putCardInHandExtraScore(CardFactory.getCard(cardName).getClass(), pFile.getDouble(key));
                } catch (HSInvalidCardException e) {
                    throw new HSInvalidParamFileException("Invalid key: " + key);
                }
            }

            // Look for a pattern: minion_on_board_value_*
            keys = pFile.getKeysContaining("minion_on_board_value_");
            for (String key : keys) {
                String minionName = key.substring(22);
                try {
                    Minion minion = (Minion) CardFactory.getCard(minionName);
                    this.scorer.putMinionOnBoardExtraScore(minion.getClass(), pFile.getDouble(key));
                } catch (HSInvalidCardException e) {
                    throw new HSInvalidParamFileException("Invalid key: " + key);
                }
            }

        } catch(HSParamNotFoundException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    public boolean getUseSparseBoardStateFactory() {
        return useSparseBoardStateFactory_;
    }

    public void setUseSparseBoardStateFactory(boolean value) {
        useSparseBoardStateFactory_ = value;
    }

    public boolean getUseDuplicateNodePruning() {
        return useDuplicateNodePruning;
    }

    public void setUseDuplicateNodePruning(boolean value) {
        useDuplicateNodePruning = value;
    }

    @Override
    public List<HearthActionBoardPair> playTurn(int turn, BoardModel board) throws HSException {
        PlayerModel playerModel0 = board.getCurrentPlayer();
        PlayerModel playerModel1 = board.getWaitingPlayer();

        BoardStateFactoryBase factory;
        if (useSparseBoardStateFactory_) {
            factory = new SparseBoardStateFactory(playerModel0.getDeck(), playerModel1.getDeck(), MAX_THINK_TIME, useDuplicateNodePruning);
        } else {
            factory = new DepthBoardStateFactory(playerModel0.getDeck(), playerModel1.getDeck(), MAX_THINK_TIME, useDuplicateNodePruning);
        }
        return this.playTurn(turn, board, factory);
    }

    @Override
    public List<HearthActionBoardPair> playTurn(int turn, BoardModel board, BoardStateFactoryBase factory)
            throws HSException {
        PlayerModel playerModel0 = board.getCurrentPlayer();

        log.debug("playing turn for " + playerModel0.getName());
        // The goal of this ai is to maximize his board score
        log.debug("start turn board state is {}", board);
        HearthTreeNode toRet = new HearthTreeNode(board);

        HearthTreeNode allMoves = factory.doMoves(toRet, this.scorer);
        ArrayList<HearthActionBoardPair> retList = new ArrayList<>();
        HearthTreeNode curMove = allMoves;

        while(curMove.getChildren() != null) {
            curMove = curMove.getChildren().get(0);
            if (curMove instanceof StopNode) {
                // Add the initial step that created the StopNode
                retList.add(new HearthActionBoardPair(curMove.getAction(), curMove.data_.deepCopy()));
                // Force the step to resolve
                HearthTreeNode allEffectsDone = ((StopNode)curMove).finishAllEffects();
                // Add the resolution to action list
                retList.add(new HearthActionBoardPair(allEffectsDone.getAction(), allEffectsDone.data_.deepCopy()));

                // Continue the turn
                List<HearthActionBoardPair> nextMoves = this.playTurn(turn, allEffectsDone.data_);
                if (nextMoves.size() > 0) {
                    for ( HearthActionBoardPair actionBoard : nextMoves) {
                        retList.add(actionBoard);
                    }
                }
                break;
            } else {
                retList.add(new HearthActionBoardPair(curMove.getAction(), curMove.data_));
            }
        }
        return retList;
    }

    @Override
    public ArtificialPlayer deepCopy() {
        BruteForceSearchAI copied = new BruteForceSearchAI();
        copied.scorer = this.scorer.deepCopy();
        copied.useSparseBoardStateFactory_ = useSparseBoardStateFactory_;
        copied.useDuplicateNodePruning = useDuplicateNodePruning;
        return copied;
    }

    @Override
    public int getMaxThinkTime() {
        return MAX_THINK_TIME;
    }
}
