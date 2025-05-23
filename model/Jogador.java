package model;

public class Jogador {
    private String nome;
    private int pontuacao;
    private Mapa mapa;

    public Jogador(String nome, int linhas, int colunas) {
        this.nome = nome;
        this.mapa = new Mapa(linhas, colunas);
        this.pontuacao = 0;
    }

    public String getNome() { return nome; }
    public int getPontuacao() { return pontuacao; }
    public void incrementarPontuacao() { pontuacao++; }
    public Mapa getMapa() { return mapa; }

    public void setPontuacao(int p) {
        this.pontuacao = p;
    }

    
}