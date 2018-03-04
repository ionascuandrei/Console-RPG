package heroes;

import main.Map;

public class Hero {
    /*Constantele standard pentru toti eroii*/
    static final int LVLCHECK = 250;
    static final int LVLMODIFIER = 50;
    static final int ZERO = 0;
    static final int XPMODIFIER = 40;
    static final int XPVALUE = 200;

    private int row;
    private int column;
    private char race;
    protected int hitPoints;
    protected int maxHealth;
    protected int experiencePoints;
    protected int level;
    protected boolean debuff;
    protected boolean isDead;
    protected float selfDamageOvertime;
    protected float selfDamageOverTimeRoundsLeft;
    protected float totalDamage;
    protected float totalDamageWithoutRaceModifier;

    public final int getRow() {
        return row;
    }

    public final int getColumn() {
        return column;
    }

    public final char getRace() {
        return race;
    }

    public final void setRace(final char race) {
        this.race = race;
    }

    public final void setPosition(final int rowNumber, final int columnNumber) {
        this.row = rowNumber;
        this.column = columnNumber;
    }

    /**
     * Actualizeaza pozitia jucatorului in functie de comanda pe care o primeste.
     *
     * @param newPosition Comanda pusa spre executare.
     */
    public final void updatePosition(final char newPosition) {
        if (newPosition == 'U') {
            this.setPosition(this.row - 1, this.column);
        }
        if (newPosition == 'D') {
            this.setPosition(this.row + 1, this.column);
        }
        if (newPosition == 'L') {
            this.setPosition(this.row, this.column - 1);
        }
        if (newPosition == 'R') {
            this.setPosition(this.row, this.column + 1);
        }
    }

    /**
     * Actualizeaza experienta jucatorului.
     *
     * @param experience Experienta calculata
     */
    private void updateExperience(final int experience) {
        this.experiencePoints += experience;
        this.checkLevelUp();
    }

    /**
     * Verifica daca jucatorul este afectat de damage overtime si este aplicat in functie de
     * parametrii primiti.
     */
    public final void updateOvertimeDamage() {
        if (this.selfDamageOverTimeRoundsLeft == 0) {
            return;
        } else {
            this.hitPoints -= this.selfDamageOvertime;
            this.selfDamageOverTimeRoundsLeft--;
            if (this.selfDamageOverTimeRoundsLeft == 0) {
                this.debuff = false;
            }
            this.checkIsDead();
        }
    }

    /**
     * Verifica daca eroul este capabil sa isi modifice pozitia.
     *
     * @return True/False
     */
    public final boolean checkIfIncapacited() {
        return this.debuff;
    }

    /**
     * Verifica daca jucatorul este eligibil pentru cresterea in nivel si actualizeaza situatia
     * acestuia in functie de experienta acumulata.
     */
    protected final void checkLevelUp() {
        while (this.experiencePoints >= LVLCHECK + (this.level * LVLMODIFIER)) {
            this.level++;
            this.levelUp();
        }
    }

    public final boolean checkIfAlive() {
        return (!this.isDead);
    }

    public final void checkIsDead() {
        if (this.hitPoints <= 0) {
            this.isDead = true;
        }
    }

    /**
     * Se verifica daca eroii mai sunt in viata, iar in caz contrar, acestia sunt scosi de pe harta
     * si sunt actualizate situatiile in functie de rezultat.
     *
     * @param map Tabla de joc
     */
    public final void eraseFromMap(final Map[][] map) {
        Hero player1 = map[this.row][this.column].getPlayer1();
        Hero player2 = map[this.row][this.column].getPlayer2();
        if (!this.checkIfAlive()) {
            if (player1 == this) {
                map[this.row][this.column].setPlayer1(null);
                if (player2 == null) {
                    map[this.row][this.column].setMultiplePlayers(false);
                }
            }
            if (player2 == this) {
                map[this.row][this.column].setPlayer2(null);

            }
            if (player1 == null && player2 == null) {
                map[this.row][this.column].setMultiplePlayers(false);
            }
        }
    }

