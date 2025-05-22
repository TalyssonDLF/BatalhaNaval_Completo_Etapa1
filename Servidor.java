import java.io.*;
import java.net.*;
import java.util.Scanner;
import model.*;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado. Aguardando jogador...");
            Socket socket = serverSocket.accept();
            System.out.println("Jogador conectado!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            Jogador jogador = new Jogador("Servidor", 10, 10);
            autoPosicionar(jogador);

            int pontos = 0;

            while (true) {
                // === DEFESA ===
                System.out.println("\nSeu mapa de embarcações:");
                jogador.getMapa().exibirMapaInterno();

                String ataque = in.readLine();
                if (ataque == null || ataque.equals("fim")) break;

                System.out.println("Jogador remoto atacou: " + ataque);

                int x = Integer.parseInt(ataque.split(",")[0]);
                int y = Integer.parseInt(ataque.split(",")[1]);

                boolean acerto = jogador.getMapa().atacar(x, y);
                out.println(acerto ? "Acertou" : "Errou");

                // === ATAQUE ===
                System.out.println("\nSeu mapa de ataque:");
                jogador.getMapa().exibirMapaVisivel();

                System.out.print("Digite coordenadas para atacar (ex: 2,4): ");
                String coords = scanner.nextLine();
                out.println(coords);

                String result = in.readLine();
                if (result == null || result.equals("fim")) break;

                int ax = Integer.parseInt(coords.split(",")[0]);
                int ay = Integer.parseInt(coords.split(",")[1]);

                jogador.getMapa().getVisivel()[ax][ay] =
                    result.equalsIgnoreCase("Acertou") ? 'X' : 'Y';

                System.out.println("Resultado do seu ataque: " + result);
                if (result.equalsIgnoreCase("Acertou")) pontos++;
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