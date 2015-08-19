package com.hearthsim.card

import com.hearthsim.card.minion.Hero
import com.hearthsim.card.minion.Minion
import com.hearthsim.card.minion.MinionMock
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.json.registry.ReferenceCardRegistry
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import java.lang.reflect.Constructor

@Slf4j
class ImplementedCardList {

    private static final ImplementedCardList instance
    
    static class TypeParser {
        private static String TYPE_CLASSNAME_PATTERN_SPELL = "spell."
        private static String TYPE_CLASSNAME_PATTERN_MINION = "minion"
        private static String TYPE_CLASSNAME_PATTERN_WEAPON = "weapon."
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
    private static String MECHANICS_CANT_ATTACK = "Can't attack.";

    public ArrayList<ImplementedCard> list_;
    public Map<Class<?>, ImplementedCard> map_;

    //TODO: give singleton constructor, then use that in Card constructor

    public static ImplementedCardList getInstance() {
        return instance
    }
    
    static
    {
        instance = new ImplementedCardList()
    }


    public class ImplementedCard implements Comparable<ImplementedCard> {

        private static final htmlTagPattern = ~/<[a-zA-Z_0-9\/]+?>/
        private static final overloadPattern = ~/Overload:\s+?\((\d+)\)/
        private static final spellEffectPattern = ~/\$(\d+) damage/
        // TODO This regex assumes spell damage is at the beginning of a line because of Ancient Mage
        private static final spellDamagePattern = ~/^Spell Damage\s+\+(\d+)/

        public Class<?> cardClass_;
        public String name_;
        public String type_;
        public String charClass_;
        public String rarity_;
        public String text_;
        public String race;
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
        public int overload;
        public int spellDamage;
        public int spellEffect;
        public boolean cantAttack;

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
		
		@Override
		public boolean equals(Object o)
		{
			try
			{
				ImplementedCard otherCard = (ImplementedCard)o;
				return this.name_.equals(otherCard.name_);
			}
			catch (Exception e)
			{
				return false;
			}
		}
		
		@Override
		public int hashCode()
		{
			return this.name_.hashCode();
		}
    }

    ImplementedCardList() {
        list_ = new ArrayList<ImplementedCard>();
        map_ = new HashMap<Class<?>, ImplementedCard>();

        def slurper = new JsonSlurper()
        def implementedCardsFromJson = slurper.parse(getClass().getResourceAsStream('/implemented_cards.json'))

        def registry = ReferenceCardRegistry.instance
        implementedCardsFromJson.each { implementedCardFromJson ->
            def cardDefinition = registry.cardById(implementedCardFromJson.id)

            def className = implementedCardFromJson['class']
            def clazz = Class.forName(className)

            def cleanedText = cardDefinition.text == null ? '' : ImplementedCard.htmlTagPattern.matcher(cardDefinition.text).replaceAll("")
            def overload = 0
            def spellDamage = 0
            def spellEffect = 0
            if (!cleanedText.equals('')) {
                def matcher = ImplementedCard.overloadPattern.matcher(cleanedText)
                overload = matcher.size() == 1 ? matcher[0][1].toInteger() : 0

                matcher = ImplementedCard.spellDamagePattern.matcher(cleanedText)
                spellDamage = matcher.size() == 1 ? matcher[0][1].toInteger() : 0

                matcher = ImplementedCard.spellEffectPattern.matcher(cleanedText)
                spellEffect = matcher.size() == 1 ? matcher[0][1].toInteger() : 0
            }

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
                    taunt_: cardDefinition.mechanics?.contains(MECHANICS_TAUNT) ?: false,
                    divineShield_: cardDefinition.mechanics?.contains(MECHANICS_SHIELD) ?: false,
                    windfury_: cardDefinition.mechanics?.contains(MECHANICS_WINDFURY) ?: false,
                    charge_: cardDefinition.mechanics?.contains(MECHANICS_CHARGE) ?: false,
                    stealth_: cardDefinition.mechanics?.contains(MECHANICS_STEALTH) ?: false,
                    cantAttack: cardDefinition.text?.contains(MECHANICS_CANT_ATTACK) ?: false,
                    text_: cardDefinition.text?: '',
                    isHero: Hero.class.isAssignableFrom(clazz),
                    collectible: cardDefinition.collectible?: false,
                    overload: overload,
                    race: cardDefinition.race,
                    spellDamage: spellDamage,
                    spellEffect: spellEffect
                    
            )
            list_ << implementedCard

            map_[clazz] = implementedCard
        }
    }

    public ArrayList<ImplementedCard> getCardList() {
        return new ArrayList<ImplementedCard>(list_);
    }

    // TODO: existing clients need to filter out collectibles
    public ImplementedCard getCardForClass(Class<?> clazz) {
        def card =map_.get(clazz)
        if (!card) {
            if ([Card, CardMock, Minion, MinionMock, TestHero].contains(clazz)) {
                return null
            } else {
                throw new RuntimeException("unable to find card for class [$clazz]")
            }
        }

        return card;
    }

    // TODO: could probably be faster
    public ImplementedCard getCardForName(String name) {
        for(card in list_) {
            if(card.name_.compareTo(name) == 0) {
                return card
            };
        }
        return null;
    }
}
