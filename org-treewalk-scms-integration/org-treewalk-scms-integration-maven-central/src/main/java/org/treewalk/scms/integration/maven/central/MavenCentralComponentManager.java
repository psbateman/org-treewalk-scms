package org.treewalk.scms.integration.maven.central;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;
import org.apache.maven.model.Model;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.treewalk.scms.core.model.component.Component;
import org.treewalk.scms.core.model.component.ComponentIdentifier;
import org.treewalk.scms.integration.core.ComponentManager;
import org.treewalk.scms.integration.core.ComponentManagerException;

/**
 * Implements the {@link org.treewalk.scms.integration.core.ComponentManager}'s API against the
 * Maven Central Repository.
 */
public class MavenCentralComponentManager implements ComponentManager {

    private RestTemplate restTemplate;
    private String url;

    /**
     * Return a {@link org.treewalk.scms.core.model.component.Component} identified by the a {@link org.treewalk.scms.core.model.component.ComponentIdentifier}.
     *
     * @param identifier the Component Identifier.
     * @return a Component or <code>null</code> if non exists.
     */
    @Override
    public Component getComponent(ComponentIdentifier identifier) {

        validateIdentifier(identifier);

        try {
            Model model = restTemplate.getForObject(url, Model.class, buildQueryPath(identifier));

            if (model != null) {
                return transformModel(model);
            }

        } catch (HttpServerErrorException e) {
            throw new ComponentManagerException(e);
        } catch (HttpClientErrorException e) {
            throw new ComponentManagerException(e);
        } catch (HttpMessageNotReadableException e) {
            throw new ComponentManagerException(e);
        }
        return null;
    }

    /**
     * Set the {@link org.springframework.web.client.RestTemplate}
     *
     * @param restTemplate
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Set the Maven Central REST API url.
     *
     * @param url the Maven Central REST API url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Validate the Component Identifier
     *
     * @param identifier
     */
    private void validateIdentifier(ComponentIdentifier identifier) {
        Assert.notNull(identifier, "getComponent requires an identifier");
        Assert.notNull(identifier.getNamespace(), "getComponent requires a namespace");
        Assert.notNull(identifier.getNamespace().getGroupId(), "getComponent requires a group");
        Assert.notNull(identifier.getNamespace().getArtifactId(), "getComponent requires an artifact");
        Assert.notNull(identifier.getVersion(), "getComponent requires a version");
        Assert.notNull(identifier.getVersion().getVersion(), "getComponent requires a version");
    }

    /**
     * Construct the Maven Central query path for the provided Component Identifier.
     *
     * @param identifier
     *
     * @return the well-form path that represents the Component Identifier
     */
    private String buildQueryPath(ComponentIdentifier identifier) {
        StringBuilder builder = new StringBuilder();

        for (String groupPart : identifier.getNamespace().getGroupId().split("\\.")) {
            if (builder.length() > 0) {
                builder.append("/");
            }
            builder.append(groupPart);
        }
        builder.append("/").append(identifier.getNamespace().getArtifactId()).append("/").append(identifier.getVersion().getVersion());

        return builder.toString();
    }

    /**
     * Transform the Maven Central Model into a component.
     *
     * @param model
     * @return
     */
    private Component transformModel(Model model) {
        return MavenComponentTransformation.transformComponent(model);
    }
}
