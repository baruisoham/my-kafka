import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import request.Request;
import request.RequestAPITypes;
import response.Response;

public class StreamUtils {
    public static Request receiveRequest(BufferedInputStream inputStreamFromClient) throws IOException{
        return Request.getRequestFromBytes(inputStreamFromClient);
    }

    public static void sendResponse(BufferedOutputStream outputStreamToClient, Response response) throws IOException {
        outputStreamToClient.write(response.getResponseAsBytes());
        outputStreamToClient.flush(); // sometimes data we want to send is still in the buffer, so we need to flush it. So data is sent immediately.
    }
}
