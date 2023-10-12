package com.hxx.sbcommon.config.appCfg;


import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class used to bind properties to object.
 *
 * @author kevin
 */
public class PropertyBinder {

    private ConfigurableEnvironment environment;
    private Binder binder;
    private BindHandler handler;
    private Set<String> blacklist;

    /**
     * create a PropertyBinder instance for property sources in environment.
     *
     * @param environment
     */
    public PropertyBinder(ConfigurableEnvironment environment) {
        this(environment, new HashSet<>());
    }

    /**
     * create a PropertyBinder instance for property sources in environment and filter the specified keys defined in blacklist.
     *
     * @param environment
     * @param blacklist
     */
    public PropertyBinder(ConfigurableEnvironment environment, Set<String> blacklist) {
        this.environment = environment;
        this.binder = new Binder(getConfigurationPropertySources(), getPropertySourcesPlaceholdersResolver());
        this.blacklist = blacklist;
    }

    /**
     * create a PropertyBinder instance for property sources in environment and customized BindHandler.
     *
     * @param environment
     * @param handler
     */
    public PropertyBinder(ConfigurableEnvironment environment, BindHandler handler) {
        this(environment, new HashSet<>(), handler);
    }

    /**
     * create a PropertyBinder instance for property sources in environment and customized BindHandler.
     * filter the specified keys defined in blacklist.
     *
     * @param environment
     * @param blacklist
     * @param handler
     */
    public PropertyBinder(ConfigurableEnvironment environment, Set<String> blacklist, BindHandler handler) {
        this.environment = environment;
        this.handler = handler;
        this.blacklist = blacklist;
        this.binder = new Binder(getConfigurationPropertySources(), getPropertySourcesPlaceholdersResolver());
    }

    /**
     * Bind the specified target {@link Class} using this binder's property sources.
     *
     * @param configPrefix
     * @param target
     * @param <T>
     * @return
     */
    public <T> BindResult<T> bind(String configPrefix, Class<T> target) {
        return bind(configPrefix, Bindable.of(target));
    }

    /**
     * Bind the specified target {@link Bindable} using this binder's property sources.
     *
     * @param configPrefix
     * @param bindable
     * @param <T>
     * @return
     */
    public <T> BindResult<T> bind(String configPrefix, Bindable<T> bindable) {
        return binder.bind(configPrefix, bindable, handler != null ? handler : new BlackListBindHandler(new IgnoreTopLevelConverterNotFoundBindHandler(), blacklist));
    }

    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources() {
        return ConfigurationPropertySources.from(environment.getPropertySources());
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver() {
        return new PropertySourcesPlaceholdersResolver(environment.getPropertySources());
    }

}
