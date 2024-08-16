import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.*;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Random;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsultaBD {
    private WebDriver driver;
    private Connection conexion;

    @BeforeEach
    public void setUp() {
        // Inicializar el WebDriver y establecer la conexión a la base de datos
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\Downloads\\pruebas-main\\pruebas-main\\Selenium-Basic\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();

        // Conectar a la base de datos
        Conexion_BD conexionBD = new Conexion_BD();
        conexion = conexionBD.conectar();
    }

    @Test
    public void AfiliadoGenerico() throws InterruptedException {
        if (conexion != null) {
            // Paso 1: Seleccionar Afiliado
            SeleccionarAfiliado();
            esperar(300); // Pausa para dar tiempo a que las acciones se reflejen en la UI

            // Paso 2: Realizar Consulta a la Base de Datos
            realizarConsultaBD();
            esperar(300);

            // Paso 3: Seleccionar Motivo, Periodo y Origen
            SeleccionarMotivoYPeriodo();
            esperar(300);

            // Paso 4: Seleccionar Municipio y Dirrecion
            SeleccionarMunicipioYDirreccion();
            esperar(300);

            SeleccionarNumeroYCorreo();
            esperar(300);

        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos.");
        }
    }

    // Método para esperar un tiempo específico (en milisegundos)
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    private void SeleccionarAfiliado() throws InterruptedException {
        // Realizar los pasos de navegación y clics en la interfaz
        driver.get("http://10.250.3.66:8080/savia/login.faces");
        driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
        driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("scMlUumJ");
        driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
        esperar(1200);

        driver.get("http://10.250.3.66:8080/savia/aseguramiento/portabilidades.faces");

        driver.findElement(By.cssSelector("button[name='frmPortabilidad:j_idt44']")).click();
        esperar(1500);

        driver.findElement(By.cssSelector("button#frmAfiliado\\:j_idt176")).click();
        esperar(4500);

        driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348")).click();
        esperar(1000);
    }

    private void realizarConsultaBD() throws InterruptedException {
        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String query = "SELECT numero_documento FROM aseg_afiliados aa WHERE mae_tipo_afiliado_codigo = 101 AND mae_estado_afiliacion_valor='Activo' LIMIT 10";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> documentos = new ArrayList<>();
            while (rs.next()) {
                documentos.add(rs.getString("numero_documento"));
            }

            if (!documentos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                String documentoAleatorio = documentos.get(random.nextInt(documentos.size()));

                WebElement inputDocumento = driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348"));
                inputDocumento.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDocumento.sendKeys(documentoAleatorio);  // Ingresar el número de documento
                esperar(1000);  // Esperar 1 segundo
                inputDocumento.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmAfiliadoBusqueda\\:tablaRegistrosAfiliados_data")).click();
                esperar(500);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(300);
    }

    private void SeleccionarMotivoYPeriodo() throws InterruptedException {

        // PERIODO INICIAL Y FINAL PORTABILIDAD
        driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoInicial_input")).click();
        esperar(300);
        Random random = new Random();
        // Generar una fecha aleatoria dentro de la última semana
        LocalDate fechaInicial = LocalDate.now().minusDays(random.nextInt(7));
        String fechaInicialFormateada = fechaInicial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Ingresar la fecha inicial en el campo correspondiente
        WebElement campoFechaInicial = driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoInicial_input"));
        campoFechaInicial.click();
        campoFechaInicial.sendKeys(fechaInicialFormateada);
        campoFechaInicial.sendKeys(Keys.ENTER);
        esperar(300);
        // Generar una fecha final aleatoria que esté al menos un día después de la fecha inicial
        LocalDate fechaFinal = fechaInicial.plusDays(1 + random.nextInt(7)); // Genera una fecha entre 1 y 7 días después
        String fechaFinalFormateada = fechaFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Ingresar la fecha final en el campo correspondiente
        WebElement campoFechaFinal = driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoFinal_input"));
        campoFechaFinal.click();
        campoFechaFinal.sendKeys(fechaFinalFormateada);
        campoFechaFinal.sendKeys(Keys.ENTER);
        esperar(300);


        // MOTIVO PORTABILIDAD
        driver.findElement(By.cssSelector("div#frmCrear\\:motivoPotabilidad")).click();
        // Esperar hasta que las opciones sean visibles
        esperar(800);
        List<WebElement> opcionesPortabilidad = driver.findElements(By.cssSelector("ul#frmCrear\\:motivoPotabilidad_items"));
        // Seleccionar una opción aleatoria
        Random randomOpcionesMotivo = new Random();
        WebElement opcionAleatoriaMotivo = opcionesPortabilidad.get(randomOpcionesMotivo.nextInt(opcionesPortabilidad.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaMotivo.click();
        esperar(800);


        // ORIGEN
        driver.findElement(By.cssSelector("div#frmCrear\\:somOrigenCrear")).click();
        // Esperar hasta que las opciones sean visibles
        esperar(800);
        List<WebElement> opcionesOrigen = driver.findElements(By.cssSelector("ul#frmCrear\\:somOrigenCrear_items"));
        // Seleccionar una opción aleatoria
        Random randomOpcionesOrigen = new Random();
        WebElement opcionAleatoriaOrigen = opcionesOrigen.get(randomOpcionesOrigen.nextInt(opcionesOrigen.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaOrigen.click();
        esperar(800);

    }

    private void SeleccionarMunicipioYDirreccion() throws InterruptedException {

        // MUNICIPIO PORTABILIDAD
        driver.findElement(By.cssSelector("span#frmCrear\\:municipioPortabilidad")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioPortabilidad_input")).sendKeys("Mede");
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioPortabilidad_input")).sendKeys(Keys.ENTER);
        esperar(1200);

        // DIRECCION
        driver.findElement(By.cssSelector("input#frmCrear\\:txtDireccion")).click();
        esperar(300);

        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt306_label")).click();
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt306_3")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt309")).click();
        esperar(200);
        String numeroDireccion = "56";
        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt309")).sendKeys(numeroDireccion);
        esperar(300);

        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt310_label")).click();
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt310_7")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt317")).click();
        esperar(200);
        String numeroPlaca = "49";
        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt317")).sendKeys(numeroPlaca);
        esperar(300);

        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt318_label")).click();
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt318_1")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt325")).click();
        esperar(200);
        String numeroPlaca2 = "29";
        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt325")).sendKeys(numeroPlaca2);
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt327")).click();
        esperar(200);
        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt327")).sendKeys("ELITE DE LA MODA");
        esperar(1000);

        driver.findElement(By.cssSelector("button#frmDireccion\\:j_idt332")).click();
        esperar(200);
    }

    private void SeleccionarNumeroYCorreo() throws InterruptedException {

        // CELULAR
        driver.findElement(By.cssSelector("input#frmCrear\\:celular")).click();
        Random random = new Random();
        StringBuilder numeroCelular = new StringBuilder("3");
        for (int i = 0; i < 9; i++) {
            numeroCelular.append(random.nextInt(10)); // Añadir un dígito aleatorio entre 0 y 9
        }
        // Convertir el número generado a una cadena
        String numeroCelularGenerado = numeroCelular.toString();
        // Ingresar el número de celular en el campo
        WebElement campoCelular = driver.findElement(By.cssSelector("input#frmCrear\\:celular"));
        campoCelular.sendKeys(numeroCelularGenerado);
        esperar(300);

        // CORREO


    }
}

