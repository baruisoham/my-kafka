package response;

//response header v0
public class Response {
    // message_size is explicitly a 4-byte (32-bit) integer
    private int message_size; // Java 'int' is always 4 bytes (32 bits)
    private Header header;

    public Response(int message_size, Header header) {
        this.message_size = message_size;
        this.header = header;
    }

    public int getMessageSize() {
        return message_size;
    }

    public Header getHeader() {
        return header;
    }

    public void setMessageSize(int message_size) {
        this.message_size = message_size;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getResponseAsBytes() {
        // Convert the message size and header to a byte array
        byte[] responseBytes = new byte[Integer.BYTES + Integer.BYTES]; // 4 bytes for message_size + 4 bytes for correlation_id
        responseBytes[0] = (byte) (message_size >> 24);
        responseBytes[1] = (byte) (message_size >> 16);
        responseBytes[2] = (byte) (message_size >> 8);
        responseBytes[3] = (byte) (message_size);
        
        int correlationId = header.getCorrelationId();
        responseBytes[4] = (byte) (correlationId >> 24);
        responseBytes[5] = (byte) (correlationId >> 16);
        responseBytes[6] = (byte) (correlationId >> 8);
        responseBytes[7] = (byte) (correlationId);

        return responseBytes;
    }

    public static Response getResponseFromBytes(byte[] responseBytes) {
        // Convert byte array to Response object
        if (responseBytes.length < 8) {
            throw new IllegalArgumentException("Response byte array is too short");
        }
        
        final int messageSize = (responseBytes[0] << 24) | ((responseBytes[1] & 0xFF) << 16) |
                          ((responseBytes[2] & 0xFF) << 8) | (responseBytes[3] & 0xFF);
        
        final int correlationId = (responseBytes[4] << 24) | ((responseBytes[5] & 0xFF) << 16) |
                            ((responseBytes[6] & 0xFF) << 8) | (responseBytes[7] & 0xFF);   //correlation_id is unsigned. So the first byte is treated as signed, the rest are treated as unsigned.

        final Header header = new Header(correlationId);
        return new Response(messageSize, header);
    }
}
