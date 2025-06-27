package response;

import java.io.ByteArrayOutputStream;

import request.RequestHeader;
import response.responseBody.IResponseBody;
import response.responseBody.ResponseBodyFactory;

//response header v0
public class Response {
    // message_size is explicitly a 4-byte (32-bit) integer
    private int message_size; // Java 'int' is always 4 bytes (32 bits)
    private ResponseHeader header;
    private IResponseBody responseBody; // This can be used to hold the body of the response.

    public Response(ResponseHeader header, IResponseBody responseBody) {
        this.message_size = header.getSizeInBytes() + responseBody.getSizeInBytes(); // message_size includes the size of the header and body
        this.header = header;
        this.responseBody = responseBody;
    }

    public int getMessageSize() {
        return message_size;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setMessageSize(int message_size) {
        this.message_size = message_size;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public byte[] getResponseAsBytes() {
        // Convert the message size and header to a byte array
        // we need a byte array, but we want it to be dynamic in size
        // vector<byte> responseBytes = new byte[Integer.BYTES + Integer.BYTES]; // 4 bytes for message_size + 4 bytes for correlation_id
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write((byte) (message_size >> 24));
            baos.write((byte) (message_size >> 16));
            baos.write((byte) (message_size >> 8));
            baos.write((byte) (message_size));

            baos.write(header.getResponseHeaderAsBytes()); // Get the header bytes

            baos.write(responseBody.toBytes()); // Get the response body bytes

            byte[] result = baos.toByteArray(); // Get the dynamic byte array
            return result;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to write response bytes", e);
        }
    }

    public static Response getResponseFromBytes(byte[] responseBytes, RequestHeader requestHeader) {
        // Convert byte array to Response object
        if (responseBytes.length < 8) {
            throw new IllegalArgumentException("Response byte array is too short");
        }
        
        final int messageSize = (responseBytes[0] << 24) | ((responseBytes[1] & 0xFF) << 16) |
                          ((responseBytes[2] & 0xFF) << 8) | (responseBytes[3] & 0xFF);
        
        final int correlationId = (responseBytes[4] << 24) | ((responseBytes[5] & 0xFF) << 16) |
                            ((responseBytes[6] & 0xFF) << 8) | (responseBytes[7] & 0xFF);   //correlation_id is unsigned. So the first byte is treated as signed, the rest are treated as unsigned.

        final ResponseHeader header = new ResponseHeader(correlationId);

        // TODO: use messageSize to understand how much to read for response body
        int responseBodySize = messageSize - header.getSizeInBytes();
        IResponseBody responseBody = ResponseBodyFactory.createResponseBody(requestHeader);

        return new Response(header, responseBody);
    }
}
