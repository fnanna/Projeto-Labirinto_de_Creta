package personagens;

import modelo.Aresta;
import modelo.Grafo;
import modelo.Vertice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Entrante {
    private Vertice posicaoAtual;
    private int tempoGasto;
    private Set<Vertice> visitados;  // "Memória" para saber por onde já passou, o Set não permite elementos duplicados
    private Stack<Vertice> caminhoDeVolta;  // O "Novelo de lã" para poder voltar de becos sem saída, a pilha do DFS
    private List<Vertice> historicoDeMovimentos;  // Histórico de todos os movimentos para o relatório final

    public Entrante(Vertice posInicial) {
        this.posicaoAtual = posInicial;
        this.tempoGasto = 0;  // Inicializa o contador de tempo
        this.visitados = new HashSet<>();
        this.caminhoDeVolta = new Stack<>();
        this.historicoDeMovimentos = new ArrayList<>();

        // Marca a posição inicial como visitada e a adiciona ao histórico
        this.visitados.add(posInicial);
        this.historicoDeMovimentos.add(posInicial);
    }

    public Vertice getPosicaoAtual() {
        return posicaoAtual;
    }

    public int getTempoGasto() {
        return tempoGasto;
    }

    public List<Vertice> getHistoricoDeMovimentos() {
        return historicoDeMovimentos;
    }

    public void mover(Grafo labirinto) {
        // Pega os vizinhos da posição atual para explorar. Corresponde a `u ← G[v].prox;`
        List<Aresta> vizinhos = labirinto.getVizinhos(this.posicaoAtual);

        // Loop para encontrar um vizinho não visitado. Corresponde ao `enquanto u ≠ λ faça`
        for (Aresta aresta : vizinhos) {
            Vertice proximoVertice = aresta.getDestino();

            // `se marca[u] = branco então`
            if (!this.visitados.contains(proximoVertice)) {
                
                // `Insere v em P;` (Empilhamos a posição ATUAL para saber como voltar)
                this.caminhoDeVolta.push(this.posicaoAtual);
                
                // Movemos para o próximo vértice
                this.posicaoAtual = proximoVertice;
                
                // `marca[u] = cinza;` (Marcamos o novo local como visitado)
                this.visitados.add(this.posicaoAtual);
                
                // Atualizamos o estado
                this.tempoGasto += aresta.getPeso();
                this.historicoDeMovimentos.add(this.posicaoAtual);
                
                // `Visita(G, u);` (A "chamada recursiva" é simulada encerrando o método aqui.
                // A próxima chamada a mover() continuará a partir da nova posição).
                return;
            }
        }

        // Se o loop 'for' terminar, significa que não há vizinhos "brancos" (beco sem saída).
        // Agora, executamos a lógica de backtracking.
        
        if (!this.caminhoDeVolta.isEmpty()) {
            // `Remove v de P;` (Pegamos o caminho de volta do nosso "novelo de lã")
            Vertice anterior = this.caminhoDeVolta.pop();

            // Precisamos encontrar o peso da aresta pela qual estamos voltando
            for(Aresta aresta : labirinto.getVizinhos(this.posicaoAtual)){
                if(aresta.getDestino().equals(anterior)){
                    this.tempoGasto += aresta.getPeso();
                    break;
                }
            }

            // Movemos para a posição anterior
            this.posicaoAtual = anterior;
            this.historicoDeMovimentos.add(this.posicaoAtual);
        }
    }

    public Stack<Vertice> getCaminhoDeVolta() {
        return caminhoDeVolta;
    }
}
