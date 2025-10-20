public class CompraBus {
    private String cedula;
    private String pasajero;
    private String ruta;
    private int boletos;

    public CompraBus(String cedula, String pasajero, String ruta, int boletos) {
        this.cedula = cedula;
        this.pasajero = pasajero;
        this.ruta = ruta;
        this.boletos = boletos;
    }

    public String getCedula() { return cedula; }
    public String getPasajero() { return pasajero; }
    public String getRuta() { return ruta; }
    public int getBoletos() { return boletos; }

    @Override
    public String toString() {
        return "Ruta: " + ruta +
                " Boletos: " + boletos +
                " Pasajero: " + pasajero +
                " Cédula: " + cedula + "\n";  // salto de línea
    }
}
