import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Random;

public class NavigationTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\Downloads\\pruebas-main\\pruebas-main\\Selenium-Basic\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void AfiliadoGenerico() throws InterruptedException {
        CrearAfiliado();
    }

    public void CrearAfiliado() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/login.faces");
        driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
        driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("scMlUumJ");
        driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
        esperar(1200);

        driver.get("http://10.250.3.66:8080/savia/aseguramiento/afiliados.faces");

        driver.findElement(By.cssSelector("button[name='frmAfiliados:j_idt43']")).click();
        esperar(1500);

        seleccionarTipoDocumento();
        esperar(800);

        ingresarNumeroDocumento(); // -------------------------- Ingreso de numeros randoms -----------------------------------
        esperar(800);

        ingresarApellidos(); // -------------------------- Ingreso de los apellidos aleatoriamente -----------------------------------
        esperar(800);

        ingresarNombres();  // -------------------------- Ingreso de los nombres aleatoriamente -----------------------------------
        esperar(800);

        seleccionarPaisNacimientoyNacionalidad();
        esperar(1000);

        seleccionarGenero();
        esperar(800);

        seleccionarGeneroIdentificacion();
        esperar(800);

        seleccionarEstadoCivil(); // ------------------ Ingreso del estado civil aleatoriamente -----------------------------------
        esperar(800);

        seleccionarTipoAfiliado();
        esperar(800);

        seleccionarOrigenAfiliado();
        esperar(800);

        seleccionarFechaAfiliacion_SGSSSyEPSS();
        esperar(800);

        SeleccionarMunicipioAfiliacion();
        esperar(800);

        SeleccionarIPSAtencion();
        esperar(800);

        SeleccionarPoblacionySisben();
        esperar(800);

        SeleccionarPoblacionalyNivel();
        esperar(800);

        SeleccionarCausa();
        esperar(800);

        ingresarFechasNacimientoYExpedicion();
        esperar(800);

        //BOTONGUARDAR();
        //esperar(800);

    }

    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }


    private void seleccionarTipoDocumento() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoDocumento_label")).click();
        esperar(1000);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoDocumento_2")).click();
    }

    private void ingresarNumeroDocumento() {
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

    private void ingresarFechasNacimientoYExpedicion() throws InterruptedException {
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

    private void ingresarApellidos() {
        ArrayList<String> lastNames = new ArrayList<>(Arrays.asList(
                "García", "Martínez", "Lopez", "Rodríguez", "Perez", "Sanchez", "Ramirez", "Gonzalez", "Fernandez", "Hernandez",
                "Torres", "Gomez", "Díaz", "Vazquez", "Alvarez", "Castro", "Morales", "Ruiz", "Mendoza", "Ortiz"
        ));
        Collections.shuffle(lastNames);
        String apellido1 = lastNames.get(0);
        String apellido2 = lastNames.get(1);

        driver.findElement(By.cssSelector("input#frmCrear\\:primerApellido")).sendKeys(apellido1);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoApellido")).sendKeys(apellido2);
    }


    private void ingresarNombres() {
        ArrayList<String> nombresMujeres = new ArrayList<>(Arrays.asList(
                "Maria", "Ana", "Lucia", "Carmen", "Sofia"
        ));
        ArrayList<String> nombresHombres = new ArrayList<>(Arrays.asList(
                "Juan", "Pedro", "Carlos", "Luis", "Miguel"
        ));

        String generoSeleccionado = seleccionarGeneroAleatorio();
        String nombre1, nombre2;
        if (generoSeleccionado.equals("Femenino")) {
            nombre1 = seleccionarNombreAleatorio(nombresMujeres);
            nombre2 = seleccionarNombreAleatorio(nombresMujeres);
        } else {
            nombre1 = seleccionarNombreAleatorio(nombresHombres);
            nombre2 = seleccionarNombreAleatorio(nombresHombres);
        }
        driver.findElement(By.cssSelector("input#frmCrear\\:primerNombre")).sendKeys(nombre1);
        driver.findElement(By.cssSelector("input#frmCrear\\:segundoNombre")).sendKeys(nombre2);

    }

    private void seleccionarPaisNacimientoyNacionalidad() throws InterruptedException {
        // Pais de nacimiento
        esperar(200);
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacimiento_label")).click();
        esperar(400);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacimiento_1")).click(); //Colombia
        esperar(600);

        // Lugar de Nacimiento
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

        // Pais de nacimiento
        driver.findElement(By.cssSelector("span#frmCrear\\:paisNacionalidad_label")).click();
        esperar(400);
        driver.findElement(By.cssSelector("li#frmCrear\\:paisNacionalidad_1")).click();
        esperar(600);
    }

    private void seleccionarGenero() {
        String generoSeleccionado = seleccionarGeneroAleatorio();
        driver.findElement(By.cssSelector("#frmCrear\\:genero_label")).click();
        if (generoSeleccionado.equals("Femenino")) {
            driver.findElement(By.cssSelector("#frmCrear\\:genero_1")).click();
        } else {
            driver.findElement(By.cssSelector("#frmCrear\\:genero_2")).click();
        }
    }

    private void seleccionarGeneroIdentificacion() {
        String generoSeleccionado = seleccionarGeneroAleatorio();
        driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_label")).click();
        if (generoSeleccionado.equals("Femenino")) {
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_1")).click();
        } else {
            driver.findElement(By.cssSelector("#frmCrear\\:generoIdentif_2")).click();
        }
    }

    private String seleccionarGeneroAleatorio() {
        return new Random().nextBoolean() ? "Femenino" : "Masculino";
    }

    private String seleccionarNombreAleatorio(ArrayList<String> nombres) {
        return nombres.get(new Random().nextInt(nombres.size()));
    }

    private void seleccionarEstadoCivil() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);

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

    private void seleccionarTipoAfiliado() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:tipoAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:tipoAfiliado_2")).click();
        esperar(300);
    }

    private void seleccionarOrigenAfiliado() throws InterruptedException {
        driver.findElement(By.cssSelector("span#frmCrear\\:origenAfiliado_label")).click();
        esperar(500);
        driver.findElement(By.cssSelector("li#frmCrear\\:origenAfiliado_8")).click();
        esperar(300);
    }

    // DATOS AFILIACION
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
        esperar(200);
        driver.findElement(By.cssSelector("span#frmDireccion\\:j_idt810_label")).click();
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

    private void BOTONGUARDAR() throws InterruptedException {
        driver.findElement(By.cssSelector("button#frmCrear\\:j_idt525")).click();
    }
    //driver.findElement(By.cssSelector("")).click();

}
