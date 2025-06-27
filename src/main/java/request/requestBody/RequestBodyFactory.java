package request.requestBody;

import java.io.BufferedInputStream;

import request.RequestAPITypes;
import request.RequestHeader;

public class RequestBodyFactory {

    /** Use this method to create request body
     * This method will create the request body based on the API type and version.
     * Then set the rest of the data in the request body as needed.
     */
    public static IRequestBody createRequestBody(RequestHeader header, BufferedInputStream in, int requestBodySizeInBytes) throws java.io.IOException {
        RequestAPITypes apiType = RequestAPITypes.getFromApiKey(header.getRequestApiKey());
        IRequestBody requestBody;
        // Only read the number of bytes required to create the request body.
        byte[] requestBodyInBytes = in.readNBytes(requestBodySizeInBytes);
        // TODO: use the requestBodyInBytes to parse the request body. Make parser methods for each API type if needed.
        switch (apiType) {
            case ApiVersions:
                requestBody = new ApiVersionsRequestBody();
                // parse the request body from requestBodyInBytes
                return requestBody; // For ApiVersions, the request body is empty.
            // Add cases for other API types as needed
            default:
                throw new IllegalArgumentException("Unknown API type requested: " + apiType);
        }
        
    }
}
