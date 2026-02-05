package TEMA1;

import TEMA1.Exceptions.InvalidMoveException;
import TEMA1.GAME.Game;
import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Player;
import TEMA1.User.User;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private User currentUser;
    private Main app;

    public MainMenu(User user, Main app) {
        this.currentUser = user;
        this.app = app;
    }

    public void playAndSaveActualGame(Scanner s, GameMenu gameMenu) throws InvalidMoveException {
        gameMenu.runGame(s);
        updateUserScore(gameMenu.getPlayerUser());
        View.printGameState(gameMenu.getGame(), currentUser);
    }

    public void startNewGame(Scanner s, long gameId) throws InvalidMoveException {
        System.out.println("Starting a new game...");
        System.out.println("Please choose a color: WHITE or BLACK");

        boolean choiceWasMade = false;

        Player userPlayer = new Player(currentUser.getUserEmail(), null);
        Player computer = new Player("computer", null);
        Game newGame = null;

        while (!choiceWasMade) {
            System.out.print("Choice: ");

            String color = s.nextLine();
            if (color.equalsIgnoreCase("white")) {
                userPlayer.setColor(Colors.WHITE);
                computer.setColor(Colors.BLACK);
                newGame = new Game(gameId, userPlayer, computer);
                choiceWasMade = true;
            } else if (color.equalsIgnoreCase("black")) {
                userPlayer.setColor(Colors.BLACK);
                computer.setColor(Colors.WHITE);
                newGame = new Game(gameId, computer, userPlayer);
                choiceWasMade = true;
            } else {
                System.out.println("Invalid command! Choose again");
            }
        }

        this.currentUser.addGame(newGame); // add new game to users list
        this.app.addGameToGameList(newGame);

        GameMenu gameMenu = new GameMenu(currentUser, newGame, userPlayer, computer);
        newGame.start();
        playAndSaveActualGame(s, gameMenu);
    }

    public void chooseOldGame(Scanner s) throws InvalidMoveException {

        boolean choosingId = true;

        while (choosingId) {
            List<Game> activeGames = this.currentUser.getActiveGames();
            Game gameToBeResumed = null;
            int chosenId = -1;

            if (activeGames.isEmpty()) {
                System.out.println("You don't have active games to resume. Returning to Main Menu.");
                return;
            }

            while (gameToBeResumed == null) {
                System.out.println("\n--- ACTIVE GAMES ---");
                System.out.print("Game ID-s: | ");

                for (Game g : activeGames) {
                    System.out.print(g.getGameId() + " | ");
                }

                System.out.print("\nChoose an ID to resume the game (or 0 to cancel): ");

                if (!s.hasNextInt()) {
                    System.out.println("Invalid command. Please choose a number");
                    s.nextLine();
                    continue;
                }

                chosenId = s.nextInt();
                s.nextLine();

                if (chosenId == 0) {
                    System.out.println("Canceled. Going back to Main Menu");
                    return;
                }

                for (Game g : activeGames) {
                    if (g.getGameId() == chosenId) {
                        gameToBeResumed = g;
                        break;
                    }
                }

                if (gameToBeResumed == null) {
                    System.out.println("Error: There s no game with the ID " + chosenId + ". Please try again.");
                }
            }

            boolean stayInOptions = true;
            while (stayInOptions) {
                View.printGameState(gameToBeResumed, currentUser);

                System.out.println("\n--- OPTIONS FOR GAME " + gameToBeResumed.getGameId() + " ---");
                System.out.println("1. Resume Game");
                System.out.println("2. Delete Game");
                System.out.println("3. Back (Choose another game / Back to menu)");
                System.out.print("Choose an option: ");

                String userChoice = s.nextLine().trim();

                switch (userChoice) {
                    case "1":
                        System.out.println("Resume game...");
                        gameToBeResumed.resume();

                        Player white = gameToBeResumed.getWhitePlayer();
                        Player black = gameToBeResumed.getBlackPlayer();

                        GameMenu gameMenu = new GameMenu(currentUser, gameToBeResumed, white, black);
                        playAndSaveActualGame(s, gameMenu);
                        this.app.updateGameInMap(gameToBeResumed);

                        return;

                    case "2":
                        System.out.println("Game with ID " + gameToBeResumed.getGameId() + " has been removed.");
                        this.currentUser.removeGame(gameToBeResumed);
                        this.app.removeGameFromGameList(gameToBeResumed);
                        this.app.updateUserInList(this.currentUser);

                        stayInOptions = false;
                        break;

                    case "3":
                        stayInOptions = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please introduce 1, 2 or 3.");
                }
            }
        }
    }

    public void showGameMenu(Scanner s, long newGameId) {

        System.out.println("\n*After playing please return to Main Menu and choose exit*");
        System.out.println("*Otherwise we won't be able to save your data*\n");

        boolean stayInMenu = true;

        while (stayInMenu) {
            System.out.println("\n--- MAIN MENU ---\n");
            System.out.println("User " + currentUser.getUserEmail() + " what do you want to do ?");
            System.out.println("1. Start a new game");
            System.out.println("2. Continue an old game");
            System.out.println("3. See user score");
            System.out.println("4. Exit and save");
            System.out.print("Choose an option: ");

            String userInput = s.nextLine();

            try {
                switch (userInput) {
                    case "1":
                        startNewGame(s, newGameId);
                        this.app.updateUserInList(this.currentUser);
                        break;
                    case "2":
                        chooseOldGame(s);
                        break;
                    case "3":
                        System.out.println("Total score of user " + currentUser.getUserEmail() + " is: "
                                + currentUser.getUserPoints() + " points.");
                        break;
                    case "4":
                        stayInMenu = false;
                        break;
                    default:
                        System.out.println("Invalid option. Retry");
                }
            } catch (Exception e) {
                System.err.println("Error while processing: " + e.getMessage());
            }
        }
    }

    private void updateUserScore(Player gamePlayer) {
        if (gamePlayer.getName().equalsIgnoreCase(this.currentUser.getUserEmail())) {

            int pointsEarned = gamePlayer.getPoints();
            this.currentUser.addTotalPoints(pointsEarned);
            System.out.println("Updated total score for " + this.currentUser.getUserEmail() + ": " + pointsEarned + " points.");
        }
    }

}
