package com.jumpcloud.connector;

import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.*;
import org.identityconnectors.framework.spi.operations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jumpcloud.api.JumpCloudApi;
import com.jumpcloud.api.JumpCloudApiImpl;
import com.jumpcloud.util.JumpCloudFilter;
import com.jumpcloud.util.JumpCloudFilterTranslator;
import org.identityconnectors.framework.spi.SearchResultsHandler;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.Set;

@ConnectorClass(configurationClass = JumpCloudConfiguration.class, displayNameKey = "JumpCloudConnector.Connector.displayName")
public class JumpCloudConnector implements Connector, SchemaOp, SearchOp {

    private static final Logger LOG = LoggerFactory.getLogger(JumpCloudConnector.class);
    private JumpCloudConfiguration configuration;
    private JumpCloudApi jumpCloudApi;

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(Configuration configuration) {
        this.configuration = (JumpCloudConfiguration) configuration;
        this.configuration.validate();
        this.jumpCloudApi = new JumpCloudApiImpl(this.configuration);
        LOG.info("JumpCloud Connector initialized.");
    }

    @Override
    public void dispose() {
        LOG.info("JumpCloud Connector disposed.");
    }

    @Override
    public void search(ObjectClass objectClass, Filter filter, SearchResultsHandler handler, OperationOptions options) {
        LOG.info("Filter {}", filter);
        JumpCloudFilter jcFilter = translateFilter(filter);
        LOG.info("Filter {}", jcFilter);
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            // Implemente a lógica para pesquisar usuários usando a API do JumpCloud
            jumpCloudApi.searchUsers(jcFilter, handler, options);
        } else {
            throw new UnsupportedOperationException("Unsupported object class: " + objectClass.getObjectClassValue());
        }
    }

    @Override
    public Schema schema() {
        SchemaBuilder schemaBuilder = new SchemaBuilder(JumpCloudConnector.class);

        // Definir o esquema para usuários
        ObjectClassInfoBuilder accountBuilder = new ObjectClassInfoBuilder();
        accountBuilder.setType(ObjectClass.ACCOUNT_NAME);
        accountBuilder.addAttributeInfo(AttributeInfoBuilder.define(Uid.NAME).setRequired(true).build());
        accountBuilder.addAttributeInfo(AttributeInfoBuilder.define(Name.NAME).setRequired(true).build());
        // Adicione outros atributos de usuário aqui (por exemplo, email, firstName, lastName)
        schemaBuilder.defineObjectClass(accountBuilder.build());

        return schemaBuilder.build();
    }

    private JumpCloudFilter translateFilter(Filter filter) {
        JumpCloudFilterTranslator trans = new JumpCloudFilterTranslator();
        JumpCloudFilter jcFilter = trans.translate(filter);
        return jcFilter;
    }

    @Override
    public FilterTranslator<JumpCloudFilter> createFilterTranslator(ObjectClass objectClass, OperationOptions options) {
        return new JumpCloudFilterTranslator();
    }
    
    @Override
    public void executeQuery(ObjectClass objectClass, JumpCloudFilter filter, ResultsHandler resultsHandler, OperationOptions options) {
        // Implemente este método ou lance uma exceção UnsupportedOperationException
        throw new UnsupportedOperationException("executeQuery is not supported");
    }
}