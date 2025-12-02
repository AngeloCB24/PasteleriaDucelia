package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JOptionPane;

public class BoletaPDF {

    public static void crearBoleta(String cliente, String usuario, List<Object[]> productos, double total) {

        try {
            // 📌 Ruta dentro del proyecto
            String ruta = System.getProperty("user.dir") + File.separator + "Boleta_Compra.pdf";

            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));

            documento.open();

            // 🟣 TÍTULO PRINCIPAL
            Font titulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.MAGENTA);
            Paragraph nombrePasteleria = new Paragraph("PASTELERÍA DUCELIA\n\n", titulo);
            nombrePasteleria.setAlignment(Element.ALIGN_CENTER);
            documento.add(nombrePasteleria);

            // 💜 Mensaje
            Font mensajeF = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);
            Paragraph mensaje = new Paragraph("Gracias por su compra\n\n", mensajeF);
            mensaje.setAlignment(Element.ALIGN_CENTER);
            documento.add(mensaje);

            // 🧾 Datos generales
            documento.add(new Paragraph("Cliente: " + cliente));
            documento.add(new Paragraph("Atendido por: " + usuario));
            documento.add(new Paragraph("Fecha: " + new java.util.Date().toString()));
            documento.add(new Paragraph("\n"));

            // 📌 TABLA
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.addCell("Producto");
            tabla.addCell("Precio");
            tabla.addCell("Cantidad");

            for (Object[] fila : productos) {
                tabla.addCell(fila[1].toString()); // nombre
                tabla.addCell("S/" + fila[3].toString()); // precio
                tabla.addCell(fila[4].toString()); // cantidad
            }

            documento.add(tabla);

            // TOTAL
            documento.add(new Paragraph("\nTotal a pagar: S/" + total, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));

            documento.close();

            JOptionPane.showMessageDialog(null, "Boleta generada con exito");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generando boleta: " + e.getMessage());
        }
    }
}
