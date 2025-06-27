package response.responseBody;

import java.io.ByteArrayOutputStream;

import request.RequestAPITypes;

public class ApiVersionsResponseBody implements IResponseBody {
    // This class represents the request body for the ApiVersions API
    private short error_code;
    private byte num_api_keys = 0; // This is the number of API keys supported by the broker. For ApiVersions, it is always 1.
    private short api_key; // This is the API key for ApiVersions, which is a constant value defined in RequestAPITypes.
    private short min_version;
    private short max_version;
    private byte tagged_fields; // This is the number of tagged fields in the response. For ApiVersions, it is always 0.
    private int throttle_time_ms; // This is the time in milliseconds that the client should wait before retrying the request. For ApiVersions, it is always 0.

    private ApiVersionsResponseBody() {}

    public ApiVersionsResponseBody(short versionRequested) {
        // Default constructor
        this.error_code = 0; // Assuming no error by default
        error_code = RequestAPITypes.ApiVersions.isVersionSupported(versionRequested); // for all other request types also, use isVersionSupported() in constructor
        // if the version is not supported, we have set error_code = UNSUPPORTED_VERSION
        num_api_keys = 2; // For ApiVersions, we have only one API key. num_api_key= 1 + 1
        api_key = RequestAPITypes.ApiVersions.getApiKey(); // Get the API key for ApiVersions
        min_version = RequestAPITypes.ApiVersions.getSupportedVersionOldest();
        max_version = RequestAPITypes.ApiVersions.getSupportedVersionNewest();
        tagged_fields = 0; // For ApiVersions, we have no tagged fields
        throttle_time_ms = 0; // For ApiVersions, we have no throttle time
    }

    public short getErrorCode() {
        return error_code;
    }

    public void setErrorCode(short error_code) {
        this.error_code = error_code;
    }
    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Serialize error_code (short)
        baos.write((error_code >> 8) & 0xFF);
        baos.write(error_code & 0xFF);
        // Serialize num_api_keys (byte)
        baos.write(num_api_keys & 0xFF);
        // Serialize api_key (short)
        baos.write((api_key >> 8) & 0xFF);
        baos.write(api_key & 0xFF);
        // Serialize min_version (short)
        baos.write((min_version >> 8) & 0xFF);
        baos.write(min_version & 0xFF);
        // Serialize max_version (short)
        baos.write((max_version >> 8) & 0xFF);
        baos.write(max_version & 0xFF);
        // Serialize tagged_fields (byte)
        baos.write(tagged_fields & 0xFF);
        // Serialize throttle_time_ms (int)
        baos.write((throttle_time_ms >> 24) & 0xFF);
        baos.write((throttle_time_ms >> 16) & 0xFF);
        baos.write((throttle_time_ms >> 8) & 0xFF);
        baos.write(throttle_time_ms & 0xFF);

        return baos.toByteArray();
    }
}
