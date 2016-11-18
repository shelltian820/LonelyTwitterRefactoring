package ca.ualberta.cs.lonelytwitter;


import java.util.Date;

public interface Tweetable {
    //modifier public is redundant
    String getMessage();

    Date getDate();

}
