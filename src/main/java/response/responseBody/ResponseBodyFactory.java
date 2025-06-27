package response.responseBody;

import java.io.BufferedInputStream;

import request.RequestAPITypes;
import request.RequestHeader;

public class ResponseBodyFactory {

    /** Use this method to create request body
     * This method will create the request body based on the API type and version.
     * Then set the rest of the data in the request body as needed.
     */
    public static IResponseBody createResponseBody(RequestHeader header) {
        RequestAPITypes apiType = RequestAPITypes.getFromApiKey(header.getRequestApiKey());
        short apiVersion = header.getRequestApiVersion(); // get the request api version, check if the version is supported while constructing the body. If not supported, send error code.
        // byte[] requestBodyInBytes = in.readNBytes(requestBodySizeInBytes);
        // TODO: use the requestBodyInBytes to parse the request body. Make parser methods for each API type if needed.
        switch (apiType) {
            case ApiVersions:
                return new ApiVersionsResponseBody(apiVersion); // the constructor sets the error code automatically
            // Add cases for other API types as needed
            default:
                throw new IllegalArgumentException("Unknown API type: " + apiType);
        }
    }

    
}
