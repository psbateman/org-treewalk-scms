package org.treewalk.scms.integration.maven.central;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.treewalk.scms.core.model.component.*;

/**
 * Transform a Maven Project Model into a Component Model
 */
public class MavenComponentTransformation {

    /**
     * Transform a Maven Model into an SCMS Component.
     *
     * @param model maven top level Model object
     *
     * @return transformed component
     */
    public static Component transformComponent(final Model model) {
        Component component = new Component();
        component.setId(transformIdentifier(model.getGroupId(), model.getArtifactId(), model.getVersion()));
        component.setPackaging(transformPackaging(model));

        for (Dependency dependency: model.getDependencies()) {
            component.addDependency(transformDependency(dependency));
        }

        return component;
    }

    /**
     * Transform a Maven model into an SCMS Component Packaging
     *
     * @param model maven top level Model object
     *
     * @return transformed component packaging
     */
    public static ComponentPackaging transformPackaging(final Model model) {

        if (model.getArtifactId() != null) {
            switch (model.getPackaging()) {
                case "jar":
                    return ComponentPackaging.JAR;
                case "war":
                    return ComponentPackaging.WAR;
                case "ear":
                    return ComponentPackaging.EAR;
                case "pom":
                    return ComponentPackaging.POM;
                default:
                    return ComponentPackaging.CUST;
            }
        }
        return null;
    }

    /**
     * Transform a Maven model into an SCMS Dependency
     *
     * @param dependency the maven dependency
     *
     * @return transformed dependency
     */
    public static ComponentDependency transformDependency(final Dependency dependency) {
        ComponentDependency componentDependency = new ComponentDependency();
        componentDependency.setDependencyId(transformIdentifier(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()));
        componentDependency.setScope(transformScope(dependency));
        for (Exclusion exclusion:  dependency.getExclusions()) {
            componentDependency.addExclusion(transformExclusion(exclusion));
        }
        return componentDependency;
    }

    /**
     * Transform a Maven model into an SCMS Dependency Scope
     *
     * @param dependency the maven dependency scope
     *
     * @return transformed dependency scope
     */
    public static ComponentDependencyScope transformScope(Dependency dependency) {
        switch (dependency.getScope()) {
            case "compile":
                return ComponentDependencyScope.COMPILE;
            case "runtime":
                return ComponentDependencyScope.RUNTIME;
            case "provided":
                return ComponentDependencyScope.PROVIDED;
            case "system":
                return ComponentDependencyScope.SYSTEM;
            case "test":
                return ComponentDependencyScope.TEST;
            default:
                throw new RuntimeException("");
        }
    }

    /**
     * Transform a Maven model into an SCMS Dependency Scope
     *
     * @param exclusion the maven dependency exclusion
     *
     * @return transformed dependency exclusion
     */
    public static ComponentDependencyExclusion transformExclusion(Exclusion exclusion) {
        ComponentDependencyExclusion componentDependencyExclusion = new ComponentDependencyExclusion();
        componentDependencyExclusion.setDependencyId(transformNamespace(exclusion.getGroupId(), exclusion.getArtifactId()));
        return componentDependencyExclusion;
    }

    public static ComponentIdentifier transformIdentifier(final String groupId, final String artifactId, final String version) {
        ComponentIdentifier identifier = new ComponentIdentifier();
        identifier.setNamespace(transformNamespace(groupId, artifactId));
        identifier.setVersion(transformVersion(version));
        return identifier;
    }
    public static ComponentNamespace transformNamespace(final String groupId, final String artifactId) {
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setGroupId(groupId);
        namespace.setArtifactId(artifactId);
        return namespace;
    }
    public static ComponentVersion transformVersion(final String version) {
        ComponentVersion componentVersion = new ComponentVersion();
        componentVersion.setVersion(version);
        return componentVersion;
    }
}
