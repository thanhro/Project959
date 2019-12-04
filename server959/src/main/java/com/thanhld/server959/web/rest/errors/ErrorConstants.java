package com.thanhld.server959.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.thanhro.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI ENTITY_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI ENTITY_PROPERTY_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/entity-property-not-found");
    public static final URI ENTITY_GOOGLE_DRIVE_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/entity-google-drive-not-found");
    public static final URI ENTITY_NOT_HAVE_PERMISSION = URI.create(PROBLEM_BASE_URL + "/entity-not-have-permission");
    public static final URI ENTITY_EXISTED = URI.create(PROBLEM_BASE_URL + "/entity-existed");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI ENTITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI ENTITY_NOT_FORMAL = URI.create(PROBLEM_BASE_URL + "/entity-not-formal");

    public static final int CLASS_NOT_FOUND = 501;
    public static final int USER_NOT_FOUND = 502;
    public static final int CLASS_ALREADY_EXISTED = 509;
    public static final int ASSIGNMENT_ALREADY_EXISTED = 510;
    public static final int ASSIGNMENT_NOT_FOUND = 512;
    public static final int USER_NOT_HAVE_PERMISSION = 513;
    public static final int FILE_GOOGLE_DRIVE_NOT_FOUND = 514;
    public static final int DRIVE_NOT_FOUND = 517;
    public static final int POINT_NOT_FORMAL = 518;

    private ErrorConstants() {
    }
}

