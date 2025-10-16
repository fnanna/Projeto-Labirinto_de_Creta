package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

   // Representa o main.java.labirinto como um grafo ponderado
   // Utiliza uma lista de adjacência para armazenar as conexões entre os vértices

public class Grafo {
    private final Map<Vertice, List<Aresta>> listaDeAdjacencia;  // Cada chave é um objeto Vertice e o valor são todos os corredores que saem do vértice
    private final List<Vertice> vertices;  // Lista auxiliar que armazena a referência de cada objeto Vertice criado, permitindo serem acessados rapidamente pelo ID
    private final int tamanho;

    public Grafo(int numVertices) {
        this.listaDeAdjacencia = new HashMap<>();
        this.vertices = new ArrayList<>();
        this.tamanho=numVertices;

        for (int i = 0; i < numVertices; i++) {
            Vertice v = new Vertice(i);
            this.vertices.add(v);  // Guarda o vértice na lista auxiliar
            this.listaDeAdjacencia.put(v, new ArrayList<>());  // Coloca cada vértice como uma nova chave e o associa a uma nova lista de arestas vazia
        }
    }

    public Vertice getVertice(int id) {
        if (id >= 0 && id < this.vertices.size()) {  // 0 <= id < 20, size = 20
            return this.vertices.get(id);
        }
        return null;
    }

    public void adicionarAresta(int idU, int idV, int peso) {
        Vertice u = getVertice(idU);
        Vertice v = getVertice(idV);

        if (u != null && v != null) {
            // Adiciona a aresta de U para V
            listaDeAdjacencia.get(u).add(new Aresta(v, peso));   // Pega a lista associada a u e adiciona a aresta uv
            listaDeAdjacencia.get(v).add(new Aresta(u, peso));   // Faz o mesmo no sentido contrário e mostra que o vértice é não direcionado
        }
    }

    public List<Aresta> getVizinhos(Vertice vertice) {
        // Se a chave (vértice) existe, retorna a List<Aresta> correspondente, funcionando como um get()
        // Senão, se a chave não existe no mapa, em vez de retornar null, retorna uma lista nova e vazia
        // Retornar null poderia acarretar em NullPointerException
        return listaDeAdjacencia.getOrDefault(vertice, new ArrayList<>());
    }

    public int getTamanho() {
        return tamanho;
    }
}
