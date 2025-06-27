package request;

public class Request {
    private int message_size; // Java 'int' is always 4 bytes (32 bits)
    private RequestHeader header;

    public Request(int message_size, RequestHeader header) {
        this.message_size = message_size;
        this.header = header;
    }

    public int getMessageSize() {
        return message_size;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public void setMessageSize(int message_size) {
        this.message_size = message_size;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public byte[] getRequestAsBytes() {
        // Convert the message size and header to a byte array
        byte[] requestBytes = new byte[Integer.BYTES + header.getSizeInBytes()];
        requestBytes[0] = (byte) (message_size >> 24);
        requestBytes[1] = (byte) (message_size >> 16);
        requestBytes[2] = (byte) (message_size >> 8);
        requestBytes[3] = (byte) (message_size);

        byte[] headerBytes = header.getRequestHeaderAsBytes();
        System.arraycopy(headerBytes, 0, requestBytes, Integer.BYTES, headerBytes.length);

        return requestBytes;
    }

    public static Request getRequestFromBytes(byte[] requestBytes) {
        // Convert byte array to Request object
        if (requestBytes.length < 8) {
            throw new IllegalArgumentException("Request byte array is too short");
        }

        int messageSize = (requestBytes[0] << 24) | ((requestBytes[1] & 0xFF) << 16) |
                ((requestBytes[2] & 0xFF) << 8) | (requestBytes[3] & 0xFF);

        // Extract header bytes (after the first 4 bytes)
        byte[] headerBytes = new byte[requestBytes.length - Integer.BYTES];  // header starts after the first 4 bytes (message_size)
        System.arraycopy(requestBytes, Integer.BYTES, headerBytes, 0, headerBytes.length);
        RequestHeader header = RequestHeader.getRequestHeaderFromBytes(headerBytes);
        return new Request(messageSize, header);
    }
}
