import java.util.LinkedList;
import java.util.Queue;

public class ColaBus {
    private Queue<CompraBus> cola;

    public ColaBus() { cola = new LinkedList<CompraBus>(); }

    public void encolar(CompraBus compra) { cola.add(compra); }

    public CompraBus desencolar() throws Exception {
        if (cola.isEmpty()) throw new Exception("No hay compras en la cola.");
        return cola.poll();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CompraBus c : cola) sb.append(c.toString());
        return sb.toString();
    }
}
