package tc3005b224.amazonconnectinsights.service;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.RoutingProfile;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;

/**
 * Service class that handles the Routing Profiles endpoints in Amazon Connect.
 * 
 * @version 1.0
 * @author Diego Jacobo Djmr5
 * @see BaseService
 */
@Service
public class RoutingProfileService extends BaseService {
    /**
     * Service that calls amazon connects ListRoutingProfiles and returns the list
     * of the Routing Profiles at the instance.
     * 
     * @param userUuid
     * @return Iterable<RoutingProfileSummary>
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see BaseService
     * @see RoutingProfileSummary
     */
    public Iterable<RoutingProfileSummary> getRoutingProfiles(String userUuid) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        // Build the request for the ListRoutingProfiles endpoint using the instanceId
        ListRoutingProfilesRequest listRoutingProfilesRequest = ListRoutingProfilesRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .build();
        // The response of the ListRoutingProfiles endpoint
        ListRoutingProfilesResponse listRoutingProfilesResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .listRoutingProfiles(listRoutingProfilesRequest);
        // The list of Routing Profiles, excluding the nextToken, hopefully the list is
        // not too long.
        // If it is, we will need to implement pagination.
        return listRoutingProfilesResponse.routingProfileSummaryList();
    }

    /**
     * Service that retrieves the Routing Profile information given its
     * routingProfileId.
     * 
     * @param userUuid
     * @param routingProfileId
     * @return RoutingProfile
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see BaseService
     * @see RoutingProfile
     */
    public RoutingProfile getRoutingProfile(String userUuid, String routingProfileId) {
        // Handle exceptions
        if (routingProfileId == null || routingProfileId.isEmpty()) {
            throw new IllegalArgumentException("routingProfileId cannot be null or empty");
        }
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Build and send the request to the DescribeRoutingProfile endpoint
        DescribeRoutingProfileRequest describeRoutingProfileRequest = DescribeRoutingProfileRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .routingProfileId(routingProfileId)
                .build();
        DescribeRoutingProfileResponse describeRoutingProfileResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeRoutingProfile(describeRoutingProfileRequest);

        // Get the Routing Profile data
        return describeRoutingProfileResponse.routingProfile();
    }
}
