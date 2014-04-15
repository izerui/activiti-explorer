package org.activiti.rest.explorer.application;

import org.activiti.rest.common.api.DefaultResource;
import org.activiti.rest.common.application.ActivitiRestApplication;
import org.activiti.rest.common.filter.JsonpFilter;
import org.activiti.rest.diagram.application.DiagramServicesInit;
import org.activiti.rest.editor.application.ModelerServicesInit;
import org.activiti.rest.service.application.ActivitiRestServicesApplication;
import org.activiti.rest.service.application.RestServicesInit;
import org.codehaus.jackson.map.SerializationConfig;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.routing.Router;

import java.util.List;

public class ExplorerRestApplication extends ActivitiRestServicesApplication {

    public ExplorerRestApplication() {
        super();
    }
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {

        initializeAuthentication();

        Router router = new Router(getContext());
        router.attachDefault(DefaultResource.class);

        RestServicesInit.attachResources(router);
        ModelerServicesInit.attachResources(router);
        DiagramServicesInit.attachResources(router);

        JsonpFilter jsonpFilter = new JsonpFilter(getContext());
        authenticator.setNext(jsonpFilter);
        jsonpFilter.setNext(router);

        // Get hold of JSONConverter and enable ISO-date format by default
        List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
        for(ConverterHelper helper : registeredConverters) {
            if(helper instanceof JacksonConverter) {
                JacksonConverter jacksonConverter = (JacksonConverter) helper;
                jacksonConverter.getObjectMapper().configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
            }
        }
        return authenticator;

    }

}
