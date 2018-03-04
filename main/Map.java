package main;


import heroes.Hero;

public class Map {
    protected LandType typeOfLand;
    protected boolean multiplePlayers;
    protected Hero player1;
    protected Hero player2;
    protected Hero pivot;

    Map() {
        multiplePlayers = false;
        typeOfLand = LandType.LAND;
        player1 = null;
        player2 = null;
        pivot = null;
    }

    /**
     * Seteaza daca sunt jucatori pe terenul actual.
     * @param multiplePlayers True/False
     */
    public final void setMultiplePlayers(final boolean multiplePlayers) {
        this.multiplePlayers = multiplePlayers;
    }

    public final Hero getPlayer1() {
        return this.player1;
    }

    public final void setPlayer1(final Hero player1) {
        this.player1 = player1;
    }

    public final Hero getPlayer2() {
        return this.player2;
    }

    public final void setPlayer2(final Hero player2) {
        this.player2 = player2;
    }

    /**
     * Verifica daca pe tile-ul curent este prezent un Wizard ca Player 1, iar in caz afirmativ,
     * acesta este setat ca Player 2 pentru o corecta calculare a parametrilor in cazul
     * implementarii curente.
     */
    public final void updateIfWizard() {
        if (this.player1.getRace() == 'W') {
            this.pivot = this.player2;
            this.player2 = this.player1;
            this.player1 = this.pivot;
            this.pivot = null;
        }
    }

    /**
     * Seteaza tipul de land in functie de litera corespunzatoare.
     * @param land Litera corespunzatoare land-ului
     */
    public final void setLandType(final char land) {
        if (land == 'L') {
            this.typeOfLand = LandType.LAND;
        }
        if (land == 'V') {
            this.typeOfLand = LandType.VOLCANIC;
        }
        if (land == 'D') {
            this.typeOfLand = LandType.DESERT;
        }
        if (land == 'W') {
            this.typeOfLand = LandType.WOODS;
        }
    }

    /**
     * Plasarea pe land-ul curent a unei referinte pentru jucatorul nou-venit pentru o memorare a
     * pozitiei jucatorilor si o accesare facila in cadrul programului.
     * @param player Tipul de jucator.
     * @param hero Referinta catre jucatorul nou-venit.
     */
    public final void setPlayer(final char player, final Hero hero) {
        if (player == 'W' || player == 'P' || player == 'K' || player == 'R') {
            if (!this.multiplePlayers) {
                this.player1 = hero;
                this.multiplePlayers = true;
            } else {
                this.player2 = hero;
            }
        }
    }

    public final boolean checkIfTwo() {
        return this.player1 != null && this.player2 != null;
    }

    /**
     * Se realizeaza mutarea unui erou de pe un tile, pe altul, in functie de noua pozitie asignata.
     * @param map Harta de joc
     * @param player Jucatorul ce trebuie mutat.
     * @param newPosition Referinta catre tile-ul nou, unde trebuie mutat.
     * @return Harta actualizata.
     */
    public final Map[][] updatePosition(final Map[][] map, final Hero player,
                                        final Map newPosition) {
        if (this.player1 == player) {
            this.player1 = null;
            this.multiplePlayers = false;
            if (newPosition.player1 == null) {
                newPosition.player1 = player;
                if (newPosition.player2 != null) {
                    newPosition.multiplePlayers = true;
                }
            } else {
                newPosition.player2 = player;
                newPosition.multiplePlayers = true;
            }
        } else {
            this.player2 = null;
            this.multiplePlayers = false;
            if (newPosition.player1 == null) {
                newPosition.player1 = player;
                if (newPosition.player2 != null) {
                    newPosition.multiplePlayers = true;
                }
            } else {
                newPosition.player2 = player;
                newPosition.multiplePlayers = true;
            }
        }
        return map;
    }

    public enum LandType {
        LAND, VOLCANIC, DESERT, WOODS
    }
}
