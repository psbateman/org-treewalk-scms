package org.treewalk.scms.integration.maven.central;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.treewalk.scms.core.model.component.Component;
import org.treewalk.scms.core.model.component.ComponentIdentifier;
import org.treewalk.scms.core.model.component.ComponentNamespace;
import org.treewalk.scms.core.model.component.ComponentVersion;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MavenCentralComponentManagerTest {


    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MavenCentralComponentManager manager;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForNullIdentifier() {
        // given
        // when
        manager.getComponent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForIdentifierWithNoNamespace() {
        // given
        ComponentIdentifier id = new ComponentIdentifier();
        ComponentVersion version = new ComponentVersion();
        version.setVersion("1");
        id.setVersion(version);

        // when
        manager.getComponent(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForIdentifierWithNoVersion() {
        // given
        ComponentIdentifier id = new ComponentIdentifier();
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setArtifactId("foo");
        namespace.setGroupId("bar");
        id.setNamespace(namespace);

        // when
        manager.getComponent(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForIdentifierWithNoVersionVersion() {
        // given
        ComponentIdentifier id = new ComponentIdentifier();
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setArtifactId("foo");
        namespace.setGroupId("bar");
        id.setNamespace(namespace);
        ComponentVersion version = new ComponentVersion();
        id.setVersion(version);

        // when
        manager.getComponent(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForIdentifierWithNoGroup() {
        // given
        ComponentIdentifier id = new ComponentIdentifier();
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setArtifactId("foo");
        id.setNamespace(namespace);
        ComponentVersion version = new ComponentVersion();
        version.setVersion("1");
        id.setVersion(version);

        // when
        manager.getComponent(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ThrowsExceptionForIdentifierWithNoArtifact() {
        // given
        ComponentIdentifier id = new ComponentIdentifier();
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setGroupId("foo");
        id.setNamespace(namespace);
        ComponentVersion version = new ComponentVersion();
        version.setVersion("1");
        id.setVersion(version);

        // when
        manager.getComponent(id);
    }
    @Test
    public void testGetComponent_ReturnsNullIfCantFindComponent() {
        // given
        mockServer.expect(requestTo("http://search.maven.org/remotecontent?filepath=foo/baa/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("<project />", MediaType.APPLICATION_XML));

        ComponentIdentifier id = new ComponentIdentifier();
        ComponentNamespace namespace = new ComponentNamespace();
        namespace.setGroupId("foo");
        namespace.setArtifactId("baa");
        id.setNamespace(namespace);
        ComponentVersion version = new ComponentVersion();
        version.setVersion("1");
        id.setVersion(version);

        // when
        Component component = manager.getComponent(id);

        // then
        assertThat(component, is(notNullValue()));
    }
}

