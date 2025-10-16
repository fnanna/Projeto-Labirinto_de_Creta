package simulacao;
import modelo.*;
import personagens.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Simulacao {
    // variáveis para guardar informações que o pdf pede no relatório final
    private static boolean detecaoOcorrida = false;
    private static int rodadaDetecao = 0;
    private static boolean encontroOcorreu = false;
    private static int rodadaEncontro = 0;
    private static String causaMorte = "";
    private static List<Vertice> caminhoMinotauroPerseguicao = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // lendo o arquivo de entrada com a descrição do main.java.labirinto
            InputStream in = Simulacao.class.getResourceAsStream("/labirinto.txt");
            if (in == null) throw new FileNotFoundException();
            Scanner leitor = new Scanner(in);

            // lendo as dimensões do grafo: número de vértices e arestas
            int numVertices = leitor.nextInt();
            int numArestas = leitor.nextInt();

            // criando o grafo que representa o main.java.labirinto
            Grafo labirinto = new Grafo(numVertices);

            System.out.println("estrutura do grafo criada para " + numVertices + " vértices");
            System.out.println("lendo " + numArestas + " arestas");

            // lendo cada aresta do arquivo e adicionando ao grafo
            for (int i = 0; i < numArestas; i++) {
                int idU = leitor.nextInt();
                int idV = leitor.nextInt();
                int peso = leitor.nextInt();
                labirinto.adicionarAresta(idU, idV, peso);
            }

            System.out.println("arestas carregadas no grafo.");

            // lendo os parâmetros da simulação
            int idEntrada = leitor.nextInt();
            int idSaida = leitor.nextInt();
            int idPosInicialMinotauro = leitor.nextInt();
            int percepcaoMinotauro = leitor.nextInt();
            int tempoMaximo = leitor.nextInt();
            leitor.close();

            // obtendo os objetos vértice correspondentes aos ids
            Vertice entrada = labirinto.getVertice(idEntrada);
            Vertice saida = labirinto.getVertice(idSaida);
            Vertice posMinotauro = labirinto.getVertice(idPosInicialMinotauro);

            // criando os personagens: entrante e minotauro
            Entrante heroi = new Entrante(entrada);
            Minotauro minotauro = new Minotauro(posMinotauro, labirinto);

            System.out.println("\n--- simulação iniciada ---");
            System.out.println("entrante começa em vértice " + entrada.getId() +
                    ". objetivo: vértice " + saida.getId() +
                    ". tempo máximo: " + tempoMaximo);
            System.out.println("minotauro começa em: vértice " + posMinotauro.getId());
            System.out.println("percepção do minotauro: " + percepcaoMinotauro);

            // variáveis de controle da simulação
            List<Integer> caminhoAtual = null; // caminho do minotauro -> heroi (na perseguição)
            boolean jogoAtivo = true;
            int rodada = 0;
            boolean perseguindo = false;
            boolean minotauroVivo=true;


            // loop principal da simulação - cada iteração é uma rodada
            while (jogoAtivo) {
                rodada++;
                System.out.println("\n=== rodada " + rodada + " ===");

                // fase 1: entrante se move (1 vértice por rodada)
                System.out.print("entrante: posição " + heroi.getPosicaoAtual().getId() + " -> ");
                heroi.mover(labirinto);
                System.out.println("moveu para " + heroi.getPosicaoAtual().getId() +
                        ". (tempo gasto: " + heroi.getTempoGasto() + ")");

                // verificação de vitória: entrante chegou na saída
                if (heroi.getPosicaoAtual().equals(saida)) {
                    System.out.println("\n--- fim da simulação ---");
                    System.out.println("vitória! o prisioneiro escapou do labirinto!");
                    jogoAtivo = false;
                    break;
                }

                // fase 2: minotauro verifica perseguição

                // o minotauro verifica se detecta o entrante (usa dijkstra)
                // o metodo checarPerseguicao retorna true se detectou
                perseguindo = minotauro.checarPerseguicao(percepcaoMinotauro, heroi.getPosicaoAtual());

                // se detectou pela primeira vez, registra o momento
                if (!detecaoOcorrida && perseguindo) {
                    detecaoOcorrida = true;
                    rodadaDetecao = rodada;
                    System.out.println(">>> detecção: minotauro detectou entrante na rodada " + rodada);
                }

                System.out.print("minotauro: posição " + minotauro.getPosicaoAtual().getId());

                // fase 3: minotauro se move

                if(minotauroVivo){

                    // se não está perseguindo, move normalmente (1 vértice por rodada)
                    if (!perseguindo) {
                        minotauro.mover();
                        System.out.println(" -> moveu para " + minotauro.getPosicaoAtual().getId() +".");
                    } else {
                        // já está em perseguição: decidir se REUSAR o caminho ou CALCULAR
                        boolean heroEstaNoCaminho;
                        if (caminhoAtual != null && caminhoAtual.indexOf(heroi.getPosicaoAtual().getId()) >= 0){
                            heroEstaNoCaminho = true;
                        }
                        else {heroEstaNoCaminho = false;}

                        if (heroEstaNoCaminho) {
                            // truncar caminhoAtual até a nova posição do herói (prefixo)
                            int posIndex = caminhoAtual.indexOf(heroi.getPosicaoAtual().getId());
                            caminhoAtual = new ArrayList<>(caminhoAtual.subList(0, posIndex + 1));

                            // minotauro avança usando o caminho já conhecido (sem dijkstra)
                            caminhoAtual = minotauro.perseguirComCaminho(caminhoAtual);
                        } else {
                            // precisa (re)calcular SPT (Dijkstra)
                            List<Integer> novoCaminho = minotauro.perseguirEntrante(heroi.getPosicaoAtual());
                            caminhoAtual = novoCaminho; // atualiza (pode ser null se inacessível)
                        }

                        System.out.println(" -> moveu para " + minotauro.getPosicaoAtual().getId() + ". (dois vértices)");
                        // registra a posição para o relatório
                        caminhoMinotauroPerseguicao.add(minotauro.getPosicaoAtual());
                    }
                }

                // verificação de encontro: entrante e minotauro no mesmo vértice
                if (heroi.getPosicaoAtual().equals(minotauro.getPosicaoAtual())) {
                    encontroOcorreu = true;
                    rodadaEncontro = rodada;
                    System.out.println("\n--- encontro fatal ---");
                    System.out.println("minotauro encontrou o entrante no vértice " + heroi.getPosicaoAtual().getId());

                    // batalha com 1% de chance de vitória do entrante
                    if (minotauro.batalhaMinotauro()) {
                        System.out.println("milagre! entrante venceu o minotauro com 1% de chance!");
                        minotauroVivo=false;
                        causaMorte = "minotauro derrotado";
                        // nesse caso o entrante continua, minotauro morre
                    } else {
                        System.out.println("derrota! minotauro eliminou o entrante (99% de chance).");
                        causaMorte = "morto pelo minotauro";
                        jogoAtivo = false;
                        break;
                    }
                }

                // verificação de derrota por tempo: comida acabou
                if (heroi.getTempoGasto() >= tempoMaximo) {
                    System.out.println("\n--- fim da simulação ---");
                    System.out.println("derrota! o prisioneiro não conseguiu escapar a tempo e ficou sem comida.");
                    causaMorte = "fome (tempo esgotado)";
                    jogoAtivo = false;
                }
            }

            // relatório final completo
            // salvando relatório em arquivo txt
            try {
                java.io.FileWriter escritor = new java.io.FileWriter("relatorio_simulacao.txt");

                escritor.write("=== RELATÓRIO FINAL - LABIRINTO DE CRETA ===\n\n");

                // informação de escape ou morte
                if (heroi.getPosicaoAtual().equals(saida)) {
                    escritor.write("RESULTADO: Prisioneiro ESCAPOU do labirinto!\n");
                } else {
                    escritor.write("RESULTADO: Prisioneiro MORREU - " + causaMorte + "\n");
                }

                // tempo restante
                int tempoRestante = tempoMaximo - heroi.getTempoGasto();
                escritor.write("Tempo restante até acabar a comida: " + (tempoRestante > 0 ? tempoRestante : 0) + "\n\n");

                // sequência de vértices visitados
                escritor.write("Sequência de vértices visitados pelo prisioneiro: " + heroi.getHistoricoDeMovimentos() + "\n\n");

                // momento da detecção
                if (detecaoOcorrida) {
                    escritor.write("Momento da detecção do prisioneiro pelo minotauro: Rodada " + rodadaDetecao + "\n");
                } else {
                    escritor.write("Momento da detecção do prisioneiro pelo minotauro: Não ocorreu\n");
                }

                // momento do alcance
                if (encontroOcorreu) {
                    escritor.write("Momento em que o minotauro alcançou o prisioneiro: Rodada " + rodadaEncontro + "\n");
                } else {
                    escritor.write("Momento em que o minotauro alcançou o prisioneiro: Não ocorreu\n");
                }

                // caminho da perseguição
                if (perseguindo) {
                    escritor.write("Caminho percorrido pelo Minotauro durante perseguição: " + caminhoMinotauroPerseguicao + "\n");
                } else {
                    escritor.write("Caminho percorrido pelo Minotauro durante perseguição: Não ocorreu perseguição\n");
                }

                escritor.close();
                System.out.println("\n>>> Relatório salvo em: relatorio_simulacao.txt");

            } catch (java.io.IOException e) {
                System.out.println("Erro ao salvar relatório em arquivo, mas o programa funcionou normalmente.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("erro crítico: arquivo do labirinto não encontrado!");
            e.printStackTrace();
        }
    }
}