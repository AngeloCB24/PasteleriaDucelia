package vistas;

import controlador.ControladorInventario;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;

import utilidades.ReporteInventarioPDF;

// Apache POI
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class VistaInventario extends JFrame {

    private modelo.Usuario usuarioActual;
    private JTextField txtBuscar;
    private JTable tablaInventario;
    private JButton btnBuscar, btnAgregar, btnEditar, btnEliminar, btnExportar, btnReporte;
    private JLabel lblFecha, lblHora;
    private ControladorInventario controlador = new ControladorInventario();
    private Timer timer;

    public VistaInventario(modelo.Usuario usuario) {
        this.usuarioActual = usuario;
        init();
    }

    public void init() {
        setTitle("GESTIÓN DE INVENTARIO - Usuario: "
                + (usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado"));

        // Pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        // --- Título ---
        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO", SwingConstants.CENTER);
        lblTitulo.setFont(fTitulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, height * 2 / 100, width, height * 5 / 100);
        fondo.add(lblTitulo);

        // --- Buscar ---
        JLabel lblBuscar = new JLabel("Buscar");
        lblBuscar.setFont(fLabel);
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setBounds(width * 3 / 100, height * 10 / 100, width * 10 / 100, height * 3 / 100);
        fondo.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(width * 3 / 100, height * 13 / 100, width * 15 / 100, height * 5 / 100);
        fondo.add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(fLabel);
        btnBuscar.setBounds(width * 20 / 100, height * 13 / 100, width * 10 / 100, height * 5 / 100);
        fondo.add(btnBuscar);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setFont(fLabel);
        btnAgregar.setBounds(width * 32 / 100, height * 13 / 100, width * 10 / 100, height * 5 / 100);
        fondo.add(btnAgregar);

        btnEditar = new JButton("Editar");
        btnEditar.setFont(fLabel);
        btnEditar.setBounds(width * 44 / 100, height * 13 / 100, width * 10 / 100, height * 5 / 100);
        fondo.add(btnEditar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(fLabel);
        btnEliminar.setBounds(width * 56 / 100, height * 13 / 100, width * 10 / 100, height * 5 / 100);
        fondo.add(btnEliminar);

        btnExportar = new JButton("Exportar Excel");
        btnExportar.setFont(fLabel);
        btnExportar.setBounds(width * 68 / 100, height * 13 / 100, width * 15 / 100, height * 5 / 100);
        fondo.add(btnExportar);

        btnReporte = new JButton("Reporte PDF");
        btnReporte.setFont(fLabel);
        btnReporte.setBounds(width * 85 / 100, height * 85 / 100, width * 12 / 100, height * 6 / 100);
        fondo.add(btnReporte);

        btnReporte.addActionListener(e -> {
            ReporteInventarioPDF.generar(usuarioActual);
            JOptionPane.showMessageDialog(this, "Reporte generado con éxito");
        });

        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(fLabel);
        btnAtras.setBounds(width * 3 / 100, height * 85 / 100, width * 10 / 100, height * 6 / 100);
        fondo.add(btnAtras);

        btnAtras.addActionListener(e -> {
            new MenuPrincipal(usuarioActual != null ? usuarioActual : new modelo.Usuario()).setVisible(true);
            dispose();
        });

        int exportarX = btnExportar.getX();
        int exportarY = btnExportar.getY();
        int exportarW = btnExportar.getWidth();
        int exportarH = btnExportar.getHeight();

        // Fecha y hora
        lblFecha = new JLabel("--/--/--");
        lblFecha.setFont(fLabel);
        lblFecha.setForeground(Color.WHITE);
        // justo a la derecha del botón
        lblFecha.setBounds(exportarX + exportarW + 20, exportarY, width * 8 / 100, exportarH);
        fondo.add(lblFecha);

        lblHora = new JLabel("00:00");
        lblHora.setFont(fLabel);
        lblHora.setForeground(Color.WHITE);
        // a la derecha de la fecha
        lblHora.setBounds(lblFecha.getX() + lblFecha.getWidth() + 20, exportarY, width * 8 / 100, exportarH);
        fondo.add(lblHora);

        // Tabla
        String[] columnas = {
            "ID", "Código", "Nombre", "Stock",
            "Stock Mínimo", "Precio", "F. Venc",
            "Unidad", "Categoría", "Proveedor", "Teléfono"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaInventario = new JTable(modelo);
        tablaInventario.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        tablaInventario.setRowHeight(height * 4 / 100);

        JScrollPane scroll = new JScrollPane(tablaInventario);
        scroll.setBounds(width * 3 / 100, height * 20 / 100, width * 94 / 100, height * 60 / 100);
        fondo.add(scroll);

        // Listeners
        btnBuscar.addActionListener(e -> buscarProducto());
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnExportar.addActionListener(e -> exportarExcel());

        cargarProductos();
        verificarAlertas();
        iniciarReloj();
    }

    // Panel personalizado
    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel(String ruta) {
            java.net.URL res = getClass().getResource(ruta);
            if (res != null) {
                imagen = new ImageIcon(res).getImage();
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

    private void iniciarReloj() {
        timer = new Timer(1000, e -> {
            Date now = new Date();
            lblFecha.setText(new SimpleDateFormat("dd/MM/yy").format(now));
            lblHora.setText(new SimpleDateFormat("HH:mm:ss").format(now));
        });
        timer.start();
    }

    private void cargarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);
        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                p.getUnidad(), p.getCategoriaId(),
                p.getProveedorNombre(), p.getProveedorTelefono()
            });
        }
    }

    private void buscarProducto() {
        String filtro = txtBuscar.getText().trim().toLowerCase();
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);
        for (Producto p : controlador.listarProductos()) {
            if (p.getNombre().toLowerCase().contains(filtro)
                    || p.getCodigo().toLowerCase().contains(filtro)) {
                modelo.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                    p.getStockMin(), p.getPrecio(), p.getFechaVencimiento(),
                    p.getUnidad(), p.getCategoriaId(),
                    p.getProveedorNombre(), p.getProveedorTelefono()
                });
            }
        }
    }

    private void agregarProducto() {
        FormProducto form = new FormProducto(() -> cargarProductos());
        form.setVisible(true);
    }

    private void editarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);

        Producto producto = controlador.obtenerProducto(id);
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo cargar el producto.");
            return;
        }

        FormProducto form = new FormProducto(producto, () -> cargarProductos());
        form.setVisible(true);
    }

    private void eliminarProducto() {
        int fila = tablaInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int id = (int) tablaInventario.getValueAt(fila, 0);
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar producto con ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (controlador.eliminarProducto(id)) {
                cargarProductos();
            }
        }
    }

    private void verificarAlertas() {
        List<String> alertas = controlador.verificarAlertas();

        if (!alertas.isEmpty()) {
            StringBuilder sb = new StringBuilder("Alertas detectadas:\n\n");
            for (String a : alertas) {
                sb.append(a).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Alertas", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < tablaInventario.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(tablaInventario.getColumnName(i));
            }

            for (int i = 0; i < tablaInventario.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < tablaInventario.getColumnCount(); j++) {
                    Object v = tablaInventario.getValueAt(i, j);
                    row.createCell(j).setCellValue(v == null ? "" : v.toString());
                }
            }

            FileOutputStream fileOut = new FileOutputStream("Inventario.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            JOptionPane.showMessageDialog(this, "Inventario exportado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}
