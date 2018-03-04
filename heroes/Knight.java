package heroes;

import main.Map;

public class Knight extends Hero {
    private Ability execute;
    private Ability slam;
    private boolean isLand;
    /* Valorile pentru trasaturile standard*/
    private static final int INITIAL_HEALTH = 900;
    private static final int HEALTH_BONUS = 80;
    private static final float LAND_MODIFIER = 1.15f;
    private static final int ZERO_DAMAGE = 0;
    /* Valorile pentru Abilitatea 1*/
    private static final int EXECUTE_DAMAGE = 200;
    private static final int EXECUTE_BONUS = 30;
    private static final float EXECUTE_HP_LIMIT = 0.2f;
    private static final float EXECUTE_HP_LIMIT_BONUS = 0.01f;
    private static final float EXECUTE_HP_LIMIT_MAX = 0.4f;
    /* Valorile pentru Abilitatea 2*/
    private static final int SLAM_ROUNDS_LEFT = 1;
    private static final int SLAM_DAMAGE = 100;
    private static final int SLAM_BONUS = 40;
    /* Modificatorii primei abilitati in functie de oponent*/
    private static final float EXECUTE_ROGUE_MODIFIER = 1.15f;
    private static final float EXECUTE_KNIGHT_MODIFIER = 1f;
    private static final float EXECUTE_PYROMANCER_MODIFIER = 1.1f;
    private static final float EXECUTE_WIZARD_MODIFIER = 0.8f;
    /* Modificatorii abilitatii numarul 2 in functie de oponent*/
    private static final float SLAM_ROGUE_MODIFIER = 0.8f;
    private static final float SLAM_KNIGHT_MODIFIER = 1.2f;
    private static final float SLAM_PYROMANCER_MODIFIER = 0.9f;
    private static final float SLAM_WIZARD_MODIFIER = 1.05f;

    public Knight() {
        this.setRace('K');
        this.isLand = false;
        this.execute = new Ability();
        this.slam = new Ability();
        this.maxHealth = INITIAL_HEALTH;

        this.hitPoints = INITIAL_HEALTH;
        this.experiencePoints = 0;
        this.level = 0;
        this.debuff = false;
        this.isDead = false;
        this.selfDamageOvertime = 0;
        this.selfDamageOverTimeRoundsLeft = 0;

        this.execute.baseDamage = EXECUTE_DAMAGE;
        this.execute.actualBaseDamage = EXECUTE_DAMAGE;

        this.execute.damageOverTime = EXECUTE_HP_LIMIT;
        this.execute.actualDamageOverTime = EXECUTE_HP_LIMIT;

        this.execute.roundsLeft = EXECUTE_HP_LIMIT_MAX;
        this.execute.actualRoundsLeft = EXECUTE_HP_LIMIT_MAX;

        this.slam.roundsLeft = SLAM_ROUNDS_LEFT;
        this.slam.actualRoundsLeft = SLAM_ROUNDS_LEFT;

        this.slam.baseDamage = SLAM_DAMAGE;
        this.slam.actualBaseDamage = SLAM_DAMAGE;

        this.slam.damageOverTime = ZERO_DAMAGE;
        this.slam.actualDamageOverTime = ZERO_DAMAGE;
    }

    @Override
    public final void levelUp() {
        this.maxHealth += HEALTH_BONUS;
        this.hitPoints = this.maxHealth;
        this.execute.baseDamage += EXECUTE_BONUS;
        if (this.execute.damageOverTime < EXECUTE_HP_LIMIT_MAX) {
            this.execute.damageOverTime += EXECUTE_HP_LIMIT_BONUS;
        }
        this.slam.baseDamage += SLAM_BONUS;
    }

    @Override
    public final void prepareFight(final Map.LandType land, final Hero oponent) {
        if (land.equals(Map.LandType.LAND)) {
            this.isLand = true;
            this.applyOpponentModifier(LAND_MODIFIER, LAND_MODIFIER);
        }
        this.totalDamageWithoutRaceModifier = Math.round(calculateDamage(oponent));
        oponent.acceptModifier(this);
    }

    @Override
    public final void acceptModifier(final Hero enemy) {
        enemy.setOpponentModifier(this);
    }

    @Override
    public final void applyOpponentModifier(final float executeModifier, final float slamModifier) {
        this.execute.baseDamage = executeModifier * this.execute.baseDamage;
        this.slam.baseDamage = slamModifier * this.slam.baseDamage;
    }

    @Override
    public final void setOpponentModifier(final Rogue enemy) {
        this.applyOpponentModifier(EXECUTE_ROGUE_MODIFIER, SLAM_ROGUE_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Pyromancer enemy) {
        this.applyOpponentModifier(EXECUTE_PYROMANCER_MODIFIER, SLAM_PYROMANCER_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Knight enemy) {
        this.applyOpponentModifier(EXECUTE_KNIGHT_MODIFIER, SLAM_KNIGHT_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Wizard enemy) {
        this.applyOpponentModifier(EXECUTE_WIZARD_MODIFIER, SLAM_WIZARD_MODIFIER);
    }

    @Override
    public final float calculateDamage(final Hero enemy) {
        float limit = Math.round(this.execute.damageOverTime * enemy.maxHealth);
        if (enemy.hitPoints <= limit) {
            if (isLand) {
                return Math.round(enemy.hitPoints * LAND_MODIFIER + this.slam.baseDamage);
            } else {
                return Math.round(enemy.hitPoints + this.slam.baseDamage);
            }
        } else {
            return Math.round(this.execute.baseDamage + this.slam.baseDamage);
        }
    }

    @Override
    public final void attack(final Hero enemy) {
        this.totalDamage = this.calculateDamage(enemy);
        enemy.hitPoints -= this.totalDamage;
        enemy.debuff = true;
        enemy.selfDamageOvertime = this.slam.damageOverTime;
        enemy.selfDamageOverTimeRoundsLeft = this.slam.roundsLeft;
    }

    @Override
    public final void endFight(final Hero enemy, final Map[][] map) {
        this.checkIsDead();
        if (this.isDead) {
            return;
        }
        this.getExperience(enemy);
        this.checkLevelUp();
        this.isLand = false;
        this.execute.baseDamage = this.execute.actualBaseDamage;
        this.slam.baseDamage = this.slam.actualBaseDamage;
    }

}
