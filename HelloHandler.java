import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
public class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        String response = "{\"message\":\"Hello from COMP3050!\"}";
        he.getResponseHeaders().set("Content-Type", "application/json");
        he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        he.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}