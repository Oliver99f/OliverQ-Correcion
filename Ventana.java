import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Ventana {
    private JPanel principal;
    private JTextField txtCedula;
    private JTextField txtPasajero;
    private JComboBox cboRuta;
    private JTextField txtBoletos;
    private JButton btnComprar;
    private JTextArea txtListar;
    private JLabel lblVendidosQG;
    private JLabel lblVendidosQC;
    private JLabel lblVendidosQL;
    private JLabel lblDispQG;
    private JLabel lblDispQC;
    private JLabel lblDispQL;
    private JLabel lblTotal;

    private ColaBus cola = new ColaBus();
    private final int CAPACIDAD = 20;
    private int vendidosQG = 0, vendidosQC = 0, vendidosQL = 0;
    private final float P_QG = 10.50f, P_QC = 12.75f, P_QL = 15.00f;
    private float totalRecaudado = 0f;
    private HashMap<String, Integer> boletosPorCedula = new HashMap<>();

    public Ventana() {
        cboRuta.setModel(new DefaultComboBoxModel(new String[]{
                "QUITO - GUAYAQUIL  $10.50",
                "QUITO - CUENCA     $12.75",
                "QUITO - LOJA       $15.00"
        }));
        txtListar.setEditable(false);
        txtListar.setLineWrap(true);
        txtListar.setWrapStyleWord(true);
        actualizarLabels();

        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedula = txtCedula.getText().trim();
                String pasajero = txtPasajero.getText().trim();
                String ruta = cboRuta.getSelectedItem().toString();
                if (cedula.isEmpty() || pasajero.isEmpty() || txtBoletos.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete cédula, pasajero y número de boletos.");
                    return;
                }
                int boletos;
                try {
                    boletos = Integer.parseInt(txtBoletos.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Número de boletos inválido.");
                    return;
                }
                if (boletos < 1 || boletos > 5) {
                    JOptionPane.showMessageDialog(null, "Solo se permiten entre 1 y 5 boletos por compra.");
                    return;
                }
                int yaTiene = boletosPorCedula.getOrDefault(cedula, 0);
                if (yaTiene + boletos > 5) {
                    JOptionPane.showMessageDialog(null, "La cédula " + cedula + " supera el máximo de 5 boletos (ya tiene " + yaTiene + ").");
                    return;
                }
                int vendidosActuales = getVendidosRuta(ruta);
                int disponibles = CAPACIDAD - vendidosActuales;
                if (boletos > disponibles) {
                    JOptionPane.showMessageDialog(null, "No hay asientos suficientes para " + ruta + ". Disponibles: " + disponibles);
                    return;
                }
                CompraBus compra = new CompraBus(cedula, pasajero, ruta, boletos);
                cola.encolar(compra);
                sumarVendidos(ruta, boletos);
                boletosPorCedula.put(cedula, yaTiene + boletos);
                totalRecaudado += precioRuta(ruta) * boletos;
                actualizarLabels();
                txtListar.setText(cola.toString());
                txtCedula.setText("");
                txtPasajero.setText("");
                txtBoletos.setText("");
                cboRuta.setSelectedIndex(0);
                txtCedula.requestFocus();
            }
        });
    }

    private String keyRuta(String ruta) {
        String r = ruta.toUpperCase();
        if (r.contains("GUAYAQUIL")) return "QG";
        if (r.contains("CUENCA")) return "QC";
        if (r.contains("LOJA")) return "QL";
        return "";
    }

    private void actualizarLabels() {
        lblVendidosQG.setText("Q-G Vendidos: " + vendidosQG);
        lblDispQG.setText("Disponibles: " + (CAPACIDAD - vendidosQG));
        lblVendidosQC.setText("Q-C Vendidos: " + vendidosQC);
        lblDispQC.setText("Disponibles: " + (CAPACIDAD - vendidosQC));
        lblVendidosQL.setText("Q-L Vendidos: " + vendidosQL);
        lblDispQL.setText("Disponibles: " + (CAPACIDAD - vendidosQL));
        lblTotal.setText("Total Recaudado: " + String.format("$ %.2f", totalRecaudado));
    }

    private int getVendidosRuta(String ruta) {
        switch (keyRuta(ruta)) {
            case "QG": return vendidosQG;
            case "QC": return vendidosQC;
            case "QL": return vendidosQL;
            default: return 0;
        }
    }

    private void sumarVendidos(String ruta, int cant) {
        switch (keyRuta(ruta)) {
            case "QG": vendidosQG += cant; break;
            case "QC": vendidosQC += cant; break;
            case "QL": vendidosQL += cant; break;
        }
    }

    private float precioRuta(String ruta) {
        switch (keyRuta(ruta)) {
            case "QG": return P_QG;
            case "QC": return P_QC;
            case "QL": return P_QL;
            default: return 0f;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Venta de Boletos – Buses");
        frame.setContentPane(new Ventana().principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
