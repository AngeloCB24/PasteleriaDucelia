package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import conexion.ConexionBD;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.JOptionPane;

public class ReporteInventarioPDF {

    public static void generar(modelo.Usuario usuario) {

        try {
            String rutaProyecto = new File("").getAbsolutePath() + File.separator + "Reporte_Inventario.pdf";
            FileOutputStream archivo = new FileOutputStream(rutaProyecto);

            Document documento = new Document();
            PdfWriter.getInstance(documento, archivo);

            documento.open(); // 🔥 OBLIGATORIO ANTES DE AGREGAR CONTENIDO

            // -------- TITULO --------
            Paragraph titulo = new Paragraph("REPORTE DE INVENTARIO",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("Generado por: " + usuario.getNombreCompleto()));
            documento.add(new Paragraph("Fecha: " + new java.util.Date().toString()));
            documento.add(new Paragraph(" "));

            // =============================================
            // 1) PRODUCTOS PRÓXIMOS A VENCER
            // =============================================
            documento.add(new Paragraph("PRODUCTOS PRÓXIMOS A VENCER",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));

            PdfPTable tablaVencimiento = new PdfPTable(3);
            tablaVencimiento.addCell("Producto");
            tablaVencimiento.addCell("Stock");
            tablaVencimiento.addCell("Fecha Venc.");

            Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT name, stock, expiration_date FROM products "
                    + "WHERE expiration_date <= DATE_ADD(CURDATE(), INTERVAL 7 DAY)");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tablaVencimiento.addCell(rs.getString("name"));
                tablaVencimiento.addCell(String.valueOf(rs.getInt("stock")));
                tablaVencimiento.addCell(String.valueOf(rs.getDate("expiration_date")));
            }

            documento.add(tablaVencimiento);
            documento.add(new Paragraph(" "));

            // =============================================
            // 2) PRODUCTOS CON BAJO STOCK
            // =============================================
            documento.add(new Paragraph("PRODUCTOS CON BAJO STOCK",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));

            PdfPTable tablaStock = new PdfPTable(2);
            tablaStock.addCell("Producto");
            tablaStock.addCell("Stock");

            ps = con.prepareStatement(
                    "SELECT name, stock FROM products WHERE stock <= 5");
            rs = ps.executeQuery();

            while (rs.next()) {
                tablaStock.addCell(rs.getString("name"));
                tablaStock.addCell(String.valueOf(rs.getInt("stock")));
            }

            documento.add(tablaStock);
            documento.add(new Paragraph(" "));

            // =============================================
            // 3) MOVIMIENTOS DE INVENTARIO
            // =============================================
            documento.add(new Paragraph("MOVIMIENTOS DE INVENTARIO",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));

            PdfPTable tablaMov = new PdfPTable(6);
            tablaMov.addCell("ID");
            tablaMov.addCell("Producto");
            tablaMov.addCell("Usuario");
            tablaMov.addCell("Tipo");
            tablaMov.addCell("Cantidad");
            tablaMov.addCell("Fecha");

            ps = con.prepareStatement(
                    "SELECT im.id, p.name AS producto, u.full_name AS usuario, "
                    + "im.movement_type, im.quantity, im.date "
                    + "FROM inventory_movements im "
                    + "LEFT JOIN products p ON im.product_id = p.id "
                    + "LEFT JOIN users u ON im.user_id = u.id "
                    + "ORDER BY im.date DESC"
            );

            rs = ps.executeQuery();

            while (rs.next()) {
                tablaMov.addCell(String.valueOf(rs.getInt("id")));
                tablaMov.addCell(rs.getString("producto"));
                tablaMov.addCell(rs.getString("usuario"));
                tablaMov.addCell(rs.getString("movement_type"));
                tablaMov.addCell(String.valueOf(rs.getInt("quantity")));
                tablaMov.addCell(String.valueOf(rs.getTimestamp("date")));
            }

            documento.add(tablaMov);

            documento.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
