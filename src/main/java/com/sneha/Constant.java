package com.sneha;

public class Constant {

    public final static String ID_EXCEPTION_MESSAGE = "Id is either null or empty";
    public final static String USER_ID_EXCEPTION_MESSAGE = "User Id provided is not valid";
    public final static String SYSTEM_EXCEPTION_MESSAGE = "Something went wrong, please try again";

    public final static String POINT_TABLE_NAME = "points";
    public final static String RECORD_ID_COLUMN_NAME= "recordid";
    public final static String RECORD_TYPE_COLUMN_NAME = "recordtype";
    public final static String AGGREGATE_POINT_COLUMN_NAME = "aggregatedpoints";
    public final static String CREATED_AT_COLUMN_NAME = "createdat";
    public final static String UPDATED_AT_COLUMN_NAME= "updatedat";

    public final static String AGGREGATE_POINT_PATH = "/point/aggregator/user";
    public final static String GET_POINTS_PATH = "/point/users";

    public final static String JSON_RESPONSE_MEDIA_TYPE =  "application/json";


}
