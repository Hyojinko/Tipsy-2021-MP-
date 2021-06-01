//댓글 관련 클라스
package com.example.tipsy;


import java.util.Date;

public class Comments {

    private String message, user_id,image_name;
    private Date timestamp;

    public Comments(){

    }

    public Comments(String message, String user_id,String image_name, Date timestamp) {
        this.message = message;
        this.user_id = user_id;
        this.image_name=image_name;
        this.timestamp = timestamp;
    }
    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
