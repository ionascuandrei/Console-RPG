package heroes;

import main.Map;

public class Rogue extends Hero {
    private Ability backstab;
    private Ability paralysis;
    private boolean isWoods;
    /* Valorile pentru trasaturile standard*/
    private static final float LAND_MODIFIER = 1.15f;
    private static final int ZERO_DAMAGE = 0;
    private static final int CRITICAL_ROUND = 3;
    /* Valorile pentru Abilitatea 1*/
    private static final int INITIAL_HEALTH = 600;
    private static final int HEALTH_BONUS = 40;
    private static final int BACKSTAB_DAMAGE = 200;
    private static final int BACKSTAB_BONUS = 20;
    private static final float BACKSTAB_CRITICAL = 1.5f;
    private static final int CRITICAL_ROUNDS_LEFT = 0;
    /* Valorile pentru Abilitatea 2*/
    private static final int PARALYSIS_DAMAGE = 40;
    private static final int PARALYSIS_BONUS = 10;
    private static final int PARALYSIS_ROUNDS = 3;
    private static final int PARALYSIS_BONUS_ROUDS = 3;
    /* Modificatorii primei abilitati in functie de oponent*/
    private static final float BACKSTAB_ROGUE_MODIFIER = 1.20f;
    private static final float BACKSTAB_KNIGHT_MODIFIER = 0.9f;
    private static final float BACKSTAB_PYROMANCER_MODIFIER = 1.25f;
    private static final float BACKSTAB_WIZARD_MODIFIER = 1.25f;
    /* Modificatorii abilitatii numarul 2 in functie de oponent*/
    private static final float PARALYSIS_ROGUE_MODIFIER = 0.9f;
    private static final float PARALYSIS_KNIGHT_MODIFIER = 0.8f;
    private static final float PARALYSIS_PYROMANCER_MODIFIER = 1.20f;
    private static final float PARALYSIS_WIZARD_MODIFIER = 1.25f;

    public Rogue() {
        this.setRace('R');
        this.backstab = new Ability();
        this.paralysis = new Ability();
        this.maxHealth = INITIAL_HEALTH;
        this.isWoods = false;

        this.hitPoints = INITIAL_HEALTH;
        this.experiencePoints = 0;
        this.level = 0;
        this.debuff = false;
        this.isDead = false;
        this.selfDamageOvertime = 0;
        this.selfDamageOverTimeRoundsLeft = 0;

        this.backstab.baseDamage = BACKSTAB_DAMAGE;
        this.backstab.actualBaseDamage = BACKSTAB_DAMAGE;

        this.backstab.damageOverTime = ZERO_DAMAGE;
        this.backstab.actualDamageOverTime = ZERO_DAMAGE;

        this.backstab.roundsLeft = CRITICAL_ROUNDS_LEFT;
        this.backstab.actualRoundsLeft = CRITICAL_ROUNDS_LEFT;

        this.paralysis.roundsLeft = PARALYSIS_ROUNDS;
        this.paralysis.actualRoundsLeft = PARALYSIS_ROUNDS;

        this.paralysis.baseDamage = PARALYSIS_DAMAGE;
        this.paralysis.actualBaseDamage = PARALYSIS_DAMAGE;

        this.paralysis.damageOverTime = PARALYSIS_DAMAGE;
        this.paralysis.actualDamageOverTime = PARALYSIS_DAMAGE;
    }

    @Override
    public final void levelUp() {
        this.maxHealth += HEALTH_BONUS;
        this.hitPoints = this.maxHealth;
        this.backstab.baseDamage += BACKSTAB_BONUS;
        this.paralysis.baseDamage += PARALYSIS_BONUS;
    }

    @Override
    public final void prepareFight(final Map.LandType land, final Hero oponent) {
        this.isWoods = false;
        if (land.equals(Map.LandType.WOODS)) {
            this.applyOpponentModifier(LAND_MODIFIER, LAND_MODIFIER);
            this.isWoods = true;
        }
        if (this.backstab.roundsLeft % CRITICAL_ROUND == 0 && this.isWoods) {
            this.backstab.baseDamage = Math.round(BACKSTAB_CRITICAL * this.backstab.baseDamage);
        }
        this.backstab.roundsLeft++;
        this.totalDamageWithoutRaceModifier = Math.round(this.calculateDamage(oponent));
        oponent.acceptModifier(this);
    }

    @Override
    public final void acceptModifier(final Hero enemy) {
        enemy.setOpponentModifier(this);
    }

    @Override
    public final void applyOpponentModifier(final float backstabModifier,
                                            final float paralysisModifier) {
        this.paralysis.baseDamage = Math.round(paralysisModifier * this.paralysis.baseDamage);
        this.paralysis.damageOverTime = Math.round(paralysisModifier
                * this.paralysis.damageOverTime);
        this.backstab.baseDamage = Math.round(backstabModifier * this.backstab.baseDamage);

        if (backstabModifier == LAND_MODIFIER) {
            this.isWoods = true;
            this.paralysis.roundsLeft += PARALYSIS_BONUS_ROUDS;
        }
    }

    @Override
    public final void setOpponentModifier(final Rogue enemy) {
        this.applyOpponentModifier(BACKSTAB_ROGUE_MODIFIER, PARALYSIS_ROGUE_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Pyromancer enemy) {
        this.applyOpponentModifier(BACKSTAB_PYROMANCER_MODIFIER, PARALYSIS_PYROMANCER_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Knight enemy) {
        this.applyOpponentModifier(BACKSTAB_KNIGHT_MODIFIER, PARALYSIS_KNIGHT_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Wizard enemy) {
        this.applyOpponentModifier(BACKSTAB_WIZARD_MODIFIER, PARALYSIS_WIZARD_MODIFIER);
    }

    @Override
    public final float calculateDamage(final Hero enemy) {
        return Math.round(this.backstab.baseDamage + this.paralysis.baseDamage);
    }

    @Override
    public final void attack(final Hero enemy) {
        this.totalDamage = this.calculateDamage(enemy);
        enemy.hitPoints -= this.totalDamage;
        enemy.debuff = true;
        enemy.selfDamageOvertime = this.paralysis.damageOverTime;
        enemy.selfDamageOverTimeRoundsLeft = this.paralysis.roundsLeft;
    }

    @Override
    public final void endFight(final Hero enemy, final Map[][] map) {
        this.checkIsDead();
        if (this.isDead) {
            return;
        }
        this.getExperience(enemy);
        this.checkLevelUp();
        this.backstab.baseDamage = this.backstab.actualBaseDamage;
        this.paralysis.baseDamage = this.paralysis.actualBaseDamage;
        this.paralysis.damageOverTime = this.paralysis.actualDamageOverTime;
        this.paralysis.roundsLeft = this.paralysis.actualRoundsLeft;
    }

}
