package vistas;

import controlador.ControladorProveedor;
import modelo.Proveedor;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaProveedores extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private ControladorProveedor controlador = new ControladorProveedor();
    private Usuario usuarioActual;

    public VistaProveedores(Usuario usuarioActual) {

        this.usuarioActual = usuarioActual;

        setTitle("GESTIÓN DE PROVEEDORES - Usuario: "
                + (usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado"));

        // Pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Fondo
        FondoPanel fondo = new FondoPanel("/images/fondoLila.png");
        fondo.setLayout(null);
        setContentPane(fondo);

        // Dimensiones de pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        int fontSize = width / 80;
        Font fTitulo = new Font("Segoe UI", Font.BOLD, fontSize + 20);
        Font fLabel = new Font("Segoe UI", Font.BOLD, fontSize + 5);

        JLabel lblTitulo = new JLabel("GESTIÓN DE PROVEEDORES", SwingConstants.CENTER);
        lblTitulo.setFont(fTitulo);
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBounds(0, height * 2 / 100, width, height * 5 / 100);
        fondo.add(lblTitulo);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Contacto", "Email", "Teléfono"}, 0
        );

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        tabla.setRowHeight(height * 4 / 100);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(width * 5 / 100, height * 12 / 100, width * 90 / 100, height * 60 / 100);
        fondo.add(scroll);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setFont(fLabel);
        btnAgregar.setBounds(width * 5 / 100, height * 80 / 100, width * 15 / 100, height * 6 / 100);
        fondo.add(btnAgregar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setFont(fLabel);
        btnEditar.setBounds(width * 25 / 100, height * 80 / 100, width * 15 / 100, height * 6 / 100);
        fondo.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(fLabel);
        btnEliminar.setBounds(width * 45 / 100, height * 80 / 100, width * 15 / 100, height * 6 / 100);
        fondo.add(btnEliminar);

        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(fLabel);
        btnAtras.setBounds(width * 65 / 100, height * 80 / 100, width * 15 / 100, height * 6 / 100);
        fondo.add(btnAtras);

        // --- ACCIONES ---
        btnAgregar.addActionListener(e -> new FormProveedor(this::cargarTabla).setVisible(true));
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnAtras.addActionListener(e -> {
            new MenuPrincipal(usuarioActual).setVisible(true);
            dispose();
        });

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Proveedor> lista = controlador.listar();

        for (Proveedor p : lista) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getEmail(),
                p.getTelefono()
            });
        }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor.");
            return;
        }

        int id = (int) tabla.getValueAt(fila, 0);
        String nombre = (String) tabla.getValueAt(fila, 1);
        String contacto = (String) tabla.getValueAt(fila, 2);
        String email = (String) tabla.getValueAt(fila, 3);
        int telefono = (int) tabla.getValueAt(fila, 4);

        Proveedor p = new Proveedor(id, nombre, contacto, email, telefono);

        new FormProveedor(this::cargarTabla, p).setVisible(true);
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor.");
            return;
        }

        int id = (int) tabla.getValueAt(fila, 0);

        if (JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            controlador.eliminar(id);
            cargarTabla();
        }
    }

    // FONDO PERSONALIZADO
    class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String path) {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                imagen = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("⚠ No se encontró la imagen: " + path);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
