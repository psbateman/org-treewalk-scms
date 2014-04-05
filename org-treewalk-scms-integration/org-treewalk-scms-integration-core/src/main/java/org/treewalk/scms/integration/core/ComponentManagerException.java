package org.treewalk.scms.integration.core;

public class ComponentManagerException extends RuntimeException{

    public ComponentManagerException(String message) {
        super(message);
    }

    public ComponentManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentManagerException(Throwable cause) {
        super(cause);
    }
}
