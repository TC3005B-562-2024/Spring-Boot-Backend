package tc3005b224.amazonconnectinsights.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.GetCurrentUserDataRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataResponse;
import software.amazon.awssdk.services.connect.model.SearchUsersRequest;
import software.amazon.awssdk.services.connect.model.SearchUsersResponse;
import software.amazon.awssdk.services.connect.model.SearchUsersRequest.Builder;
import software.amazon.awssdk.services.connect.model.StringCondition;
import software.amazon.awssdk.services.connect.model.UpdateUserRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.UserData;
import software.amazon.awssdk.services.connect.model.UserDataFilters;
import software.amazon.awssdk.services.connect.model.UserSearchCriteria;
import software.amazon.awssdk.services.connect.model.UserSearchSummary;

/**
 * Service class that handles the Users endpoints in Amazon Connect.
 * 
 * @version 1.0
 * @author Diego Jacobo Djmr5
 * @see BaseService
 */
@Service
public class UserService extends BaseService {
    /**
     * Service that calls amazon connects SearchUsers and returns the complete list
     * of
     * users at the instance.
     * 
     * @param userUuid
     * @return List<UserSearchSummary>
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see UserSearchSummary
     */
    public List<UserSearchSummary> searchUsers(String userUuid) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Make the request
        SearchUsersResponse searchUsersResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion()).searchUsers(
                        SearchUsersRequest.builder().instanceId(clientInfo.getInstanceId())
                                .maxResults(500).build());

        // Return the list of users
        return searchUsersResponse.users();
    }

    /**
     * Service that calls amazon connects SearchUsers and returns the list of users
     * at the instance filtered by the routingProfileId.
     * 
     * @param userUuid
     * @param routingProfileId
     * @return List<UserSearchSummary>
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see UserSearchSummary
     */
    public List<UserSearchSummary> searchUsers(String userUuid, String routingProfileId) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Build the request for the SearchUsers endpoint using the instanceId
        Builder searchUserRequest = SearchUsersRequest.builder().instanceId(clientInfo.getInstanceId())
                .maxResults(500);

        // If a resourceId is provided, filter the agents by that resourceId
        UserSearchCriteria criteria = UserSearchCriteria.builder().stringCondition(
                StringCondition.builder().comparisonType("EXACT").fieldName("RoutingProfileId")
                        .value(routingProfileId)
                        .build())
                .build();
        searchUserRequest.searchCriteria(criteria);

        // Make the request
        SearchUsersResponse searchUsersResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .searchUsers(searchUserRequest.build());

        // Return the list of users
        return searchUsersResponse.users();
    }

    /**
     * Service that calls amazon connects SearchUsers and returns the list of users
     * at the instance filtered by certain criterias.
     * 
     * @param userUuid
     * @param criterias
     * @return List<UserSearchSummary>
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see UserSearchSummary
     */
    public List<UserSearchSummary> searchUsers(String userUuid, List<UserSearchCriteria> criterias) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Build the request for the SearchUsers endpoint using the instanceId
        Builder searchUserRequest = SearchUsersRequest.builder().instanceId(clientInfo.getInstanceId())
                .maxResults(500);

        // Add the criterias to the request
        searchUserRequest.searchCriteria(UserSearchCriteria.builder().andConditions(criterias).build());

        // Make the request
        SearchUsersResponse searchUsersResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .searchUsers(searchUserRequest.build());

        // Return the list of users
        return searchUsersResponse.users();
    }

    /**
     * Service that calls amazon connects GetCurrentUserData and returns the list of
     * users at the instance filtered by the userIds.
     * 
     * @param userUuid
     * @param userIds
     * @return List<UserData>
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see UserData
     */
    public List<UserData> getCurrentUserData(String userUuid, Collection<String> userIds) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Make the request
        GetCurrentUserDataResponse getCurrentUserDataResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .getCurrentUserData(GetCurrentUserDataRequest.builder()
                        .instanceId(clientInfo.getInstanceId())
                        .filters(UserDataFilters.builder().agents(userIds).build())
                        .build());

        // Return the list of users
        return getCurrentUserDataResponse.userDataList();
    }

    /**
     * Service that calls amazon connects UpdateUserRoutingProfile and transfers an
     * agent to a different routing profile.
     * 
     * @param userUuid
     * @param agentId
     * @param destinationRoutingProfileId
     * 
     * @author Diego Jacobo Djmr5
     */
    public void transfer(String userUuid, String agentId, String destinationRoutingProfileId) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Build the request for the UpdateUserRoutingProfile endpoint
        UpdateUserRoutingProfileRequest updateUserRoutingProfileRequest = UpdateUserRoutingProfileRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .userId(agentId)
                .routingProfileId(destinationRoutingProfileId)
                .build();
        
        // Make the request
        getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .updateUserRoutingProfile(updateUserRoutingProfileRequest);
    }
}
