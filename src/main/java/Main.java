import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import request.Request;
import response.Header;
import response.Response;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;

    Header header = new Header(7);
    Response response = new Response(0, header);
    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      clientSocket = serverSocket.accept();
      // clientSocket.getOutputStream().write(response.getResponseAsBytes());
      byte[] requestBytes = clientSocket.getInputStream().readAllBytes();
      Request newRequest = Request.getRequestFromBytes(requestBytes);
      Response newResponse = new Response(0, new Header(newRequest.getCorrelationId()));
      // Send the response back to the client
      clientSocket.getOutputStream().write(newResponse.getResponseAsBytes());
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
}
