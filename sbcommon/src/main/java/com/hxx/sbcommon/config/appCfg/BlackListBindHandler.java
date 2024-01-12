package com.hxx.sbcommon.config.appCfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.AbstractBindHandler;
import org.springframework.boot.context.properties.bind.BindContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

import java.util.HashSet;
import java.util.Set;

/**
 * Black list to filter specified property
 *
 * @author kevin
 */
public class BlackListBindHandler extends AbstractBindHandler {

    private static final Logger logger = LoggerFactory.getLogger(BlackListBindHandler.class);

    private Set<String> blacklist = new HashSet<>();

    public BlackListBindHandler() {
    }

    public BlackListBindHandler(BindHandler parent) {
        super(parent);
    }

    public BlackListBindHandler(BindHandler parent, Set<String> blacklist) {
        super(parent);
        this.blacklist = blacklist;
    }

    @Override
    public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        if (checkBlackList(name.toString())) {
            logger.info("property name : {} hits black list.", name);
            return null;
        }
        return super.onStart(name, target, context);
    }

    private boolean checkBlackList(String propertyName) {
        return blacklist.stream()
                .anyMatch(exclude -> exclude.equals(propertyName));
    }

}

