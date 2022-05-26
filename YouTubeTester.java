import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileReader;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YouTubeTester {
    WebDriver driver;
    boolean isGetCookie = true;
    String cookiePath = "youtubecookie.json";
    String visitPath = "visit.json";
    String Area = "Japan";
    Set<String> keywords = new HashSet<String>() {{
        //add("柴犬 遊び");
        //add("柴犬 戦う");
        //add("柴犬 一人で");
        add("柴犬 ひと");
        //add("柴犬 歩く");
        //add("柴犬 走る");
    }};
    Set<String> urls = new HashSet<String>(){{}};

    public YouTubeTester(String site){
        System.setProperty("webdriver.chrome.driver","D:\\Files/drive/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(site);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /*Set Cookies*/
    public void SetCookies() throws IOException, ClassNotFoundException, InterruptedException {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory",".");
        options.setExperimentalOption("prefs",prefs);
        FileReader fr = new FileReader(new File(cookiePath));
        BufferedReader br = new BufferedReader(fr);
        String fileStr = "";
        String tempStr = br.readLine();
        while(tempStr!=null){
            fileStr += tempStr;
            tempStr = br.readLine();
        }
        JSONArray jo2 = new JSONArray(fileStr);
        for (int i = 0; i < jo2.length(); i++) {
            JSONObject jsonObject = jo2.getJSONObject(i);
            Cookie cookie = new Cookie(jsonObject.getString("name"), jsonObject.getString("value"));
            driver.manage().addCookie(cookie);
        }
        driver.navigate().refresh();
        Thread.sleep(3000);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void getAudio(String keyword) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement searchInput;
        WebElement contentContainer, tmpContent, processContent, singleVideo = null;
        WebElement searchBar = driver.findElement(By.id("search-input"));
        List<WebElement> webContent, videoList;
        //Actions act = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int childId = 0;
        boolean stay = true;
        String name, link, quit;
        String script = "return arguments[0].scrollIntoView();";
        searchInput = searchBar.findElement(By.xpath("./*"));
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        contentContainer = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-search/div[1]/ytd-two-column-search-results-renderer/div/ytd-section-list-renderer/div[2]"));
        while (stay) {
            webContent = contentContainer.findElements(By.xpath("./*"));
            tmpContent = webContent.get(webContent.size() - 1);
            if (Objects.equals(tmpContent.findElements(By.xpath("./*")).get(0).getAttribute("id"), "ghost-cards") && stay) {
                stay = true;
            } else {
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(keyword + ".txt", true));
                    for (String url : urls) {
                        if (url != null) {
                            bw.write(url);
                            bw.newLine();
                            bw.flush();
                        }
                    }
                    bw.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                break;
            }
            // process each audio!
            processContent = webContent.get(childId);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            processContent = processContent.findElement(By.id("contents"));
            videoList = processContent.findElements(By.xpath("./*"));
            for (int j = 0; j < videoList.size(); j++) {
                try {
                    singleVideo = videoList.get(j);
                    if (j == 0) {
                        quit = singleVideo.getText();
                        if (quit == "无更多结果") {
                            stay = false;
                            break;
                        }
                    }
                    singleVideo = singleVideo.findElement(By.id("video-title"));
                    link = singleVideo.getAttribute("href");
                    //System.out.println(link);
                    urls.add(link);
                } catch (Exception eee) {
                    System.out.println("Wrong in one video");
                }
            }
            js.executeScript(script, singleVideo);
            Thread.sleep(5000);
            childId += 1;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        YouTubeTester youtubeTester = new YouTubeTester("https://www.youtube.com");
        youtubeTester.SetCookies();
        for (String keyword: youtubeTester.keywords) {
            youtubeTester.getAudio(keyword);
        }
    }
}
