package com.hearthsim.card

import com.hearthsim.json.registry.CardRegistry
import groovy.json.JsonSlurper

class ImplementedCardList {

    public ArrayList<ImplementedCard> list_;
    public Map<Class<?>, ImplementedCard> map_;

    public class ImplementedCard implements Comparable<ImplementedCard> {

        public Class<?> cardClass_;
        public String name_;
        public String type_;
        public String charClass_;
        public String rarity_;
        public String text_;
        public int mana_;
        public int attack_;
        public int health_;

        @Override
        public int compareTo(ImplementedCard o) {
            int result = Integer.compare(this.mana_, o.mana_);
            if (result == 0) {
                result = this.name_.compareTo(o.name_);
            }
            return result;
        }

    }

    ImplementedCardList() {
        list_ = new ArrayList<ImplementedCard>();
        map_ = new HashMap<Class<?>, ImplementedCard>();

        def slurper = new JsonSlurper()
        def implementedCardsFromJson = slurper.parse(getClass().getResourceAsStream('/implemented_cards.json'))

        def registry = CardRegistry.instance
        implementedCardsFromJson.each { implementedCardFromJson ->
            def cardDefinition = registry.cardByName(implementedCardFromJson.name)

            def className = implementedCardFromJson['class']
            def implementedCard = new ImplementedCard(
                    cardClass_: className,
                    name_: cardDefinition.name,
                    type_: cardDefinition.type.toLowerCase(),
                    charClass_: cardDefinition.playerClass.toLowerCase(),
                    rarity_: cardDefinition.rarity.toLowerCase(),
                    mana_: cardDefinition.cost,
                    attack_: cardDefinition.attack ?: -1,
                    health_: cardDefinition.health ?: -1
            )
            list_ << implementedCard

            def clazz = Class.forName(className)
            map_[clazz] = implementedCard
        }

    }

    public ArrayList<ImplementedCard> getCardList() {
        return list_;
    }

    public ImplementedCard getCardForClass(Class<?> clazz) {
        def card = map_.get(clazz)
        if (!card)
            throw new RuntimeException("unable to find card for class [$clazz]")
        return card;
    }
}
