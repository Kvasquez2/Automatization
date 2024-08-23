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
import java.util.*;

public class general_ContraReferencia {

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
    public void Crear_ContraReferencia() throws InterruptedException {
        if (conexion != null) {

            // Paso 1: Ingreso y seleccion de: Tipo de Solucitud y Canal de comunucaion
            Ingreso();
            esperar(300);// Pausa para dar tiempo a que las acciones se reflejen en la UI

            // Paso 2: Seleccion del Afiliado para la referencia
            SeleccionarAfiliado();
            esperar(1000); // Pausa para dar tiempo a que las acciones se reflejen en la UI

            //Paso 3: DATOS DEL ACOMPAÑANTE
            SeleccionarTipoYDocumento();
            esperar(300);

            SeleccionarNombresYApellidos();
            esperar(300);

            SeleccionarTelefonoYDireccionYCiudad();
            esperar(300);

            //Paso 4: DATOS DE IPS
            SeleccionarIps();
            esperar(300);

            // Paso 5: DATOS PROFECIONAL
            SeleccionarProfecional();
            esperar(300);

            // Paso 6: DATOS ESPECIALIDAD
            SeleccionarEspecialidad();
            esperar(300);

            // Paso 7: DATOS SERVICIOS
            SeleccionarServicios();
            esperar(300);

            // Paso 8: DATOS INFORMANTE
            SeleccionarInformante();
            esperar(300);

            // Paso 9: DATOS CLINICA
            SeleccionarInfoClinical();
            esperar(300);

            // Paso 10: DATOS ANTECEDENTES
            SeleccionarInfoAntecedentes();
            esperar(300);

            // Paso 11: DATOS SIGNOS VITALES
            SeleccionarSignos();
            esperar(300);

            // Paso 12: DATOS PESO Y TALLA
            SeleccionarPesoYTalla();
            esperar(300);

            // Paso 13: DATOS EXAMENES
            SeleccionarHallazgosExamenes();
            esperar(300);

            // Paso 14: DATOS DIAGNOSTICO
            SeleccionarDiagnosticos();
            esperar(300);

            // Paso 15: DATOS TRIAGE
            SeleccionarTriage();
            esperar(300);

            // Paso 16: DATOS MOTIVO
            SeleccionarMotivo();
            esperar(300);

            // Paso 17: BOTON GUARDAR - Ejecutar prueba
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

    // Iniciar
    private void Ingreso() throws InterruptedException {
        // Realizar los pasos de navegación y clics en la interfaz
        driver.manage().window().setSize(new Dimension(1800, 1000));
        driver.get("http://10.250.3.66:8080/savia/login.faces");
        driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
        driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("scMlUumJ");
        driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
        esperar(1000);

        driver.get("http://10.250.3.66:8080/savia/crue/referencia_contraref.faces");
        esperar(200);

        driver.findElement(By.cssSelector("button[name='frmReferencia:j_idt52']")).click();
        esperar(1500);

        // Seleccionar tipo de solicitud
        // Hacer clic en el div con las clases especificadas
        driver.findElement(By.cssSelector("div.ui-radiobutton-box.ui-widget.ui-corner-all.ui-state-default")).click();
        esperar(250);
        // Hacer clic en el ícono del botón de radio que tiene la clase específica
        driver.findElement(By.cssSelector(".ui-radiobutton-icon.ui-icon.ui-c.ui-icon-blank")).click();
        esperar(500);


        // Seleccionar Canal de Comunicacion
        driver.findElement(By.cssSelector("div#frmCrearAfiliado\\:somCanal")).click();
        esperar(800);
        // Leer la lista de opciones del canal de comunicacion
        List<WebElement> opcionesComunicacion = driver.findElements(By.cssSelector("ul#frmCrearAfiliado\\:somCanal_items li"));
        esperar(200);
        // Filtrar opciones válidas (excluir "frmCrearAfiliado:somCanal_0")
        List<WebElement> opcionesFiltradasComunicacion = new ArrayList<>();
        for (WebElement opcion : opcionesComunicacion) {
            // Excluir la opción con id 'frmCrearAfiliado:somCanal_0'
            if (!opcion.getAttribute("id").equals("frmCrearAfiliado:somCanal_0")) {
                opcionesFiltradasComunicacion.add(opcion);
            }
        }
        // Validar que haya opciones válidas disponibles
        if (opcionesFiltradasComunicacion.isEmpty()) {
            throw new IllegalStateException("No hay opciones válidas disponibles para seleccionar.");
        }
        // Seleccionar una opción aleatoria de las opciones filtradas
        Random randomOpcionesComunicacion = new Random();
        WebElement opcionAleatoriaCanal = opcionesFiltradasComunicacion.get(randomOpcionesComunicacion.nextInt(opcionesFiltradasComunicacion.size()));
        // Hacer clic en la opción seleccionada
        opcionAleatoriaCanal.click();
        esperar(400);
    }

    // Seleccion el Aifliado para la referencia
    private void SeleccionarAfiliado() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt542")).click();
        esperar(4000);

        driver.findElement(By.name("frmAfiliadoLista:tablaRegistrosAfiliados:j_idt2085")).click();
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
                WebElement inputDocumento = driver.findElement(By.name("frmAfiliadoLista:tablaRegistrosAfiliados:j_idt2085"));
                inputDocumento.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDocumento.sendKeys(documentoAleatorio);  // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputDocumento.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmAfiliadoLista\\:j_idt2077")).click();
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmAfiliadoLista\\:tablaRegistrosAfiliados_data")).click();
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(800);
    }

    // DATOS INFORMACION ACOMPAÑANTE
    // Seleccionar tipo documento y numero de documento
    private void SeleccionarTipoYDocumento() throws InterruptedException {
        // TIPO DE DOCUMENTO
        driver.findElement(By.cssSelector("span#frmCrearAfiliado\\:somTipoDocumento_label")).click();
        esperar(100);
        driver.findElement(By.cssSelector("li#frmCrearAfiliado\\:somTipoDocumento_3")).click();
        esperar(200);

        // NUMERO DE DOCUMENTO
        Random random = new Random();
        String randomNumberStr;

        while (true) {
            // Decidir si el número tendrá 8 o 10 dígitos
            int numberOfDigits = random.nextBoolean() ? 8 : 10;
            long minValue = (long) Math.pow(10, numberOfDigits - 1);
            long maxValue = (long) Math.pow(10, numberOfDigits) - 1;
            long randomNumber = minValue + (long) (random.nextDouble() * (maxValue - minValue));
            randomNumberStr = String.valueOf(randomNumber);
            // Verificar si el número es de 8 o 10 dígitos y no inicia con '0'
            if ((randomNumberStr.length() == 8 || randomNumberStr.length() == 10) && randomNumberStr.charAt(0) != '0') {
                break;
            }
        }
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:numeroDocumento")).sendKeys(randomNumberStr);
    }

    private void SeleccionarNombresYApellidos() throws InterruptedException {
        // NOMBRES
        ArrayList<String> nombresMujeres = new ArrayList<>(Arrays.asList(
                "María", "Laura", "Ana", "Sofía", "Carmen", "Isabella", "Lucía", "Elena", "Valeria", "Marta"
        ));
        ArrayList<String> nombresHombres = new ArrayList<>(Arrays.asList(
                "Juan", "Carlos", "Miguel", "David", "José", "Alejandro", "Javier", "Daniel", "Luis", "Andrés"
        ));
        Random random = new Random();
        // Decidir aleatoriamente si usar nombres de mujeres o de hombres
        boolean esMujer = random.nextBoolean();
        // Seleccionar el nombre 1 y nombre 2 de la misma lista
        String nombre1, nombre2;
        ArrayList<String> listaSeleccionada;
        if (esMujer) {
            nombre1 = nombresMujeres.get(random.nextInt(nombresMujeres.size()));
            nombre2 = nombresMujeres.get(random.nextInt(nombresMujeres.size()));
            listaSeleccionada = nombresMujeres;
        } else {
            nombre1 = nombresHombres.get(random.nextInt(nombresHombres.size()));
            nombre2 = nombresHombres.get(random.nextInt(nombresHombres.size()));
            listaSeleccionada = nombresHombres;
        }
        // Ingresar el nombre 1 (siempre se llena)
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:nombreP")).sendKeys(nombre1);
        esperar(400);
        // Verificar si el nombre 1 fue ingresado correctamente
        String valorIngresadoNombre1 = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:nombreP")).getAttribute("value");
        if (valorIngresadoNombre1 == null || valorIngresadoNombre1.isEmpty()) {
            // Si el nombre 1 es null o vacío, seleccionar un nombre al azar de la lista y volver a intentar
            nombre1 = listaSeleccionada.get(random.nextInt(listaSeleccionada.size()));
            driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:nombreP")).clear();  // Limpiar el campo antes de volver a ingresar
            driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:nombreP")).sendKeys(nombre1);
            esperar(400);
        }
        // Ingresar el nombre 2 (solo si es diferente de nombre 1)
        if (!nombre1.equals(nombre2)) {
            driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:nombreS")).sendKeys(nombre2);
            esperar(400);
        }

        // APELLIDOS
        ArrayList<String> lastNames = new ArrayList<>(Arrays.asList(
                "García", "Martínez", "Lopez", "Rodríguez", "Perez", "Sanchez", "Ramirez", "Gonzalez", "Fernandez", "Hernandez",
                "Torres", "Gomez", "Díaz", "Vazquez", "Alvarez", "Castro", "Morales", "Ruiz", "Mendoza", "Ortiz"
        ));
        Collections.shuffle(lastNames);
        String apellido1 = lastNames.get(0);
        String apellido2 = lastNames.get(1);

        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:apellidoP")).sendKeys(apellido1);
        esperar(400);
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:apellidoS")).sendKeys(apellido2);
        esperar(400);
    }

    private void SeleccionarTelefonoYDireccionYCiudad() throws InterruptedException {
        // Generar un número de teléfono colombiano (10 dígitos, iniciando con 3)
        Random random = new Random();
        String numeroTelefonoColombia = "3" + (random.nextInt(10) + 0) + String.format("%08d", random.nextInt(100000000));
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:j_idt630_input")).click();
        esperar(400);
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:j_idt630_input")).sendKeys(numeroTelefonoColombia);
        esperar(300);
        // Generar una dirección aleatoria en Antioquia
        String[] direccionesAntioquia = {
                "Calle 10 #20-30, Mede",
                "Carrera 45 #56-78, Bello",
                "Avenida 33 #12-34, Envigado",
                "Calle 50 #30-20, Itagu",
                "Carrera 70 #40-50, Sabaneta",
                "Avenida Oriental #15-55, Rionegro",
                "Calle 12 Sur #13-14, La Estrella",
                "Carrera 80 #25-35, Copacabana"
        };
        String direccionAleatoria = direccionesAntioquia[random.nextInt(direccionesAntioquia.length)];
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:j_idt632")).sendKeys(direccionAleatoria);
        // Extraer la ciudad de la dirección generada
        String ciudad = direccionAleatoria.split(",")[1].trim();
        // Ingresar la ciudad en el campo correspondiente y presionar Enter
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:municipioAfiliacion_input")).sendKeys(ciudad);
        esperar(800);
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:municipioAfiliacion_input")).sendKeys(Keys.ENTER);
        esperar(300);
    }

    // DATOS INFORMACION IPS
    private void SeleccionarIps() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt637")).click();
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
                WebElement inputDocumento = driver.findElement(By.name("frmIpsLista:tablaRegistrosIps:j_idt2154"));
                inputDocumento.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDocumento.sendKeys(documentoAleatorio);  // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputDocumento.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmIpsLista\\:j_idt2143")).click();
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
        driver.findElement(By.cssSelector("span#frmCrearAfiliado\\:somTipoDocumentoPersona_label")).click();
        esperar(300);
        driver.findElement(By.cssSelector("li#frmCrearAfiliado\\:somTipoDocumentoPersona_2")).click();
        esperar(500);

        // NUMERO DOCUMENTO
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtDocumentoPro")).click();
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
                driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtDocumentoPro")).click();
                esperar(1000);  // Esperar 1 segundo

                driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtDocumentoPro")).sendKeys(documentoAleatorio); // Ingresar el número de documento
                esperar(1000);  // Esperar 1 segundo

                driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtDocumentoPro")).sendKeys(Keys.ENTER); // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("span#frmCrearAfiliado\\:pnlPrimerNombreProfe")).click();
                esperar(100);
            }

        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    private void SeleccionarEspecialidad() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt681")).click();
        esperar(500);

