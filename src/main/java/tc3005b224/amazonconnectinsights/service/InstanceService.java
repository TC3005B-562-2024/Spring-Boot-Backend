package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeInstanceRequest;
import software.amazon.awssdk.services.connect.model.DescribeInstanceResponse;
import software.amazon.awssdk.services.connect.model.Instance;


import software.amazon.awssdk.services.connect.model.InstanceStatus;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;

@Service
public class InstanceService extends BaseService {
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public InstanceDTO getInstanceDetails(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Build the request for the DescribeInstance endpoint using the instanceId
        DescribeInstanceRequest describeInstanceRequest = DescribeInstanceRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .build();

        // The response of the DescribeInstance endpoint
        DescribeInstanceResponse describeInstanceResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeInstance(describeInstanceRequest);

        // The Details of the Instance using the instanceId

        Instance data = describeInstanceResponse.instance();
        // Build the result as a JSON with InstanceDTO
        InstanceDTO result = new InstanceDTO();
        data = new InstanceDTO(data.arn(), data.id(), data.instanceAlias(), data.instanceAccessUrl(), data.instanceStatusAsString(), data.serviceRole());
        return result;
    }

}