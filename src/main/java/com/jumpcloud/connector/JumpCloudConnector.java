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
import org.identityconnectors.framework.common.objects.filter.Filter;
import com.jumpcloud.handler.JumpCloudUserHandler;
import com.jumpcloud.handler.JumpCloudGroupHandler;

import java.util.Set;

@ConnectorClass(configurationClass = JumpCloudConfiguration.class, displayNameKey = "JumpCloudConnector.Connector.displayName")
public class JumpCloudConnector implements Connector, SchemaOp {

    private static final Logger LOG = LoggerFactory.getLogger(JumpCloudConnector.class);
    private JumpCloudConfiguration configuration;
    private JumpCloudApi jumpCloudApi;
    private JumpCloudUserHandler userHandler;
    private JumpCloudGroupHandler groupHandler;

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(Configuration configuration) {
        this.configuration = (JumpCloudConfiguration) configuration;
        this.configuration.validate();
        this.jumpCloudApi = new JumpCloudApiImpl(this.configuration);
        this.userHandler = new JumpCloudUserHandler(this.configuration, jumpCloudApi);
        this.groupHandler = new JumpCloudGroupHandler(this.configuration, jumpCloudApi);
        LOG.info("JumpCloud Connector initialized.");
    }

    @Override
    public void dispose() {
        LOG.info("JumpCloud Connector disposed.");
    }

     @Override
    public void search(ObjectClass objectClass, Filter query, SearchResultsHandler handler, OperationOptions options) {
        JumpCloudFilter filter = null;

        JumpCloudFilterTranslator filterTranslator = new JumpCloudFilterTranslator();
        filter = filterTranslator.translate(query);
        if (objectClass.equals(ObjectClass.ACCOUNT)) {
            userHandler.searchUsers(filter, handler, options);
        } else if (objectClass.equals(ObjectClass.GROUP)) {
            groupHandler.searchGroups(filter, handler, options);
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

        ObjectClassInfoBuilder groupBuilder = new ObjectClassInfoBuilder();
        groupBuilder.setType(ObjectClass.GROUP_NAME);
        groupBuilder.addAttributeInfo(AttributeInfoBuilder.define(Uid.NAME).setRequired(true).build());
        groupBuilder.addAttributeInfo(AttributeInfoBuilder.define(Name.NAME).setRequired(true).build());
        schemaBuilder.defineObjectClass(groupBuilder.build());

        return schemaBuilder.build();
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

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> attributes, OperationOptions options) {
        // Implemente a lógica para criar um objeto (usuário ou grupo)
        throw new UnsupportedOperationException("Create operation is not yet implemented.");
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> attributes, OperationOptions options) {
        // Implemente a lógica para atualizar um objeto (usuário ou grupo)
        throw new UnsupportedOperationException("Update operation is not yet implemented.");
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions options) {
        // Implemente a lógica para excluir um objeto (usuário ou grupo)
        throw new UnsupportedOperationException("Delete operation is not yet implemented.");
    }
}