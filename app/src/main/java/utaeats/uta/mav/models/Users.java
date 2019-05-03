package utaeats.uta.mav.models;

public class Users {

    private String id;
    private String uid;
    private String password;
    private String role;

    public Users(){

    }

    public Users(String id, String uid, String password, String role) {
        this.id = id;
        this.uid = uid;
        this.password = password;
        this.role = role;
    }

    public Users(String role){
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.password = role;
    }
}
