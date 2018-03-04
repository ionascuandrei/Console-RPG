package heroes;

import main.Map;

public class Wizard extends Hero {
    private Ability drain;
    private Ability deflect;
    private boolean againstWizard;
    private float drainDamage;
    private float deflectDamage;
    /* Valorile pentru trasaturile standard*/
    private static final float LAND_MODIFIER = 1.1f;
    private static final int INITIAL_HEALTH = 400;
    private static final int HEALTH_BONUS = 30;
    private static final int ZERO_DAMAGE = 0;
    /* Valorile pentru Abilitatea 1*/
    private static final float DRAIN_PERCENT = 0.2f;
    private static final float DRAIN_BONUS = 0.05f;
    private static final float DRAIN_MODIFIER = 0.3f;
    /* Valorile pentru Abilitatea 2*/
    private static final float DEFLECT_PERCENT = 0.35f;
    private static final float DEFLECT_BONUS = 0.02f;
    private static final float DEFLECT_MAX = 0.7f;
    /* Modificatorii primei abilitati in functie de oponent*/
    private static final float DRAIN_ROGUE_MODIFIER = 0.8f;
    private static final float DRAIN_KNIGHT_MODIFIER = 1.2f;
    private static final float DRAIN_PYROMANCER_MODIFIER = 0.9f;
    private static final float DRAIN_WIZARD_MODIFIER = 1.05f;
    /* Modificatorii abilitatii numarul 2 in functie de oponent*/
    private static final float DEFLECT_ROGUE_MODIFIER = 1.2f;
    private static final float DEFLECT_KNIGHT_MODIFIER = 1.4f;
    private static final float DEFLECT_PYROMANCER_MODIFIER = 1.3f;
    private static final float DEFLECT_WIZARD_MODIFIER = 0;

    public Wizard() {
        this.setRace('W');
        this.maxHealth = INITIAL_HEALTH;
        this.deflect = new Ability();
        this.drain = new Ability();

        this.hitPoints = INITIAL_HEALTH;
        this.experiencePoints = 0;
        this.level = 0;
        this.debuff = false;
        this.isDead = false;
        this.selfDamageOvertime = 0;
        this.selfDamageOverTimeRoundsLeft = 0;
        /** Pentru a folosi o structura comuna pentru baza fiecarei abilitati, am realizat clasa
         * Ability care integreaza parametrii necesari tuturor claselor. Avand in vedere ca aceasta
         * clasa este speciala pe partea abilitatilor, am pastrat aceeasi structura ca si pentru
         * celelalte clase, insa denumirea campurilor nu mai este sugestiva pentru caracteristicile
         * abilitatilor eroului Wizard. Astfel, am realizat aceasta conventie pentru a intelege
         * intrebuintarea fiecarui camp:
         *
         * Drain.baseDamage/actualBaseDamage = procentul ce trebuie aplicat asupra vietii
         *                                     adversarului
         * Drain.damageOverTime/actualDamageOvertime/roundsLeft = ZERO
         *
         * Deflect.baseDamage/actualBaseDamage = procentul ce trebuie aplicat asupra damage-ului
         *                                       inamicului
         * Deflect.damageOverTime/actualDamageOvertime = ZERO
         * Drain.roundsLeft = Procentul maxim pe care il poate atinge aceasta abilitate
         */
        this.drain.baseDamage = DRAIN_PERCENT;
        this.drain.actualBaseDamage = DRAIN_PERCENT;

        this.drain.damageOverTime = ZERO_DAMAGE;
        this.drain.actualDamageOverTime = ZERO_DAMAGE;

        this.drain.roundsLeft = ZERO_DAMAGE;
        this.drain.actualRoundsLeft = ZERO_DAMAGE;

        this.deflect.roundsLeft = DEFLECT_MAX;
        this.deflect.actualRoundsLeft = DEFLECT_MAX;

        this.deflect.baseDamage = DEFLECT_PERCENT;
        this.deflect.actualBaseDamage = DEFLECT_PERCENT;

        this.deflect.damageOverTime = ZERO_DAMAGE;
        this.deflect.actualDamageOverTime = ZERO_DAMAGE;
    }

    @Override
    public final void levelUp() {
        this.maxHealth += HEALTH_BONUS;
        this.hitPoints = this.maxHealth;
        this.drain.baseDamage += DRAIN_BONUS;
        if (this.deflect.baseDamage < DEFLECT_MAX) {
            this.deflect.baseDamage += DEFLECT_BONUS;
        }
    }

    @Override
    public final void prepareFight(final Map.LandType land, final Hero oponent) {
        this.againstWizard = false;
        this.totalDamage = this.calculateDamage(oponent);
        if (land.equals(Map.LandType.DESERT)) {
            this.applyOpponentModifier(LAND_MODIFIER, LAND_MODIFIER);
        }
        oponent.acceptModifier(this);
    }

    @Override
    public final void acceptModifier(final Hero enemy) {
        enemy.setOpponentModifier(this);
    }

    @Override
    public final void applyOpponentModifier(final float drainModifier,
                                            final float deflectModifier) {
        this.drainDamage = drainModifier * this.drainDamage;
        this.deflectDamage = deflectModifier * this.deflectDamage;
    }

    @Override
    public final void setOpponentModifier(final Rogue enemy) {
        this.applyOpponentModifier(DRAIN_ROGUE_MODIFIER, DEFLECT_ROGUE_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Pyromancer enemy) {
        this.applyOpponentModifier(DRAIN_PYROMANCER_MODIFIER, DEFLECT_PYROMANCER_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Knight enemy) {
        this.applyOpponentModifier(DRAIN_KNIGHT_MODIFIER, DEFLECT_KNIGHT_MODIFIER);
    }

    @Override
    public final void setOpponentModifier(final Wizard enemy) {
        this.againstWizard = true;
        this.applyOpponentModifier(DRAIN_WIZARD_MODIFIER, DEFLECT_WIZARD_MODIFIER);
    }

    @Override
    public final float calculateDamage(final Hero enemy) {
        this.drainDamage = drain.baseDamage * Math.min(DRAIN_MODIFIER * enemy.maxHealth,
                enemy.hitPoints);

        if (this.againstWizard) {
            this.deflectDamage = 0;
        } else {
            this.deflectDamage = deflect.baseDamage * enemy.totalDamageWithoutRaceModifier;
        }
        return this.drainDamage + this.deflectDamage;
    }

    @Override
    public final void endFight(final Hero enemy, final Map[][] map) {
        this.checkIsDead();
        if (this.isDead) {
            return;
        }
        this.getExperience(enemy);
        this.checkLevelUp();
        this.drain.baseDamage = this.drain.actualBaseDamage;
        this.deflect.baseDamage = this.deflect.actualBaseDamage;
    }

    @Override
    public final void attack(final Hero enemy) {
        enemy.hitPoints -= Math.round(this.drainDamage + this.deflectDamage);
    }

}
