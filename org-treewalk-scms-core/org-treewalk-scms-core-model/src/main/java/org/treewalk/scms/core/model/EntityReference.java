package org.treewalk.scms.core.model;

/**
 *
 */
public class EntityReference<type> {
    private type objectRef;
    private ID id;

    public EntityReference(final type objectRef, final ID id) {
        this.objectRef = objectRef;
        this.id = id;
    }

    public type getObjectRef() {
        return objectRef;
    }

    public ID getId() {
        return id;
    }
}
