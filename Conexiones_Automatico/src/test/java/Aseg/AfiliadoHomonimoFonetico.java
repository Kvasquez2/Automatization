import conexion.Conexion_BD;
import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class AfiliadoHomonimoFonetico {  // El nombre de la clase debe ser descriptivo
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
    public void afiliadoHomonimo() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al módulo de aseguramiento y la opción funcional
        aseguramiento();
        esperar(300);

        seleccionarDocumento();
        esperar(300);

        seleccionarFechasNaciyExpe();
        esperar(300);

        seleccionarNombres();
        esperar(300);

        seleccionarGenero();
        esperar(300);

        seleccionarEstadoCivil();
        esperar(300);

        seleccionarTipoyOrigenAfi();
        esperar(300);

        seleccionarFechaAfiliacion_SGSSSyEPSS();
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

        seleccionarPais();
        esperar(300);

        // VALIDAR BOTON PARA GUARDAR EL REGISTRO
        BotonGuardar();
        esperar(300);

        // VALIDAR BOTON PARA EL HOMONIMO FONETICO
        homonimoFonetico();
        esperar(300);

        //importInfo();
        esperar(300);

    }

    // Ingreso al módulo de Aseguramiento
    private void aseguramiento() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/afiliados.faces");
        esperar(300);

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
    }

    private void seleccionarFechasNaciyExpe() throws InterruptedException {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Generar un número aleatorio de días para crear una fecha de nacimiento aleatoria
        Random random = new Random();
        int diasParaRestar = random.nextInt(365 * 30) + 365 * 18; // Entre 18 y 48 años atrás

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
    }

    List<String[]> listas = new ArrayList<>();
    // SELECT BD - NOMBRE AFILIADO
    private String[] seleccionarNombres() throws InterruptedException {
        esperar(500);
        List<String[]> lista = new ArrayList<>();
        String query = "select mae_tipo_documento_valor,numero_documento,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido " +
                "from aseg_afiliados aa WHERE mae_tipo_documento_id = 401 ORDER BY 1 DESC LIMIT 100;";


        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] datos = new String[5];
                datos[0] = rs.getString("numero_documento");
                datos[1] = rs.getString("primer_nombre");
                datos[2] = rs.getString("segundo_nombre");
                datos[3] = rs.getString("primer_apellido");
                datos[4] = rs.getString("segundo_apellido");
                lista.add(datos);
            }

            // Verificar que la lista no esté vacía
            if (!lista.isEmpty()) {
                // Seleccionar un elemento aleatorio
                Random random = new Random();
                int indexAleatorio = random.nextInt(lista.size());
                String[] datosAleatorios = lista.get(indexAleatorio);
                listas.add(lista.get(indexAleatorio));

                // Imprimir datos seleccionados
                System.out.printf(" - - - \n");
                System.out.println("Datos seleccionados:");
                System.out.println("Número de documento: " + datosAleatorios[0]);
                System.out.println("Primer nombre: " + datosAleatorios[1]);
                System.out.println("Segundo nombre: " + datosAleatorios[2]);
                System.out.println("Primer apellido: " + datosAleatorios[3]);
                System.out.println("Segundo apellido: " + datosAleatorios[4]);
                System.out.printf(" - - - \n");

                // Llenar los campos del formulario con los datos obtenidos
                driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(datosAleatorios[3]);
                esperar(300);
                driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(datosAleatorios[4]);
                esperar(300);
                driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(datosAleatorios[1]);
                esperar(300);
                driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(datosAleatorios[2]);
                esperar(300);
            } else {
                System.out.println("No se encontraron datos en la base de datos.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    // DATOS PERSONALES
    // SELECCIONADO: PAIS NACIMIENTO, PAIS NACIONALIDAD
    // RANDOM :      LUGAR NACIMIENTO
    private void seleccionarPais() throws InterruptedException {
        // PAIS NACIMIENTO
        System.out.printf(" ");
        esperar(100);
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacimiento_label")).click();
        esperar(400);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacimiento_1")).click(); //Colombia
        esperar(600);

        // LUGAR NACIMIENTO
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

        // PAIS NACIONALIDAD
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacionalidad_label")).click();
        esperar(400);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacionalidad_1")).click();
        esperar(600);
    }

    // GENERO
    private String seleccionarGeneroAleatorio() {
        return new Random().nextBoolean() ? "Femenino" : "Masculino";
    }
    private void seleccionarGenero() throws InterruptedException {
        // GENERO
        String generoSeleccionado = seleccionarGeneroAleatorio();
        driver.findElement(By.cssSelector("#frmCrear\\:genero_label")).click();
        if (generoSeleccionado.equals("Femenino")) {
            driver.findElement(By.cssSelector("#frmCrear\\:genero_1")).click();
            esperar(400);

            // GENERO IDENTIFICACION
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_1")).click();
            esperar(300);

        } else {
            driver.findElement(By.cssSelector("#frmCrear\\:genero_2")).click();
            esperar(400);

            // GENERO IDENTIFICACION
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_label")).click();
            esperar(200);
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_2")).click();
            esperar(300);
        }

    }

    // ESTADO CIVIL
    private void seleccionarEstadoCivil() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //WebDriverWait wait = new WebDriverWait(driver, "10");

        // Hacer clic en el campo de estado civil
        WebElement estadoCivilLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span#frmCrear\\:estadoCivil_label")));
        estadoCivilLabel.click();

        // Esperar un segundo para que las opciones aparezcan
        Thread.sleep(1000);

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

    private void seleccionarTipoyOrigenAfi() throws InterruptedException {
        // TIPO AFILIADO
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoAfiliado_2")).click();
        esperar(300);

        // ORIGEN AFILIADO
        driver.findElement(By.cssSelector("span#frmCrear\\:origenAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:origenAfiliado_8")).click();
        esperar(300);
    }

    // DATOS AFILIACION
    // FECHA AFILIACION SGSSS Y FECHA AFILIACION EPSS
    private void seleccionarFechaAfiliacion_SGSSSyEPSS() throws InterruptedException {
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
    }

    // DATOS IPS ATENCION PRIMARIA
    private void SeleccionarIPSAtencion() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:sedeIpsPrimaria_label")).click();
        driver.findElement(By.cssSelector("li#frmCrear\\:sedeIpsPrimaria_1")).click();
        esperar(200);
    }

    // DATOS SOCIOECONIMICOS
        // SELECCIONADO: GRUPO POBLACIONAL, GRUPO SISBEN,
        // RANDOM :      GRUPO ETNICO, SUB-GRUPO SUSBEN,
        private void SeleccionarPoblacionySisben() throws InterruptedException {
        // GRUPO POBLACIONAL
        driver.findElement(By.cssSelector("span#frmCrear\\:grupoPoblacional_label")).click();
        esperar(200);
        driver.findElement(By.cssSelector("li#frmCrear\\:grupoPoblacional_26")).click();
        esperar(300);

        // GRUPO ETNICO
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

        // GRUPO SISBEN,
        driver.findElement(By.cssSelector("span#frmCrear\\:grupoSisben_label")).click();
        driver.findElement(By.cssSelector("li#frmCrear\\:grupoSisben_1")).click();
        esperar(300);

        // SUB-GRUPO SUSBEN
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
        // SELECCIONADO: NIVEL
        // RANDOM :      METODOLOGIA GRUPO POBLACIONAL
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

    private void homonimoFonetico() throws InterruptedException{
        esperar(300);
        System.out.printf("Validar boton homonimo");
        driver.findElement(By.cssSelector("button#frmHomonimo\\:j_idt976")).click();
    }

    private void importInfo() throws InterruptedException{
        System.out.printf("abajo\n");

        for (String[] elemento : listas) {
            System.out.println(elemento);
        }
    }

}
