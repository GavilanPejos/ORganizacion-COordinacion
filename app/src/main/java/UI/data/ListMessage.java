package UI.data;

public class ListMessage {
    private String name;
    private String email;
    private String ultMessage;

    private String imgProfile;
    private int uSMessage;

    public ListMessage(String name, String email, String ultMessage, String imgProfile, int uSMessage) {
        this.name = name;
        this.email = email;
        this.ultMessage = ultMessage;
        this.imgProfile = imgProfile;
        this.uSMessage = uSMessage;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUltMessage() {
        return ultMessage;
    }

    public int getuSMessage() {
        return uSMessage;
    }

    public String getImgProfile() {
        return imgProfile;
    }
}
