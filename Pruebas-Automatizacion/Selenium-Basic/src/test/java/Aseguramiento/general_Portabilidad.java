package Aseguramiento;

import conexion.Conexion_BD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class general_Portabilidad {

    private WebDriver driver;
    private Connection conexion;

    // métodos de conexion 'driver' y 'dbeaver'
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
    public void PortabilidadGenerico() throws InterruptedException {
        if (conexion != null) {
            // Paso 1: Seleccionar Afiliado
            SeleccionarAfiliado();
            esperar(300); // Pausa para dar tiempo a que las acciones se reflejen en la UI

            // Paso 2: Realizar Consulta a la Base de Datos
            String numeroDocumentoFinal = realizarConsultaBD();
            esperar(300);

            // Paso 3: Seleccionar Municipio y Dirrecion
            SeleccionarMunicipioYDirreccion();
            esperar(300);

            // Paso 4: Seleccionar Municipio y Dirrecion
            SeleccionarSede();
            esperar(300);

            // Paso 5: Seleccionar Motivo, Periodo y Origen
            SeleccionarMotivoYPeriodo();
            esperar(300);

            // Paso 6: Seleccionar Celular, Telefono y Correo
            SeleccionarNumeroYCorreo(numeroDocumentoFinal);
            esperar(300);

            // Paso 7: Seleccionar Estado Portabilidad y Observacion
            SeleccionarEstado_Observacion();
            esperar(300);

            // Paso 8: Boton Guardar - Ejecutar prueba
            BotonGuardar();
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

    private String realizarConsultaBD() throws InterruptedException {
        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String documentoAleatorio = "";
        System.out.printf("Realizando Consulta");
        String query = "SELECT numero_documento, afiliacion_ubicaciones_id FROM aseg_afiliados aa \n" +
                "WHERE mae_tipo_afiliado_codigo = 101 AND mae_estado_afiliacion_valor='Activo' \n" +
                "AND afiliacion_ubicaciones_id != 3 LIMIT 50";
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
        return documentoAleatorio;
    }

    // Registro Solicitud de Portabilidad
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
        esperar(800);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesPortabilidad = driver.findElements(By.cssSelector("ul#frmCrear\\:motivoPotabilidad_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_"
        List<WebElement> opcionesFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesPortabilidad) {
            if (opcion.getAttribute("id").startsWith("frmCrear:motivoPotabilidad_")) {
                opcionesFiltradas.add(opcion);
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        Random randomOpcionesMotivo = new Random();
        WebElement opcionAleatoriaMotivo = opcionesFiltradas.get(randomOpcionesMotivo.nextInt(opcionesFiltradas.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaMotivo.click();
        esperar(800);


        // ORIGEN
        driver.findElement(By.cssSelector("div#frmCrear\\:somOrigenCrear")).click();
        esperar(800);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesOrigen = driver.findElements(By.cssSelector("ul#frmCrear\\:somOrigenCrear_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_"
        List<WebElement> opcionFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesOrigen) {
            if (opcion.getAttribute("id").startsWith("frmCrear:somOrigenCrear_")) {
                opcionFiltradas.add(opcion);
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        Random randomOpcionesOrigen = new Random();
        WebElement opcionAleatoriaOrigen = opcionFiltradas.get(randomOpcionesOrigen.nextInt(opcionFiltradas.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaOrigen.click();
        esperar(800);

    }

    private void SeleccionarMunicipioYDirreccion() throws InterruptedException {

        // MUNICIPIO PORTABILIDAD
        driver.findElement(By.cssSelector("span#frmCrear\\:municipioPortabilidad")).click();
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioPortabilidad_input")).sendKeys("Mede");
        esperar(400);
        driver.findElement(By.cssSelector("li#frmCrear\\:municipioPortabilidad_item_0")).click();
        esperar(600);

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

    private void SeleccionarNumeroYCorreo(String numeroDocumento) throws InterruptedException {

        // Realizar la consulta a la base de datos y seleccionar un número y correo
        System.out.printf("\n Realizando Consulta, Espere ...");
        String ConsulNumeroYCorreo = "SELECT telefono_fijo, telefono_movil, email FROM aseg_afiliados aa WHERE numero_documento = " + numeroDocumento + ";";
        esperar(300);

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(ConsulNumeroYCorreo)) {

            // Verificar si la consulta devuelve algún resultado
            if (rs.next()) {
                // Extraer los valores del teléfono fijo, móvil y correo electrónico
                String telefonoFijo = rs.getString("telefono_fijo");
                String telefonoMovil = rs.getString("telefono_movil");
                String correo = rs.getString("email");

                // TELEFONO FIJO
                WebElement campoTelefonoFijo = driver.findElement(By.cssSelector("input#frmCrear\\:telefono"));
                campoTelefonoFijo.click(); // Hacer clic en el campo de teléfono fijo
                campoTelefonoFijo.sendKeys(telefonoFijo); // Ingresar el número de teléfono fijo
                esperar(300);

                // TELEFONO MOVIL
                WebElement campoTelefonoMovil = driver.findElement(By.cssSelector("input#frmCrear\\:celular"));
                campoTelefonoMovil.click(); // Hacer clic en el campo de teléfono móvil
                campoTelefonoMovil.sendKeys(telefonoMovil); // Ingresar el número de teléfono móvil
                esperar(300);

                // CORREO ELECTRONICO
                WebElement campoCorreo = driver.findElement(By.cssSelector("input#frmCrear\\:correoElectronico"));
                campoCorreo.click(); // Hacer clic en el campo de correo electrónico
                if (correo == null || correo.trim().isEmpty()) {
                    // Si el correo está vacío o es nulo, ingresar un correo alternativo
                    campoCorreo.sendKeys("Pruebas@gmail.com");
                } else {
                    // Si el correo tiene un valor, ingresarlo en el campo
                    campoCorreo.sendKeys(correo);
                }
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta de contacto: " + e.getMessage());
        }

        // OBSERVACION
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt214")).click();
        esperar(300);
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt214")).sendKeys("Prueba Automática de portabilidad");
        esperar(300);
    }

    //Asignación de Portabilidad
    private void SeleccionarSede() throws InterruptedException {

        // SEDE IPS ASIGNADA
        driver.findElement(By.cssSelector("span#frmCrear\\:sedeIpsPrimaria_label")).click();
        esperar(300);
        driver.findElement(By.cssSelector("li#frmCrear\\:sedeIpsPrimaria_1")).click();
        esperar(300);
        System.out.printf("\n Seleccion nombre sede ");
    }

    private void SeleccionarEstado_Observacion() throws InterruptedException {

        // ESTADO PORTABILIDAD
        driver.findElement(By.cssSelector("div#frmCrear\\:somEstadoPortabilidad")).click();
        esperar(800);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesPortabilidad = driver.findElements(By.cssSelector("ul#frmCrear\\:somEstadoPortabilidad_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_"
        List<WebElement> opcionFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesPortabilidad) {
            if (opcion.getAttribute("id").startsWith("frmCrear:somEstadoPortabilidad_")&& !opcion.getText().equals("0")) {
                opcionFiltradas.add(opcion);
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        Random randomOpcionesPortabilidad = new Random();
        WebElement opcionAleatoriaPortabilidad = opcionFiltradas.get(randomOpcionesPortabilidad.nextInt(opcionFiltradas.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaPortabilidad.click();
        esperar(800);


        // OBSERVACION
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt223")).click();
        esperar(300);
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt223")).sendKeys("Prueba Automática de portabilidad");
        esperar(300);
    }

    // Ejecutar Toda la portabilidad Con el boton de Guardar
    private void BotonGuardar() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt226")).click();
    }
}
