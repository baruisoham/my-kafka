package response;

import java.io.ByteArrayOutputStream;

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

    public byte[] getResponseHeaderAsBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write((correlation_id >> 24) & 0xFF); // high byte
        baos.write((correlation_id >> 16) & 0xFF); // next
        baos.write((correlation_id >> 8) & 0xFF);  // next
        baos.write(correlation_id & 0xFF);         // low byte
        return baos.toByteArray();
    }

    public int getSizeInBytes() {
        return getResponseHeaderAsBytes().length;
    }
}
