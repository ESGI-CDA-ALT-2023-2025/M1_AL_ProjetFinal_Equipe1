package fr.esgi.dvf.controller.exception;

public class TechniqueException extends
                                Exception {

    private static final long serialVersionUID = -2341148542918418441L;

    public TechniqueException() {
        super();
    }
    
    public TechniqueException(String message) {
        super(message);
    }
}
