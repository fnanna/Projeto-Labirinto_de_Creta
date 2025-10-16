package personagens;

import modelo.*;

import java.util.*;

public class Minotauro {
    private Vertice posicaoAtual;
    private Grafo labirinto;

    private boolean[] foiVisitado;
    private Stack<Vertice> visitados;

    public Minotauro(Vertice posicao, Grafo labirinto){
        this.posicaoAtual=posicao;
        this.foiVisitado=new boolean[labirinto.getTamanho()];
        this.visitados=new Stack<>();
        this.labirinto=labirinto;

        this.foiVisitado[posicaoAtual.getId()]=true;
        visitados.push(this.posicaoAtual);
    }

    public Vertice getPosicaoAtual(){ return this.posicaoAtual; }

    public void mover(){
        //percorre os vértices usando DFS

        Vertice proximo = null;
        Vertice topo = visitados.peek();
        List<Aresta> adjacentes = labirinto.getVizinhos(topo);

        //pega aresta na lista de adjacência
        for (Aresta e : adjacentes) {
            if (!foiVisitado[e.getDestino().getId()]) {
                proximo = e.getDestino();
                break;
            }
        }
        //move o minotauro para o vizinho
        if (proximo != null) {
            //ir para vizinho não visitado
            foiVisitado[proximo.getId()] = true;

            if (visitados.isEmpty() || visitados.peek() != proximo) {
                visitados.push(proximo);
            }

            this.posicaoAtual = proximo;
        } else {
            // não há vizinhos não visitados = voltar para o pai
            visitados.pop(); //desempilha o atual
            if (!visitados.isEmpty()) {
                this.posicaoAtual = visitados.peek(); // mover para o vértice pai
            }
        }
        //fim da movimentação = moveu 1 vértice
    }

    public boolean batalhaMinotauro(){
        Random rand = new Random();
        int chance = rand.nextInt(100) + 1; // gera um número aleatório entre 1 e 100
        return chance==1; // retorna true se caiu no 1% (entrante venceu) e false c.c. (minotauro venceu)
    }

    public boolean checarPerseguicao(int parametro,Vertice posEntrante){
        //usa-se dijkstra para ver a distância
        dijkstra();

        //se o herói estiver no raio do minotauro, começa a perseguição
        return posEntrante.getCusto() <= parametro;
    }

    public List<Integer> perseguirEntrante(Vertice posHeroi){
        int[] caminhoMin = dijkstra();

        //reconstrói o caminho do herói até o minotauro (caminho reverso)
        List<Integer> caminhoRev = new ArrayList<>();
        int atual = posHeroi.getId();
        while (atual != -1) {
            caminhoRev.add(atual);
            if (atual == posicaoAtual.getId()) break;
            atual = caminhoMin[atual];
        }

        //inverte para obter minotauro -> herói
        Collections.reverse(caminhoRev);

        //pega o próximo vertice da lista
        int passos = Math.min(2, caminhoRev.size() - 1); // caminhoRev.size()-1 = número de arestas até o herói
        for (int s = 1; s <= passos; s++) {
            int novoId = caminhoRev.get(s);             // próximo vértice no caminho
            Vertice novo = labirinto.getVertice(novoId);

            //atualiza posição do minotauro
            this.posicaoAtual = novo;

            // marca/atualiza estruturas de DFS
            if (!foiVisitado[novoId]) {
                foiVisitado[novoId] = true;
            }

            if (visitados.isEmpty() || visitados.peek() != novo) {
                visitados.push(novo); // mantém histórico
            }

        }

        // removemos os nós já percorridos do início do caminhoRev
        List<Integer> novoCaminho = new ArrayList<>(caminhoRev.subList(passos, caminhoRev.size()));

        return novoCaminho;
    }

    // sem chamar Dijkstra, recebe caminhoAtual onde caminhoAtual.get(0) == posição atual do minotauro.
    public List<Integer> perseguirComCaminho(List<Integer> caminhoAtual) {

        // quantos passos podemos avançar (até 2)
        int passos = Math.min(2, caminhoAtual.size() - 1);
        for (int s = 0; s < passos; s++) {
            // sempre pegar o próximo vértice no índice 1 (0 = posição atual)
            int novoId = caminhoAtual.get(1);
            Vertice novoV = labirinto.getVertice(novoId);
            this.posicaoAtual = novoV;

            if (!foiVisitado[novoId]) foiVisitado[novoId] = true;

            if (visitados.isEmpty() || visitados.peek() != novoV) {
                visitados.push(novoV);
            }

            // remove o primeiro elemento para que caminhoAtual[0] seja a nova posição
            caminhoAtual.remove(0);
        }

        // retorna o caminho truncado/atualizado (0 == posicaoAtual)
        return caminhoAtual;
    }

    private int[] dijkstra(){
        int n = labirinto.getTamanho();
        int[] pai = new int[n];

        //inicializa oos custos dos vértices
        for (int i=0; i<n; i++){
            labirinto.getVertice(i).setCusto(Integer.MAX_VALUE);
            pai[i]=-1;
        }
        labirinto.getVertice(this.posicaoAtual.getId()).setCusto(0);

        //inicaliza heap
        MinHeap heap = new MinHeap(n);
        //adiciona vertices na heap
        heap.adicionarVertices(labirinto,this.posicaoAtual.getId());

        //enquanto a heap não está vazia, checa cada véertice
        while(!heap.estaVazia()){
            //remove o vértice de custo mínimo
            Vertice v = heap.remover();

            //percorre as arestas
            for(Aresta e : labirinto.getVizinhos(v)){
                Vertice u = e.getDestino();

                // se u não está na heap, pega o próximo vizinho
                if(!heap.contem(u)){continue;}

                if(v.getCusto() + e.getPeso() < u.getCusto()){
                    u.setCusto(v.getCusto() + e.getPeso());
                    heap.atualizar(u);
                    pai[u.getId()]=v.getId();
                }
            }
        }
        return pai;
    }
}
