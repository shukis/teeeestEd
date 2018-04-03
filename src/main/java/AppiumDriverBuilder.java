import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumDriverBuilder {
    private static AndroidDriver androidDriver = null;
    private static DesiredCapabilities capabilities = null;
    private static String errorMessage;

    public static AndroidDriver configureAppiumAndroid(String app, String devicePlatform, String deviceName, String appPackage, String appActivity, String port) {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, devicePlatform);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.UDID, deviceName);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
        capabilities.setCapability(MobileCapabilityType.APP, app);
        createAndroidDriver(port, capabilities);
        return androidDriver;
    }

    private static void createAndroidDriver(String port, DesiredCapabilities capabilities) {
        String appiumUrl = "http://0.0.0.0:";
        String minorUrl = "/wd/hub";
        try {
            androidDriver = new AndroidDriver(new URL(appiumUrl + port + minorUrl), capabilities);
        } catch (MalformedURLException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }

    }


}
