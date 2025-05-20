package model;

public abstract class Embarcacao {
    protected char simbolo;
    protected int tamanho;
    protected String nome;

    public Embarcacao(char simbolo, int tamanho, String nome) {
        this.simbolo = simbolo;
        this.tamanho = tamanho;
        this.nome = nome;
    }

    public char getSimbolo() { return simbolo; }
    public int getTamanho() { return tamanho; }
    public String getNome() { return nome; }
}