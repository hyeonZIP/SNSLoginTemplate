package zip.hyeon.snslogintemplate.security.jwt.provider;

public enum ClaimType {
    USER_ID("userId"),
    CATEGORY("category"),
    USER_ROLE("userRole");

    private final String key;

    ClaimType(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }
}