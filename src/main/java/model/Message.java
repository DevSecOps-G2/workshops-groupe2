package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date date_time;

    @Column(nullable = false)
    private boolean isRead = false;


    public Message(){}
    public Message (User sender, User receiver, String content, Date date_time){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date_time = date_time;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public User getSender(){return sender;}
    public void setSender(User sender){this.sender = sender;}

    public User getReceiver(){return receiver;}
    public void setReceiver(User receiver){this.receiver = receiver;}

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public Date getDate_time(){return date_time;}
    public void setDate_time(Date date_time) {this.date_time = date_time;}

    public boolean getIsRead(){return isRead;}
    public void setIsRead(boolean read) {isRead = read;}
}
