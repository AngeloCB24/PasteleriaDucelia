package vistas;

import controlador.ControladorProducto;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VistaProductos extends JFrame {
    private ControladorProducto controlador = new ControladorProducto();

    public VistaProductos() {
        setTitle("Lista de Productos");
        setSize(800,400);
        setLocationRelativeTo(null);

        String[] columnas = {"ID","Código","Nombre","Stock","Stock Mínimo","Precio","Vencimiento"};
        DefaultTableModel modelo = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla));

        List<Producto> lista = controlador.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getStock(),
                p.getStockMin(), p.getPrecio(), p.getFechaVencimiento()
            });
        }
    }
}
