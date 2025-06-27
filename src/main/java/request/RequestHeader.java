package request;

//request header v2
public class RequestHeader {
    private short request_api_key; // API key for request, 2 bytes
    private short request_api_version; // API version, 2 bytes
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

    public static RequestHeader getRequestHeaderFromBytes(byte[] requestHeaderBytes) {
        // Convert byte array to RequestHeader object
        if (requestHeaderBytes.length < 8) {
            throw new IllegalArgumentException("RequestHeader byte array is too short");
        }

        short apiKey = (short) ((requestHeaderBytes[0] << 8) | (requestHeaderBytes[1] & 0xFF)); //unsigned 16 bit integer
        short apiVersion = (short) ((requestHeaderBytes[2] << 8) | (requestHeaderBytes[3] & 0xFF));
        int correlationId = (requestHeaderBytes[4] << 24) | ((requestHeaderBytes[5] & 0xFF) << 16) |
                    ((requestHeaderBytes[6] & 0xFF) << 8) | (requestHeaderBytes[7] & 0xFF);
        String clientId = new String(requestHeaderBytes, 8, requestHeaderBytes.length - 8); // client id length needs to be fixed so that the rest of the bytes can go to tagBuffer
        
        // Assuming tag_buffer is empty for simplicity
        byte[] tagBuffer = null; // This should be replaced with actual logic to parse tags if needed
        
        return new RequestHeader(apiKey, apiVersion, correlationId, clientId, tagBuffer);
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
