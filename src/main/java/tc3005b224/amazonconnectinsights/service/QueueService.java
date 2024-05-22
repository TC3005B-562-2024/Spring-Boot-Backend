package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeQueueResponse;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataResponse;
import software.amazon.awssdk.services.connect.model.DescribeQueueRequest;
import software.amazon.awssdk.services.connect.model.SearchQueuesRequest;
import software.amazon.awssdk.services.connect.model.SearchQueuesResponse;
import software.amazon.awssdk.services.connect.model.UserDataFilters;
import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;

@Service
public class QueueService extends BaseService {
    @Autowired
    private MetricService metricService;

    /**
     * Get all the queues general information required to display at the queue cards
     * if more specific information is required, use the findById method
     * Amount of API calls: 1 + 2n + c where n is the amount of agents
     * and c is the amount of total contacts all agents have
     * this is the worst case scenario where no contact has a negative sentiment
     * 
     * @param token
     * @param resourceId
     * @return Iterable<QueueCardDTO>
     * @throws BadRequestException
     * 
     * @author Moisés Adame
     * 
     * @see QueueCardDTO
     */
    public Iterable<QueueCardDTO> findAll(String token, String resourceId) throws BadRequestException {
        // Buld the request for getting the queues.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        SearchQueuesRequest.Builder searchQueuesRequest = SearchQueuesRequest.builder().instanceId(clientInfo.getInstanceId());
        SearchQueuesResponse queues = getConnectClient(
            clientInfo.getAccessKeyId(), 
            clientInfo.getSecretAccessKey(), 
            clientInfo.getRegion()
        ).searchQueues(searchQueuesRequest.build());

        // Create the an array of the queue of the queue id's.
        Collection<String> queueIds = new ArrayList<String>();
        queues.queues().forEach(
            queue -> {
                queueIds.add(queue.queueId());
            }
        );

        // Instantiate an iterarble of QueueCardDTO
        List<QueueCardDTO> queueCardDTOs = new ArrayList<QueueCardDTO>();

        // Iterate over the queue id's and get the queue information.
        queues.queues().forEach(
            queue -> {
                // Request for describing an individual queue.
                DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queue.queueId());
                DescribeQueueResponse queuesInfo = getConnectClient(
                    clientInfo.getAccessKeyId(), 
                    clientInfo.getSecretAccessKey(), 
                    clientInfo.getRegion()
                ).describeQueue(describeQueueRequest.build());

                // Create a new QueueCardDTO object and add it to the iterable.
                QueueCardDTO queueCardDTO = new QueueCardDTO(
                    queuesInfo.queue().queueId(),
                    queuesInfo.queue().queueArn(),
                    queuesInfo.queue().name(),
                    queuesInfo.queue().description(),
                    queuesInfo.queue().status().toString(),
                    queuesInfo.queue().maxContacts(),
                    findAgentsInQueue(token, queuesInfo.queue().queueId())
                );

                queueCardDTOs.add(queueCardDTO);
            }
        );

        return queueCardDTOs;
    }

    /**
     * Get the number of agents in a queue using GetCurrentUserData
     * 
     * @param token
     * @param queueId
     * @return Integer
     * 
     * @author Moisés Adame
     * 
     */
    public Integer findAgentsInQueue(String token, String queueId) {
        // Request for getting the users in the instance.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        GetCurrentUserDataRequest.Builder searchUsersRequest = GetCurrentUserDataRequest.builder()
            .instanceId(clientInfo.getInstanceId())
            .filters(
                UserDataFilters.builder()
                    .queues(
                        new ArrayList<String>() {{
                            add(queueId);
                        }}
                    )
                    .build()
            );
        GetCurrentUserDataResponse userDataValues = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).getCurrentUserData(searchUsersRequest.build());

        return userDataValues.userDataList().size();
    }

    /**
     * Get the information of a specific queue
     * 
     * @param token
     * @param queueId
     * @return QueueDTO
     * @throws BadRequestException
     * 
     * @see QueueDTO
     * @see BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public QueueDTO findById(String token, String queueId) throws BadRequestException {
        // Request for getting the queue information.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queueId);
        DescribeQueueResponse queuesInfo = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).describeQueue(describeQueueRequest.build());

        // Create a new QueueDTO object and return it.
        return new QueueDTO(
            queuesInfo.queue().queueId(),
            queuesInfo.queue().queueArn(),
            metricService.getMetricsById(token, "QUEUE", queuesInfo.queue().queueArn()),
            new InformationSectionListDTO(),
            "alerts",
            "trainings",
            "agents"
        );
    }
}
