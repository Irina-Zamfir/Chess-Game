package TEMA1;

import TEMA1.DataSaving.JsonReaderUtil;
import TEMA1.DataSaving.JsonWriterUtil;
import TEMA1.GAME.Game;
import TEMA1.User.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private List<User> userList;
    private Map<Long, Game> gameList;
    private User currentUser;
    private static final Path ACCOUNTS_PATH = Path.of("src/TEMA1/data/accounts.json");
    private static final Path GAMES_PATH = Path.of("src/TEMA1/data/games.json");

    public Main() {
        this.userList = new ArrayList<>();
        this.gameList = new HashMap<>();
        this.currentUser = new User(null, null);
    }

    public void addGameToGameList(Game game) {
        this.gameList.put(game.getGameId(), game);
    }

    public void removeGameFromGameList(Game game) {
        this.gameList.remove(game.getGameId());
    }

    public void updateGameInMap(Game gameToBeResumed) {
        this.gameList.put(gameToBeResumed.getGameId(), gameToBeResumed);
    }

    public void updateUserInList(User updatedUser) {
        for (int i = 0; i < this.userList.size(); i++) {
            if (this.userList.get(i).getUserEmail().equals(updatedUser.getUserEmail())) {
                this.userList.set(i, updatedUser);
                return;
            }
        }
    }

    public void read() {
        try {
            this.gameList = JsonReaderUtil.readGamesAsMap(GAMES_PATH);
            this.userList = JsonReaderUtil.readAccounts(ACCOUNTS_PATH, GAMES_PATH);

        } catch (IOException e) {
            System.err.println("I/O Error at reading the JSON: " + e.getMessage());
            this.userList = new ArrayList<>();
            this.gameList = new HashMap<>();
        } catch (ParseException e) {
            System.err.println("Error of parsing JSON: " + e.getMessage());
            this.userList = new ArrayList<>();
            this.gameList = new HashMap<>();
        }
    }

    public void saveData() {
        System.out.println("\n--- SAVING DATA TO JSON FILES... ---");

        JsonWriterUtil.writeAccounts(ACCOUNTS_PATH, this.userList);
        JsonWriterUtil.writeGames(GAMES_PATH, this.gameList.values());

        System.out.println("--- Data successfully saved. ---");
    }

    public long getNextGameId() {
        if (this.gameList.isEmpty()) {
            return 1;
        }

        return Collections.max(this.gameList.keySet()) + 1;
    }

    public User newAccount(String email, String password) {
        User newUser = new User(email, password);
        userList.add(newUser);
        return newUser;
    }

    public User signUpUser(Scanner s) {
        String email;
        String password;
        User newUser = null;

        System.out.println("\n--- SignUp New User ---");

        while (newUser == null) {
            System.out.print("Introduce Email (or 'exit' to cancel): ");
            email = s.nextLine().trim();

            if (email.equalsIgnoreCase("exit")) {
                return null;
            }

            boolean emailExists = false;
            for (User user : userList) {
                if(user.getUserEmail().equalsIgnoreCase(email)) {
                    emailExists = true;
                    break;
                }
            }

            if (emailExists) {
                System.out.println("Error: This email is already registered. Please use another email");
                continue;
            }

            System.out.print("Introduce Password: ");
            password = s.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please try again");
                continue;
            }

            newUser = newAccount(email, password);

            System.out.println("Successful Signup! Welcome, " + newUser.getUserEmail() + " !");
            this.userList.add(newUser);

            return newUser;
        }

        return newUser;
    }

    public User login(String email, String password) {
        User userToLogin = null;

        for (User user : this.userList) {
            if (user.getUserEmail().equalsIgnoreCase(email) && user.getUserPassword().equalsIgnoreCase(password)) {
                userToLogin = user;
                break;
            }
        }

        return userToLogin;
    }

    public User loginUser(Scanner s) {
        String email;
        String password;
        User loggedInUser = null;

        while (loggedInUser == null) {
            System.out.println("\n--- Login User ---");
            System.out.print("Email: ");
            email = s.nextLine().trim();

            if (email.equalsIgnoreCase("exit")) {
                return null;
            }

            System.out.print("Password: ");
            password = s.nextLine().trim();

            loggedInUser = login(email, password);

            if (loggedInUser == null) {
                System.out.println("Error: Incorrect email or password. Enter 'exit' or try again");
            }
        }

        System.out.println("Successful login! Welcome, " + loggedInUser.getUserEmail() + " !");
        return loggedInUser;
    }

    public void runApp() {
        Scanner s = new Scanner(System.in);
        currentUser = null;
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("\n=== IRINA'S SUPER GOOD CHESS GAME ===");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = s.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        currentUser = loginUser(s);
                        break;
                    case "2":
                        currentUser = signUpUser(s);
                        break;
                    case "3":
                        exitApp = true;
                        break;
                    default:
                        System.out.println("Invalid option. Retry");
                }
            } catch (Exception e) {
                System.err.println("Error while processing: " + e.getMessage());
            }

            // if authentication worked => go to MainMenu to choose the game from the list of games
            if (currentUser != null) {
                System.out.println("Going to Main Menu " + currentUser.getUserEmail());

                MainMenu mainMenu = new MainMenu(currentUser, this);
                long newGameId = this.getNextGameId();
                mainMenu.showGameMenu(s, newGameId);
                this.saveData();

                // it goes back to the opening menu
                currentUser = null;
            }

            if (exitApp) {
                System.out.println("--- Game is closing... ---");
                System.out.println("--- Thank you for playing with us! ---");
                System.out.println("\n=== IRINA'S SUPER GOOD CHESS GAME ===");

            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.read();
        app.runApp();
    }
}
