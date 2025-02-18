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

public class JumpCloudUserHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JumpCloudUserHandler.class);
    private final JumpCloudConfiguration configuration;
    private final JumpCloudApi jumpCloudApi;

    public JumpCloudUserHandler(JumpCloudConfiguration configuration, JumpCloudApi jumpCloudApi) {
        this.configuration = configuration;
        this.jumpCloudApi = jumpCloudApi;
    }

    public ObjectClassInfo buildAccountObjectClassInfo() {
        ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
        builder.setType(ObjectClass.ACCOUNT_NAME);

        // Required attributes
        builder.addAttributeInfo(AttributeInfoBuilder.define(Uid.NAME).setRequired(true).build());
        builder.addAttributeInfo(AttributeInfoBuilder.define(Name.NAME).setRequired(true).build());

        // Optional attributes (example)
        AttributeInfoBuilder emailBuilder = AttributeInfoBuilder.define("email");
        emailBuilder.setFlags(new HashSet<Flags>() {{
            add(Flags.NOT_RETURNED_BY_DEFAULT);
        }});
        builder.addAttributeInfo(emailBuilder.build());

        AttributeInfoBuilder firstNameBuilder = AttributeInfoBuilder.define("firstname");
        firstNameBuilder.setFlags(new HashSet<Flags>() {{
            add(Flags.NOT_RETURNED_BY_DEFAULT);
        }});
        builder.addAttributeInfo(firstNameBuilder.build());

        AttributeInfoBuilder lastNameBuilder = AttributeInfoBuilder.define("lastname");
        lastNameBuilder.setFlags(new HashSet<Flags>() {{
            add(Flags.NOT_RETURNED_BY_DEFAULT);
        }});
        builder.addAttributeInfo(lastNameBuilder.build());

        // Add __ENABLE__ attribute
        AttributeInfoBuilder enableBuilder = AttributeInfoBuilder.define(OperationalAttributes.ENABLE_NAME, Boolean.class);
        enableBuilder.setFlags(new HashSet<Flags>() {{
            add(Flags.NOT_RETURNED_BY_DEFAULT);
        }});
        builder.addAttributeInfo(enableBuilder.build());

        return builder.build();
    }

    public Uid createUser(Set<Attribute> attributes) {
        try {
            Map<String, Object> attributeMap = new HashMap<>();
            for (Attribute attribute : attributes) {
                attributeMap.put(attribute.getName(), attribute.getValue().get(0));
            }
            String userId = jumpCloudApi.createUser(attributeMap);
            return new Uid(userId);
        } catch (Exception e) {
            throw new ConnectorException("Error creating user: " + e.getMessage(), e);
        }
    }

    public Uid updateUser(Uid uid, Set<Attribute> attributes) {
        try {
            Map<String, Object> attributeMap = new HashMap<>();
            for (Attribute attribute : attributes) {
                attributeMap.put(attribute.getName(), attribute.getValue().get(0));
            }
            jumpCloudApi.updateUser(uid.getUidValue(), attributeMap);
            return uid;
        } catch (Exception e) {
            throw new ConnectorException("Error updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(Uid uid) {
        try {
            jumpCloudApi.deleteUser(uid.getUidValue());
        } catch (Exception e) {
            throw new ConnectorException("Error deleting user: " + e.getMessage(), e);
        }
    }

    public void searchUsers(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options) {
        try {
            jumpCloudApi.searchUsers(filter, resultsHandler, options);
        } catch (Exception e) {
            throw new ConnectorException("Error searching users: " + e.getMessage(), e);
        }
    }
}