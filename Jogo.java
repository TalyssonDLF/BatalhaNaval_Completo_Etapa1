import java.util.*;
import model.Bote;
import model.Destroyer;
import model.Embarcacao;
import model.Fragata;
import model.Jogador;
import model.Mapa;
import model.PortaAvioes;
import model.Submarino;

public class Jogo {
    private Jogador jogador1;
    private Jogador jogador2;
    private Scanner scanner = new Scanner(System.in);
    private final int LINHAS = 10;
    private final int COLUNAS = 10;
    private boolean mostrarInimigo = false;

    public void iniciar(boolean mostrarEmbarcacoes) {
        this.mostrarInimigo = mostrarEmbarcacoes;
        System.out.println("=== BATALHA NAVAL ===");
        System.out.print("Digite o nome do Jogador 1: ");
        String nome1 = scanner.nextLine();
        jogador1 = new Jogador(nome1, LINHAS, COLUNAS);

        System.out.print("Digite o nome do Jogador 2: ");
        String nome2 = scanner.nextLine();
        jogador2 = new Jogador(nome2, LINHAS, COLUNAS);

        System.out.println("\nPosicionando embarcações automaticamente...");
        posicionarEmbarcacoes(jogador1);
        posicionarEmbarcacoes(jogador2);

        System.out.println("\nJogo iniciado!\n");

        boolean fimDeJogo = false;
        Jogador atacante = jogador1;
        Jogador defensor = jogador2;

        while (!fimDeJogo) {
            System.out.println("Vez de " + atacante.getNome());
            if (mostrarInimigo) {
                System.out.println(">>> Mapa real do inimigo:");
                defensor.getMapa().exibirMapaInterno();
            }

            System.out.print("Digite a linha do ataque (0-" + (LINHAS - 1) + "): ");
            int x = scanner.nextInt();
            System.out.print("Digite a coluna do ataque (0-" + (COLUNAS - 1) + "): ");
            int y = scanner.nextInt();

            boolean acerto = defensor.getMapa().atacar(x, y);
            if (acerto) {
                System.out.println(">> ACERTOU!");
                atacante.incrementarPontuacao();
            } else {
                System.out.println(">> Errou!");
            }

            if (atacante.getPontuacao() >= 5) {
                System.out.println("\n*** " + atacante.getNome() + " venceu o jogo! ***");
                fimDeJogo = true;
            }

            // Troca de turno
            Jogador temp = atacante;
            atacante = defensor;
            defensor = temp;
        }
    }

    private void posicionarEmbarcacoes(Jogador jogador) {
        Mapa mapa = jogador.getMapa();
        List<Embarcacao> lista = new ArrayList<>();

        for (int i = 0; i < 2; i++) lista.add(new PortaAvioes());
        for (int i = 0; i < 3; i++) lista.add(new Destroyer());
        for (int i = 0; i < 4; i++) lista.add(new Submarino());
        for (int i = 0; i < 5; i++) lista.add(new Fragata());
        for (int i = 0; i < 6; i++) lista.add(new Bote());

        for (Embarcacao e : lista) {
            boolean colocado = mapa.posicionarEmbarcacao(e);
            if (!colocado) {
                System.out.println("Erro ao posicionar: " + e.getNome());
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Jogo jogo = new Jogo();
    
        while (true) {
            System.out.println("\n====== MENU ======");
            System.out.println("1. Novo jogo");
            System.out.println("2. Sair");
            System.out.println("3. Novo jogo (com embarcações visíveis)");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer
    
            if (opcao == 1) {
                jogo.iniciar(false);
            } else if (opcao == 2) {
                System.out.println("Saindo...");
                break;
            } else if (opcao == 3) {
                jogo.iniciar(true);
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }
}