package tc3005b224.amazonconnectinsights.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connectcontactlens.ConnectContactLensClient;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;

/**
 * Base service class with common functionalities that might be used in most
 * services.
 *
 * @version 1.0
 * @author Diego Jacobo Djmr5
 */
public class BaseService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Value("${aws_accessKeyId}")
    private String accessKeyId;

    @Value("${aws_secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws_region}")
    private String region;

    @Value("${aws_connect_instanceId}")
    private String instanceId;
    
    /**
     * Decodes a URL encoded string.
     *
     * Example: decodeUrl("https%3A%2F%2Fwww.google.com") ->
     * "https://www.google.com"
     *
     * @param url
     * @return String as ASCII
     * @author Diego Jacobo Djmr5
     */
    protected String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * Convert a value to a target specific type.
     *
     * Example: convertValue("123", Integer.class) -> 123
     *
     * @param value
     * @param targetType
     * @return the value converted to the target type
     * @author Diego Jacobo Djmr5
     */
    protected Object convertValue(Object value, Class<?> targetType) {
        if (targetType == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (targetType == Long.class) {
            return Long.parseLong(value.toString());
        } else if (targetType == Short.class) {
            return Short.parseShort(value.toString());
        } else if (targetType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else {
            return value;
        }
    }

    /**
     * Check if the fields parameter contains the identifier field. An exception is
     * thrown if the identifier field is present because the identifier field cannot
     * be modified.
     *
     * @param fields
     * @return void
     * @throws IllegalArgumentException if the identifier field is present
     * @author Diego Jacobo Djmr5
     */
    protected void fieldsHasId(Map<String, Object> fields) {
        if (fields.containsKey("identifier")) {
            throw new IllegalArgumentException("The identifier field cannot be modified");
        }
    }

    /**
     * A function that maps the valid values from the inputted fields parameter to a
     * given Class
     * @param <T>
     * @param fields
     * @param entityInstance
     * @return
     */
    protected <T> T mapValuesToEntity(Map<String, Object> fields, T entityInstance) {
        // Get the class of the entity instance
        Class<?> clazz = entityInstance.getClass();
        // For each field in the fields map the value to the entity instance
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(clazz, key);
            field.setAccessible(true);
            // Convert the value to the appropriate type
            Class<?> fieldType = field.getType();
            Object convertedValue = convertValue(value, fieldType);
            // Set the field value to the new, converted value
            ReflectionUtils.setField(field, entityInstance, convertedValue);
        });
        return entityInstance;
    }

    protected ConnectClient getConnectClient(String accessKeyId, String secretAccessKey, Region region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return ConnectClient.builder()
                .credentialsProvider(() -> awsCreds)
                .region(region)
                .build();
    }

    private ConnectClientInfo mapConnectionToConnectClientInfo(Connection connection) {
        ConnectClientInfo clientInfo = new ConnectClientInfo();
        clientInfo.setIdentifier(connection.getIdentifier());
        clientInfo.setAccessKeyId(accessKeyId);
        clientInfo.setSecretAccessKey(secretAccessKey);
        clientInfo.setInstanceId(instanceId);
        clientInfo.setRegion(Region.of(region));
        clientInfo.setContactLensEnabled(false);
        clientInfo.setSupervisor(connection.getSupervisor());
        clientInfo.setUid(connection.getUid());
        return clientInfo;
    }

    protected ConnectClientInfo getConnectClientInfo(String userUuid) {
        // If token matches, returns data from the database
        Optional<Connection> optConnection = connectionRepository.findByUid(userUuid);
        
        if (!optConnection.isPresent()) {
            throw new IllegalArgumentException("Connection not found");
        }

        // TODO: Review and add verify contact lens via Endpoint
        Connection connection = optConnection.get();
        return mapConnectionToConnectClientInfo(connection);
    }

    protected ConnectClientInfo getConnectClientInfoByIdentifier(Integer identifier) {
        Optional<Connection> optConnection = connectionRepository.findById(identifier.shortValue());
        if (!optConnection.isPresent()) {
            throw new IllegalArgumentException("Connection not found");
        }

        Connection connection = optConnection.get();
        return mapConnectionToConnectClientInfo(connection);
    }

    protected ConnectContactLensClient getConnectContactLensClient(ConnectClientInfo clientInfo) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey());
        return ConnectContactLensClient.builder()
                .credentialsProvider(() -> awsCreds)
                .region(clientInfo.getRegion())
                .build();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    protected class ConnectClientInfo {
        private Integer identifier;
        private String accessKeyId;
        private String secretAccessKey;
        private String instanceId;
        private Region region;
        private Boolean contactLensEnabled;
        private String supervisor;
        private String uid;
    }
}

