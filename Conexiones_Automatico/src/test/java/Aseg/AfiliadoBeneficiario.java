import conexion.ingreso;
import conexion.Conexion_BD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AfiliadoBeneficiario {
    private WebDriver driver;
    private conexion.ingreso ingreso;
    private Connection conexion;
    private List<String> lista;
    private WebDriverWait wait;

    // Configuración del driver de Chrome
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\IdeaProjects\\Conexiones_Automatico\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();

        ingreso = new ingreso(driver);  // Inicialización de la clase 'Ingreso'

        configurarConexionBD(); // Configuración de la conexión a la base de datos

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Espera de hasta 10 segundos
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

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

    // Metodo para pausar la ejecución por un tiempo determinado
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    /*
        Pruebas con los datos para cabeza de hogar como "beneficiario"
        con los demás datos quemados.
    */
    @Test
    public void testBeneficiario() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al módulo y la option funcional
        aseguramiento();
        esperar(300);

        // DEMAS METODOS
        seleccionarDocumento();
        esperar(300);

        seleccionarFechas(); // Fecha Nacimiento Y Expedicion
        esperar(300);

        seleccionarNombre(); // Nombres, Apellidos, Genero y Genero identificador
        esperar(300);

        seleccionarPais(); // Pais de nacimiento, Lugar de nacimiento, Pais Nacionalidad
        esperar(300);

        seleccionarEstadoCivil();
        esperar(300);

        seleccionarTyOAfiliado();
        esperar(300);

        seleccionarParentesco();
        esperar(300);

        seleccionarCabezaFamilia();
        esperar(300);

        seleccionarFecha();
        esperar(300);

        SeleccionarMunicipioAfiliacion();
        esperar(300);

        SeleccionarIPSAtencion();
        esperar(300);

        SeleccionarPoblacionySisben();
        esperar(300);

        SeleccionarPoblacionalyNivel();
        esperar(300);

        SeleccionarCausa();
        esperar(300);

        BotonGuardar();
        esperar(300);
    }

    // INGRESO AL MODULO DE ASEGURAMIENTO
    private void aseguramiento() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/afiliados.faces");
        esperar(500);

        driver.findElement(By.cssSelector("button[name='frmAfiliados:j_idt43']")).click();
        esperar(1500);
    }

    private void seleccionarDocumento() throws InterruptedException {
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
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumento")).sendKeys(randomNumberStr);
        esperar(300);
        driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumento")).sendKeys(Keys.TAB);
        esperar(2000);
    }

    private void seleccionarFechas() throws InterruptedException {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        Random random = new Random();

        // Generar un número aleatorio de días para la fecha de expedición (hasta 1 mes antes de la fecha actual)
        int diasParaRestar = random.nextInt(30) + 1; // Entre 1 y 30 días atrás
        LocalDate fechaExpedicion = fechaActual.minusDays(diasParaRestar);
        String fechaExpedicionFormateada = fechaExpedicion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ingresar la fecha de expedición en el campo correspondiente
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).click();
        esperar(100);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).sendKeys(fechaExpedicionFormateada);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaExpDoc_input")).sendKeys(Keys.TAB);
        esperar(500);

        // Generar un número aleatorio de años para la fecha de nacimiento (entre 18 y 100 años atrás)
        int añosParaRestar = random.nextInt(83) + 18; // Entre 18 y 100 años atrás
        LocalDate fechaNacimiento = fechaActual.minusYears(añosParaRestar);
        String fechaNacimientoFormateada = fechaNacimiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ingresar la fecha de nacimiento en el campo correspondiente
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).click();
        esperar(100);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).sendKeys(fechaNacimientoFormateada);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaNacimiento_input")).sendKeys(Keys.TAB);
        esperar(500);
    }

    private void seleccionarNombre() throws InterruptedException {
        // APELLIDOS
        ArrayList<String> lastNames = new ArrayList<>(Arrays.asList(
                "García", "Martínez", "López", "Rodríguez", "Pérez", "Sánchez", "Ramírez", "González", "Fernández", "Hernández",
                "Torres", "Gómez", "Díaz", "Vázquez", "Álvarez", "Castro", "Morales", "Ruiz", "Mendoza", "Ortiz",
                "Jiménez", "Reyes", "Cruz", "Flores", "Rojas", "Rivera", "Domínguez", "Cárdenas", "Ramos", "Soto",
                "Núñez", "Silva", "Vargas", "Chávez", "Guerrero", "Luna", "Molina", "Cabrera", "Ibarra", "Pacheco"
        ));
        Collections.shuffle(lastNames);
        String apellido1 = lastNames.get(0), apellido2 = lastNames.get(1);

        // Ingresar apellidos en los campos correspondientes
        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(apellido1);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(Keys.TAB);
        esperar(100);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(apellido2);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(Keys.TAB);
        esperar(100);

        // Nombres
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

        // Ingresar los nombres en los campos correspondientes
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(nombre1);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(Keys.TAB);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(nombre2);
        esperar(200);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(Keys.TAB);
        esperar(200);

        // Seleccionar género y género identificador en base al género
        driver.findElement(By.cssSelector("span#frmCrear\\:genero_label")).click();
        esperar(200);

        if (esFemenino) {
            // Seleccionar género: Femenino
            driver.findElement(By.cssSelector("li#frmCrear\\:genero_1")).click();
            esperar(400);
            driver.findElement(By.cssSelector("span#frmCrear\\:genero_label")).sendKeys(Keys.TAB);
            esperar(400);

            // Seleccionar género identificador: Femenino
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("li#frmCrear\\:generoIdentif_1")).click();
            esperar(300);
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).sendKeys(Keys.TAB);
            esperar(300);
        } else {
            // Seleccionar género: Masculino
            driver.findElement(By.cssSelector("li#frmCrear\\:genero_2")).click();
            esperar(400);

            // Seleccionar género identificador: Masculino
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("li#frmCrear\\:generoIdentif_2")).click();
            esperar(300);
            driver.findElement(By.cssSelector("span#frmCrear\\:generoIdentif_label")).sendKeys(Keys.TAB);
            esperar(300);
        }
    }

    private void seleccionarPais() throws InterruptedException {

        // PAIS DE NACIMIENTO
        WebElement paisNacimientoLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:paisNacimiento_label")));
        paisNacimientoLabel.click();
        esperar(100);
        // Esperar hasta que la opción Colombia esté clickeable y seleccionarla
        WebElement paisColombia = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li#frmCrear\\:paisNacimiento_1")));
        paisColombia.click();
        esperar(600);

        // LUGAR DE NACIMIENTO
        driver.findElement(By.cssSelector("span#frmCrear\\:lugarNacimiento_label")).click();
        Thread.sleep(600);
        // Obtener todas las opciones de lugar de nacimiento
        List<WebElement> opcionesLugarNacimiento = driver.findElements(By.cssSelector("li[id^='frmCrear\\:lugarNacimiento_']"));
        // Seleccionar un índice aleatorio
        Random random = new Random();
        int indiceAleatorio = random.nextInt(opcionesLugarNacimiento.size());
        // Hacer clic en la opción seleccionada
        WebElement lugarNacimientoSeleccionado = opcionesLugarNacimiento.get(indiceAleatorio);
        lugarNacimientoSeleccionado.click();
        esperar(600);

        // PAIS DE NACIONALIDAD
        WebElement paisNacionalidadLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:paisNacionalidad_label")));
        paisNacionalidadLabel.click();
        esperar(100);
        // Esperar hasta que la opción Colombia esté clickeable y seleccionarla
        WebElement paisNacionalidadColombia = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li#frmCrear\\:paisNacionalidad_1")));
        paisNacionalidadColombia.click();
        esperar(600);

    }

    private void seleccionarEstadoCivil() throws InterruptedException {
        WebElement estadoCivilLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:estadoCivil_label")));
        estadoCivilLabel.click();
        esperar(1000);

        // Obtener todas las opciones de estado civil
        List<WebElement> opcionesEstadoCivil = driver.findElements(By.cssSelector("li[id^='frmCrear\\:estadoCivil_']"));

        // Seleccionar un índice aleatorio
        Random random = new Random();
        int indiceAleatorio = random.nextInt(opcionesEstadoCivil.size());

        // Asegurarse de que la lista de opciones tenga más de una opción
        if (opcionesEstadoCivil.size() > 1) {
            // Seleccionar un índice aleatorio, evitando el índice 0
            Random random1 = new Random();
            indiceAleatorio = random.nextInt(opcionesEstadoCivil.size() - 1) + 1;

            // Hacer clic en la opción seleccionada
            WebElement estadoCivilSeleccionado = opcionesEstadoCivil.get(indiceAleatorio);
            estadoCivilSeleccionado.click();
        } else {
            System.out.println("No hay suficientes opciones para seleccionar.");
        }
    }

    private void seleccionarTyOAfiliado() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // TIPO AFILIADO
        WebElement tipoAfiliadoLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:tipoAfiliado_label")));
        tipoAfiliadoLabel.click();
        esperar(500);

        WebElement tipoAfiliadoOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li#frmCrear\\:tipoAfiliado_1")));
        tipoAfiliadoOption.click();
        esperar(300);

        // ORIGEN AFILIADO
        WebElement origenAfiliadoLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:origenAfiliado_label")));
        origenAfiliadoLabel.click();
        esperar(500);

        WebElement origenAfiliadoOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li#frmCrear\\:origenAfiliado_8")));
        origenAfiliadoOption.click();
        esperar(300);
    }

    private void seleccionarParentesco() throws InterruptedException{
        driver.findElement(By.cssSelector("span#frmCrear\\:parentesco_label")).click();
        esperar(300);
        // Obtener todas las opciones
        List<WebElement> opcionesParentesco = driver.findElements(By.cssSelector("li[id^='frmCrear\\:parentesco_']"));
        // Seleccionar un índice aleatorio
        Random random = new Random();
        int indiceAleatorio = random.nextInt(opcionesParentesco.size());
        // Hacer clic en la opción seleccionada
        WebElement lugarNacimientoSeleccionado = opcionesParentesco.get(indiceAleatorio);
        lugarNacimientoSeleccionado.click();
        esperar(600);
    }

    // CABEZA DE FAMILIA - UTILIZA UN SELECT
    private void seleccionarCabezaFamilia() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Esperar antes de comenzar
        System.out.println("\nDATOS CABEZA DE FAMILIA\n");
        esperar(100);

        // Lista para almacenar los resultados de la consulta
        List<String[]> listaDatos = new ArrayList<>();
        String query = "SELECT mae_tipo_documento_id, numero_documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido " +
                "FROM aseg_afiliados WHERE mae_tipo_documento_id NOT IN (405, 406, 409, 412, 413) AND mae_estado_afiliacion_codigo = 101 ORDER BY RAND() LIMIT 1";

        // Mapa que asocia el tipo de documento con su selector correspondiente
        Map<String, String> selectoresTipoDocumento = new HashMap<>();
        selectoresTipoDocumento.put("411", "li#frmCrear\\:tipoDocumentoCF_1");  // Adulto sin Identificación
        selectoresTipoDocumento.put("401", "li#frmCrear\\:tipoDocumentoCF_2");  // Cédula de Ciudadanía
        selectoresTipoDocumento.put("407", "li#frmCrear\\:tipoDocumentoCF_3");  // Carné Diplomático
        selectoresTipoDocumento.put("402", "li#frmCrear\\:tipoDocumentoCF_4");  // Cédula de Extranjería
        selectoresTipoDocumento.put("410", "li#frmCrear\\:tipoDocumentoCF_6");  // Menor sin Identificación
        selectoresTipoDocumento.put("404", "li#frmCrear\\:tipoDocumentoCF_7");  // Pasaporte
        selectoresTipoDocumento.put("408", "li#frmCrear\\:tipoDocumentoCF_8");  // Permiso Especial de Permanencia
        selectoresTipoDocumento.put("12308", "li#frmCrear\\:tipoDocumentoCF_9"); // Permiso por Protección Temporal
        selectoresTipoDocumento.put("414", "li#frmCrear\\:tipoDocumentoCF_11"); // Salvoconducto
        selectoresTipoDocumento.put("403", "li#frmCrear\\:tipoDocumentoCF_12"); // Tarjeta de Identidad

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Almacenar el resultado de la consulta en la lista
            if (rs.next()) {
                String[] datos = new String[6];
                datos[0] = rs.getString("mae_tipo_documento_id"); // Tipo de documento
                datos[1] = rs.getString("numero_documento"); // Número de documento
                datos[2] = rs.getString("primer_nombre"); // Primer nombre
                datos[3] = rs.getString("segundo_nombre"); // Segundo nombre
                datos[4] = rs.getString("primer_apellido"); // Primer apellido
                datos[5] = rs.getString("segundo_apellido"); // Segundo apellido
                listaDatos.add(datos);

                // Seleccionar el primer (y único) elemento de la lista
                String[] datosSeleccionados = listaDatos.get(0);

                String tipoDocumento = datosSeleccionados[0];
                String numeroDocumento = datosSeleccionados[1];
                String primerNombre = datosSeleccionados[2];
                String segundoNombre = datosSeleccionados[3];
                String primerApellido = datosSeleccionados[4];
                String segundoApellido = datosSeleccionados[5];

                // Imprimir datos seleccionados
                System.out.println(" - - - ");
                System.out.println("Datos seleccionados:");
                System.out.println("Número de documento: " + numeroDocumento);
                System.out.println("Primer nombre: " + primerNombre + " " + segundoNombre);
                System.out.println("Primer apellido: " + primerApellido + " " + segundoApellido);
                System.out.println(" - - - ");

                // Seleccionar el tipo de documento en el formulario
                esperar(200);
                driver.findElement(By.cssSelector("span#frmCrear\\:tipoDocumentoCF_label")).click();
                esperar(200);

                // Obtener el selector del tipo de documento usando el mapa
                String selector = selectoresTipoDocumento.get(tipoDocumento);

                if (selector != null) {
                    driver.findElement(By.cssSelector(selector)).click();
                    esperar(300);

                    // Ingresar el número de documento en el formulario
                    driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumentoCF")).click();
                    esperar(200);
                    driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumentoCF")).sendKeys(numeroDocumento);
                    esperar(200);
                    driver.findElement(By.cssSelector("input#frmCrear\\:numeroDocumentoCF")).sendKeys(Keys.TAB);

                } else {
                    System.out.println("Tipo de documento no reconocido: " + tipoDocumento);
                }
            } else {
                System.out.println("No se encontraron datos en la base de datos.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DATOS AFILIACION
    private void seleccionarFecha() throws InterruptedException {
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
        driver.findElement(By.cssSelector("input#frmCrear\\:fechaAfilEPS_input")).sendKeys(Keys.TAB);
        esperar(500);
    }

    // DATOS MUNUCIPIO AFILIACION
    // MUNICIPIO, DIRECCION, ZONA, BARRIO, CONTACTO
    private void SeleccionarMunicipioAfiliacion() throws InterruptedException {
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

        // Municipio de Afiliacion
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).click();
        esperar(3000);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).sendKeys("MEDE");
        esperar(800);
        driver.findElement(By.cssSelector("input#frmCrear\\:municipioAfiliacion_input")).sendKeys(Keys.ENTER);
        esperar(300);
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
