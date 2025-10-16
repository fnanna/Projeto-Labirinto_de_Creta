package modelo;

public class Aresta {
    private final Vertice destino;
    private final int peso;

    public Aresta(Vertice destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
}
