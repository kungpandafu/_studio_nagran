package com.example._studio_nagran;



public final class ManageUserSessionController {
    private static ManageUserSessionController instance;
    private String username;
    private Integer permissions;

    private ManageUserSessionController(String username, Integer permissions){
        this.username = username;
        this.permissions = permissions;
    }
    public static ManageUserSessionController getInstance(String username, Integer permissions){
        if(instance == null){
            instance = new ManageUserSessionController(username, permissions);
        }
        return instance;
    }
    public String getUsername(){
        return username;
    }
    public int getPermissions(){
        return permissions;
    }
    public void cleanSession(){
        username = null;
        permissions = null;
    }
    @Override
    public String toString(){
        return "Session{" + "username='" +username+ '\'' + ", permissions " + permissions+'}';
    }
}
