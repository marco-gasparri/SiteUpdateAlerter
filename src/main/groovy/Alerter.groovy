import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.digest.DigestUtils

import java.awt.Toolkit
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat

class Alerter {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static HttpRequest getReuqest;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Config config = new Config();

    public static void main(String[] args){

        if(args.size() == 1) {
            initConfig(args[0]);
        }
        else{
            System.out.println("Please, insert json config file path")
            return;
        }

        initRequest();

        String baseResponse = getMd5SiteBody();

        while (true){
            Thread.sleep(config.checkIntervalSec * 1000);
            String currentResponse = getMd5SiteBody();
            Date date = new Date();

            if(baseResponse != currentResponse){
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("Changed response: " + formatter.format(date));
                    return;
            }
            else{
                System.out.println("...still the same response: " + formatter.format(date));
            }
        }
    }

    private static void initConfig(String path){
        String jsonConfig = Files.readString(Path.of(path));
        ObjectMapper objectMapper = new ObjectMapper();
        config = objectMapper.readValue(jsonConfig, Config.class);
    }

    private static void initRequest(){
        getReuqest = HttpRequest.newBuilder().uri(URI.create(config.url)).GET().build();
    }

    private static String getMd5SiteBody(){
        HttpResponse<String> response = client.send(getReuqest, HttpResponse.BodyHandlers.ofString());
        return DigestUtils.md5Hex(response.body());
    }
}
