package modelo;
import java.util.Objects;

public class Vertice {
    private final int id;
    private int custo;

    public Vertice(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCusto(int custo){ this.custo = custo; }
    public int getCusto(){ return this.custo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return id == vertice.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "V(" + id + ")";
    }
}
