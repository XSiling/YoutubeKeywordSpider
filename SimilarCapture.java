import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONException;
//import org.json.JSONObject;
import java.io.FileReader;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.support.ui.Select;

import java.io.*;
//import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
public class SimilarCapture {
    WebDriver driver;
    boolean isGetCookie = true;
    String cookiePath = "youtubecookie.json";
    String filePath = "tovisit4.txt";
    String storePath = "similarurl4.txt";
    Set<String> urls = new HashSet<String>(){{}};

    public SimilarCapture(String site){
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


    public void GetSimilar() throws IOException {
        WebElement usrChannel, newVideoHeader, singleVideo, newVideo, newVideoTitleElement, videoTab;
        List<WebElement> newVideoList;
        String usrUrl, newVideoUrl, newVideoTitle;
        FileReader pathfr = new FileReader(new File(filePath));
        BufferedReader pathbr = new BufferedReader(pathfr);
        BufferedWriter bw;
        String visitUrl = pathbr.readLine();
        int count = 0, videoCount = 0;
        while(visitUrl!=null){
            try{
            driver.get(visitUrl);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            //usrChannel = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[5]/div[1]/div/div[2]/div[7]/div[2]/div[2]/ytd-video-secondary-info-renderer/div/div/ytd-video-owner-renderer/div[1]/ytd-channel-name/div/div/yt-formatted-string"));
            //usrChannel = driver.findElement(By.id("meta-contents")).findElement(By.id("channel-name")).findElement(By.xpath("//*[@id=\"text\"]/a)"));
            //usrChannel = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[5]/div[1]/div/div[2]/div[7]/div[2]/div[2]/ytd-video-secondary-info-renderer/div/div/ytd-video-owner-renderer/div[1]/ytd-channel-name/div/div/yt-formatted-string/a"));
            if (visitUrl.contains("shorts")){
                visitUrl = pathbr.readLine();
                videoCount += 1;
                System.out.println(videoCount);
                continue;
            }else{
            usrChannel = driver.findElement(By.className("ytd-video-owner-renderer"));
            }
            usrUrl = usrChannel.getAttribute("href");
            driver.get(usrUrl);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            videoTab = driver.findElement(By.id("tabsContent"));
            videoTab = videoTab.findElements(By.xpath("./*")).get(3);
            videoTab.findElement(By.xpath("./*")).click();

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            newVideoHeader = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-browse/ytd-two-column-browse-results-renderer/div[1]/ytd-section-list-renderer/div[2]/ytd-item-section-renderer/div[3]/ytd-grid-renderer/div[1]"));
            //newVideoHeader = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-browse/ytd-two-column-browse-results-renderer/div[1]/ytd-section-list-renderer/div[2]/ytd-item-section-renderer/div[3]/ytd-shelf-renderer/div[1]/div[2]/yt-horizontal-list-renderer/div[2]/div"));
            newVideoList = newVideoHeader.findElements(By.xpath("./*"));

            for (int i = 0; i < newVideoList.size()-1; i++) {
                singleVideo = newVideoList.get(i);
                newVideo = singleVideo.findElement(By.id("thumbnail"));
                newVideoUrl = newVideo.getAttribute("href");
                // here add a name selector
                newVideoTitleElement = singleVideo.findElement(By.id("video-title"));
                newVideoTitle = newVideoTitleElement.getText();
                if (newVideoTitle.contains("Shiba Inu") || newVideoTitle.contains("Shiba inu") || newVideoTitle.contains("shiba inu") || newVideoTitle.contains("SHIBA INU")){
                    urls.add(newVideoUrl);
                    count +=1 ;
                }
                else
                {
                    if (newVideoTitle.contains("柴犬")){
                        urls.add(newVideoUrl);
                        count += 1;
                    }
                }
            }
                //urls.add(newVideoUrl);
            visitUrl = pathbr.readLine();
            videoCount += 1;
            System.out.print("video");
            System.out.println(videoCount);}
            catch(Exception eee){
                visitUrl = pathbr.readLine();
                videoCount += 1;
                System.out.println(videoCount);
            }
        }

        bw = new BufferedWriter(new FileWriter(storePath, true));
        for (String url : urls) {
            if (url != null) {
                bw.write(url);
                bw.newLine();
                bw.flush();
            }
        }
        bw.close();
    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        SimilarCapture similarcapture = new SimilarCapture("https://www.youtube.com");
        similarcapture.SetCookies();
        similarcapture.GetSimilar();
    }
}
