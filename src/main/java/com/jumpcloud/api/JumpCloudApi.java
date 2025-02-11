package com.jumpcloud.api;

import com.jumpcloud.util.JumpCloudFilter;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.spi.SearchResultsHandler;

import java.util.Set;

public interface JumpCloudApi {
    void searchUsers(JumpCloudFilter filter, SearchResultsHandler resultsHandler, OperationOptions options);
}