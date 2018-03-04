package main;

import fileio.FileSystem;
import heroes.Hero;
import heroes.Knight;
import heroes.Pyromancer;
import heroes.Wizard;
import heroes.Rogue;

import java.io.IOException;

final class Main {
    private static final int ZERO = 0;

    private Main() {
    }

    /**
     * Functia initializeaza eroii in functie de tipul primit.
     *
     * @param playerType Caracterul tipului de erou pe care dorim sa il initializam.
     * @return Un erou specific din clasele de eroi puse la dispozitie.
     */
    private static Hero setPlayer(final char playerType) {
        if (playerType == 'W') {
            return new Wizard();
        }
        if (playerType == 'P') {
            return new Pyromancer();
        }
        if (playerType == 'K') {
            return new Knight();
        }
        if (playerType == 'R') {
            return new Rogue();
        }
        return null;
    }

    public static void main(final String[] args) throws IOException {
        /* Instantiez accesul catre fisierele de input si output, dedicate pentru scriere si
        citire*/
        FileSystem fileSystem = new FileSystem(args[0], args[1]);
        /*Citesc marimea hartii de joc*/
        int length = fileSystem.nextInt();
        int width = fileSystem.nextInt();
        /*Creez harta de jos*/
        Map[][] map = new Map[length][width];
        /*Setez fiecarei zone de joc, tipul de teren, specific*/
        for (int i = 0; i < length; i++) {
            int indexLand = 0;
            String land = fileSystem.nextWord();
            for (int j = 0; j < width; j++) {
                map[i][j] = new Map();
                map[i][j].setLandType(land.charAt(indexLand));
                indexLand++;
            }
        }
        /*Citesc numarul de jucatori*/
        int players = fileSystem.nextInt();
        /*Creez vectorul in care stochez toti eroii*/
        Hero[] heroes = new Hero[players];
        /*Initializez fiecare tip de jucator cu clasa specifica si il pozitionez pe harta la locul
        respectiv*/
        for (int i = 0; i < players; i++) {
            String player = fileSystem.nextWord();
            int row = fileSystem.nextInt();
            int column = fileSystem.nextInt();

            heroes[i] = setPlayer(player.charAt(ZERO));
            map[row][column].setPlayer(player.charAt(ZERO), heroes[i]);
            heroes[i].setPosition(row, column);
        }
        /*Citesc numarul de runde*/
        int rounds = fileSystem.nextInt();
        /*Decurgerea jocului*/
        for (int currentRound = 0; currentRound < rounds; currentRound++) {
            /*Citesc din fisier tipurile de mutari pe runda curenta*/
            String moves = fileSystem.nextWord();
            for (int playerIndex = 0; playerIndex < players; playerIndex++) {
                /*Realizez mutarile pe harta doar pentru eroii care sunt inca in viata si daca nu
                sunt incapacitati*/
                if (heroes[playerIndex].checkIfAlive()
                        && !heroes[playerIndex].checkIfIncapacited()) {
                    int oldRow = heroes[playerIndex].getRow();
                    int oldColumn = heroes[playerIndex].getColumn();
                    heroes[playerIndex].updatePosition(moves.charAt(playerIndex));
                    map = map[oldRow][oldColumn].updatePosition(map, heroes[playerIndex],
                            map[heroes[playerIndex].getRow()][heroes[playerIndex].getColumn()]);
                }
                /*Aplic damage-ul overtime daca acestia sunt afectati*/
                heroes[playerIndex].updateOvertimeDamage();
                /*Elimin jucatorii de pe harta in cazul in care acestia nu mai sunt in viata*/
                heroes[playerIndex].eraseFromMap(map);
            }
            /*Parcurg harta pentru e verifica daca exista lupte si execut pasii necesari*/
            for (int row = 0; row < length; row++) {
                for (int column = 0; column < width; column++) {
                    /*Verific daca sunt 2 jucatori pe pozitia actuala si initializez lupta*/
                    if (map[row][column].multiplePlayers && map[row][column].checkIfTwo()) {
                        /*Pozitionez Wizard-ul ca fiind al doilea player in executarea miscarilor
                        pentru o calculare corecta a parametrilor luptei, in cazul implementarii
                        actuale*/
                        map[row][column].updateIfWizard();
                        /*Pregatesc jucatorii de lupta*/
                        map[row][column].player1.prepareFight(map[row][column].typeOfLand,
                                map[row][column].player2);
                        map[row][column].player2.prepareFight(map[row][column].typeOfLand,
                                map[row][column].player1);
                        /*Aplic atacurile*/
                        map[row][column].player1.attack(map[row][column].player2);
                        map[row][column].player2.attack(map[row][column].player1);
                        /*Calculez daunele si verific integritatea jucatorilor*/
                        map[row][column].player1.endFight(map[row][column].player2, map);
                        map[row][column].player2.endFight(map[row][column].player1, map);
                        /*Elimin jucatorii omorati dupa finalul luptei*/
                        map[row][column].player1.eraseFromMap(map);
                        map[row][column].player2.eraseFromMap(map);
                    }

                }
            }
        }
        /*Afisez parametrii doriti la finalul jocului*/
        for (int playerIndex = 0; playerIndex < players; playerIndex++) {
            fileSystem.writeWord(heroes[playerIndex].printPlayer());
            fileSystem.writeNewLine();
        }
        fileSystem.close();
    }
}
