import java.io.*;
import java.net.*;
import java.util.Scanner;
import model.*;

public class Servidor {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("[ SERVIDOR - BATALHA NAVAL ]");
        System.out.println("1. Novo Jogo");
        System.out.println("2. Continuar Jogo Salvo");
        System.out.print("Escolha: ");
        int opcao = entrada.nextInt();
        entrada.nextLine();

        Jogador jogador;
        if (opcao == 2) {
            jogador = Persistencia.carregarJogo("save_servidor.txt");
            if (jogador == null) {
                System.out.println("Falha ao carregar. Iniciando novo jogo...");
                jogador = new Jogador("Servidor", 10, 10);
                autoPosicionar(jogador);
            } else {
                System.out.println("Jogo carregado com sucesso!");
            }
        } else {
            jogador = new Jogador("Servidor", 10, 10);
            autoPosicionar(jogador);
        }

        try (ServerSocket serverSocket = new ServerSocket(12345);
             Socket clientSocket = serverSocket.accept()) {

            System.out.println("Cliente conectado!");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            int pontos = jogador.getPontuacao();

            while (true) {
                String ataque = in.readLine();
                if (ataque == null || ataque.equals("fim")) break;

                System.out.println("Cliente atacou: " + ataque);
                int x = Integer.parseInt(ataque.split(",")[0]);
                int y = Integer.parseInt(ataque.split(",")[1]);

                boolean acerto = jogador.getMapa().atacar(x, y);
                jogador.getMapa().exibirMapaInterno();

                out.println(acerto ? "Acertou" : "Errou");
                Persistencia.salvarJogo(jogador, "save_servidor.txt");

                System.out.println("\nSeu mapa de ataque:");
                jogador.getMapa().exibirMapaVisivel();

                System.out.print("Digite coordenadas para atacar (ex: 2,4) ou 'sair': ");
                String coords = entrada.nextLine();
                if (coords.equalsIgnoreCase("sair")) {
                    out.println("fim");
                    System.out.println("Encerrando...");
                    break;
                }

                out.println(coords);
                String resultado = in.readLine();
                if (resultado == null || resultado.equals("fim")) break;

                int ax = Integer.parseInt(coords.split(",")[0]);
                int ay = Integer.parseInt(coords.split(",")[1]);

                jogador.getMapa().getVisivel()[ax][ay] =
                    resultado.equalsIgnoreCase("Acertou") ? 'X' : 'Y';

                if (resultado.equalsIgnoreCase("Acertou")) pontos++;

                jogador.setPontuacao(pontos);
                Persistencia.salvarJogo(jogador, "save_servidor.txt");

                System.out.println("Resultado do ataque: " + resultado);
                System.out.println("Pontuação atual: " + pontos);

                if (pontos >= 5) {
                    System.out.println("Você venceu!");
                    out.println("fim");
                    break;
                }
            }

            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void autoPosicionar(Jogador jogador) {
        Mapa mapa = jogador.getMapa();
        for (int i = 0; i < 2; i++) mapa.posicionarEmbarcacao(new PortaAvioes());
        for (int i = 0; i < 3; i++) mapa.posicionarEmbarcacao(new Destroyer());
        for (int i = 0; i < 4; i++) mapa.posicionarEmbarcacao(new Submarino());
        for (int i = 0; i < 5; i++) mapa.posicionarEmbarcacao(new Fragata());
        for (int i = 0; i < 6; i++) mapa.posicionarEmbarcacao(new Bote());
    }
}