package TEMA1.User;

import TEMA1.GAME.Game;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userEmail;
    private String userPassword;
    private List<Game> userGames; // only the active games
    private int userPoints;

    public User(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
        this.userGames = new ArrayList<>();
        this.userPoints = 0;
    }

    public void addGame(Game game) {
        this.userGames.add(game);
    }

    public void removeGame(Game game) {
        this.userGames.remove(game);
    }

    public List<Game> getActiveGames() {
        return this.userGames;
    }

    public int getUserPoints() {
        return this.userPoints;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void addTotalPoints(int pointsToAdd) {
        this.userPoints += pointsToAdd;
    }

    public void setTotalPoints(int newPoints) {
        this.userPoints = newPoints;
    }

}
