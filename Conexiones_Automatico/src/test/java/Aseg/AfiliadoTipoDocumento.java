import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

public class AfiliadoTipoDocumento {
    private WebDriver driver;
    private ingreso ingreso;
    private WebDriverWait wait;

    // Configuración del driver de Chrome
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kvasquez\\IdeaProjects\\Conexiones_Automatico\\src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        ingreso = new ingreso(driver);  // Inicialización de la clase 'Ingreso'

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Espera de hasta 10 segundos

    }

    // Metodo para pausar la ejecución por un tiempo determinado
    private void esperar(int milisegundos) throws InterruptedException {
        Thread.sleep(milisegundos);
    }

    @Test
    public void testCasos() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al modulo y la opcion funcional
        aseguramiento();
        esperar(300);

    }

    // INGRESO AL MODULO DE ASEGURAMIENTO
    private void aseguramiento() throws InterruptedException{
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/afiliados.faces");
        esperar(300);

        driver.findElement(By.cssSelector("button[name='frmAfiliados:j_idt43']")).click();
        esperar(1500);
    }

}
