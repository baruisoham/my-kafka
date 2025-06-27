package response;

public class ResponseHeader {
    private int correlation_id; // Signed 32-bit integer (Java 'int' is always signed 32 bits)

    public ResponseHeader(int correlation_id) {
        this.correlation_id = correlation_id;
    }

    public int getCorrelationId() {
        return correlation_id;
    }

    public void setCorrelationId(int correlation_id) {
        this.correlation_id = correlation_id;
    }
}
