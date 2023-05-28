package UI.data;

import java.util.List;

public class User {
    private String email;
    private String idGroup;
    private String id;
    private String username;
    private String imageUrl;
    private List<Message> messageList;

    public User() {
        // Constructor vacío requerido para Firebase
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public User(String idgroup, String email, String id, String username, String imageUrl) {

        this.idGroup = idgroup;
        this.email = email;
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", idGroup='" + idGroup + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", messageList=" + messageList +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
