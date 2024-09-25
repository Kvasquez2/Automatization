import conexion.Conexion_BD;
import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AfiliadoBDUA {
    private WebDriver driver;
    private ingreso ingreso;
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

        // Validar si la conexión a la base de datos se estableció correctamente
        if (conexion != null) {
            System.out.println("Conexión a la base de datos establecida correctamente.");
        } else {
            System.err.println("Error al establecer la conexión a la base de datos.");
        }
    }

    // Metodo para esperar la ejecución (simular delay)
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    @Test
    public void testAfiliadoBDUA() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al modulo y la opcion funcional
        aseguramiento();
        esperar(300);

        seleccionarDatos();
        esperar(300);

        seleccionarEps();
        esperar(300);

        seleccionarPais();
        esperar(300);

        seleccionarEstadoCivil();
        esperar(300);

        // DATOS AFILIACION
        seleccionarFechaAfiliacion();
        esperar(300);

        // DATOS MUNUCIPIO AFILIACION
        SeleccionarMunicipio();
        esperar(300);

        // DATOS IPS ATENCION PRIMARIA
        SeleccionarIPSAtencion();
        esperar(300);

        // DATOS SOCIOECONIMICOS
        SeleccionarPoblacionySisben();
        esperar(300);

        SeleccionarPoblacionalyNivel();
        esperar(300);

        // DATOS ESTADO AFILIACION
        SeleccionarCausa();
        esperar(300);

        BotonGuardar();
        esperar(300);

    }

    // INGRESO AL MODULO DE ASEGURAMIENTO
    private void aseguramiento() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/afiliados.faces");
        esperar(300);

        driver.findElement(By.cssSelector("button[name='frmAfiliados:j_idt43']")).click();
        esperar(1500);
    }

    private void seleccionarDatos() throws InterruptedException {
        // TIPO DOCUMENTO
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoDocumento_label")).click();
        esperar(1000);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoDocumento_2")).click();
        esperar(300);

        // NUMERO DOCUMENTO
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
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumento")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumento")).sendKeys(randomNumberStr);
        esperar(100);
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumento")).sendKeys(Keys.TAB);

        // APELLIDOS
        ArrayList<String> lastNames = new ArrayList<>(Arrays.asList(
                "García", "Martínez", "López", "Rodríguez", "Pérez", "Sánchez", "Ramírez", "González", "Fernández", "Hernández",
                "Torres", "Gómez", "Díaz", "Vázquez", "Álvarez", "Castro", "Morales", "Ruiz", "Mendoza", "Ortiz",
                "Jiménez", "Reyes", "Cruz", "Flores", "Rojas", "Rivera", "Domínguez", "Cárdenas", "Ramos", "Soto",
                "Núñez", "Silva", "Vargas", "Chávez", "Guerrero", "Luna", "Molina", "Cabrera", "Ibarra", "Pacheco"
        ));
        Collections.shuffle(lastNames);
        String apellido1 = lastNames.get(0), apellido2 = lastNames.get(1);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(apellido1);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(Keys.TAB);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(apellido2);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(Keys.TAB);
        esperar(200);

        // NOMBRES
        List<String> nombresMujeres = Arrays.asList(
                "Maria", "Ana", "Lucia", "Carmen", "Sofia", "Isabella", "Valentina", "Gabriela", "Fernanda", "Alejandra",
                "Mariana", "Daniela", "Paula", "Laura", "Andrea", "Jimena", "Clara", "Eva", "Irene", "Julia"
        );
        List<String> nombresHombres = Arrays.asList(
                "Juan", "Pedro", "Carlos", "Luis", "Miguel", "Daniel", "Andres", "Felipe", "Jorge", "Diego",
                "Manuel", "Santiago", "Ricardo", "Alberto", "Eduardo", "Fernando", "Oscar", "Pablo", "Rafael", "Enrique"
        );
        // Selección de género y nombres aleatorios
        boolean esFemenino = new Random().nextBoolean(); // true = Femenino, false = Masculino
        List<String> nombres = esFemenino ? nombresMujeres : nombresHombres;
        // Seleccionar dos nombres aleatorios
        Collections.shuffle(nombres);
        String nombre1 = nombres.get(0), nombre2 = nombres.get(1);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(nombre1);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(Keys.TAB);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(nombre2);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(Keys.TAB);
        esperar(200);

        // SELECCIONAR GENERO DEPENDIENDO AL NOMBRE ESCOGIDO
        driver.findElement(By.cssSelector("span#frmCrear\\:genero_label")).click();
        esperar(200);
        if (esFemenino) {
            // GENERO - - - FEMENINO
            driver.findElement(By.cssSelector("li#frmCrear\\:genero_1")).click();
            esperar(400);
            driver.findElement(By.cssSelector("span#frmCrear\\:genero_label")).sendKeys(Keys.TAB);
            esperar(400);

            // GENERO IDENTIFICADOR - - - FEMENINO
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("li#frmCrear\\:generoIdentif_1")).click();
            esperar(300);
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).sendKeys(Keys.TAB);
            esperar(300);
        } else {
            // GENERO - - - MASCULINO
            driver.findElement(By.cssSelector("li#frmCrear\\:genero_2")).click();
            esperar(400);

            // GENERO IDENTIFICADOR - - - MASCULINO
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("li#frmCrear\\:generoIdentif_2")).click();
            esperar(300);
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).sendKeys(Keys.TAB);
            esperar(300);
        }

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Generar un número aleatorio de días para crear una fecha de nacimiento aleatoria
        Random randomFecha = new Random();
        int diasParaRestar = randomFecha.nextInt(365 * 30) + 365 * 18; // Entre 18 y 48 años atrás

        // Calcular la fecha de nacimiento restando los días
        LocalDate fechaNacimiento = fechaActual.minusDays(diasParaRestar);
        String fechaNacimientoFormateada = fechaNacimiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ingresar la fecha de nacimiento en el campo correspondiente
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).sendKeys(fechaNacimientoFormateada);
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).sendKeys(Keys.ENTER);
        esperar(500);

        // Calcular la fecha de expedición del documento (18 años y 1 día después de la fecha de nacimiento)
        LocalDate fechaExpedicionDocumento = fechaNacimiento.plusYears(18).plusDays(1);
        String fechaExpedicionFormateada = fechaExpedicionDocumento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).sendKeys(fechaExpedicionFormateada);
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).sendKeys(Keys.ENTER);
        esperar(500);

        // TIPO AFILIADO
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoAfiliado_2")).click();
        esperar(300);
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoAfiliado_label")).sendKeys(Keys.TAB);

        // ORIGEN AFILIADO
        driver.findElement(By.cssSelector("span#frmCrear\\:origenAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:origenAfiliado_10")).click();
        esperar(300);
        driver.findElement(By.cssSelector("span#frmCrear\\:origenAfiliado_label")).sendKeys(Keys.TAB);

        /*
         *
         */

        // REGISTRADO EN BDUA

        // TIPO DOCUMENTO
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoDocumentoBDUA_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoDocumentoBDUA_2")).click();
        esperar(200);

        // NUMERO DOCUMENTO
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumentoBDUA")).sendKeys(randomNumberStr);
        esperar(200);

        // APELLIDOS
        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellidoBDUA")).sendKeys(apellido1, Keys.TAB);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellidoBDUA")).sendKeys(apellido2, Keys.TAB);
        esperar(200);

        // NOMBRES
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombreBDUA")).sendKeys(nombre1, Keys.TAB);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombreBDUA")).sendKeys(nombre2, Keys.TAB);
        esperar(200);

        // FECHA DE NACIMIENTO
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimientoBDUA_input")).sendKeys(fechaNacimientoFormateada, Keys.TAB);
        esperar(500);
        System.out.printf(fechaNacimientoFormateada);
    }

    // SELECT - EPS BDUA
    private void seleccionarEps() throws InterruptedException{
        // Realizar la consulta a la base de datos y seleccionar un número de documento al azar
        String documentoAleatorio = "";
        String query = "SELECT nombre FROM gn_maestros gm WHERE tipo = 34 and valor not in(\"EPSS40\",\"EPS040\");";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<String> documentos = new ArrayList<>();
            while (rs.next()) {
                documentos.add(rs.getString("nombre"));
            }

            if (!documentos.isEmpty()) {
                // Generar un número de documento aleatorio y realizar la acción en la interfaz
                Random random = new Random();
                documentoAleatorio = documentos.get(random.nextInt(documentos.size()));

                driver.findElement(By.cssSelector("input#frmCrear\\:epsBDUA_input")).click();
                esperar(200);
                driver.findElement(By.cssSelector("input#frmCrear\\:epsBDUA_input")).sendKeys(documentoAleatorio);
                esperar(1000);
                driver.findElement(By.cssSelector("input#frmCrear\\:epsBDUA_input")).sendKeys(Keys.TAB);
                esperar(100);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        esperar(400);
    }

    private void seleccionarEstadoCivil() throws InterruptedException {
        System.out.printf("Seleccion Estado Civil");
        esperar(200);

        // Clic en el label de estado civil
        driver.findElement(By.cssSelector("span#frmCrear\\:estadoCivil_label")).click();
        esperar(200);

        // Obtener opciones de estado civil y seleccionar un índice aleatorio
        List<WebElement> opcionesEstadoCivil = driver.findElements(By.cssSelector("li[id^='frmCrear\\:estadoCivil_']"));
        esperar(200);
        if (opcionesEstadoCivil.size() > 1) {
            int indiceAleatorio = new Random().nextInt(opcionesEstadoCivil.size() - 1) + 1;
            opcionesEstadoCivil.get(indiceAleatorio).click();
        } else {
            System.out.println("No hay suficientes opciones para seleccionar.");
        }
    }

    private void seleccionarPais() throws InterruptedException {
        // PAIS DE NACIMIENTO
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacimiento_label")).click();
        esperar(100);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacimiento_1")).click();
        esperar(600);

        // LUGAR DE NACIMIENTO - Selección aleatoria
        driver.findElement(By.cssSelector("span#frmCrear\\:lugarNacimiento_label")).click();
        esperar(600);
        List<WebElement> opcionesLugarNacimiento = driver.findElements(By.cssSelector("li[id^='frmCrear\\:lugarNacimiento_']"));
        opcionesLugarNacimiento.get(new Random().nextInt(opcionesLugarNacimiento.size())).click();
        esperar(600);

        // PAIS DE NACIONALIDAD
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacionalidad_label")).click();
        esperar(100);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacionalidad_1")).click();
        esperar(600);
    }

    // DATOS AFILIACION
    private void seleccionarFechaAfiliacion() throws InterruptedException {
        // Obtener la fecha actual en formato YYYY-MM-DD
        LocalDate fechaActual = LocalDate.now();
        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ingresar la fecha actual en el campo 'fechaAfilSGSSS'
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilSGSSS_input")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilSGSSS_input")).sendKeys(fechaFormateada);
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilSGSSS_input")).sendKeys(Keys.ENTER);
        esperar(500);

        // Ingresar la fecha actual en el campo 'fechaAfilEPS'
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilEPS_input")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilEPS_input")).sendKeys(fechaFormateada);
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilEPS_input")).sendKeys(Keys.ENTER);
        esperar(500);
    }

    // DATOS MUNUCIPIO AFILIACION
    private void SeleccionarMunicipio() throws InterruptedException {
        // Municipio de Afiliacion
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).click();
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).sendKeys("MEDE");
        esperar(800);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).sendKeys(Keys.ENTER);
        esperar(300);

        // Direccion
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt440")).click();
        esperar(1000);
        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt810_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt810_4")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:numeroDirecion")).click();
        esperar(200);
        String numeroDireccion = "56";
        driver.findElement(By.cssSelector("input#frmDireccion\\:numeroDirecion")).sendKeys(numeroDireccion);
        esperar(300);

        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt820_label")).click();
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt820_7")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:placa")).click();
        esperar(200);
        String numeroPlaca = "49";
        driver.findElement(By.cssSelector("input#frmDireccion\\:placa")).sendKeys(numeroPlaca);
        esperar(300);

        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt859_label")).click();
        driver.findElement(By.cssSelector("li#frmDireccion\\:j_idt859_1")).click();
        esperar(300);

        driver.findElement(By.cssSelector("input#frmDireccion\\:placa2")).click();
        esperar(200);
        String numeroPlaca2 = "29";
        driver.findElement(By.cssSelector("input#frmDireccion\\:placa2")).sendKeys(numeroPlaca2);
        esperar(300);


        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt899")).click();
        esperar(200);
        driver.findElement(By.cssSelector("input#frmDireccion\\:j_idt899")).sendKeys("ELITE DE LA MODA");
        esperar(1000);

        driver.findElement(By.cssSelector("button#frmDireccion\\:j_idt906")).click();


        // Zona
        driver.findElement(By.cssSelector("span#frmCrear\\:zona_label")).click();
        esperar(200);
        driver.findElement(By.cssSelector("li#frmCrear\\:zona_4")).click();
        esperar(300);

        // Barrio
        driver.findElement(By.cssSelector("input#frmCrear\\:barrio")).click();
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:barrio")).sendKeys("LA CANDELARIA");
        esperar(800);
        driver.findElement(By.cssSelector("input#frmCrear\\:barrio")).sendKeys(Keys.ENTER);
        esperar(300);

        //Button Actualizar dirreccion
        driver.findElement(By.cssSelector("button#frmDireccion\\:j_idt906")).click();
        esperar(200);

        // Contacto
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt452")).click();
        esperar(300);
        driver.findElement(By.cssSelector("span#frmCrearContacto\\:tipoContacto_label")).click();
        driver.findElement(By.cssSelector("li#frmCrearContacto\\:tipoContacto_1")).click();
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrearContacto\\:numeroContacto")).click();
        String numeroContacto = "3152871824";
        driver.findElement(By.cssSelector("input#frmCrearContacto\\:numeroContacto")).sendKeys(numeroContacto);
        esperar(300);
        driver.findElement(By.cssSelector("textarea#frmCrearContacto\\:observacion")).click();
        driver.findElement(By.cssSelector("textarea#frmCrearContacto\\:observacion")).sendKeys("Numero de prueba automatica");
        esperar(300);
        driver.findElement(By.cssSelector("button#frmCrearContacto\\:j_idt991")).click();
        esperar(500);
    }

    // DATOS IPS ATENCION PRIMARIA
    private void SeleccionarIPSAtencion() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:sedeIpsPrimaria_label")).click();
        driver.findElement(By.cssSelector("li#frmCrear\\:sedeIpsPrimaria_1")).click();
        esperar(200);
    }

    // DATOS SOCIOECONIMICOS
    private void SeleccionarPoblacionySisben() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:grupoPoblacional_label")).click();
        esperar(200);
        driver.findElement(By.cssSelector("li#frmCrear\\:grupoPoblacional_26")).click();
        esperar(300);

        // Seleccionar Grupo etnico
        driver.findElement(By.cssSelector("span#frmCrear\\:grupoEtnico_label")).click();
        // Obtener todas las opciones del grupo étnico
        List<WebElement> opcionesGrupoEtnico = driver.findElements(By.cssSelector("li[id^='frmCrear\\:grupoEtnico_']"));
        // Asegurarse de que haya más de una opción para seleccionar
        if (opcionesGrupoEtnico.size() > 1) {
            Random random = new Random();
            int indiceAleatorio = random.nextInt(opcionesGrupoEtnico.size() - 1) + 1;
            // Construir el selector CSS basado en el índice aleatorio
            String SeleccionGrupo = "li#frmCrear\\:grupoEtnico_" + indiceAleatorio;
            // Hacer clic en la opción seleccionada utilizando el selector CSS construido
            driver.findElement(By.cssSelector(SeleccionGrupo)).click();
        }
        esperar(300);

        // Seleccionar Grupo SISBEN
        driver.findElement(By.cssSelector("span#frmCrear\\:grupoSisben_label")).click();
        driver.findElement(By.cssSelector("li#frmCrear\\:grupoSisben_1")).click();
        esperar(300);

        // Seleccionar Sub-Grupo SISBEN
        driver.findElement(By.cssSelector("span#frmCrear\\:subgrupoSisben_label")).click();
        // Obtener todas las opciones del grupo étnico
        List<WebElement> opcionesSubGrupoSisben = driver.findElements(By.cssSelector("li[id^='frmCrear\\:subgrupoSisben_']"));
        // Asegurarse de que haya más de una opción para seleccionar
        if (opcionesSubGrupoSisben.size() > 1) {
            Random random = new Random();
            int indiceAleatorio = random.nextInt(opcionesSubGrupoSisben.size() - 1) + 1;
            // Construir el selector CSS basado en el índice aleatorio
            String SeleccionSubgrupo = "li#frmCrear\\:subgrupoSisben_" + indiceAleatorio;
            // Hacer clic en la opción seleccionada utilizando el selector CSS construido
            driver.findElement(By.cssSelector(SeleccionSubgrupo)).click();
        }
        esperar(300);
    }

    private void SeleccionarPoblacionalyNivel() throws InterruptedException {
        // Seleccionar Metodología Grupo Poblacional
        driver.findElement(By.cssSelector("span#frmCrear\\:metodGrupoPoblacional_label")).click();
        // Obtener todas las opciones del grupo étnico
        List<WebElement> opcionesGrupoPoblacional = driver.findElements(By.cssSelector("li[id^='frmCrear\\:metodGrupoPoblacional_']"));
        // Asegurarse de que haya más de una opción para seleccionar
        if (opcionesGrupoPoblacional.size() > 1) {
            Random random = new Random();
            int indiceAleatorio = random.nextInt(opcionesGrupoPoblacional.size() - 1) + 1;
            // Construir el selector CSS basado en el índice aleatorio
            String SeleccionPoblacion = "li#frmCrear\\:metodGrupoPoblacional_" + indiceAleatorio;
            // Hacer clic en la opción seleccionada utilizando el selector CSS construido
            driver.findElement(By.cssSelector(SeleccionPoblacion)).click();
        }
        esperar(300);

        // Seleccionar Nivel
        driver.findElement(By.cssSelector("span#frmCrear\\:nivelSisben_label")).click();
        esperar(300);
        driver.findElement(By.cssSelector("li#frmCrear\\:nivelSisben_1")).click();
        esperar(300);
    }

    // DATOS ESTADO AFILIACION
    private void SeleccionarCausa() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:causaNovedad_label")).click();
        esperar(300);
        driver.findElement(By.cssSelector("li#frmCrear\\:causaNovedad_3")).click();
        esperar(300);
    }

    private void BotonGuardar() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt525")).click();
    }

}
