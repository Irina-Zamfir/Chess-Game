package TEMA1.DataSaving; // Asigură-te că pachetul este corect

import TEMA1.GAME.Game;
import TEMA1.GAME.Move;
import TEMA1.GAME.Pieces.Position;
import TEMA1.GAME.Player;
import TEMA1.GAME.Board; // Presupunem că Board este aici
import TEMA1.GAME.ChessPair; // Presupunem că ChessPair este definit
import TEMA1.GAME.Pieces.Piece;
import TEMA1.User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JsonWriterUtil {

    private static void writeToFile(Path path, JSONArray jsonArray) {
        try (FileWriter file = new FileWriter(path.toFile())) {
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file " + path + ": " + e.getMessage());
        }
    }

    private static JSONObject serializePiece(Piece piece) {
        if (piece == null) {
            return null;
        }
        JSONObject jsonPiece = new JSONObject();
        jsonPiece.put("type", String.valueOf(piece.type()));
        jsonPiece.put("color", piece.getColor().toString());
        jsonPiece.put("position", piece.getPosition().toString());
        return jsonPiece;
    }

    private static JSONObject serializeMove(Move move) {
        JSONObject jsonMove = new JSONObject();
        jsonMove.put("playerColor", move.getPlayerColor().toString());
        jsonMove.put("from", move.getFrom().toString());
        jsonMove.put("to", move.getTo().toString());

        if (move.getCapturedPiece() != null) {
            Piece capturedPiece = move.getCapturedPiece();

            JSONObject jsonCaptured = new JSONObject();
            jsonCaptured.put("type", String.valueOf(capturedPiece.type()));
            jsonCaptured.put("color", capturedPiece.getColor().toString());

            jsonMove.put("captured", jsonCaptured);
        }
        return jsonMove;
    }

    private static JSONObject serializeGamePlayer(Player player) {
        JSONObject jsonPlayer = new JSONObject();
        jsonPlayer.put("email", player.getName());
        jsonPlayer.put("color", player.getColor().toString());
        return jsonPlayer;
    }

    public static void writeGames(Path path, Collection<Game> gameList) {
        JSONArray gamesArray = new JSONArray();

        for (Game game : gameList) {
            JSONObject jsonGame = new JSONObject();
            jsonGame.put("id", game.getGameId());

            JSONArray playersArray = new JSONArray();
            playersArray.add(serializeGamePlayer(game.getWhitePlayer()));
            playersArray.add(serializeGamePlayer(game.getBlackPlayer()));
            jsonGame.put("players", playersArray);

            jsonGame.put("currentPlayerColor", game.getCurrentPlayer().getColor().toString());
            JSONArray boardArray = new JSONArray();

            Collection<ChessPair<Position, Piece>> piecesOnBoard = game.getChessBoard().getBoardPieces();

            for (ChessPair<Position, Piece> pair : piecesOnBoard) {
                boardArray.add(serializePiece(pair.getValue()));
            }
            jsonGame.put("board", boardArray);

            JSONArray movesArray = new JSONArray();
            for (Move move : game.getGameMoves()) {
                movesArray.add(serializeMove(move));
            }
            jsonGame.put("moves", movesArray);

            gamesArray.add(jsonGame);
        }

        writeToFile(path, gamesArray);
    }

    public static void writeAccounts(Path path, List<User> userList) {
        JSONArray accountsArray = new JSONArray();

        for (User user : userList) {
            JSONObject jsonUser = new JSONObject();

            jsonUser.put("email", user.getUserEmail());
            jsonUser.put("password", user.getUserPassword());

            jsonUser.put("points", user.getUserPoints());

            List<Long> gameIds = user.getActiveGames().stream()
                    .map(Game::getGameId)
                    .collect(Collectors.toList());

            JSONArray gamesJsonArray = new JSONArray();
            for (Long id : gameIds) {
                gamesJsonArray.add(id);
            }

            jsonUser.put("games", gamesJsonArray);

            accountsArray.add(jsonUser);
        }

        writeToFile(path, accountsArray);
    }
}