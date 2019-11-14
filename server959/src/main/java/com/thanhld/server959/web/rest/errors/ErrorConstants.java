package com.thanhld.server959.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.thanhro.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI NO_DATA_TYPE = URI.create(PROBLEM_BASE_URL + "/no-data");
    public static final URI ENTITY_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI PARAMETERIZED_TYPE = URI.create(PROBLEM_BASE_URL + "/parameterized");
    public static final URI ENTITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI EMAIL_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/email-not-found");
    public static final URI EXCEPTION_THROWED_TYPE = URI.create(PROBLEM_BASE_URL + "/exception-throwed");
    public static final URI VERIFY_ACCESSTOKEN_CRASH_TYPE = URI.create(PROBLEM_BASE_URL + "/verify-accesstoken");
    public static final URI DATA_EXISTED_TYPE = URI.create(PROBLEM_BASE_URL + "/data-existed");

    public static final int CLASS_NOT_FOUND = 501;
    public static final int USER_NOT_FOUND = 502;
    public static final int EXCEPTION_THROWED_CODE = 503;
    public static final int EMAIL_ALREADY_USED_CODE = 504;
    public static final int VERIFY_ACCESSTOKEN_CRASH_CODE = 505;
    public static final int DATA_EXISTED_CODE = 506;
    public static final int NO_DATA_CODE = 507;
    public static final int LOGIN_ALREADY_USED_CODE = 508;
    public static final int NULL_VALUE = 511;

    private ErrorConstants() {
    }
}

