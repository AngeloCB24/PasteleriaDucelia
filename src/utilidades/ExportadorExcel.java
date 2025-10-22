package utilidades;

import modelo.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportadorExcel {

    public static void exportarInventario(List<Producto> productos) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario");

        String[] columnas = {"ID", "Código", "Nombre", "Categoría", "Stock", "Precio", "Descripción"};
        Row header = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        int rowNum = 1;
        for (Producto p : productos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getCodigo());
            row.createCell(2).setCellValue(p.getNombre());
            row.createCell(3).setCellValue(p.getCategoriaId() != null ? p.getCategoriaId() : 0);
            row.createCell(4).setCellValue(p.getStock());
            row.createCell(5).setCellValue(p.getPrecio());
            row.createCell(6).setCellValue(p.getDescripcion());
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream("Inventario.xlsx")) {
            workbook.write(fileOut);
            LogUtil.info("Inventario exportado correctamente a Excel.");
            workbook.close();
        } catch (IOException e) {
            LogUtil.error("Error al exportar inventario a Excel", e);
        }
    }
}
