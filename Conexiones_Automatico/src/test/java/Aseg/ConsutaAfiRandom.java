import conexion.Conexion_BD;
import conexion.ingreso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsutaAfiRandom {
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
    public void ConsulAfiRandom() throws InterruptedException {
        // Paso 1: Iniciar sesión utilizando la clase Ingreso
        ingreso.iniciarSesion();
        esperar(300);

        // Paso 2: Ingreso al módulo de aseguramiento y la opción funcional
        asegConsultaRandom();
        esperar(300);

        // Paso 3 : VER OPCIONES DE LA CONSULTA AFILIADO
        VerNovedades();
        esperar(200);

        VerProgEspeciales();
        esperar(200);

        VerAutorizaciones();
        esperar(200);

        VerCertificados();
        esperar(200);

        VerReferencias();
        esperar(200);

        VerPqrsf();
        esperar(200);

        VerMipres();
        esperar(200);
/*
        //VerTutelas();
        esperar(200);

        VerHospitalizaciones();
        esperar(200);

        VerDireccionados();
        esperar(200);

        VerSolicitudes();
        esperar(200);


        VerAuditoria();
        esperar(200);

 */

        // Ejecutar Toda la portabilidad Con el boton de Guardar
        //BotonGuardar();
        esperar(200);

        // Iniciar el temporizador de cierre después de la ejecución del último método
        iniciarTemporizadorDeCierre();


    }

    // SELECCIONAR AFILIADO
    private void asegConsultaRandom() throws InterruptedException {
        driver.get("http://10.250.3.66:8080/savia/aseguramiento/consultar_afiliado.faces");
        esperar(1000);

        Random random = new Random();
        int numeroAleatorio = random.nextInt(2); // 0 o 1
        System.out.println("Número aleatorio generado: " + numeroAleatorio);

        switch (numeroAleatorio) {
            case 0:
                // Nro Documento
                String NroDocumento = "";
                while (NroDocumento.isEmpty()) {
                    String query = "SELECT numero_documento FROM aseg_afiliados aa WHERE mae_tipo_documento_id = 401;";

                    try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                        List<String> documentos = new ArrayList<>();
                        while (rs.next()) documentos.add(rs.getString("numero_documento"));

                        if (!documentos.isEmpty()) {
                            NroDocumento = documentos.get(new Random().nextInt(documentos.size()));  // Seleccionar documento aleatorio
                        } else {
                            Thread.sleep(1000);  // Esperar antes de reintentar
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!NroDocumento.isEmpty()) {
                    WebElement inputDocumento = driver.findElement(By.id("numeroDocumento"));
                    inputDocumento.click();
                    Thread.sleep(200);
                    inputDocumento.sendKeys(NroDocumento);
                    Thread.sleep(1000);
                }
                System.out.println("ELEGIDO NUMERO DE DOCUMENTO : " + NroDocumento);
                break;

            case 1:
                // Nro Contrato
                String NroContrato = "";
                while (NroContrato.isEmpty()) {
                    String query = "SELECT id_afiliado FROM aseg_afiliados aa WHERE mae_tipo_documento_id = 401;";

                    try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                        List<String> contrato = new ArrayList<>();
                        while (rs.next()) contrato.add(rs.getString("id_afiliado"));

                        if (!contrato.isEmpty()) {
                            NroContrato = contrato.get(new Random().nextInt(contrato.size()));
                        } else {
                            esperar(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!NroContrato.isEmpty()) {
                    WebElement inputDocumento = driver.findElement(By.id("contradoAfiliado"));
                    inputDocumento.click();
                    Thread.sleep(200);
                    inputDocumento.sendKeys(NroContrato);
                    Thread.sleep(1000);
                }
                System.out.println("ELEGIDO NUMERO DE CONTRATO : " + NroContrato);
                break;

            default:
                System.out.println("Número aleatorio no esperado: " + numeroAleatorio);
                break;
        }
        esperar(300);

        // BUTTON "Buscar"
        driver.findElement(By.id("j_idt58")).click();
        esperar(200);
    }

    // NOVEDADES
    private void VerNovedades() throws InterruptedException {
        // ESPERAR 10 SEGUNDOS PARA LA VISUALIZACION DE LOS DATOS DEL AFILIADO
        esperar(10000);

        // INGRESAR PARA VER LAS NOVEDADES
        driver.findElement(By.id("frmVer:j_idt65")).click();
        System.out.println("\n--- ° ---");
        System.out.println("Ingresando para ver las novedades...");
        esperar(500);


        // NRO DE REGISTROS EN LA TBODY
        WebElement tbody = driver.findElement(By.id("frmHistorial:tablaRegistros_data"));
        tbody.click();
        // Contar la cantidad de registros en el tbody
        List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmHistorial\\:tablaRegistros_data tr"));
        int totalRegistros = registros.size();
        // Imprimir la cantidad de registros
        System.out.println("Total de registros encontrados en las NOVEDADES: " + totalRegistros);

        // CLICK CAMPO
        driver.findElement(By.id("frmHistorial:tablaRegistros:j_idt308")).click();
        esperar(200);

        // ESPERAR UN TIEMPO PARA VALIDAR QUE SE CARGUEN LAS NOVEDADES
        esperar(8000);

        // CERRAR DE VER LAS NOVEDADES
        Actions action = new Actions(driver);
        WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
        action.moveToElement(closeButton).click().perform();
        esperar(200);
        System.out.println("Novedades visualizadas y cerradas.");
        System.out.println("--- ° ---");
    }

    // PROGRAMAS ESPECIALES
    private void VerProgEspeciales() throws InterruptedException {
        // INGRESAR PARA VER LOS PROGRAMAS ESPECIALES
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt66")).click();
        System.out.println("Ingresando para ver los Programas Especiales...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en los Programas Especiales.");
        } else {
            // NRO DE REGISTROS EN LA TBODY
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmProgramasEspeciales\\:tablaRegistrosProgramas_data"));
            tbody.click();
            // Contar la cantidad de registros en el tbody (número de filas <tr>)
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmProgramasEspeciales\\:tablaRegistrosProgramas_data tr"));
            int totalRegistros = registros.size();
            // Imprimir la cantidad de registros
            System.out.println("Total de registros encontrados en los Programas Especiales: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmProgramasEspeciales\\:tablaRegistrosProgramas\\:j_idt351")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LOS PROGRAMAS ESPECIALES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Programas especiales visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER AUTORIZACIONES
    private void VerAutorizaciones() throws InterruptedException {
        // INGRESAR PARA VER LAS AUTORIZACIONES
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt67")).click();
        System.out.println("Ingresando para ver las Autorizaciones...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Autorizaciones.");
        } else {
            // NRO DE REGISTROS EN LA TBODY
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmAutorizaciones\\:tablaRegistrosAutorizaciones_data"));
            tbody.click();
            // Contar la cantidad de registros en el tbody (número de filas <tr>)
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmAutorizaciones\\:tablaRegistrosAutorizaciones_data tr"));
            int totalRegistros = registros.size();
            // Imprimir la cantidad de registros
            System.out.println("Total de registros encontrados en las Autorizaciones: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmAutorizaciones\\:tablaRegistrosAutorizaciones\\:j_idt437")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LAS ACTUALIZACIONES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Autorizaciones visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // CERTIFICADOS
    private void VerCertificados() throws InterruptedException {
        // INGRESAR PARA VER LAS AUTORIZACIONES
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt68")).click();
        System.out.println("Ingresando para ver los Certificados...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Autorizaciones.");
        } else {

            //  CERTIFICADO DE AFILIACION
            driver.findElement(By.cssSelector("button#frmCrearCertificado\\:j_idt725")).click();
            esperar(2000);
            System.out.printf("DESCARGADO CERTIFICADO DE AFILIACION\n");

            // CERTIFICADO DE PORTABILIDAD
            WebElement botonCertificado = driver.findElement(By.cssSelector("button#frmCrearCertificado\\:j_idt727"));
            if (botonCertificado.isEnabled()) {
                botonCertificado.click();
                esperar(2000);
                System.out.printf("DESCARGADO CERTIFICADO DE PORTABILIDAD\n");
            } else {
                System.out.printf("NO TIENE CERTIFICADO DE PORTABILIDAD\n");
            }


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(4000);


            // CERRAR DE VER LAS NOVEDADES
            driver.findElement(By.cssSelector("button#frmCrearCertificado\\:j_idt732")).click();
            esperar(200);
            System.out.println("Certificados visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER REFERENCIAS
    private void VerReferencias() throws InterruptedException {
        // INGRESAR PARA VER LAS REFERENCIAS
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt69")).click();
        System.out.println("Ingresando para ver las Referencias...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Referencias.");
        } else {

            // NRO DE REGISTROS EN LA TBODY
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmReferencias\\:tablaRegistrosReferencias_data"));
            tbody.click();
            // Contar la cantidad de registros en el tbody (número de filas <tr>)
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmReferencias\\:tablaRegistrosReferencias_data tr"));
            int totalRegistros = registros.size();
            // Imprimir la cantidad de registros
            System.out.println("Total de registros encontrados en las Referencias: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmReferencias\\:tablaRegistrosReferencias\\:j_idt481")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LAS REFERENCIAS
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Referencias visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER PQRSF
    private void VerPqrsf() throws InterruptedException {
        // INGRESAR PARA VER LAS PQRSF
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt70")).click();
        System.out.println("Ingresando para ver las Pqrsf...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Pqrsf.");
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmPQRSF\\:tablaRegistrosPQRSF_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmPQRSF\\:tablaRegistrosPQRSF_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en las Pqrsf: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmPQRSF\\:tablaRegistrosPQRSF\\:j_idt511")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LAS PQRSF
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Pqrsf visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER MIPRES
    private void VerMipres() throws InterruptedException {
        // INGRESAR PARA VER LOS MIPRES
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt71")).click();
        System.out.println("Ingresando para ver los Mipres...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en los Mipres.");
            esperar(500);
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmMIPRES\\:tablaRegistrosMIPRES_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmMIPRES\\:tablaRegistrosMIPRES_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en los Mipres: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmMIPRES\\:tablaRegistrosMIPRES\\:j_idt653")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LOS MIPRES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Mipres visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER TUTELAS
    private void VerTutelas() throws InterruptedException {
        // INGRESAR PARA VER LAS TUTELAS

        esperar(1500);
        driver.findElement(By.id("frmVer:j_idt72")).click();
        System.out.println("Ingresando para ver las Tutelas...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Tutelas.");
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmTutelas\\:tablaRegistrosTutelas_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmTutelas\\:tablaRegistrosTutelas_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en las Tutelas: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmTutelas\\:tablaRegistrosTutelas\\:j_idt739")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LOS MIPRES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Tutelas visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER HOSPITALIZACIONES
    private void VerHospitalizaciones() throws InterruptedException {
        // INGRESAR PARA VER LAS HOSPITALIZACIONES
        esperar(1300);
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt73")).click();
        System.out.println("Ingresando para ver las Hospitalizaciones...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Hospitalizaciones.");
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmHospitalizacion\\:tablaRegistrosHospitalizacion_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmHospitalizacion\\:tablaRegistrosHospitalizacion_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en las Hospitalizaciones: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmHospitalizacion\\:tablaRegistrosHospitalizacion\\:j_idt808_input")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LAS HOSPITALIZACIONES
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Hospitalizaciones visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER DIRECCIONADOS
    private void VerDireccionados() throws InterruptedException {
        // INGRESAR PARA VER LOS DIRECCIONADOS
        driver.findElement(By.id("frmVer:j_idt74")).click();
        System.out.println("Ingresando para ver los direccionados...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.id("frmSelEmpBoton:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en los direccionados.");
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.id("frmDireccionado:tablaRegistrosDireccionado_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.id("frmDireccionado:tablaRegistrosDireccionado_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en los direccionados: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.id("frmDireccionado:tablaRegistrosDireccionado:j_idt1067")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LOS DIRECCIONADOS
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Direccionados visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // VER SOLICITUDES
    private void VerSolicitudes() throws InterruptedException {
        // INGRESAR PARA VER LOS DIRECCIONADOS
        driver.findElement(By.cssSelector("button#frmVer\\:j_idt75")).click();
        System.out.println("Ingresando para ver las Solicitudes...");
        esperar(500);


        // Verificar si el mensaje, está presente y visible
        WebElement mensajeGeneral = driver.findElement(By.cssSelector("div#frmSelEmpBoton\\:mensajeGeneral_container"));

        if (mensajeGeneral.isDisplayed()) {
            // Si el mensaje está visible, imprimir que no tiene registros y no realizar nada más
            System.out.println("No tiene registros disponibles en las Solicitudes.");
        } else {

            // NRO DE REGISTROS
            WebElement tbody = driver.findElement(By.cssSelector("tbody#frmSolicitudes\\:tablaRegistrosSolicitud_data"));
            tbody.click();
            List<WebElement> registros = driver.findElements(By.cssSelector("tbody#frmSolicitudes\\:tablaRegistrosSolicitud_data tr"));
            int totalRegistros = registros.size();
            System.out.println("Total de registros encontrados en las Solicitudes: " + totalRegistros);


            // CLILC CAMPO
            driver.findElement(By.cssSelector("input#frmSolicitudes\\:tablaRegistrosSolicitud\\:j_idt1482")).click();
            esperar(200);


            // ESPERAR UN TIEMPO PARA VALIDAR
            esperar(8000);


            // CERRAR DE VER LOS DIRECCIONADOS
            Actions action = new Actions(driver);
            WebElement closeButton = driver.findElement(By.cssSelector(".ui-dialog-titlebar-close"));
            action.moveToElement(closeButton).click().perform();
            esperar(200);
            System.out.println("Direccionados visualizadas y cerradas.");
        }
        System.out.println("--- ° ---");
    }

    // AUDITORIA
    private void VerAuditoria() throws InterruptedException {
        // INGRESAR PARA VER LOS DIRECCIONADOS
        driver.findElement(By.id("button#frmVer\\:auditoria")).click();
        System.out.println("Ingresando para ver las Solicitudes...");
        esperar(500);

        // Encontrar todos los elementos que coinciden con el selector CSS
        List<WebElement> mensajesGenerales = driver.findElements(By.id("div#frmSelEmpBoton\\:mensajeGeneral_container"));
        // Obtener el tamaño de la lista (número de elementos)
        int cantidadDeElementos = mensajesGenerales.size();
        // Mostrar la cantidad de elementos en la consola
        System.out.println("Cantidad de elementos encontrados: " + cantidadDeElementos);
        System.out.println("--- ° ---");
    }

    // GUARDAR
    // SE GUARDA EL CONTENIDO
    private void BotonGuardar() throws InterruptedException {
        driver.findElement(By.id("a#frmVer\\:pnlVerDatosPersonales_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerDatosAfiliacion_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerGeoreferenciacion_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerDatosEmergencia_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:j_idt196_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerIPSAtencionPrimaria_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerDatosSocioeconomicos_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerGrupoFamiliarAfiliado_toggler")).click();
        esperar(500);
        driver.findElement(By.id("a#frmVer\\:pnlVerPortabilidad_toggler")).click();
        esperar(500);
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

