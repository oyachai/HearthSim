package com.hearthsim.card

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hearthsim.card.minion.Hero
import com.hearthsim.card.minion.Minion
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.json.registry.ReferenceCardRegistry

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class ImplementedCardList {

    private static ImplementedCardList instance
	
	static class TypeParser {
		private static String TYPE_CLASSNAME_PATTERN_SPELL = "spellcard.concrete"
		private static String TYPE_CLASSNAME_PATTERN_MINION = "minion.concrete"
		private static String TYPE_CLASSNAME_PATTERN_WEAPON = "weapon.concrete"
		private static String TYPE_CLASSNAME_PATTERN_HERO = "minion.heroes"
    
		public static String parse(String classPath) {
			if (classPath.contains(TYPE_CLASSNAME_PATTERN_HERO))
				return "Hero"
			else if (classPath.contains(TYPE_CLASSNAME_PATTERN_MINION))
				return "Minion"
			else if (classPath.contains(TYPE_CLASSNAME_PATTERN_WEAPON))
				return "Weapon"
			else if (classPath.contains(TYPE_CLASSNAME_PATTERN_SPELL))
				return "Spell"
			return "Unknown"
		}	
	}
	
	private static String MECHANICS_TAUNT = "Taunt";
	private static String MECHANICS_SHIELD = "Divine Shield";
	private static String MECHANICS_WINDFURY = "Windfury";
	private static String MECHANICS_CHARGE = "Charge";
	private static String MECHANICS_STEALTH = "Stealth";

    public ArrayList<ImplementedCard> list_;
    public Map<Class<?>, ImplementedCard> map_;

    //TODO: give singleton constructor, then use that in Card constructor

    public synchronized static ImplementedCardList getInstance() {
        if (!instance) {
            instance = new ImplementedCardList()
        }
        return instance
    }


    public class ImplementedCard implements Comparable<ImplementedCard> {

        public Class<?> cardClass_;
        public String name_;
        public String type_;
        public String charClass_;
        public String rarity_;
        public String text_;
		public boolean isHero;
		public boolean collectible;
        public boolean taunt_;
        public boolean divineShield_;
        public boolean windfury_;
        public boolean charge_;
        public boolean stealth_;
        public int mana_;
        public int attack_;
        public int health_;
        public int durability;

        @Override
        public int compareTo(ImplementedCard o) {
            int result = Integer.compare(this.mana_, o.mana_);
            if (result == 0) {
                result = this.name_.compareTo(o.name_);
            }
            return result;
        }

		public Card createCardInstance() {
			Constructor<?> ctor;
			ctor = this.cardClass_.getConstructor();
			Card card = (Card)ctor.newInstance();
			return card;
		}
    }

    ImplementedCardList() {
        list_ = new ArrayList<ImplementedCard>();
        map_ = new HashMap<Class<?>, ImplementedCard>();

        def slurper = new JsonSlurper()
        def implementedCardsFromJson = slurper.parse(getClass().getResourceAsStream('/implemented_cards.json'))

        def registry = ReferenceCardRegistry.instance
        implementedCardsFromJson.each { implementedCardFromJson ->
            def cardDefinition = registry.cardByNameAndType(implementedCardFromJson.name, TypeParser.parse(implementedCardFromJson.class))

            def className = implementedCardFromJson['class']
            def clazz = Class.forName(className)
            def implementedCard = new ImplementedCard(
                    cardClass_: className,
                    name_: cardDefinition.name,
                    type_: cardDefinition.type.toLowerCase(),
                    charClass_: cardDefinition.playerClass.toLowerCase(),
                    rarity_: cardDefinition.rarity?.toLowerCase(),
                    mana_: cardDefinition.cost?: 0,
                    attack_: (cardDefinition.attack == null) ? -1 : cardDefinition.attack, //return -1 if it's 0. only if null.
                    health_: (cardDefinition.health == null) ? -1 : cardDefinition.health,
                    durability: (cardDefinition.durability == null) ? -1 : cardDefinition.durability,
                    taunt_: cardDefinition.mechanics?.contains(ImplementedCardList.MECHANICS_TAUNT) ?: false,
                    divineShield_: cardDefinition.mechanics?.contains(ImplementedCardList.MECHANICS_SHIELD) ?: false,
                    windfury_: cardDefinition.mechanics?.contains(ImplementedCardList.MECHANICS_WINDFURY) ?: false,
                    charge_: cardDefinition.mechanics?.contains(ImplementedCardList.MECHANICS_CHARGE) ?: false,
                    stealth_: cardDefinition.mechanics?.contains(ImplementedCardList.MECHANICS_STEALTH) ?: false,
					text_: cardDefinition.text?: '',
					isHero: Hero.class.isAssignableFrom(clazz),
					collectible: cardDefinition.collectible?: false,
            )
            list_ << implementedCard

            map_[clazz] = implementedCard
        }
    }

    public ArrayList<ImplementedCard> getCardList() {
        return list_;
    }

    // TODO: existing clients need to filter out collectibles
    public ImplementedCard getCardForClass(Class<?> clazz) {
        def card =map_.get(clazz)
        if (!card) {
            if ([Minion, TestHero].contains(clazz)) {
                return null
            } else {
                throw new RuntimeException("unable to find card for class [$clazz]")
            }
        }

        return card;
    }
}
