import java.io.*;
import java.net.*;
import java.util.Scanner;
import model.*;

public class Cliente {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("[ CLIENTE - BATALHA NAVAL ]");
        System.out.println("1. Novo Jogo");
        System.out.println("2. Continuar Jogo Salvo");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        Jogador jogador;
        if (opcao == 2) {
            jogador = Persistencia.carregarJogo("save_cliente.txt");
            if (jogador == null) {
                System.out.println("Falha ao carregar. Iniciando novo jogo...");
                jogador = new Jogador("Cliente", 10, 10);
                autoPosicionar(jogador);
            } else {
                System.out.println("Jogo carregado com sucesso!");
            }
        } else {
            jogador = new Jogador("Cliente", 10, 10);
            autoPosicionar(jogador);
        }

        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Conectado ao servidor!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int pontos = jogador.getPontuacao();

            while (true) {
                // === ATAQUE ===
                System.out.println("\nSeu mapa de ataque:");
                jogador.getMapa().exibirMapaVisivel();

                System.out.print("Digite coordenadas para atacar (ex: 2,4) ou 'sair': ");
                String coords = scanner.nextLine();
                if (coords.equalsIgnoreCase("sair")) {
                    out.println("fim");
                    System.out.println("Encerrando manualmente...");
                    break;
                }

                out.println(coords);

                String result = in.readLine();
                if (result == null || result.equals("fim")) break;

                int ax = Integer.parseInt(coords.split(",")[0]);
                int ay = Integer.parseInt(coords.split(",")[1]);

                jogador.getMapa().getVisivel()[ax][ay] =
                    result.equalsIgnoreCase("Acertou") ? 'X' : 'Y';

                System.out.println("Resultado do seu ataque: " + result);
                if (result.equalsIgnoreCase("Acertou")) pontos++;

                jogador.setPontuacao(pontos);
                Persistencia.salvarJogo(jogador, "save_cliente.txt");
                System.out.println("Jogo salvo em save_cliente.txt");
                System.out.println("Pontuação atual: " + pontos);

                if (pontos >= 5) {
                    System.out.println("Você venceu!");
                    out.println("fim");
                    break;
                }

                // === DEFESA ===
                System.out.println("\nSeu mapa de embarcações:");
                jogador.getMapa().exibirMapaInterno();

                String ataque = in.readLine();
                if (ataque == null || ataque.equals("fim")) break;

                System.out.println("Servidor atacou: " + ataque);

                int x = Integer.parseInt(ataque.split(",")[0]);
                int y = Integer.parseInt(ataque.split(",")[1]);

                boolean acerto = jogador.getMapa().atacar(x, y);
                out.println(acerto ? "Acertou" : "Errou");

                Persistencia.salvarJogo(jogador, "save_cliente.txt");
                System.out.println("Jogo salvo após defesa.");
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