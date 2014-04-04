package org.treewalk.scms.core.model.component;


import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ComponentTest {

    @Test
    public void testComponent_componentIdentifier() {
        // given
        Component comp = new Component();
        ComponentIdentifier id = new ComponentIdentifier();

        // when
        comp.setId(id);

        // then
        assertThat(id, is(id));
    }
}
