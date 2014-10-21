package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.minion.Hero;

public class TestHero extends Hero {
    public TestHero() {
        this("NoHero", (byte) 30);
    }

    public TestHero(String name, byte health) {
        this.name_ = name;
        this.health_ = health;
        this.baseHealth_ = health_;
        this.maxHealth_ = health_;
        this.heroTargetable_ = true;
    }
}
