package tc3005b224.amazonconnectinsights.service;

import java.util.List;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;

@Service
public class RoutingProfileService extends BaseService {
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
        // The list of Routing Profiles, excluding the nextToken, hopefully the list is not too long.
        // If it is, we will need to implement pagination.
        List<RoutingProfileSummary> data = listRoutingProfilesResponse.routingProfileSummaryList();
        return data;
    }
}
