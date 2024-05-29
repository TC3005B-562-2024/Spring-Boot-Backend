package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.SearchContactsRequest;
import software.amazon.awssdk.services.connect.model.SearchContactsResponse;
import software.amazon.awssdk.services.connect.model.SearchContactsTimeRange;
import software.amazon.awssdk.services.connect.model.SearchContactsTimeRangeType;
import software.amazon.awssdk.services.connect.model.SearchCriteria;
import tc3005b224.amazonconnectinsights.dto.contact.ContactDTO;

@Service
public class ContactService extends BaseService {
    /**
     * Find all contacts by user id of the last 2 hours.
     * 
     * @param userUuid
     * @param userId
     * @return
     */
    public List<ContactDTO> findAllContactsByUserId(String userUuid, String userId) {
        // Get the client info.
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Create the criterias to search the contacts.
        SearchCriteria cirterias = SearchCriteria.builder()
                .agentIds(userId)
                .build();

        // Create the request builder.
        SearchContactsRequest request = SearchContactsRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .searchCriteria(cirterias)
                .maxResults(100)
                .timeRange(SearchContactsTimeRange.builder()
                        .startTime(
                                Instant.now().minusSeconds(7200))
                        .endTime(
                                Instant.now().minusSeconds(10))
                        .type(
                                SearchContactsTimeRangeType.CONNECTED_TO_AGENT_TIMESTAMP)
                        .build())
                .build();

        // Get the contacts.
        SearchContactsResponse response = getConnectClient(
                clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(),
                clientInfo.getRegion())
                .searchContacts(request);

        List<ContactDTO> contacts = new ArrayList<>();
        response.contacts().forEach(contact -> {
            contacts.add(new ContactDTO(
                        contact.arn(),
                        contact.id(),
                        contact.initiationMethodAsString(),
                        contact.channelAsString(),
                        contact.queueInfo().id(),
                        contact.queueInfo().enqueueTimestamp().toString(),
                        contact.agentInfo().id(),
                        contact.agentInfo().connectedToAgentTimestamp().toString(),
                        contact.disconnectTimestamp() != null ? contact.disconnectTimestamp().toString() : null
            ));
        });
                
        return contacts;
    }
}
