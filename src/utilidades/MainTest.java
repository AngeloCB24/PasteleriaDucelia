package utilidades;

public class MainTest {
    public static void main(String[] args) {
        // crea carpeta logs si no existe
        new java.io.File("logs").mkdirs();

        LogUtil.info("Prueba de Logback - info");
        LogUtil.debug("Prueba de Logback - debug (puede no mostrarse si root level=INFO)");
        LogUtil.warn("Prueba de Logback - warn");
        LogUtil.error("Prueba de Logback - error con excepción", new RuntimeException("Excepción de prueba"));
    }
}
