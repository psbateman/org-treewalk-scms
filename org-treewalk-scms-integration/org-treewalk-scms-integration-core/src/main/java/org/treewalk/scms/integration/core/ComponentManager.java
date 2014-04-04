package org.treewalk.scms.integration.core;

import org.treewalk.scms.core.model.component.Component;
import org.treewalk.scms.core.model.component.ComponentIdentifier;

/**
 * Component Management provides a set of methods for discovering and interacting with Components.
 */
public interface ComponentManager {

    /**
     * Return a {@link Component} identified by the a {@link org.treewalk.scms.core.model.component.ComponentIdentifier}.
     *
     * @param identifier the Component Identifier.
     * @return a Component or <code>null</code> if non exists.
     */
    Component getComponent(ComponentIdentifier identifier);
}
