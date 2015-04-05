package com.hearthsim.card.minion;

public class MinionMock extends Minion {
    private String name;
    private byte baseAttack;
    private byte baseHealth;
    private byte baseManaCost;

    public MinionMock() {
        this("", (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0);
    }

    public MinionMock(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
        super();
        this.name = name;
        this.baseManaCost = mana;
        this.attack_ = attack;
        this.health_ = health;
        this.baseAttack = baseAttack;
        this.baseHealth = baseHealth;
        this.maxHealth_ = maxHealth;
    }

    @Override
    public byte getBaseAttack() {
        return this.baseAttack;
    }

    @Override
    public byte getBaseHealth() {
        return this.baseHealth;
    }

    @Override
    public byte getBaseManaCost() {
        return this.baseManaCost;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MinionMock deepCopy() {
        MinionMock card = (MinionMock)super.deepCopy();
        card.name = this.name;
        card.baseAttack = this.baseAttack;
        card.baseHealth = this.baseHealth;
        card.baseManaCost = this.baseManaCost;
        return card;
    }
}
