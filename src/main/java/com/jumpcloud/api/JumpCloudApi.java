package com.jumpcloud.api;

import com.jumpcloud.util.JumpCloudFilter;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.spi.SearchResultsHandler;

import java.util.Set;

public interface JumpCloudApi {
    String createUser(Set<Attribute> attributes);
    void updateUser(String userId, Set<Attribute> attributes);
    void deleteUser(String userId);
    void searchUsers(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options);

    String createGroup(Set<Attribute> attributes);

    void updateGroup(String groupId, Set<Attribute> attributes);

    void deleteGroup(String groupId);

    void searchGroups(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options);
}