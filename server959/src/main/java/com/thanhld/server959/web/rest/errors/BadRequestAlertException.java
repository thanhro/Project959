package com.thanhld.server959.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class  BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final int errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, int errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, int errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey + "";
    }

    private static Map<String, Object> getAlertParameters(String entityName, int errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("errorcode", "" + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}

