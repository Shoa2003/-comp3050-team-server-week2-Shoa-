import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MoveHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        
        he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        he.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        
        if ("OPTIONS".equals(he.getRequestMethod())) {
            he.sendResponseHeaders(204, -1);
            return;
        }

        String query = he.getRequestURI().getQuery();
        int dy = 0;
        int dx = 0;

        // Parse the URL query string to get movement distances (e.g., ?dy=-1&dx=0)
        // If the user sends an empty request (/move), it safely defaults to 0 for both dy and dx, meaning no movement.
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("dy")) {
                        dy = Integer.parseInt(pair[1]);
                    } else if (pair[0].equals("dx")) {
                        dx = Integer.parseInt(pair[1]);
                    }
                }
            }
        }

        // Validate movement distance
        // This prevents the player from moving diagonally or jumping multiple tiles at once
        // (e.g., |dy| + |dx| must be 1 or 0)
        if (Math.abs(dy) + Math.abs(dx) > 1) {
            send204(he);
            return;
        }

        int targetY = Test.playerY + dy;
        int targetX = Test.playerX + dx;

        // Security check: Ensure the new coordinates do not exceed the map boundaries
        // This prevents ArrayIndexOutOfBounds exceptions when rendering the map
        if (!GameMap.isInBounds(targetY, targetX)) {
            send204(he);
            return;
        }

        // Collision check: Check if the target tile is a wall, water, or locked door
        if (GameMap.isBlocking(targetY, targetX)) {
            send204(he);
            return;
        }

        // If all checks pass, update the player's position
        Test.playerY = targetY;
        Test.playerX = targetX;

        String response = String.format("{\"y\": %d, \"x\": %d}", Test.playerY, Test.playerX);
        
        he.getResponseHeaders().set("Content-Type", "application/json");
        he.sendResponseHeaders(200, response.getBytes().length);
        
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void send204(HttpExchange he) throws IOException {
        he.sendResponseHeaders(204, -1);
        he.close();
    }
}