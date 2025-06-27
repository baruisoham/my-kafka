package response.responseBody;

import java.io.ByteArrayOutputStream;

import request.RequestAPITypes;

public class ApiVersionsResponseBody implements IResponseBody {
    // This class represents the request body for the ApiVersions API
    private short error_code;

    private ApiVersionsResponseBody() {}

    public ApiVersionsResponseBody(short versionRequested) {
        // Default constructor
        this.error_code = 0; // Assuming no error by default
        error_code = RequestAPITypes.ApiVersions.isVersionSupported(versionRequested); // for all other request types also, use isVersionSupported() in constructor
        // if the version is not supported, we have set error_code = UNSUPPORTED_VERSION
    }

    public short getErrorCode() {
        return error_code;
    }

    public void setErrorCode(short error_code) {
        this.error_code = error_code;
    }

    public byte[] toBytes() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write((error_code >> 8) & 0xFF); // high byte
    baos.write(error_code & 0xFF);        // low byte
    return baos.toByteArray();
}

}
