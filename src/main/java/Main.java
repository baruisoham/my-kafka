import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      clientSocket = serverSocket.accept();
      BufferedInputStream inputStreamFromClient = new BufferedInputStream(clientSocket.getInputStream());
      BufferedOutputStream outStreamToClient = new BufferedOutputStream(clientSocket.getOutputStream());
      Request newRequest = StreamUtils.receiveRequest(inputStreamFromClient);
      int correlationId = newRequest.getHeader().getCorrelationId();
      Response newResponse = new Response(0, new Header(correlationId));
      StreamUtils.sendResponse(outStreamToClient, newResponse); // to send response back to the client
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
