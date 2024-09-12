package uz.pdp.jwtsecure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface RestConstants {

    String[] OPEN_PAGES = {
            RestConstants.BASE_PATH + "/swagger-ui/**",
            RestConstants.BASE_PATH + "/swagger-ui.html",
            "auth/**"

    };
    String BASE_PATH = "/api";
    String BASE_PATH_V1 = BASE_PATH + "/v1";

    String AUTHENTICATION_HEADER = "Authorization";
    ObjectMapper objectMapper = new ObjectMapper();

    String TOKEN_TYPE = "Bearer ";



    String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";


    String NUMBER_GREATER = "NUMBER_GREATER";
    String ONE_OF_LIST = "ONE_OF_LIST";
    String USER_ENTERED = "USER_ENTERED";
    String DEFAULT_PAGE = "0";
    String DEFAULT_PAGE_SIZE_FOR_SEARCH = "5";
    String DEFAULT_PAGE_SIZE = "10";
    String APPLICATION_NAME = "DIABLO AIRWAYS";
    String WHERE_CLAUSE = "deleted=false";
    String SQL_DELETED = "";
    String BEARER_TOKEN = "Bearer ";
}