        // Hacer clic en el primer elemento
        WebElement inputField = driver.findElement(By.cssSelector("input#frmEspecialidadBusqueda\\:tablaRegistrosEspecialidades\\:j_idt2338"));
        inputField.click();

        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String codigoAleatorio = "";
        String query = "SELECT codigo FROM ma_especialidades me WHERE activo = 1";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> codigo = new ArrayList<>();
            while (rs.next()) {
                codigo.add(rs.getString("codigo"));
            }

            if (!codigo.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                codigoAleatorio = codigo.get(random.nextInt(codigo.size()));
                WebElement inputCodigo = driver.findElement(By.name("frmEspecialidadBusqueda:tablaRegistrosEspecialidades:j_idt2338"));
                inputCodigo.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputCodigo.sendKeys(codigoAleatorio); // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputCodigo.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmEspecialidadBusqueda\\:j_idt2335")).click();
                esperar(300);


                driver.findElement(By.cssSelector("tbody#frmEspecialidadBusqueda\\:tablaRegistrosEspecialidades_data")).click();
                esperar(100);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    // DATOS INFORMACIÓN SERVICIO
    private void SeleccionarServicios() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt695")).click();
        esperar(500);

        // Hacer clic en el primer elemento
        WebElement inputField = driver.findElement(By.cssSelector("input#frmServiciosHabilitacionBusqueda\\:tablaRegistrosServiciosHabilitacion\\:j_idt2354"));
        inputField.click();

        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String codigoAleatorio = "";
        String query = "SELECT codigo FROM ma_servicios_habilitacion msh WHERE activo = 1";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> codigo = new ArrayList<>();
            while (rs.next()) {
                codigo.add(rs.getString("codigo"));
            }

            if (!codigo.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                codigoAleatorio = codigo.get(random.nextInt(codigo.size()));
                WebElement inputCodigo = driver.findElement(By.name("frmServiciosHabilitacionBusqueda\\:tablaRegistrosServiciosHabilitacion\\:j_idt2354"));
                inputCodigo.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputCodigo.sendKeys(codigoAleatorio); // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputCodigo.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmServiciosHabilitacionBusqueda\\:j_idt2351")).click();
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmServiciosHabilitacionBusqueda\\:tablaRegistrosServiciosHabilitacion_data")).click();
                esperar(100);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    // DATOS INFORMACIÓN DEL INFORMANTE
    private void SeleccionarInformante() throws InterruptedException {
        driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtNombreInf")).click();
        esperar(500);

        // Nombres aleatorios para usar en el campo
        String[] nombres = {"Juan", "Ana", "Carlos", "Maria", "Pedro", "Luisa"};
        Random random = new Random();

        // Seleccionar un nombre aleatorio
        String nombreAleatorio = nombres[random.nextInt(nombres.length)];

        // Hacer clic en el campo de nombre y colocar el nombre aleatorio
        WebElement nombreField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtNombreInf"));
        nombreField.click();
        nombreField.sendKeys(nombreAleatorio);

        // Cargos aleatorios para usar en el campo
        String[] cargos = {"Gerente", "Analista", "Desarrollador", "Asesor", "Coordinador", "Director"};

        // Seleccionar un cargo aleatorio
        String cargoAleatorio = cargos[random.nextInt(cargos.length)];

        // Hacer clic en el campo de cargo y colocar el cargo aleatorio
        WebElement cargoField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtCargoInf"));
        cargoField.click();
        cargoField.sendKeys(cargoAleatorio);

        // Generar un número de teléfono colombiano aleatorio
        int numeroTelefono = 300000000 + random.nextInt(10000000); // Número de 10 dígitos que comienza con 3

        // Hacer clic en el campo de teléfono y colocar el número generado
        WebElement telefonoField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtTelefonoInf_input"));
        telefonoField.click();
        telefonoField.sendKeys(String.valueOf(numeroTelefono));
    }

    // DATOS INFORMACION CLINICA RELEVANTE
    private void SeleccionarInfoClinical() throws InterruptedException {
        WebElement cargoField = driver.findElement(By.cssSelector("textarea#frmCrearAfiliado\\:txtaEnfermedad"));
        cargoField.click();
        esperar(400);
        cargoField.sendKeys("El Paciente anteriormente se ha encontrado hospitalizado con grandes problemas \n" +
                "de respiracion, llegando a tener un edema pulmonal");
        esperar(300);
    }

    // DATOS INFORMACION ANTECEDENTES
    private void SeleccionarInfoAntecedentes() throws InterruptedException {
        WebElement cargoField = driver.findElement(By.cssSelector("textarea#frmCrearAfiliado\\:txtaAntecedentes"));
        cargoField.click();
        esperar(400);
        cargoField.sendKeys("El Paciente anteriormente se ha encontrado hospitalizado con grandes problemas \n" +
                "de respiracion, llegando a tener un edema pulmonal");
        esperar(300);
    }

    // DATOS INFORMACION SIGNOS VITALES
    private void SeleccionarSignos() throws InterruptedException {
        // Inicializador para la variable Random
        Random random = new Random();

        try {
            // TEMPERATURA: Genera una temperatura aleatoria entre 35.0 y 42.0 grados Celsius
            double temperatura = 35.0 + (7.0) * random.nextDouble(); // Rango de 35.0 a 42.0
            WebElement temperaturaField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:tnTemperatura_input"));
            temperaturaField.click();
            temperaturaField.sendKeys(String.format("%.1f", temperatura)); // Formato con 1 decimal
            esperar(300);

            // FRECUENCIA CARDIACA: Genera un valor aleatorio entre 60 y 100 latidos por minuto
            int frecuenciaCardiaca = 60 + random.nextInt(41); // Rango de 60 a 100
            WebElement frecuenciaCardiacaField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtFrecCard_input"));
            frecuenciaCardiacaField.click();
            frecuenciaCardiacaField.sendKeys(String.valueOf(frecuenciaCardiaca));
            esperar(300);

            // FRECUENCIA RESPIRATORIA: Genera un valor aleatorio entre 12 y 20 respiraciones por minuto
            int frecuenciaRespiratoria = 12 + random.nextInt(9); // Rango de 12 a 20
            WebElement frecuenciaRespiratoriaField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtFrecRespi_input"));
            frecuenciaRespiratoriaField.click();
            frecuenciaRespiratoriaField.sendKeys(String.valueOf(frecuenciaRespiratoria));
            esperar(300);

            // TENSION ARTERIAL SISTOLICA: Genera un valor aleatorio entre 90 y 140 mm Hg
            int tensionArterialSistolica = 90 + random.nextInt(51); // Rango de 90 a 140
            WebElement tensionArterialSField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtTensionArtS_input"));
            tensionArterialSField.click();
            tensionArterialSField.sendKeys(String.valueOf(tensionArterialSistolica));
            esperar(300);

            // TENSION ARTERIAL DIASTOLICA: Genera un valor aleatorio entre 60 y 90 mm Hg
            int tensionArterialDiastolica = 60 + random.nextInt(31); // Rango de 60 a 90
            WebElement tensionArterialDField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtTensionArtD_input"));
            tensionArterialDField.click();
            tensionArterialDField.sendKeys(String.valueOf(tensionArterialDiastolica));
            esperar(300);

            // SATURACION DE OXIGENO: Genera un valor aleatorio entre 95 y 100%
            int saturacionOxigeno = 95 + random.nextInt(6); // Rango de 95% a 100%
            WebElement saturacionOxigenoField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtSaturacionOxi_input"));
            saturacionOxigenoField.click();
            saturacionOxigenoField.sendKeys(String.valueOf(saturacionOxigeno));
            esperar(300);

        } finally {

        }

    }

    // DATOS INFORMACION ANTROPOMETRICOS
    private void SeleccionarPesoYTalla() throws InterruptedException {
        Random random = new Random();
        try {

            // PESO
            int peso = 50 + random.nextInt(51); // Genera un valor entre 50 y 100 kg
            WebElement pesoField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtPeso_input"));
            pesoField.click();
            pesoField.sendKeys(String.valueOf(peso));
            esperar(300);

            // TALLA
            int talla = 150 + random.nextInt(51); // Genera un valor entre 150 y 200 cm
            WebElement tallaField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtTalla_input"));
            tallaField.click();
            tallaField.sendKeys(String.valueOf(talla));
            esperar(300);
            tallaField.sendKeys(Keys.ENTER);
        } finally {

        }
    }

    // DATOS INFORMACION
    private void SeleccionarHallazgosExamenes() throws InterruptedException {
        Random random = new Random();
        // Lista de descripciones posibles para el suceso
        String[] descripciones = {
                "Paciente consciente, orientado en tiempo y espacio.",
                "Paciente en estado somnoliento, responde a estimulos verbales.",
                "Paciente con perdida temporal de la conciencia.",
                "Paciente en coma, sin respuesta a estimulos.",
                "Paciente agitado, con respuestas incoherentes.",
                "Paciente presenta perdida parcial de memoria.",
                "Paciente responde a estimulos dolorosos pero no verbales."
        };
        try {
            // ESCALA GLASGOW: Genera un número aleatorio entre 3 y 15
            int escalaGlasgow = 3 + random.nextInt(13); // Genera un valor entre 3 y 15
            WebElement glasgowField = driver.findElement(By.cssSelector("input#frmCrearAfiliado\\:txtglasglow_input"));
            glasgowField.click();
            glasgowField.sendKeys(String.valueOf(escalaGlasgow));
            esperar(300);

            // DESCRIPCIÓN: Selecciona una descripción aleatoria de la lista
            String descripcion = descripciones[random.nextInt(descripciones.length)];
            WebElement descripcionField = driver.findElement(By.cssSelector("textarea#frmCrearAfiliado\\:txtaDescrip"));
            descripcionField.click();
            descripcionField.sendKeys(descripcion);
            esperar(300);

        } finally {
        }
    }

    // DIAGNOSTICOS
    private void SeleccionarDiagnosticos() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt804")).click();
        esperar(300);

        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String diagnosticoAleatorio = "";
        String query = "SELECT codigo FROM ma_diagnosticos md WHERE activo = 1";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> diagnosticos = new ArrayList<>();
            while (rs.next()) {
                diagnosticos.add(rs.getString("codigo"));
            }

            if (!diagnosticos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                diagnosticoAleatorio = diagnosticos.get(random.nextInt(diagnosticos.size()));
                esperar(200);
                WebElement inputDiagnostico = driver.findElement(By.name("frmDiagnosticoBusqueda:tablaRegistrosDiagnoticos:j_idt2325"));
                inputDiagnostico.click();  // Hacer clic en el campo
                esperar(1000);  // Esperar 1 segundo

                inputDiagnostico.sendKeys(diagnosticoAleatorio);  // Ingresar el número de documento

                esperar(1000);  // Esperar 1 segundo
                inputDiagnostico.sendKeys(Keys.ENTER);  // Simular ENTER
                esperar(800);

                driver.findElement(By.cssSelector("button#frmDiagnosticoBusqueda\\:j_idt2316")).click();
                esperar(300);

                driver.findElement(By.cssSelector("tbody#frmDiagnosticoBusqueda\\:tablaRegistrosDiagnoticos_data")).click();
                esperar(300);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    // TRIAGE
    private void SeleccionarTriage() throws InterruptedException {
        Random random = new Random();
        // Lista de opciones posibles
        String[] opciones = {
                "frmCrearAfiliado:somTriage_1",
                "frmCrearAfiliado:somTriage_2",
                "frmCrearAfiliado:somTriage_3",
                "frmCrearAfiliado:somTriage_4",
                "frmCrearAfiliado:somTriage_5"
        };
        try {
            // Hacer clic en el menú desplegable de triage
            WebElement triageDropdown = driver.findElement(By.cssSelector("span#frmCrearAfiliado\\:somTriage_label"));
            triageDropdown.click();
            esperar(300);

            // Seleccionar una opción aleatoria de la lista
            String opcionSeleccionada = opciones[random.nextInt(opciones.length)];
            WebElement opcionElement = driver.findElement(By.id(opcionSeleccionada));
            opcionElement.click();

            // Esperar un tiempo después de seleccionar la opción
            esperar(1000); // Espera de 1 segundo

        } finally {
        }
    }

    // MOTIVO REMISION
    private void SeleccionarMotivo() throws InterruptedException {
        Random random = new Random();

        // Lista de mensajes posibles para el motivo de la remisión
        String[] mensajes = {
                "Evaluacion general debido a dolor persistente.",
                "Consulta para seguimiento de tratamiento.",
                "Examen de rutina por sintomas recientes.",
                "Reevaluacion post-cirugia.",
                "Pruebas adicionales para diagnostico.",
                "Consulta especializada para evaluacion de síntomas específicos."
        };

        try {
            // Hacer clic en el campo de texto para la descripción de la remisión
            WebElement descripcionRemisionField = driver.findElement(By.cssSelector("textarea#frmCrearAfiliado\\:txtaDescripRem"));
            descripcionRemisionField.click();
            esperar(300);

            // Seleccionar un mensaje aleatorio de la lista
            String mensaje = mensajes[random.nextInt(mensajes.length)];
            descripcionRemisionField.sendKeys(mensaje);

        } finally {
        }
    }

    // BOTON GUARDAR: guardar toda la Referencia
    private void BotonGuardar() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrearAfiliado\\:j_idt841")).click();
    }

}