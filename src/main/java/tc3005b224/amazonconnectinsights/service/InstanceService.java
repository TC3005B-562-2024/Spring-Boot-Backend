package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeInstanceRequest;
import software.amazon.awssdk.services.connect.model.DescribeInstanceResponse;
import software.amazon.awssdk.services.connect.model.Instance;

import software.amazon.awssdk.services.connect.model.DescribeInstanceAttributeRequest;
import software.amazon.awssdk.services.connect.model.DescribeInstanceAttributeResponse;
import software.amazon.awssdk.services.connect.model.Attribute;


import software.amazon.awssdk.services.connect.model.InstanceStatus;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;

@Service
public class InstanceService extends BaseService {
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public InstanceDTO getInstanceDetails(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // --------- REQUESTS ---------

        // Build the request for the DescribeInstance endpoint using the instanceId
        DescribeInstanceRequest describeInstanceRequest = DescribeInstanceRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .build();

        // Build the request for the DescribeInstanceAttribute endpoint using the instanceId & attributeType to obtain the CONTACT_LENS value
        DescribeInstanceAttributeRequest describeInstanceAttributeRequestCL = DescribeInstanceAttributeRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .attributeType("CONTACT_LENS")
                .build();

        // Build the request for the DescribeInstanceAttribute endpoint using the instanceId & attributeType to obtain the INBOUND_CALLS value
        DescribeInstanceAttributeRequest describeInstanceAttributeRequestIC = DescribeInstanceAttributeRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .attributeType("INBOUND_CALLS")
                .build();

        // Build the request for the DescribeInstanceAttribute endpoint using the instanceId & attributeType to obtain the OUTBOUND_CALLS value
        DescribeInstanceAttributeRequest describeInstanceAttributeRequestOC = DescribeInstanceAttributeRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .attributeType("OUTBOUND_CALLS")
                .build();

        // --------- RESPONSES ---------

        // The response of the DescribeInstance endpoint
        DescribeInstanceResponse describeInstanceResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeInstance(describeInstanceRequest);

        // The response of the DescribeInstanceAttribute endpoint for CONTACT_LENS values
        DescribeInstanceAttributeResponse describeInstanceAttributeResponseCL = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeInstanceAttribute(describeInstanceAttributeRequestCL);

        // The response of the DescribeInstanceAttribute endpoint for INBOUND_CALLS values
        DescribeInstanceAttributeResponse describeInstanceAttributeResponseIC = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeInstanceAttribute(describeInstanceAttributeRequestIC);

        // The response of the DescribeInstanceAttribute endpoint for OUTBOUND_CALLS values
        DescribeInstanceAttributeResponse describeInstanceAttributeResponseOC = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeInstanceAttribute(describeInstanceAttributeRequestOC);

        // --------- RETURN CONSTRUCTOR WITH DTO ----------

        Instance dataDI = describeInstanceResponse.instance();
        Attribute dataDIACL = describeInstanceAttributeResponseCL.attribute();
        Attribute dataDIAIC = describeInstanceAttributeResponseIC.attribute();
        Attribute dataDIAOC = describeInstanceAttributeResponseOC.attribute();


        InstanceDTO result = new InstanceDTO();
        result = new InstanceDTO(dataDI.arn(), dataDI.id(), dataDIAOC.value(), dataDI.instanceAlias(), dataDI.instanceAccessUrl(), dataDI.instanceStatusAsString(), dataDI.serviceRole(), dataDIACL.value(), dataDIAIC.value());
        return result;
    }
}