package modelo;

import java.util.ArrayList;

public class MinHeap {
    private ArrayList<Vertice> heap;
    private int[] posicaoHeap;

    public MinHeap(int n) {
        this.heap = new ArrayList<>(n);
        this.posicaoHeap = new int[n];

        for (int i = 0; i < n; i++) posicaoHeap[i] = -1;
    }

    public void adicionarVertices(Grafo grafo, int fonteId){
        heap.clear();
        // adiciona a fonte primeiro (custo 0)
        Vertice fonte = grafo.getVertice(fonteId);
        heap.add(fonte);
        posicaoHeap[fonte.getId()] = 0;

        // adiciona os outros vértices (custo = Integer.MAX_VALUE)
        for (int i = 0; i < grafo.getTamanho(); i++){
            if (i == fonteId) continue;
            Vertice v = grafo.getVertice(i);
            heap.add(v);
            posicaoHeap[v.getId()] = heap.size() - 1;
        }
    }

    public void adicionar(Vertice v){
        //adicionamos o elemento na ultima posicao e subimos para o topo da heap
        heap.add(v);
        posicaoHeap[v.getId()] = heap.size() - 1;
        subir(heap.size() - 1);
    }

    public Vertice remover(){
        //removemos o menor elemento da heap
        Vertice raiz = heap.get(0);
        posicaoHeap[raiz.getId()] = -1;

        //adicionamos o último elemento no topo e o descemos para a posição correta
        Vertice ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {                  // se só houver a raiz, não precisamos descer o elemento
            heap.set(0, ultimo);
            posicaoHeap[ultimo.getId()] = 0;
            descer(0);
        }

        return raiz;
    }

    public void atualizar(Vertice v){
        //mudamos a posição no heap após alterarmos o valor
        int pos = posicaoHeap[v.getId()];
        if (pos != -1) {
            subir(pos);
            descer(pos);
        }
    }

    public boolean contem(Vertice v){
        return posicaoHeap[v.getId()] != -1;
    }

    public boolean estaVazia(){ return heap.isEmpty(); }

    private void subir(int pos){
        Vertice atual = heap.get(pos);

        //enquanto não estivermos na raiz
        while (pos > 0) {
            int pai = (pos - 1) / 2;
            Vertice verticePai = heap.get(pai);

            if (atual.getCusto() >= verticePai.getCusto()) break;

            // se o custo do filho for menor
            heap.set(pos, verticePai); //descemos o pai
            posicaoHeap[verticePai.getId()] = pos; //atualizamos a posição
            pos = pai;                             // mudamos a referencia
        }

        // atualizamos o vertice atual na posição calculada
        heap.set(pos, atual);
        posicaoHeap[atual.getId()] = pos;
    }

    private void descer(int pos){
        Vertice atual = heap.get(pos);
        int tamanho = heap.size();

        //enquanto o vertice tiver um filho
        while (pos < tamanho / 2) {
            int filhoEsq = 2 * pos + 1;
            int filhoDir = 2 * pos + 2;
            int menor = filhoEsq; // assumimos que o esquerdo é maior

            //se o existe filho direito e ele é menor, atualizamos
            if (filhoDir < tamanho &&
                    heap.get(filhoDir).getCusto() < heap.get(filhoEsq).getCusto()) {
                menor = filhoDir;
            }

            if (heap.get(menor).getCusto() >= atual.getCusto()) break;

            //move o menor filho para cima
            heap.set(pos, heap.get(menor));
            posicaoHeap[heap.get(menor).getId()] = pos;
            pos = menor;  //mudamos a referência a partir da posicao do filho
        }

        // atualizamos o vertice atual na posição calculada
        heap.set(pos, atual);
        posicaoHeap[atual.getId()] = pos;
    }
}
