package model;

import java.util.*;

public class Mapa {
    private char[][] mapaVisivel;
    private char[][] mapaInterno;
    private int linhas;
    private int colunas;

    public Mapa(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        mapaVisivel = new char[linhas][colunas];
        mapaInterno = new char[linhas][colunas];
        inicializarMapas();
    }

    private void inicializarMapas() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                mapaVisivel[i][j] = 'O'; // oculto
                mapaInterno[i][j] = 'V'; // vazio
            }
        }
    }

    public boolean posicionarEmbarcacao(model.Embarcacao e) {
        Random rand = new Random();
        int tentativas = 0;
        while (tentativas < 100) {
            int x = rand.nextInt(linhas);
            int y = rand.nextInt(colunas);
            int dir = rand.nextInt(3); // 0 = horizontal, 1 = vertical, 2 = diagonal

            if (podeColocar(e, x, y, dir)) {
                colocar(e, x, y, dir);
                return true;
            }
            tentativas++;
        }
        return false;
    }

    private boolean podeColocar(model.Embarcacao e, int x, int y, int dir) {
        int dx = 0, dy = 0;
        switch (dir) {
            case 0: dy = 1; break;
            case 1: dx = 1; break;
            case 2: dx = 1; dy = 1; break;
        }

        for (int i = 0; i < e.getTamanho(); i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            if (nx < 0 || ny < 0 || nx >= linhas || ny >= colunas || mapaInterno[nx][ny] != 'V')
                return false;

            for (int ix = -1; ix <= 1; ix++) {
                for (int iy = -1; iy <= 1; iy++) {
                    int vx = nx + ix;
                    int vy = ny + iy;
                    if (vx >= 0 && vy >= 0 && vx < linhas && vy < colunas) {
                        if (mapaInterno[vx][vy] != 'V') return false;
                    }
                }
            }
        }
        return true;
    }

    private void colocar(model.Embarcacao e, int x, int y, int dir) {
        int dx = 0, dy = 0;
        switch (dir) {
            case 0: dy = 1; break;
            case 1: dx = 1; break;
            case 2: dx = 1; dy = 1; break;
        }
        for (int i = 0; i < e.getTamanho(); i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            mapaInterno[nx][ny] = e.getSimbolo();
        }
    }

    public void exibirMapaVisivel() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(mapaVisivel[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean atacar(int x, int y) {
        if (mapaVisivel[x][y] != 'O') return false;

        if (mapaInterno[x][y] != 'V') {
            mapaVisivel[x][y] = 'X';
            return true;
        } else {
            mapaVisivel[x][y] = 'Y';
            return false;
        }
    }

    public void exibirMapaInterno() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(mapaInterno[i][j] + " ");
            }
            System.out.println();
        }
    }

    public char[][] getVisivel() {
        return mapaVisivel;
    }
    
    public char[][] getInterno() {
        return mapaInterno;
    }
}