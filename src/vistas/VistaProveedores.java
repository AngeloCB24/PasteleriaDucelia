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

        this.usuarioActual = usuarioActual;  // 🔥 guardar correctamente el usuario

        setTitle("GESTIÓN DE PROVEEDORES - Usuario: " +
                (usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado"));

        setSize(700, 500);
        setLocationRelativeTo(null);

        // ⬅️ FONDO LILA
        FondoPanel fondo = new FondoPanel("/images/fondoLila.png");
        fondo.setLayout(null);
        setContentPane(fondo);

        JLabel lblTitulo = new JLabel("GESTIÓN DE PROVEEDORES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 10, 700, 40);
        fondo.add(lblTitulo);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Contacto", "Email", "Teléfono"}, 0
        );

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(40, 70, 620, 280);
        fondo.add(scroll);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(40, 380, 130, 35);
        fondo.add(btnAgregar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(200, 380, 130, 35);
        fondo.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(360, 380, 130, 35);
        fondo.add(btnEliminar);

        JButton btnAtras = new JButton("Atrás");
        btnAtras.setBounds(520, 380, 130, 35);
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
