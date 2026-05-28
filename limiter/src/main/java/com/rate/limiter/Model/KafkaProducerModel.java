package com.rate.limiter.Model;

public class KafkaProducerModel {
    public String userID;
    public String message;
    public long timeStamp;
    public  String endPoint;
    public KafkaProducerModel(String userID , String message , String endPoint){
        this.userID = userID;
        this.message = message;
        this.endPoint = endPoint;
        this.timeStamp = System.currentTimeMillis();
    }
    @Override
    public String toString() {
        return  "{" +
        "\"userID\":\"" + userID + "\"," +
        "\"message\":\"" + message + "\"," +
        "\"timeStamp\":\"" + timeStamp + "\"," +
        "\"endpoint\":\"" + endPoint + "\"" +
        "}";
    }
}
