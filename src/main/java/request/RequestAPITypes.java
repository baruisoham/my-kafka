package request;

import java.util.HashMap;

import utils.Constants;


// This is the request types that can be used in Kafka.

// This enum defines the different types of request APIs in Kafka.
// Each API type corresponds to a specific request that can be made to the Kafka server. Depending on these types, the request body changes.
// Each API type is identified by a unique API key, and has a range of supported versions.
// Each API type has an associated API key, the oldest supported version, and the newest supported version.
// The API key is used to identify the request type, and the version range is used to validate the request.
public enum RequestAPITypes {
    Invalid(-1, 0, 0),
    ApiVersions(18 , 0, 4),
    Produce(0, 0,4),
    CreateTopics(2, 0, 4),
    Fetch(3, 0, 4),
    ListOffsets(4, 0, 4),
    Metadata(5, 0, 4);

    public int apiKey;
    public int supported_version_oldest;
    public int supported_version_newest;
    
    RequestAPITypes(int apiKey, int supported_version_oldest, int supported_version_newest) {
        this.apiKey = apiKey;
        this.supported_version_oldest = supported_version_oldest;
        this.supported_version_newest = supported_version_newest;
    }

    public int getApiKey() {
        return apiKey;
    }

    public int getSupportedVersionOldest() {
        return supported_version_oldest;
    }

    public int getSupportedVersionNewest() {
        return supported_version_newest;
    }

    static private HashMap<Integer, RequestAPITypes> IdMapping = new HashMap<>();
    static private HashMap<String, RequestAPITypes> NameMapping = new HashMap<>();
    static{
        for (RequestAPITypes type : RequestAPITypes.values()) {
            IdMapping.put(type.apiKey, type);
            NameMapping.put(type.name(), type);
        }
    }

    public static RequestAPITypes getFromApiKey(int apiKey) {
        return IdMapping.get(apiKey);
    }

    public static RequestAPITypes getFromName(String name) {
        return NameMapping.get(name);
    }

    public short isVersionSupported(short versionRequested) { //check if the version requested for a request type is supported or not.
        if (versionRequested >= supported_version_oldest && versionRequested <= supported_version_newest) {
            return 0; // No error
        }
        return Constants.UNSUPPORTED_VERSION; // Error
    }

}