    /**
     * Se calculeaza experienta pe care o primeste invinatorul unei lupte, in cazul in care isi
     * doboara cu succes adversarul, in runda in care s-a realizat lupta.
     *
     * @param loser Pointer catre inamic pentru verificarea vietii
     */
    public final void getExperience(final Hero loser) {
        if (loser.hitPoints > 0) {
            return;
        }
        int experienceWinner = Math.max(ZERO, XPVALUE - ((this.level - loser.level) * XPMODIFIER));
        this.updateExperience(experienceWinner);
    }

    /**
     * In cazul in care eroul creste in nivel, i se actualizeaza parametrii in functie de
     * modificatorii de nivel. Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     */
    public void levelUp() {
    }

    /**
     * Este luat in calcul tipul adversarului si tipul de teren pe care se tine lupta si sunt
     * actualizate toate abilitatile in functie de acestea. Functia este suprascrisa in fiecare
     * clasa, cu parametrii specifici.
     *
     * @param land    Tipul de teren pe care este situata lupta.
     * @param oponent Eroul cu care acesta ce confrunta. Aplicarea modificatorilor pentru oponent
     *                se realizeaza prin double-dispatch!
     */
    public void prepareFight(final Map.LandType land, final Hero oponent) {
    }

    /**
     * Acceptul pentru modificator, din cadrul double-dispatch-ului! Functia este suprascrisa in
     * fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Eroul cu care se confrunta jucatorul.
     */
    public void acceptModifier(final Hero enemy) {
    }

    /**
     * Aplicarea modificatorilor pentru abilitatile fiecarui erou. Functia este suprascrisa in
     * fiecare clasa, cu parametrii specifici.
     *
     * @param modifierAbility1 Modificatorul pentru prima abilitate.
     * @param modifierAbility2 Modificatorul pentru a doua abilitate.
     */
    public void applyOpponentModifier(final float modifierAbility1, final float modifierAbility2) {
    }

    /**
     * Trimiterea spre aplicare, a modificatorului specific inamicului curent, din cadrul
     * double-dispatch-ului. Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul, cu clasa particulara acestuia.
     */
    public void setOpponentModifier(final Rogue enemy) {
    }

    /**
     * Trimiterea spre aplicare, a modificatorului specific inamicului curent, din cadrul
     * double-dispatch-ului. Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul, cu clasa particulara acestuia.
     */
    public void setOpponentModifier(final Pyromancer enemy) {
    }

    /**
     * Trimiterea spre aplicare, a modificatorului specific inamicului curent, din cadrul
     * double-dispatch-ului. Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul, cu clasa particulara acestuia.
     */
    public void setOpponentModifier(final Knight enemy) {
    }

    /**
     * Trimiterea spre aplicare, a modificatorului specific inamicului curent, din cadrul
     * double-dispatch-ului. Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul, cu clasa particulara acestuia.
     */
    public void setOpponentModifier(final Wizard enemy) {
    }

    /**
     * Calcularea damage-ului total in urma aplicarii abilitatilor, in stadiul apelat.
     * Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul
     * @return Valoarea finala a damage-ului.
     */
    public float calculateDamage(final Hero enemy) {
        return 1;
    }

    /**
     * Aplicarea asupra inamicului, a tuturor abilitatilor pe care le detine eroul. Prin aceasta
     * functie este actualizata atat viata, cat si parametrii damage-ului overtime si a debuff-ului.
     * Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul cu care se poarta lupta.
     */
    public void attack(final Hero enemy) {
    }

    /**
     * Verificarea daca acesta mai este in viata. In caz afirmativ, se readuce la parametrii normali
     * toate abilitatile modificate in urma aplicarii modficatorului de teren si pentru oponent.
     * Functia este suprascrisa in fiecare clasa, cu parametrii specifici.
     *
     * @param enemy Inamicul
     * @param map   Harta
     */
    public void endFight(final Hero enemy, final Map[][] map) {
    }


    public final String printPlayer() {
        if (this.checkIfAlive()) {
            return this.race + " " + this.level + " " + this.experiencePoints + " " + this.hitPoints
                    + " " + this.row + " " + this.column;
        } else {
            return this.race + " dead";
        }
    }

}
