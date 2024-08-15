import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsultaBD {

    public static void main(String[] args) {
        // Establecer conexión a la base de datos
        Conexion_BD conexionBD = new Conexion_BD();
        Connection conexion = conexionBD.conectar();

        if (conexion != null) {
            // Inicializar el WebDriver
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\Downloads\\pruebas-main\\pruebas-main\\Selenium-Basic\\src\\test\\resources\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();

            try {
                // Paso 1: Navegar a la página de login
                driver.get("http://10.250.3.66:8080/savia/login.faces");
                driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
                driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("scMlUumJ");
                driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
                esperar(1200);

                // Paso 2: Navegar a la página de portabilidades
                driver.get("http://10.250.3.66:8080/savia/aseguramiento/portabilidades.faces");
                driver.findElement(By.cssSelector("button[name='frmPortabilidad:j_idt44']")).click();
                esperar(1500);

                // Paso 3: Hacer clic en el botón deseado
                driver.findElement(By.cssSelector("button#frmAfiliado\\:j_idt176")).click();
                esperar(4500);
                driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348")).click();
                esperar(1000);

                // Paso 4: Realizar la consulta a la base de datos y seleccionar un número de documento al azar
                String query = "SELECT numero_documento FROM aseg_afiliados aa WHERE mae_tipo_afiliado_codigo = 101 AND mae_estado_afiliacion_valor='Activo' LIMIT 10";
                try (Statement stmt = conexion.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    List<String> documentos = new ArrayList<>();
                    while (rs.next()) {
                        documentos.add(rs.getString("numero_documento"));
                    }

                    if (!documentos.isEmpty()) {
                        // Generar un número de documento aleatorio
                        Random random = new Random();
                        String documentoAleatorio = documentos.get(random.nextInt(documentos.size()));

// Realizar primero un clic en el input correspondiente
                        WebElement inputDocumento = driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348"));
                        inputDocumento.click(); // Hacer clic en el campo
                        esperar(1000); // Esperar 1 segundo

// Ingresar el número de documento generado
                        inputDocumento.sendKeys(documentoAleatorio);
                        esperar(1000); // Esperar 1 segundo
                        inputDocumento.sendKeys(Keys.ENTER);
                        esperar(300);
                        //driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348")).click();
                        driver.findElement(By.cssSelector("tbody#frmAfiliadoBusqueda\\:tablaRegistrosAfiliados_data")).click();


                    } else {
                        System.out.println("No se encontraron documentos en la consulta.");
                    }

                } catch (SQLException e) {
                    System.err.println("Error al ejecutar la consulta: " + e.getMessage());
                }

            } finally {
                // Cerrar el navegador
                //driver.quit();
                // Cerrar la conexión a la base de datos
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }


            }
        }
    }

    // Método para esperar un tiempo específico (en milisegundos)
    private static void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
