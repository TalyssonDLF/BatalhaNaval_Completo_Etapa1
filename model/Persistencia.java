package model;
import java.io.*;
import java.util.*;

public class Persistencia {

    public static void salvarJogo(Jogador jogador, String caminho) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            writer.println(jogador.getNome());
            writer.println(jogador.getPontuacao());

            char[][] visivel = jogador.getMapa().getVisivel();
            for (char[] linha : visivel) {
                for (char c : linha) {
                    writer.print(c + " ");
                }
                writer.println();
            }

            writer.println("---");

            char[][] interno = jogador.getMapa().getInterno();
            for (char[] linha : interno) {
                for (char c : linha) {
                    writer.print(c + " ");
                }
                writer.println();
            }

            System.out.println("Jogo salvo em " + caminho);
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static Jogador carregarJogo(String caminho) {
        try (Scanner sc = new Scanner(new File(caminho))) {
            String nome = sc.nextLine();
            int pontuacao = Integer.parseInt(sc.nextLine());

            List<char[]> visivelList = new ArrayList<>();
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                if (linha.equals("---")) break;
                visivelList.add(toCharArray(linha));
            }

            List<char[]> internoList = new ArrayList<>();
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                internoList.add(toCharArray(linha));
            }

            int linhas = visivelList.size();
            int colunas = visivelList.get(0).length;
            Jogador jogador = new Jogador(nome, linhas, colunas);
            jogador.setPontuacao(pontuacao);

            char[][] visivel = new char[linhas][colunas];
            char[][] interno = new char[linhas][colunas];
            for (int i = 0; i < linhas; i++) {
                visivel[i] = visivelList.get(i);
                interno[i] = internoList.get(i);
            }

            jogador.getMapa().setVisivel(visivel);
            jogador.getMapa().setInterno(interno);

            System.out.println("Jogo carregado de " + caminho);
            return jogador;
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            return null;
        }
    }

    private static char[] toCharArray(String linha) {
        String[] tokens = linha.trim().split(" ");
        char[] resultado = new char[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            resultado[i] = tokens[i].charAt(0);
        }
        return resultado;
    }
}