package com.hearthsim.gui;

import com.hearthsim.HearthSimGUI;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.event.HSSimulationEventListener;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HSSimulation {

    private ArtificialPlayer ai0_;
    private ArtificialPlayer ai1_;

    private Hero hero0_;
    private Hero hero1_;

    private Deck deck0_;
    private Deck deck1_;

    private SimulationConfig config_;

    private HearthSimGUI simulation_;

    private HSMainFrameModel model_;

    private List<HSSimulationEventListener> listeners_;

    public class SimulationConfig {
        public int numSimulations_;
        public int numThreads_;
        public String simName_;
    }

    public HSSimulation(HSMainFrameModel model) {
        config_ = new SimulationConfig();
        model_ = model;
        listeners_ = new ArrayList<>();
        hero0_ = new TestHero();
        hero1_ = new TestHero();
    }

    public void run() {
        try {
            this.fireStartEvent();
            simulation_ = new HearthSimGUI(config_.numSimulations_,
                    config_.numThreads_, hero0_, deck0_, ai0_, hero1_, deck1_,
                    ai1_);
            simulation_.addObserver(model_);
            simulation_.run();
            this.fireFinishEvent();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            this.fireFinishEvent();
        }
    }

    public ArtificialPlayer getAI_p0() {
        return ai0_;
    }

    public ArtificialPlayer getAI_p1() {
        return ai1_;
    }

    public void setAI_p0(ArtificialPlayer ai) {
        ai0_ = ai;
    }

    public void setAI_p1(ArtificialPlayer ai) {
        ai1_ = ai;
    }

    public Hero getHero_p0() {
        return hero0_;
    }

    public void setHero_p0(Hero hero0_) {
        this.hero0_ = hero0_;
    }

    public Hero getHero_p1() {
        return hero1_;
    }

    public void setHero_p1(Hero hero1_) {
        this.hero1_ = hero1_;
    }

    public Deck getDeck_p0() {
        return deck0_;
    }

    public void setDeck_p0(Deck deck0_) {
        this.deck0_ = deck0_;
    }

    public Deck getDeck_p1() {
        return deck1_;
    }

    public void setDeck_p1(Deck deck1_) {
        this.deck1_ = deck1_;
    }

    public SimulationConfig getConfig() {
        return config_;
    }

    public void setConfig(SimulationConfig config_) {
        this.config_ = config_;
    }

    public void addSimulationEventListener(HSSimulationEventListener listener) {
        listeners_.add(listener);
    }

    private void fireStartEvent() {
        for (HSSimulationEventListener listener : listeners_) {
            listener.simulationStarted();
        }
    }

    private void fireFinishEvent() {
        for (HSSimulationEventListener listener : listeners_) {
            listener.simulationFinished();
        }
    }

    public void stop() {
        simulation_.stop();
    }

}
