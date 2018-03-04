package heroes;

import main.Map;

public class Pyromancer extends Hero {
    private Ability fireblast;
    private Ability ignite;
    /* Valorile pentru trasaturile standard*/
    private static final float LAND_MODIFIER = 1.25f;
    private static final int INITIAL_HEALTH = 500;
    private static final int HEALTH_BONUS = 50;
    private static final int ZERO_DAMAGE = 0;
    /* Valorile pentru Abilitatea 1*/
    private static final int FIREBLAST_DAMAGE = 350;
    private static final int FIREBLAST_BONUS = 50;
    /* Valorile pentru Abilitatea 2*/
    private static final int IGNITE_DAMAGE = 150;
    private static final int IGNITE_OVERTIME_DAMAGE = 50;
    private static final int INITIAL_ROUNDS_LEFT = 2;
    private static final int IGNITE_BONUS = 20;
    private static final int IGNITE_OVERTIME_BONUS = 30;
    /* Modificatorii abilitatilor in functie de oponent*/
    private static final float ROGUE_MODIFIER = 0.8f;
    private static final float KNIGHT_MODIFIER = 1.20f;
    private static final float PYROMANCER_MODIFIER = 0.9f;
    private static final float WIZARD_MODIFIER = 1.05f;

    public Pyromancer() {
        this.setRace('P');
        this.fireblast = new Ability();
        this.ignite = new Ability();

        this.hitPoints = INITIAL_HEALTH;
        this.experiencePoints = 0;
        this.level = 0;
        this.debuff = false;
        this.isDead = false;
        this.selfDamageOvertime = 0;
        this.selfDamageOverTimeRoundsLeft = 0;

        this.maxHealth = INITIAL_HEALTH;

        this.fireblast.baseDamage = FIREBLAST_DAMAGE;
        this.fireblast.actualBaseDamage = FIREBLAST_DAMAGE;

        this.fireblast.damageOverTime = ZERO_DAMAGE;
        this.fireblast.actualDamageOverTime = ZERO_DAMAGE;

        this.fireblast.roundsLeft = ZERO_DAMAGE;
        this.fireblast.actualRoundsLeft = ZERO_DAMAGE;

        this.ignite.roundsLeft = INITIAL_ROUNDS_LEFT;
        this.ignite.actualRoundsLeft = INITIAL_ROUNDS_LEFT;

        this.ignite.baseDamage = IGNITE_DAMAGE;
        this.ignite.actualBaseDamage = IGNITE_DAMAGE;

        this.ignite.damageOverTime = IGNITE_OVERTIME_DAMAGE;
        this.ignite.actualDamageOverTime = IGNITE_OVERTIME_DAMAGE;
    }

    @Override
    public final void levelUp() {
        this.maxHealth += HEALTH_BONUS;
        this.hitPoints = this.maxHealth;
        this.fireblast.actualBaseDamage += FIREBLAST_BONUS;
        this.ignite.actualBaseDamage += IGNITE_BONUS;
        this.ignite.actualDamageOverTime += IGNITE_OVERTIME_BONUS;
    }

    @Override
    public final void prepareFight(final Map.LandType land, final Hero oponent) {
        if (land.equals(Map.LandType.VOLCANIC)) {
            this.applyOpponentModifier(LAND_MODIFIER, LAND_MODIFIER);
        }
        this.totalDamageWithoutRaceModifier = this.calculateDamage(oponent);
        oponent.acceptModifier(this);
    }

    @Override
    public final void acceptModifier(final Hero enemy) {
        enemy.setOpponentModifier(this);
    }

    @Override
    public final void applyOpponentModifier(final float fireblastModifier,
                                            final float igniteModifier) {
        this.fireblast.baseDamage = Math.round(fireblastModifier * this.fireblast.baseDamage);
        this.ignite.baseDamage = Math.round(igniteModifier * this.ignite.baseDamage);
        this.ignite.damageOverTime = Math.round(igniteModifier * this.ignite.damageOverTime);
    }

    @Override
    public final void setOpponentModifier(final Rogue enemy) {
        this.applyOpponentModifier(ROGUE_MODIFIER, ROGUE_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Pyromancer enemy) {
        this.applyOpponentModifier(PYROMANCER_MODIFIER, PYROMANCER_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Knight enemy) {
        this.applyOpponentModifier(KNIGHT_MODIFIER, KNIGHT_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Wizard enemy) {
        this.applyOpponentModifier(WIZARD_MODIFIER, WIZARD_MODIFIER);
    }

    @Override
    public final float calculateDamage(final Hero enemy) {
        return Math.round(this.fireblast.baseDamage + this.ignite.baseDamage);
    }

    @Override
    public final void attack(final Hero enemy) {
        this.totalDamage = Math.round(this.calculateDamage(enemy));
        enemy.hitPoints -= this.totalDamage;
        enemy.debuff = false;
        enemy.selfDamageOvertime = this.ignite.damageOverTime;
        enemy.selfDamageOverTimeRoundsLeft = this.ignite.roundsLeft;
    }

    @Override
    public final void endFight(final Hero enemy, final Map[][] map) {
        this.checkIsDead();
        if (this.isDead) {
            return;
        }
        this.getExperience(enemy);
        this.checkLevelUp();
        this.fireblast.baseDamage = this.fireblast.actualBaseDamage;
        this.ignite.baseDamage = this.ignite.actualBaseDamage;
        this.ignite.damageOverTime = this.ignite.actualDamageOverTime;
    }

}
