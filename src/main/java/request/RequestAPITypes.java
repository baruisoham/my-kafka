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
    Invalid((short)-1, (short)0, (short)0),
    ApiVersions((short)18, (short)0, (short)4),
    Produce((short)0, (short)0, (short)4),
    CreateTopics((short)2, (short)0, (short)4),
    Fetch((short)3, (short)0, (short)4),
    ListOffsets((short)4, (short)0, (short)4),
    Metadata((short)5, (short)0, (short)4);

    public short apiKey;
    public short supported_version_oldest;
    public short supported_version_newest;

    RequestAPITypes(short apiKey, short supported_version_oldest, short supported_version_newest) {
        this.apiKey = apiKey;
        this.supported_version_oldest = supported_version_oldest;
        this.supported_version_newest = supported_version_newest;
    }

    public short getApiKey() {
        return apiKey;
    }

    public short getSupportedVersionOldest() {
        return supported_version_oldest;
    }

    public short getSupportedVersionNewest() {
        return supported_version_newest;
    }

    static private HashMap<Short, RequestAPITypes> IdMapping = new HashMap<>();
    static private HashMap<String, RequestAPITypes> NameMapping = new HashMap<>();
    static{
        for (RequestAPITypes type : RequestAPITypes.values()) {
            IdMapping.put(type.apiKey, type);
            NameMapping.put(type.name(), type);
        }
    }

    public static RequestAPITypes getFromApiKey(short apiKey) {
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
