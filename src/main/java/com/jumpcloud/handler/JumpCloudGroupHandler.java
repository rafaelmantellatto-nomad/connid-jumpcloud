package com.jumpcloud.handler;

import com.jumpcloud.api.JumpCloudApi;
import com.jumpcloud.connector.JumpCloudConfiguration;
import com.jumpcloud.util.JumpCloudFilter;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.AttributeInfo.Flags;
import org.identityconnectors.framework.spi.SearchResultsHandler;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class JumpCloudGroupHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JumpCloudGroupHandler.class);
    private final JumpCloudConfiguration configuration;
    private final JumpCloudApi jumpCloudApi;

    public JumpCloudGroupHandler(JumpCloudConfiguration configuration, JumpCloudApi jumpCloudApi) {
        this.configuration = configuration;
        this.jumpCloudApi = jumpCloudApi;
    }

    public ObjectClassInfo buildGroupObjectClassInfo() {
        ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
        builder.setType(ObjectClass.GROUP_NAME);

        // Required attributes
        builder.addAttributeInfo(AttributeInfoBuilder.define(Uid.NAME).setRequired(true).build());
        builder.addAttributeInfo(AttributeInfoBuilder.define(Name.NAME).setRequired(true).build());

        // Optional attributes (example)
        AttributeInfoBuilder descriptionBuilder = AttributeInfoBuilder.define("description");
        descriptionBuilder.setFlags(new HashSet<Flags>() {{
            add(Flags.NOT_RETURNED_BY_DEFAULT);
        }});
        builder.addAttributeInfo(descriptionBuilder.build());

        return builder.build();
    }

    public Uid createGroup(Set<Attribute> attributes) {
        try {
            Map<String, Object> attributeMap = new HashMap<>();
            for (Attribute attribute : attributes) {
                attributeMap.put(attribute.getName(), attribute.getValue().get(0));
            }
            String groupId = jumpCloudApi.createGroup(attributeMap);
            return new Uid(groupId);
        } catch (Exception e) {
            throw new ConnectorException("Error creating group: " + e.getMessage(), e);
        }
    }

    public Uid updateGroup(Uid uid, Set<Attribute> attributes) {
        try {
            Map<String, Object> attributeMap = new HashMap<>();
            for (Attribute attribute : attributes) {
                attributeMap.put(attribute.getName(), attribute.getValue().get(0));
            }
            jumpCloudApi.updateGroup(uid.getUidValue(), attributeMap);
            return uid;
        } catch (Exception e) {
            throw new ConnectorException("Error updating group: " + e.getMessage(), e);
        }
    }

    public void deleteGroup(Uid uid) {
        try {
            jumpCloudApi.deleteGroup(uid.getUidValue());
        } catch (Exception e) {
            throw new ConnectorException("Error deleting group: " + e.getMessage(), e);
        }
    }

    public void searchGroups(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options) {
        try {
            jumpCloudApi.searchGroups(filter, resultsHandler, options);
        } catch (Exception e) {
            throw new ConnectorException("Error searching groups: " + e.getMessage(), e);
        }
    }
}