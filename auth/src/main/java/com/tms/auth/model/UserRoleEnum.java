package com.tms.auth.model;


public enum UserRoleEnum {
    USER(Authority.USER),
    HUB(Authority.HUB),
    STORE(Authority.STORE),
    MASTER(Authority.MASTER);

    private String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String HUB = "ROLE_HUB";
        public static final String STORE = "ROLE_STORE";
        public static final String MASTER = "ROLE_MASTER";
    }

}
