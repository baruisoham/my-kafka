package request;

import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

//request header v2
public class RequestHeader {
    private short request_api_key; // Every Kafka request is an API call. The Kafka protocol defines over 70 different APIs, all of which do different things. 
    private short request_api_version; // Every API call has a version. If I send a request for an older version of API, server will respond with a response of the same old version.
    private int correlation_id; // Correlation ID, 4 bytes signed int - correlation ID is the same in request and its corresponding response. Done to match the request with the response.
    private String client_id;
    private byte[] tag_buffer; // Tag buffer, variable length, can be empty.

    public RequestHeader(short request_api_key, short request_api_version, int correlation_id, String client_id, byte[] tag_buffer) {
        this.request_api_key = request_api_key;
        this.request_api_version = request_api_version;
        this.correlation_id = correlation_id;
        this.client_id = client_id;
        this.tag_buffer = tag_buffer;
    }

    public short getRequestApiKey() {
        return request_api_key;
    }

    public void setRequestApiKey(short request_api_key) {
        this.request_api_key = request_api_key;
    }

    public short getRequestApiVersion() {
        return request_api_version;
    }

    public void setRequestApiVersion(short request_api_version) {
        this.request_api_version = request_api_version;
    }

    public int getCorrelationId() {
        return correlation_id;
    }

    public void setCorrelationId(int correlation_id) {
        this.correlation_id = correlation_id;
    }

    public String getClientId() {
        return client_id;
    }

    public void setClientId(String client_id) {
        this.client_id = client_id;
    }

    public byte[] getTagBuffer() {
        return tag_buffer;
    }

    public void setTagBuffer(byte[] tag_buffer) {
        this.tag_buffer = tag_buffer;
    }

    public static RequestHeader getRequestHeaderFromBytes(BufferedInputStream in) {
        // Convert byte array to RequestHeader object
        try {
            //in.ReadBytes gives us bytes. These bytes are then converted to the appropriate data types.
            short apiKey = ByteBuffer.wrap(in.readNBytes(2)).getShort(); //unsigned 16 bit integer
            short apiVersion = ByteBuffer.wrap(in.readNBytes(2)).getShort();
            int correlationId = ByteBuffer.wrap(in.readNBytes(4)).getInt();
            
            // TODO: Replace 1 with the actual clientId length as per protocol
            int clientIdLength = 1; // Example fixed length, adjust as needed
            byte[] clientIdBytes = in.readNBytes(clientIdLength);
            String clientId = new String(clientIdBytes); // client id length needs to be fixed so that the rest of the bytes can go to tagBuffer

            // Assuming tag_buffer is empty for simplicity
            byte[] tagBuffer = null; // This should be replaced with actual logic to parse tags if needed
            
            return new RequestHeader(apiKey, apiVersion, correlationId, clientId, tagBuffer);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read RequestHeader from stream", e);
        }
    }

    public int getSizeInBytes() {
        // Calculate the size of the request header in bytes
        return getRequestHeaderAsBytes().length;
    }

    public byte[] getRequestHeaderAsBytes() {
        // Convert the request header to a byte array
        byte[] clientIdBytes = client_id.getBytes();
        int tagBufferLength = (tag_buffer != null) ? tag_buffer.length : 0;
        byte[] requestHeaderBytes = new byte[8 + clientIdBytes.length + tagBufferLength];

        requestHeaderBytes[0] = (byte) (request_api_key >> 8);
        requestHeaderBytes[1] = (byte) (request_api_key);
        requestHeaderBytes[2] = (byte) (request_api_version >> 8);
        requestHeaderBytes[3] = (byte) (request_api_version);
        requestHeaderBytes[4] = (byte) (correlation_id >> 24);
        requestHeaderBytes[5] = (byte) (correlation_id >> 16);
        requestHeaderBytes[6] = (byte) (correlation_id >> 8);
        requestHeaderBytes[7] = (byte) (correlation_id);

        System.arraycopy(clientIdBytes, 0, requestHeaderBytes, 8, clientIdBytes.length);
        
        if (tag_buffer != null && tag_buffer.length > 0) {
            System.arraycopy(tag_buffer, 0, requestHeaderBytes, 8 + clientIdBytes.length, tagBufferLength);
        }

        return requestHeaderBytes;
    }
}
