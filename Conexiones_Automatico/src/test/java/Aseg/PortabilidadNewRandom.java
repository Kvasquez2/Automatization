import conexion.Conexion_BD;
import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PortabilidadNewRandom {

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
        // Crear una instancia de la clase Conexion_BD y obtener la conexión
        Conexion_BD conexionBD = new Conexion_BD();
        conexion = conexionBD.conectar();
    }

    // Metodo para esperar la ejecución (simular delay)
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    // Otros métodos que utilizan `driver` y `conexion`
    @Test
    public void afiliadoMismoDocumento() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al módulo de aseguramiento y la opción funcional
        asegPortabilidad();
        esperar(300);

        // Afiliado aleatorio y asignarlo a una variable para después
        String documentoAleatorio = seleccionarAfiDocumento();
        esperar(200);

        SeleccionarMotivoYPeriodo();
        esperar(200);

        SeleccionarDestino();
        esperar(200);

        // ingresar número y correo del afiliado
        SeleccionarNumeroYCorreo(documentoAleatorio);
        esperar(200);

        SeleccionarSedeYEstado();
        esperar(200);

        // Ejecutar Toda la portabilidad Con el boton de Guardar
        BotonGuardar();
        esperar(200);

        // Iniciar el temporizador de cierre después de la ejecución del último método
        iniciarTemporizadorDeCierre();

    }

    // Ingreso al módulo de Aseguramiento
    private void asegPortabilidad() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/portabilidades.faces");
        esperar(300);

        driver.findElement(By.cssSelector("button[name='frmPortabilidad:j_idt44']")).click();
        esperar(1500);
    }

    // SELECT PARA AFILIADO
    private String seleccionarAfiDocumento() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmAfiliado\\:j_idt176")).click();
        esperar(4500);

        driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348")).click();
        esperar(1000);

        String documentoAleatorio = "";
        while (documentoAleatorio.isEmpty()) {
            String query = "SELECT aseg.numero_documento FROM aseg_afiliados as aseg INNER JOIN aseg_portabilidades as porta ON aseg.id = porta.aseg_afiliados_id\n" +
                    "WHERE aseg.mae_tipo_afiliado_codigo = 101 AND aseg.mae_estado_afiliacion_valor = 'Activo' AND porta.mae_estado_portabilidad_valor not in ('Aprobada', 'En tramite') LIMIT 50;";

            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                List<String> documentos = new ArrayList<>();
                while (rs.next()) documentos.add(rs.getString("numero_documento"));

                if (!documentos.isEmpty()) {
                    documentoAleatorio = documentos.get(new Random().nextInt(documentos.size()));  // Seleccionar documento aleatorio
                } else {
                    Thread.sleep(1000);  // Esperar antes de reintentar
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!documentoAleatorio.isEmpty()) {
            WebElement inputDocumento = driver.findElement(By.name("frmAfiliadoBusqueda:tablaRegistrosAfiliados:j_idt348"));
            inputDocumento.click();
            Thread.sleep(1000);
            inputDocumento.sendKeys(documentoAleatorio);
            Thread.sleep(1000);
            inputDocumento.sendKeys(Keys.ENTER);
            Thread.sleep(300);

            driver.findElement(By.cssSelector("tbody#frmAfiliadoBusqueda\\:tablaRegistrosAfiliados_data")).click();
            Thread.sleep(500);
        }
        return documentoAleatorio;  // Retornar el documento generado
    }

    // SELECT PARA NUMERO Y CORREO DEL AFILIADO GENERADO
    private void SeleccionarNumeroYCorreo(String documentoAleatorio) throws InterruptedException {
        String ConsulNumeroYCorreo = "SELECT telefono_fijo, telefono_movil, email FROM aseg_afiliados WHERE numero_documento = '" + documentoAleatorio + "'";
        esperar(300);

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(ConsulNumeroYCorreo)) {
            if (rs.next()) {
                String telefonoFijo = rs.getString("telefono_fijo");
                String telefonoMovil = rs.getString("telefono_movil");
                String correo = rs.getString("email");

                // Ingresar los valores en los campos de la página web
                // Campo de teléfono fijo
                WebElement campoTelefonoFijo = driver.findElement(By.cssSelector("input#frmCrear\\:telefono"));
                campoTelefonoFijo.click();
                campoTelefonoFijo.sendKeys(telefonoFijo != null && !telefonoFijo.trim().isEmpty() ? telefonoFijo : "1234567890");
                esperar(300);

                // Campo de teléfono móvil
                WebElement campoTelefonoMovil = driver.findElement(By.cssSelector("input#frmCrear\\:celular"));
                campoTelefonoMovil.click();
                campoTelefonoMovil.sendKeys(telefonoMovil != null && !telefonoMovil.trim().isEmpty() ? telefonoMovil : "0987654321");
                esperar(300);

                // Campo de correo electronico
                WebElement campoCorreo = driver.findElement(By.cssSelector("input#frmCrear\\:correoElectronico"));
                campoCorreo.click();
                campoCorreo.sendKeys(correo != null && !correo.trim().isEmpty() ? correo : "Pruebas@gmail.com");
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta de contacto: " + e.getMessage());
        }

        // Ingresar la observación
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt214")).click();
        esperar(300);
        driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt214")).sendKeys("Prueba Automática de portabilidad");
        esperar(300);
    }

    // Registro Solicitud de Portabilidad
    private void SeleccionarMotivoYPeriodo() throws InterruptedException {
        // PERIODO INICIAL Y FINAL PORTABILIDAD

        // FECHA INICIAL
        driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoInicial_input")).click();
        esperar(300);
        LocalDate fechaInicial = LocalDate.now();
        String fechaInicialFormateada = fechaInicial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        WebElement campoFechaInicial = driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoInicial_input"));
        campoFechaInicial.click();
        campoFechaInicial.sendKeys(fechaInicialFormateada);
        campoFechaInicial.sendKeys(Keys.ENTER);
        esperar(300);

        // FECHA FINAL
        LocalDate fechaFinal = fechaInicial.plusDays(31 + new Random().nextInt(334)); // fecha entre 1 mes y 1 año
        String fechaFinalFormateada = fechaFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        WebElement campoFechaFinal = driver.findElement(By.cssSelector("input#frmCrear\\:calPeriodoFinal_input"));
        campoFechaFinal.click();
        campoFechaFinal.sendKeys(fechaFinalFormateada);
        campoFechaFinal.sendKeys(Keys.ENTER);
        esperar(300);


        // MOTIVO PORTABILIDAD
        driver.findElement(By.cssSelector("div#frmCrear\\:motivoPotabilidad")).click();
        esperar(800);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesMotivoPortabilidad = driver.findElements(By.cssSelector("ul#frmCrear\\:motivoPotabilidad_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_" y cuyo número sea menor a 1
        List<WebElement> opcionesMotivoFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesMotivoPortabilidad) {
            String idOpcion = opcion.getAttribute("id");
            if (idOpcion.startsWith("frmCrear:motivoPotabilidad_")) {
                int numeroOpcion = Integer.parseInt(idOpcion.substring(idOpcion.lastIndexOf('_') + 1));
                if (numeroOpcion >= 1) { // Verifica que la opción sea mayor a 1
                    opcionesMotivoFiltradas.add(opcion);
                }
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        if (!opcionesMotivoFiltradas.isEmpty()) {
            Random randomOpcionesMotivo = new Random();
            WebElement opcionAleatoriaMotivo = opcionesMotivoFiltradas.get(randomOpcionesMotivo.nextInt(opcionesMotivoFiltradas.size()));
            // Hacer clic en la opción seleccionada
            opcionAleatoriaMotivo.click();
        }
        esperar(800);


        // ORIGEN
        driver.findElement(By.cssSelector("div#frmCrear\\:somOrigenCrear")).click();
        esperar(800);
        // Leer la lista de opciones de origen
        List<WebElement> opcionesOrigen = driver.findElements(By.cssSelector("ul#frmCrear\\:somOrigenCrear_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:somOrigenCrear_" y cuyo número sea menor a 1
        List<WebElement> opcionOrigenFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesOrigen) {
            String idOpcion = opcion.getAttribute("id");
            if (idOpcion.startsWith("frmCrear:somOrigenCrear_")) {
                int numeroOpcion = Integer.parseInt(idOpcion.substring(idOpcion.lastIndexOf('_') + 1));
                if (numeroOpcion >= 1) { // Verifica que la opción sea menor a 1
                    opcionOrigenFiltradas.add(opcion);
                }
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        if (!opcionOrigenFiltradas.isEmpty()) {
            Random randomOpcionesOrigen = new Random();
            WebElement opcionAleatoriaOrigen = opcionOrigenFiltradas.get(randomOpcionesOrigen.nextInt(opcionOrigenFiltradas.size()));
            // Hacer clic en la opción seleccionada
            opcionAleatoriaOrigen.click();
        }
        esperar(800);
    }

    // SELECCIONAR MUNICIPIO Y DIRECCION PARA LA PORTABILIDAD
    private void SeleccionarDestino() throws InterruptedException {
        // MUNICIPIO
        // Lista de municipios de Antioquia
        List<String> municipiosAntioquia = Arrays.asList(
                "Medell", "Bello", "Envigado", "Itagui", "Rionegro",
                "Apartado", "Turbo", "Sabaneta", "La Estrella", "Copacabana",
                "Caucasia", "El Carmen de Vib", "Guarne", "Marinilla",
                "Girardota", "Guatape", "Santuario", "Amaga", "Caldas"
        );

        // Seleccionar aleatoriamente un municipio
        Random random = new Random();
        String municipioAleatorio = municipiosAntioquia.get(random.nextInt(municipiosAntioquia.size()));

        // Interacción con el campo de municipio
        driver.findElement(By.cssSelector("span#frmCrear\\:municipioPortabilidad")).click();
        esperar(100);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioPortabilidad_input")).sendKeys(municipioAleatorio);
        esperar(500);
        // Seleccionar la primera opción
        driver.findElement(By.cssSelector("li#frmCrear\\:municipioPortabilidad_item_0")).click();
        esperar(100);
        // Realizar el sendKeys con TAB para avanzar
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioPortabilidad_input")).sendKeys(Keys.TAB);
        esperar(100);


        // DIRECCION
        // Mapa de municipios con direcciones aleatorias
        Map<String, List<String>> direccionesPorMunicipio = new HashMap<>();
        direccionesPorMunicipio.put("Medell", Arrays.asList("Carrera 50 #30-20", "Calle 45 #80-12", "Diagonal 75 #32A-10", "Avenida 80 #33-60", "Carrera 15 #20-18"));
        direccionesPorMunicipio.put("Bello", Arrays.asList("Carrera 12 #10-20", "Calle 30 #45-12", "Avenida 25 #50-60", "Diagonal 45 #12-34", "Carrera 67 #45-89"));
        direccionesPorMunicipio.put("Envigado", Arrays.asList("Calle 10 #15-30", "Carrera 25 #18-45", "Calle 30A #25-67", "Carrera 40 #45-78", "Avenida 34 #50-12"));
        direccionesPorMunicipio.put("Itagui", Arrays.asList("Carrera 15 #22-50", "Calle 7 #18-34", "Calle 8 #25-14", "Avenida 10 #14-67", "Diagonal 17 #15-22"));
        direccionesPorMunicipio.put("Rionegro", Arrays.asList("Calle 12 #14-15", "Carrera 40 #34-20", "Avenida 50 #15-30", "Carrera 20 #10-22", "Calle 8A #22-14"));
        direccionesPorMunicipio.put("Apartado", Arrays.asList("Calle 45 #10-12", "Carrera 5 #34-21", "Avenida 4 #15-56", "Calle 33 #10-10", "Carrera 20 #45-33"));
        direccionesPorMunicipio.put("Turbo", Arrays.asList("Carrera 25 #45-10", "Calle 30 #25-14", "Diagonal 50 #12-32", "Calle 10 #22-45", "Carrera 30 #35-67"));
        direccionesPorMunicipio.put("Sabaneta", Arrays.asList("Carrera 12 #15-60", "Calle 25 #18-78", "Avenida 40 #22-14", "Calle 30 #12-34", "Carrera 19 #17-33"));
        direccionesPorMunicipio.put("La Estrella", Arrays.asList("Carrera 5 #14-30", "Calle 18 #22-14", "Avenida 50 #10-20", "Diagonal 12 #5-34", "Carrera 7 #30-15"));
        direccionesPorMunicipio.put("Copacabana", Arrays.asList("Carrera 25 #45-30", "Calle 10 #22-18", "Avenida 12 #15-40", "Diagonal 7 #10-45", "Calle 15 #5-33"));
        direccionesPorMunicipio.put("Caucasia", Arrays.asList("Carrera 14 #20-30", "Calle 34 #15-12", "Avenida 22 #14-40", "Calle 45 #10-34", "Carrera 5 #25-15"));
        direccionesPorMunicipio.put("El Carmen de Vib", Arrays.asList("Carrera 10 #15-20", "Calle 8 #14-30", "Avenida 50 #20-22", "Calle 30 #14-45", "Carrera 25 #10-12"));
        direccionesPorMunicipio.put("Guarne", Arrays.asList("Carrera 6 #10-30", "Calle 22 #12-14", "Avenida 10 #30-45", "Diagonal 5 #10-12", "Carrera 8 #25-22"));
        direccionesPorMunicipio.put("Marinilla", Arrays.asList("Carrera 7 #15-45", "Calle 10 #30-12", "Avenida 15 #14-40", "Calle 50 #12-18", "Carrera 30 #20-15"));
        direccionesPorMunicipio.put("Girardota", Arrays.asList("Calle 12 #20-30", "Carrera 10 #15-50", "Avenida 40 #15-12", "Calle 25 #18-33", "Carrera 14 #10-22"));
        direccionesPorMunicipio.put("Santa Fe de Antioquia", Arrays.asList("Carrera 9 #15-45", "Calle 40 #22-18", "Avenida 7 #30-12", "Calle 8 #22-30", "Carrera 14 #15-10"));
        direccionesPorMunicipio.put("Guatape", Arrays.asList("Calle 8 #22-15", "Carrera 9 #14-12", "Avenida 50 #10-34", "Carrera 8 #25-18", "Calle 10 #15-45"));
        direccionesPorMunicipio.put("Santuario", Arrays.asList("Carrera 10 #20-25", "Calle 7 #15-12", "Avenida 12 #30-14", "Calle 8A #10-33", "Carrera 14 #22-40"));
        direccionesPorMunicipio.put("Amaga", Arrays.asList("Calle 50 #10-12", "Carrera 12 #15-10", "Avenida 14 #22-33", "Calle 20 #12-18", "Carrera 10 #5-30"));
        direccionesPorMunicipio.put("Caldas", Arrays.asList("Carrera 5 #20-22", "Calle 12 #10-18", "Avenida 15 #20-30", "Calle 14 #18-12", "Carrera 20 #10-22"));

        // Seleccionar aleatoriamente una dirección según el municipio
        if (direccionesPorMunicipio.containsKey(municipioAleatorio)) {
            List<String> direcciones = direccionesPorMunicipio.get(municipioAleatorio);
            String direccionAleatoria = direcciones.get(random.nextInt(direcciones.size()));

            // Analizar la dirección para obtener los componentes
            String[] partes = direccionAleatoria.split(" #");
            String direccionPrincipal = partes[0];
            String[] numeroParte = partes[1].split("-");
            String numeroAntesNumeral = numeroParte[0];
            String numeroDespuesNumeral = numeroParte[1];

            // Determinar el tipo de dirección
            String tipoDireccion = "Calle"; // Valor predeterminado
            if (direccionPrincipal.contains("Avenida")) {
                tipoDireccion = "Avenida";
            } else if (direccionPrincipal.contains("Carrera")) {
                tipoDireccion = "Carrera";
            } else if (direccionPrincipal.contains("Diagonal")) {
                tipoDireccion = "Diagonal";
            } else if (direccionPrincipal.contains("Circular")) {
                tipoDireccion = "Circular";
            } else if (direccionPrincipal.contains("Transversal")) {
                tipoDireccion = "Transversal";
            }

            // Interacción con los campos de dirección
            // Seleccionar el tipo de dirección
            driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt306_label")).click();
            esperar(300);

            String tipoDireccionSelector = "";
            switch (tipoDireccion) {
                case "Avenida":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_2";
                    break;
                case "Calle":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_3";
                    break;
                case "Circular":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_4";
                    break;
                case "Carrera":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_5";
                    break;
                case "Diagonal":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_6";
                    break;
                case "Transversal":
                    tipoDireccionSelector = "li#frmDireccion\\:j_idt306_7";
                    break;
            }
            driver.findElement(By.cssSelector(tipoDireccionSelector)).click();
            esperar(300);

            // Ingresar el número antes del numeral
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt309")).click();
            esperar(200);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt309")).sendKeys(numeroAntesNumeral);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt309")).sendKeys(Keys.TAB);
            esperar(300);

            // Ingresar el número después del numeral
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt317")).click();
            esperar(200);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt317")).sendKeys(numeroDespuesNumeral);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt317")).sendKeys(Keys.TAB);
            esperar(300);

            // Ingresar el número después del guion
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt325")).click();
            esperar(200);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt325")).sendKeys(numeroParte[1]);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt325")).sendKeys(Keys.TAB);
            esperar(300);

            // Ingresar la dirección completa para validación
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt327")).click();
            esperar(200);
            driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt327")).sendKeys(direccionAleatoria);
            esperar(1000);

            // Hacer clic en el botón para guardar o enviar
            driver.findElement(By.cssSelector("button#frmDireccion\\:j_idt332")).click();
            esperar(200);
        }
    }

    // ASIGNACION DE PORTABILIDAD
    private void SeleccionarSedeYEstado() throws InterruptedException {

        // SEDE IPS ASIGNADA
        driver.findElement(By.cssSelector("span#frmCrear\\:sedeIpsPrimaria_label")).click();
        esperar(800);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesSede = driver.findElements(By.cssSelector("ul#frmCrear\\:sedeIpsPrimaria_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_" y cuyo número sea menor a 1
        List<WebElement> opcionesSedeFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesSede) {
            String idOpcion = opcion.getAttribute("id");
            if (idOpcion.startsWith("frmCrear:sedeIpsPrimaria_")) {
                int numeroOpcion = Integer.parseInt(idOpcion.substring(idOpcion.lastIndexOf('_') + 1));
                if (numeroOpcion >= 1) { // Verifica que la opción sea mayor a 1
                    opcionesSedeFiltradas.add(opcion);
                }
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        if (!opcionesSedeFiltradas.isEmpty()) {
            Random randomOpcionesMotivo = new Random();
            WebElement opcionAleatoriaSede = opcionesSedeFiltradas.get(randomOpcionesMotivo.nextInt(opcionesSedeFiltradas.size()));
            opcionAleatoriaSede.click();
        }
        esperar(800);


        // ESTADO PORTABILIDAD
        driver.findElement(By.cssSelector("span#frmCrear\\:somEstadoPortabilidad_label")).click();
        esperar(400);
        // Leer la lista de opciones de motivo de portabilidad
        List<WebElement> opcionesEstado = driver.findElements(By.cssSelector("ul#frmCrear\\:somEstadoPortabilidad_items li"));
        // Filtrar las opciones que tengan un `id` que comience con "frmCrear:motivoPotabilidad_" y cuyo número sea menor a 1
        List<WebElement> opcionesEstadoFiltradas = new ArrayList<>();
        for (WebElement opcion : opcionesEstado) {
            String idOpcion = opcion.getAttribute("id");
            if (idOpcion.startsWith("frmCrear:somEstadoPortabilidad_")) {
                int numeroOpcion = Integer.parseInt(idOpcion.substring(idOpcion.lastIndexOf('_') + 1));
                if (numeroOpcion >= 1) { // Verifica que la opción sea mayor a 1
                    opcionesEstadoFiltradas.add(opcion);
                }
            }
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        if (!opcionesEstadoFiltradas.isEmpty()) {
            Random randomOpcionesMotivo = new Random();
            WebElement opcionAleatoriaEstado = opcionesEstadoFiltradas.get(randomOpcionesMotivo.nextInt(opcionesEstadoFiltradas.size()));
            opcionAleatoriaEstado.click();
        }
        esperar(800);


        // OBSERVACION
        List<String> observaciones = Arrays.asList(
                "Prueba Automática de portabilidad",
                "Verificación de estado de portabilidad",
                "Revisión del estado de la sede IPS primaria",
                "Validación de opciones de portabilidad",
                "Comprobación del formulario de creación"
        );
        String observacionAleatoria = observaciones.get(new Random().nextInt(observaciones.size()));
        WebElement campoObservacion = driver.findElement(By.cssSelector("textarea#frmCrear\\:j_idt223"));
        campoObservacion.click();
        esperar(300);
        campoObservacion.sendKeys(observacionAleatoria);
        esperar(300);
    }

    // Ejecutar Toda la portabilidad Con el boton de Guardar
    private void BotonGuardar() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt226")).click();
    }

    private void iniciarTemporizadorDeCierre() {
        try {
            Thread.sleep(30000);  // Esperar 30 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restaurar el estado de interrupción
        } finally {
            if (driver != null) driver.quit();  // Cerrar el navegador
            System.out.println("Ejecución terminada después de 30 segundos.");
        }
    }

}

