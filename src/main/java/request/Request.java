package request;

//request header v2
public class Request {
    private short request_api_key; // API key for request, 2 bytes
    private short request_api_version; // API version, 2 bytes
    private int correlation_id; // Correlation ID, 4 bytes signed int - correlation ID is the same in request and its corresponding response. Done to match the request with the response.
    private String client_id;
    private byte[] tag_buffer; // Tag buffer, variable length, can be empty.

    public Request(short request_api_key, short request_api_version, int correlation_id, String client_id, byte[] tag_buffer) {
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

    public static Request getRequestFromBytes(byte[] requestBytes) {
        // Convert byte array to Request object
        // if (requestBytes.length < 10) {
        //     throw new IllegalArgumentException("Request byte array is too short");
        // }
        
        short apiKey = (short) ((requestBytes[0] << 8) | (requestBytes[1] & 0xFF)); //unsigned 16 bit integer
        short apiVersion = (short) ((requestBytes[2] << 8) | (requestBytes[3] & 0xFF));
        int correlationId = (requestBytes[4] << 24) | ((requestBytes[5] & 0xFF) << 16) |
                    ((requestBytes[6] & 0xFF) << 8) | (requestBytes[7] & 0xFF);
        String clientId = new String(requestBytes, 8, requestBytes.length - 8); // client id length needs to be fixed so that the rest of the bytes can go to tagBuffer
        
        // Assuming tag_buffer is empty for simplicity
        byte[] tagBuffer = null; // This should be replaced with actual logic to parse tags if needed
        
        return new Request(apiKey, apiVersion, correlationId, clientId, tagBuffer);
    }
}
