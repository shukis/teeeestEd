import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    private Process appium_Process;
    private static String userDir, fileName, app, devicePlatform, appPackage, appActivity;
    public static AndroidDriver driver = null;
    private static File appDir = null;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("BeforeSuite:");
        userDir = "/Users/" +
                System.getProperty("user.name") +
                "/Downloads/test_clients/";
        userDir = userDir.replace("\"", "");
        fileName = "ed.apk";
        appDir = new File(userDir, fileName);
        app = appDir.getAbsolutePath();
        devicePlatform = MobilePlatform.ANDROID;
        appPackage = "com.example.pavel.testappfored";
        appActivity = ".MainActivity";
        startLocalAppiumServer();
        System.out.println("BeforeSuite: end");

    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("BeforeTest()");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        System.out.println("Before method:");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver = AppiumDriverBuilder.configureAppiumAndroid(app, devicePlatform, "ce12160c130ae41504", appPackage, appActivity, "4725");
        System.out.println("Before method: end");
    }

    @AfterMethod(alwaysRun = true)
    public void AfterMethod() {
        System.out.println("AfterMethod method:");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("AfterTest():");
        System.out.println("Quit driver()...");
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("AfterSuite:");
        if (appium_Process != null) {
            try {
                System.out.println("closing Appium server 1");
                appium_Process.destroy();
            } catch (Exception e) {
                System.out.println("Appium server 1 FAILED to close");
                e.printStackTrace();
            }
            if (appium_Process.isAlive()) {
                System.out.println("closing Appium server 1 Forcibly");
                appium_Process.destroyForcibly();
            }
        }
    }

    private void startLocalAppiumServer() {
        String[] cmd;
        System.out.println("   kill Appium server");
        if (System.getProperty("os.name").contains("Windows")) {
            cmd = new String[]{"cmd.exe", "/c", "FOR /F \"tokens=5 delims= \" %P IN ('netstat -a -n -o ^| findstr :" + "4725" + "') DO TaskKill.exe /F /PID %P"};
        } else {
            cmd = new String[]{"sh", "-c", "lsof -P | grep ':" + "4725" + "' | awk '{print $2}' | xargs kill -9"};
        }
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            final Process p = pb.start();
            p.waitFor();
            p.destroy();
            if (p.isAlive()) {
                System.out.println("  executeShell(): closing Forcibly");
                p.destroyForcibly();
            }
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("   start Appium server");
        List<String> list = new ArrayList<>();
        System.out.println(System.getProperty("os.name"));
        if (System.getProperty("os.name").contains("Windows")) {
            list.add("appium.cmd");
        } else {
            list.add("appium");

        }
        list.add("--log-level");
        if (devicePlatform.contains("android")) {
            list.add("error");
        } else {
            list.add("error"); //iOS output FILTERS! check it below if need to see output correctly.
        }
        list.add("--port");
        list.add("4725");
        list.add("--bootstrap-port");
        list.add(String.valueOf(Integer.parseInt("4725") + 1000));
        list.add("--command-timeout");
        list.add("90");
        list.add("--session-override");
        list.add("--log-timestamp");

        System.out.println(" start appium as: " + list.toString());
        try {
            ProcessBuilder pb1 = new ProcessBuilder(list);

            pb1.redirectErrorStream(true);
            pb1.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            appium_Process = pb1.start();

            Thread.sleep(10000);
            System.out.println("  appium server started");
        } catch (Exception e) {
            System.out.println("  appium server start FAILED");
            e.printStackTrace();
        }

    }
}
