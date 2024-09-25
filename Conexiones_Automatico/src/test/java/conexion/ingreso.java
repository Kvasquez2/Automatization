package conexion;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.Test;


public class ingreso {
    private WebDriver driver;

    public ingreso(WebDriver driver) {
        this.driver = driver;
    }

    public void iniciarSesion() {
        driver.get("http://10.250.3.66:8080/savia/login.faces");
        esperar(500);
        driver.findElement(By.cssSelector("input#login\\:usuario")).sendKeys("kvasquec");
        esperar(500);
        driver.findElement(By.cssSelector("input#login\\:contrasena")).sendKeys("kBXSFADe");
        esperar(500);
        driver.findElement(By.cssSelector("button[name='login:j_idt23']")).click();
        esperar(2000);
    }

    private void esperar(long tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
