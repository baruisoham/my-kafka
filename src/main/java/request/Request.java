package request;

import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

import request.requestBody.ApiVersionsRequestBody;
import request.requestBody.IRequestBody;
import request.requestBody.RequestBodyFactory;

public class Request {
    private int message_size; // Java 'int' is always 4 bytes (32 bits)
    private RequestHeader header;
    private IRequestBody requestBody;

    public Request(int message_size, RequestHeader header, IRequestBody requestBody) {
        this.message_size = message_size;
        this.header = header;
        this.requestBody = requestBody;
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

    public IRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(IRequestBody requestBody) {
        this.requestBody = requestBody;
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

    public static Request getRequestFromBytes(BufferedInputStream in) throws java.io.IOException {
        // Convert byte array to Request object

        byte[] messageSizeBytes = in.readNBytes(4);
        int messageSize = ByteBuffer.wrap(messageSizeBytes).getInt();

        // Extract header bytes (after the first 4 bytes)
        RequestHeader header = RequestHeader.getRequestHeaderFromBytes(in);
        IRequestBody requestBody = RequestBodyFactory.createRequestBody(header, in);

        return new Request(messageSize, header, requestBody);
    }
}
