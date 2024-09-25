import conexion.Conexion_BD;
import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.time.Duration;
import java.util.List;

public class ConsultAfiNroDoc {
    private WebDriver driver;
    private conexion.ingreso ingreso;
    private Connection conexion;
    private List<String> lista;
    private WebDriverWait wait;

    // Configuración del driver de Chrome y la conexión a la BD
    @BeforeEach
    public void setUp() {
        // Configuración del WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\IdeaProjects\\Conexiones_Automatico\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        ingreso = new ingreso(driver);  // Inicialización de la clase 'Ingreso'

        // Configuración de la conexión a la base de datos
        configurarConexionBD();
    }

    // Metodo para configurar la conexión a la BD
    private void configurarConexionBD() {
        Conexion_BD conexionBD = new Conexion_BD();
        conexion = conexionBD.conectar();
    }

    // Metodo para esperar la ejecución (simular delay)
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    // Otros métodos que utilizan `driver` y `conexion`
    @Test
    public void ConsulAfiNroDoc() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al módulo de aseguramiento y la opción funcional
        asegConsultaNroDoc();
        esperar(300);

        VerNovedades();
        esperar(300);

        VerProgEspeciales();
        esperar(300);

        VerAutorizaciones();
        esperar(300);

        // Iniciar el temporizador de cierre después de la ejecución del último método
        iniciarTemporizadorDeCierre();
    }

    // SELECCIONAR AFILIADO
    private void asegConsultaNroDoc() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/consultar_afiliado.faces");
        esperar(1000);

        driver.findElement(By.id("numeroDocumento")).click();
        esperar(300);
        WebElement inputDocumento = driver.findElement(By.id("numeroDocumento"));
        // INGRESAR NUMERO DE DOCUMENTO MANUAL MENTE
        //inputDocumento.sendKeys("1128272678"); // SIN AUTORIZACIONES
        inputDocumento.sendKeys("7889929"); // CON AUTORIZACIONES

        esperar(1000);

        // Validar si el número de documento fue diligenciado
        if (inputDocumento.getAttribute("value").isEmpty()) {
            // Si el campo está vacío
            System.out.println("NUMERO DE DOCUMENTO NO DILIGENCIADO");
        } else {
            // Si el campo tiene un valor
            System.out.println("NUMERO DE DOCUMENTO INGRESADO");
        }
        esperar(300);

        // BUTTON "Buscar"
        driver.findElement(By.id("j_idt58")).click();
        esperar(1000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_idt62_content")));

            esperar(15000);
            System.out.println("Esperando 15 segundos.");

        } catch (Exception e) {
            System.out.println("El elemento no se hizo visible en el tiempo esperado.");
        }
        System.out.printf("--- ° DATOS ° ---\n");
    }

    // NOVEDADES
    private void VerNovedades() throws InterruptedException {
        // INGRESAR PARA VER LAS NOVEDADES
        driver.findElement(By.id("frmVer:j_idt65")).click();
        System.out.println("Ingresando para ver las novedades...");
        esperar(500);

        // CLICK CAMPO
        driver.findElement(By.id("frmHistorial:tablaRegistros:j_idt308")).click();
        esperar(1000);

        // NRO DE REGISTROS EN LA TBODY
        WebElement tbody = driver.findElement(By.id("frmHistorial:tablaRegistros_data"));
        tbody.click();
        // Contar la cantidad de registros
        List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmHistorial\\:tablaRegistros_data tr"));
        int totalRegistros = registros.size();
        // Imprimir la cantidad de registros
        System.out.println("Total de registros encontrados en las NOVEDADES: " + totalRegistros);

        // ESPERAR UN TIEMPO PARA VALIDAR QUE SE CARGUEN LAS NOVEDADES
        esperar(6000);

        // CLICK CAMPO
        driver.findElement(By.id("frmHistorial:tablaRegistros:j_idt308")).click();
        esperar(1000);

        // CERRAR DE VER LAS NOVEDADES
        Actions action = new Actions(driver);
        WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
        action.moveToElement(closeButton).click().perform();
        esperar(200);
        System.out.println("Novedades visualizadas y cerradas.");
        System.out.println("--- ° ---");
    }

    // PROGRAMAS ESPECIALES
    private void VerProgEspeciales() throws InterruptedException {
        // INGRESAR PARA VER LOS PROGRAMAS ESPECIALES
        driver.findElement(By.id("frmVer:j_idt66")).click();
        System.out.println("Ingresando para ver los Programas Especiales...");
        esperar(500);

        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            System.out.println("No tiene registros disponibles en los Programas Especiales.");
            esperar(500);
        } else {
            // CLICK CAMPO
            driver.findElement(By.id("frmProgramasEspeciales:tablaRegistrosProgramas:j_idt351")).click();
            esperar(1000);

            // NRO DE REGISTROS EN LA TBODY
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmProgramasEspeciales\\:tablaRegistrosProgramas_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmProgramasEspeciales\\:tablaRegistrosProgramas_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en los Programas Especiales: " + totalRegistros);
            esperar(1000);

            // VER PROGRAMA ESPECIAL
            driver.findElement(By.id("frmProgramasEspeciales:tablaRegistrosProgramas:0:j_idt344")).click();
            esperar(3000);
            // CERRAR DE VER EL PROGRAMA ESPECIAL
            driver.findElement(By.id("frmVerPrograma:j_idt423")).click();

            // ESPERAR UN TIEMPO PARA VALIDAR QUE SE CARGUEN LAS NOVEDADES
            esperar(2000);

            // CLICK CAMPO
            driver.findElement(By.id("frmProgramasEspeciales:tablaRegistrosProgramas:j_idt351")).click();
            esperar(1000);

            // CERRAR DE VER LAS NOVEDADES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Novedades visualizadas y cerradas.");
            System.out.println("--- ° ---");
        }
    }

    // AUTORIZACIONES
    private void VerAutorizaciones() throws InterruptedException {
        // INGRESAR PARA VER LAS AUTORIZACIONES
        driver.findElement(By.id("frmVer:j_idt67")).click();
        System.out.println("Ingresando para ver las Autorizaciones...");
        esperar(500);

        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            System.out.println("No tiene registros disponibles en las Autorizaciones.");
            esperar(500);
        } else {
            // CLILC CAMPO
            driver.findElement(By.id("frmAutorizaciones:tablaRegistrosAutorizaciones:j_idt437")).click();
            esperar(800);

            // NRO DE REGISTROS EN LA TBODY
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmAutorizaciones\\:tablaRegistrosAutorizaciones_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmAutorizaciones\\:tablaRegistrosAutorizaciones_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en las Autorizaciones: " + totalRegistros);
            esperar(1000);

            // VER AUTORIZACION
            driver.findElement(By.id("frmAutorizaciones:tablaRegistrosAutorizaciones:0:j_idt428")).click();
            esperar(3000);
            // CERRAR DE VER LA AUTORIZACION
            driver.findElement(By.id("frmVerAutorizacion:j_idt1439")).click();

            // ESPERAR UN TIEMPO PARA VALIDAR QUE SE CARGUEN LAS NOVEDADES
            esperar(2000);

            // CLICK CAMPO
            driver.findElement(By.id("frmAutorizaciones:tablaRegistrosAutorizaciones:j_idt437")).click();
            esperar(1000);

            try {
                // Esperar hasta que el botón de cerrar sea clicable
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.ui-dialog-titlebar-close")));

                // Usar Actions para mover el mouse al botón y hacer clic
                Actions action = new Actions(driver);
                action.moveToElement(closeButton).click().perform();  // Simula el clic en el botón de cerrar
                esperar(200);  // Pausa breve después del clic

                System.out.println("Ventana de 'Autorizaciones' cerrada correctamente.");
                System.out.println("--- ° ---");

            } catch (Exception e) {
                System.out.println("Error al cerrar la ventana de 'Autorizaciones': " + e.getMessage());
            }
            esperar(3000);  // Pausa final opcional
        }
    }

    private void iniciarTemporizadorDeCierre() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (driver != null) driver.quit();
            System.out.println("Ejecución terminada después de 30 segundos.");
        }
    }
}
