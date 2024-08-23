package centro_Regulador;

import conexion.Conexion_BD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class general_Urgencia_Medica {

    private WebDriver driver;
    private Connection conexion;

    @BeforeEach
    public void setUp() {
        // Configuración del ChromeDriver
        configurarChromeDriver();

        // Conectar a la base de datos
        configurarConexionBD();
    }

    private void configurarChromeDriver() {
        // Establecer la ruta del ChromeDriver desde el paquete resources
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\Downloads\\pruebas-main\\pruebas-main\\Selenium-Basic\\src\\test\\resources\\chromedriver.exe");

        // Iniciar la instancia del WebDriver
        driver = new ChromeDriver();

        // Validar si el driver se inició correctamente
        if (driver != null) {
            System.out.println("ChromeDriver iniciado correctamente.");
        } else {
            System.err.println("Error al iniciar el ChromeDriver.");
        }
    }

    private void configurarConexionBD() {
        // Crear una instancia de la clase Conexion_BD y obtener la conexión
        Conexion_BD conexionBD = new Conexion_BD();
        conexion = conexionBD.conectar();

        // Validar si la conexión a la base de datos se estableció correctamente
        if (conexion != null) {
            System.out.println("Conexión a la base de datos establecida correctamente.");
        } else {
            System.err.println("Error al establecer la conexión a la base de datos.");
        }
    }

    // Otros métodos que utilizan `driver` y `conexion`

    @Test
    public void Crear_UrgenciaMedica() throws InterruptedException {
        if (conexion != null) {

            // Paso 1: Ingreso y seleccion de: Tipo de Solucitud y Canal de comunucaion
            Ingreso();
            esperar(300);// Pausa para dar tiempo a que las acciones se reflejen en la UI

            // Paso 2: Seleccion del Afiliado para la referencia
            SeleccionarAfiliado();
            esperar(1000); // Pausa para dar tiempo a que las acciones se reflejen en la UI

            // Paso 17: BOTON GUARDAR - Ejecutar prueba
            //BotonGuardar();
            esperar(300);

        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos.");
        }
    }

    // Método para esperar un tiempo específico (en milisegundos)
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    // Iniciar
    private void Ingreso() throws InterruptedException {
        // Realizar los pasos de navegación y clics en la interfaz
        driver.manage().window().setSize(new Dimension(1800, 1000));
        driver.get("http://10.250.3.66:8080/savia/login.faces");
        driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
        driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("scMlUumJ");
        driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
        esperar(1000);

        driver.get("http://10.250.3.66:8080/savia/crue/solicitudes_urgencias.faces");
        esperar(200);

        driver.findElement(By.cssSelector("button[name='frmReferencia:j_idt46']")).click();
        esperar(1800);

        // Seleccionar tipo de solicitud
        // Hacer clic en el div con las clases especificadas
        driver.findElement(By.cssSelector("div.ui-radiobutton-box.ui-widget.ui-corner-all.ui-state-default")).click();
        esperar(250);

    }

    // Seleccion el Afiliado para la Urgencia Médica
    private void SeleccionarAfiliado() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearSolicitudUrgencia\\:j_idt367")).click();
        esperar(4000);

        driver.findElement(By.name("frmAfiliadoLista:tablaRegistrosAfiliados:j_idt625")).click();
        esperar(200);

        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String documentoAleatorio = "";
        String query = "SELECT numero_documento FROM aseg_afiliados aa WHERE mae_tipo_afiliado_codigo = 101 AND mae_estado_afiliacion_valor='Activo' LIMIT 50";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> documentos = new ArrayList<>();
            while (rs.next()) {
                documentos.add(rs.getString("numero_documento"));
            }

            if (!documentos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                documentoAleatorio = documentos.get(random.nextInt(documentos.size()));
                WebElement inputDocumento = driver.findElement(By.name("frmAfiliadoLista:tablaRegistrosAfiliados:j_idt625"));
                inputDocumento.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDocumento.sendKeys(documentoAleatorio);  // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputDocumento.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmAfiliadoLista\\:j_idt617")).click();
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmAfiliadoLista\\:tablaRegistrosAfiliados_data")).click();
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(800);
    }

    // DATOS INFORMACION IPS
    private void SeleccionarIps() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearSolicitudUrgencia\\:j_idt453")).click();
        esperar(300);

        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String documentoAleatorio = "";
        String query = "SELECT p.numero_documento\n" +
                "FROM cnt_prestadores p\n" +
                "JOIN cnt_prestador_sedes s ON p.id = s.id\n" +
                "WHERE s.estado_sede = 1";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> documentos = new ArrayList<>();
            while (rs.next()) {
                documentos.add(rs.getString("numero_documento"));
            }

            if (!documentos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                documentoAleatorio = documentos.get(random.nextInt(documentos.size()));
                esperar(200);
                WebElement inputDocumento = driver.findElement(By.name("frmIpsLista:tablaRegistrosIps:j_idt669"));
                inputDocumento.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDocumento.sendKeys(documentoAleatorio);  // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputDocumento.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmIpsLista\\:j_idt658")).click();
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmIpsLista\\:tablaRegistrosIps_data")).click();
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }


    // DATOS INFORMATION PROFESSIONAL
    private void SeleccionarProfecional() throws InterruptedException {

        // TIPO DOCUMENTO
        driver.findElement(By.cssSelector("span#frmCrearSolicitudUrgencia\\:somTipoDocumentoPersona_label")).click();
        esperar(300);
        driver.findElement(By.cssSelector("li#frmCrearSolicitudUrgencia\\:somTipoDocumentoPersona_2")).click();
        esperar(500);

        // NUMERO DOCUMENTO
        driver.findElement(By.cssSelector("input#frmCrearSolicitudUrgencia\\:txtDocumentoPro")).click();
        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String documentoAleatorio = "";
        String query = "SELECT documento FROM cnt_profesionales cp WHERE mae_tipo_documento_valor = 'Cedula Ciudadania'";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> documentos = new ArrayList<>();
            while (rs.next()) {
                documentos.add(rs.getString("documento"));
            }

            if (!documentos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                documentoAleatorio = documentos.get(random.nextInt(documentos.size()));
                esperar(200);
                driver.findElement(By.cssSelector("input#frmCrearSolicitudUrgencia\\:txtDocumentoPro")).click();
                esperar(1000);  // Esperar 1 segundo

                driver.findElement(By.cssSelector("input#frmCrearSolicitudUrgencia\\:txtDocumentoPro")).sendKeys(documentoAleatorio); // Ingresar el número de documento
                esperar(1000);  // Esperar 1 segundo

                driver.findElement(By.cssSelector("input#frmCrearSolicitudUrgencia\\:txtDocumentoPro")).sendKeys(Keys.ENTER); // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("input#frmCrearSolicitudUrgencia\\:txtPrimerNombrePro")).click();
                esperar(100);
            }

        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    // DATOS - ORIGEN ATENCION
    private void SeleccionarOrigen() throws InterruptedException {
        // Seleccionar Origen de atencion
        driver.findElement(By.cssSelector("span#frmCrearSolicitudUrgencia\\:somOrigenA_label")).click();
        esperar(800);

        // Leer la lista de opciones
        List<WebElement> opcionesOrigen = driver.findElements(By.cssSelector("ul#frmCrearSolicitudUrgencia\\:somOrigenA_items li"));
        esperar(200);
        // Filtrar opciones válidas (excluir "frmCrearSolicitudUrgencia:somOrigenA_0")
        List<WebElement> opcionesFiltradasOrigen = new ArrayList<>();
        for (WebElement opcion : opcionesOrigen) {
            // Excluir la opción con ID 'frmCrearSolicitudUrgencia:somOrigenA_0'
            if (!opcion.getAttribute("id").equals("frmCrearSolicitudUrgencia:somOrigenA_0")) {
                opcionesFiltradasOrigen.add(opcion);
            }
        }
        // Validar que haya opciones válidas disponibles
        if (opcionesFiltradasOrigen.isEmpty()) {
            throw new IllegalStateException("No hay opciones válidas disponibles para seleccionar.");
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        Random randomOpcionesOrigen = new Random();
        WebElement opcionAleatoriaOrigen = opcionesFiltradasOrigen.get(randomOpcionesOrigen.nextInt(opcionesFiltradasOrigen.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaOrigen.click();
        esperar(400);
    }


}
