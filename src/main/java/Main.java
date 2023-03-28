import net.infogram.api.InfogramAPI;
import net.infogram.api.response.Response;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        InfogramAPI infogram = new InfogramAPI(Secrets.API_KEY, Secrets.API_SECRET);
        try {
            Response response = infogram.sendRequest("GET", "infographics", null);
            System.out.println(response.getHeaders());
            if (response.isSuccessful()) {
                InputStream is = response.getResponseBody();
                System.out.println(new String(is.readAllBytes()));
            } else {
                String errmsg = String.format("The server returned %d %s", response.getHttpStatusCode(), response.getHttpStatusMessage());
                System.err.println(errmsg);
            }
        } catch (IOException e) {
            System.err.println("There was a problem connecting to the server");
            e.printStackTrace();
        }
    }
}
