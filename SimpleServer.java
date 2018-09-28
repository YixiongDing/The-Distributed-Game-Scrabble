import java.io.IOException;

// SimpleServer.java: a simple server program

public class SimpleServer {
 public static void main(String args[]) throws IOException {
   // Register service on port 1234
	 Server s = new Server();
	 s.run();
 }
 
}
        