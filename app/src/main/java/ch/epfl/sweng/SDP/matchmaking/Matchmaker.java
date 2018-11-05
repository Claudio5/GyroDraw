package ch.epfl.sweng.SDP.matchmaking;

import android.support.annotation.NonNull;

import ch.epfl.sweng.SDP.firebase.database.Database;
import ch.epfl.sweng.SDP.firebase.user.RealCurrentUser;
import ch.epfl.sweng.SDP.firebase.database.RealDatabase;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Matchmaker implements MatchmakingInterface {

    private static Matchmaker singleInstance = null;
    // static method to create instance of Singleton class
    private String userId;
    private Database database;

    /**
     * Create a singleton Instance.
     *
     * @return returns a singleton instance.
     */
    public static Matchmaker getInstance() {
        if (singleInstance == null) {
            singleInstance = new Matchmaker();
        }

        return singleInstance;
    }

    /**
     * Matchmaker init.
     */
    private Matchmaker() {
        this.userId = RealCurrentUser.getInstance().getCurrentUserId();
        this.database = RealDatabase.getInstance();
    }

    /**
     * join a room.
     */
    public Boolean joinRoomOther() {

        Boolean successful = false;
        HttpURLConnection connection = null;

        try {
            //Create connection

            String urlParameters = "userId=" + URLEncoder.encode(userId, "UTF-8");
            URL url = new URL("https://us-central1-gyrodraw.cloudfunctions.net/joinGame?" + urlParameters);
            connection = createConnection(url);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
                successful = true;
                // otherwise, if any other status code is returned, or no status
                // code is returned, do stuff in the else block
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return successful;
    }

    /**
     * Creates a connection.
     *
     */
    public Task<String> joinRoom() {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        Map<String, Object> data = new HashMap<>();

        // Pass the ID for the moment
        data.put("username", userId);

        return mFunctions.getHttpsCallable("joinGame2")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    /**
     * Creates a connection.
     * @return set up connection
     */
    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        return connection;
    }

    /**
     * leave a room.
     *
     * @param roomId the id of the room.
     */
    public void leaveRoom(String roomId) {
        database.removeValue("realRooms."+roomId+".users."+userId);
    }

    public Boolean leaveRoomOther(String roomId) {
        database.removeValue("rooms."+roomId+".users."+userId);
        return true;
    }
}