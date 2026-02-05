package TEMA1.DataSaving;

import TEMA1.GAME.Game;
import TEMA1.GAME.Board;
import TEMA1.GAME.Move;
import TEMA1.GAME.Pieces.*;
import TEMA1.GAME.Player;
import TEMA1.User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading JSON documents using JSON.simple ("simple-json").
 *
 * ###### IMPORTANT: This is just an example of how to read JSON documents using the library.
 * Your classes might differ slightly, so don’t hesitate to update this class as needed.
 *
 * Expected structures:
 * - accounts.json: an array of objects with fields: email (String), password (String), points (Number), games (array of numbers)
 * - games.json: an array of objects with fields matching the JSON provided:
 * id (Number), players (array of {email, color}), currentPlayerColor (String),
 * board (array of {type, color, position}), moves (array of {playerColor, from, to})
 */
public final class JsonReaderUtil {

    public JsonReaderUtil() {
    }

    /**
     * Reads the accounts from the given JSON file path.
     *
     * @param path path to accounts.json
     * @param pathGames path to games.json
     * @return list of Account objects (empty list if file not found or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static List<User> readAccounts(Path path, Path pathGames) throws IOException, ParseException {

        Map<Long, Game> mapGames = readGamesAsMap(pathGames);

        if (path == null || !Files.exists(path)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            List<User> result = new ArrayList<>();

            if (arr == null) {
                return result;
            }

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }

                User user = new User(asString(obj.get("email")), asString(obj.get("password")));

                long points = asLong(obj.get("points"), 0);
                user.setTotalPoints((int)points);

                JSONArray games = asArray(obj.get("games"));
                if (games != null) {
                    for (Object gid : games) {
                        Long gameId = asLong(gid, 0);
                        Game jsonGame = mapGames.get(gameId);

                        if (jsonGame != null) {
                            user.addGame(jsonGame);
                        }
                    }
                }

                result.add(user);
            }
            return result;
        }
    }

    /**
     * Reads the games from the given JSON file path and returns them as a map by id.
     * The structure strictly follows games.json as provided (no title/genre).
     *
     * @param path path to games.json
     * @return map id -> Game (empty if file missing or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static Map<Long, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Long, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) {
            return map;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            if (arr == null) return map;
            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }
                long id = asLong(obj.get("id"), -1);
                if (id < 0) {
                    continue;
                }// skip invalid

                // players array
                JSONArray playersArr = asArray(obj.get("players"));
                Player whitePlayer = null;
                Player blackPlayer = null;
                if (playersArr != null) {
                    for (Object pItem : playersArr) {
                        JSONObject pObj = asObject(pItem);
                        if (pObj == null) {
                            continue;
                        }
                        String email = asString(pObj.get("email"));
                        String color = asString(pObj.get("color"));
                        Colors playerColor = Colors.WHITE;

                        if(color.equals("BLACK")) {
                            playerColor = Colors.BLACK;
                        }

                        if (playerColor == Colors.WHITE) {
                            whitePlayer = new Player(email, playerColor);
                        } else {
                            blackPlayer = new Player(email, playerColor);
                        }
                    }
                }

                // currentPlayerColor
                Colors currentPlayerColor = getColorFromString(asString(obj.get("currentPlayerColor")));

                Game g = new Game(id, whitePlayer, blackPlayer);
                g.setCurrentPlayerColor(currentPlayerColor);

                // board array
                JSONArray boardArr = asArray(obj.get("board"));
                if (boardArr != null) {
                    List<Piece> board = new ArrayList<>();
                    for (Object bItem : boardArr) {
                        JSONObject bObj = asObject(bItem);
                        if (bObj == null) {
                            continue;
                        }

                        String type = asString(bObj.get("type"));
                        Colors pieceColor = getColorFromString(asString(bObj.get("color")));

                        String position = asString(bObj.get("position"));
                        Piece piece = getPiece(position, type, pieceColor);

                        // to check if the piece is a pawn and not where it's supposed to be in order
                        // to determine if it made it's first move
                        if (type.equals("P")) {
                            if (position.charAt(1) == '2' || position.charAt(1) == '7') {
                                ((Pawn)piece).setFirstMove(true);
                            } else {
                                ((Pawn)piece).setFirstMove(false);
                            }
                        }

                        board.add(piece);
                    }

                    Board chessBoardFromJson = g.getChessBoard();
                    chessBoardFromJson.initializeBoardFromList(board);
                }

                // Parse optional moves array
                JSONArray movesArr = asArray(obj.get("moves"));
                if (movesArr != null) {
                    List<Move> moves = new ArrayList<>();
                    for (Object mItem : movesArr) {
                        JSONObject mObj = asObject(mItem);
                        if (mObj == null) {
                            continue;
                        }

                        Colors playerColor = getColorFromString(asString(mObj.get("playerColor")));
                        Position from = getPositionFromString(asString(mObj.get("from")));
                        Position to = getPositionFromString(asString(mObj.get("to")));

                        Piece capturedPiece = null;

                        if (mObj.get("captured") != null) {
                            JSONObject pieceObj = asObject(mObj.get("captured"));

                            if (pieceObj != null) {
                                String type = asString(pieceObj.get("type"));
                                Colors pcColor = getColorFromString(asString(pieceObj.get("color")));
                                String position = "A1"; // position don t matter
                                capturedPiece = getPiece(position, type, pcColor);
                            }
                        }

                        moves.add(new Move(playerColor, from, to, capturedPiece, '0'));
                    }
                    g.setGameMoves(moves);

                    // adds capturedPieces to the players of the game
                    assert whitePlayer != null;
                    Player p1 = (whitePlayer.getColor() == Colors.WHITE) ? whitePlayer : blackPlayer;
                    Player p2 = (p1 == whitePlayer) ? blackPlayer : whitePlayer;

                    for (Move move : moves) {
                        Piece captured = move.getCapturedPiece();
                        if (captured != null) {
                            assert p1 != null;
                            Player capturingPlayer = (move.getPlayerColor() == p1.getColor()) ? p1 : p2;

                            assert capturingPlayer != null;
                            capturingPlayer.getCapturedPieces().add(captured);
                            capturingPlayer.setPoints(capturingPlayer.calculatePoints(captured));
                        }
                    }
                }
                map.put(id, g);
            }
        }
        return map;
    }

    private static Colors getColorFromString(String colorString) {
        Colors color = Colors.WHITE;
        if(colorString.equals("BLACK")) {
            color = Colors.BLACK;
        }
        return color;
    }

    private static Position getPositionFromString(String positionString) {
        return new Position(positionString.charAt(0), positionString.charAt(1)-'0');
    }

    private static Piece getPiece(String position, String type, Colors pieceColor) {
        Position piecePosition = getPositionFromString(position);

        Piece piece = null;
        if (type.equals("P")) {
            piece = new Pawn(pieceColor, piecePosition);
        } else if (type.equals("B")) {
            piece = new Bishop(pieceColor, piecePosition);
        } else if (type.equals("N")) {
            piece = new Knight(pieceColor, piecePosition);
        } else if (type.equals("R")) {
            piece = new Rook(pieceColor, piecePosition);
        } else if (type.equals("Q")) {
            piece = new Queen(pieceColor, piecePosition);
        } else if (type.equals("K")) {
            piece = new King(pieceColor, piecePosition);
        }
        return piece;
    }

    // -------- helper converters --------

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static long asLong(Object o, long defaultValue) {
        if (o instanceof Number) return ((Number) o).longValue();
        try {
            return o != null ? Long.parseLong(String.valueOf(o)) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}