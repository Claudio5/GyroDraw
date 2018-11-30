package ch.epfl.sweng.SDP.game.drawing;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.auth.Account;
import ch.epfl.sweng.SDP.firebase.Database;
import ch.epfl.sweng.SDP.game.VotingPageActivity;
import ch.epfl.sweng.SDP.localDatabase.LocalDbHandlerForImages;
import ch.epfl.sweng.SDP.matchmaking.GameStates;
import ch.epfl.sweng.SDP.matchmaking.Matchmaker;

import static java.lang.String.format;

public class DrawingOnline extends GyroDrawingActivity {

    private String winningWord;

    private static final String TOP_ROOM_NODE_ID = "realRooms";

    private String roomId;

    private DatabaseReference timerRef;
    private DatabaseReference stateRef;
    private boolean isVotingActivityLaunched = false;

    protected final ValueEventListener listenerTimer = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Integer value = dataSnapshot.getValue(Integer.class);

            if (value != null) {
                ((TextView) findViewById(R.id.timeRemaining)).setText(String.valueOf(value));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Does nothing for the moment
        }
    };

    protected final ValueEventListener listenerState = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Integer state = dataSnapshot.getValue(Integer.class);
            if (state != null) {
                GameStates stateEnum = GameStates.convertValueIntoState(state);
                switch (stateEnum) {
                    case WAITING_UPLOAD:
                        uploadDrawing().addOnCompleteListener(
                                new OnCompleteListener<TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<TaskSnapshot> task) {
                                        Database.getReference(
                                                format("%s.%s.uploadDrawing.%s", TOP_ROOM_NODE_ID,
                                                        roomId,
                                                        Account.getInstance(getApplicationContext())
                                                                .getUsername())).setValue(1);
                                        Log.d(TAG, "Upload completed");

                                        Log.d(TAG, winningWord);
                                        isVotingActivityLaunched = true;
                                        timerRef.removeEventListener(listenerTimer);

                                        Intent intent = new Intent(getApplicationContext(),
                                                VotingPageActivity.class);
                                        intent.putExtra("RoomID", roomId);
                                        startActivity(intent);
                                    }
                                });
                        break;
                    default:
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Does nothing for the moment
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drawing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface typeMuro = Typeface.createFromAsset(getAssets(), "fonts/Muro.otf");

        Intent intent = getIntent();

        roomId = intent.getStringExtra("RoomID");
        winningWord = intent.getStringExtra("WinningWord");

        TextView wordView = findViewById(R.id.winningWord);
        wordView.setText(winningWord);
        wordView.setTypeface(typeMuro);

        ((TextView) findViewById(R.id.timeRemaining)).setTypeface(typeMuro);

        timerRef = Database.getReference(TOP_ROOM_NODE_ID + "." + roomId + ".timer.observableTime");
        timerRef.addValueEventListener(listenerTimer);
        stateRef = Database.getReference(TOP_ROOM_NODE_ID + "." + roomId + ".state");
        stateRef.addValueEventListener(listenerState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Does not leave the room if the activity is stopped because
        // voting activity is launched.
        if (!isVotingActivityLaunched) {
            Matchmaker.getInstance(Account.getInstance(this)).leaveRoom(roomId);
        }

        removeAllListeners();

        finish();
    }

    protected void removeAllListeners() {
        timerRef.removeEventListener(listenerTimer);
        stateRef.removeEventListener(listenerState);
    }

    /**
     * Saves drawing in the local database and uploads it to Firebase Storage.
     *
     * @return the {@link StorageTask} in charge of the upload
     */
    private StorageTask<TaskSnapshot> uploadDrawing() {
        LocalDbHandlerForImages localDbHandler = new LocalDbHandlerForImages(this, null, 1);
        paintView.saveCanvasInDb(localDbHandler);
        return paintView.saveCanvasInStorage();
    }

    /**
     * Method that call onDataChange on the UI thread.
     *
     * @param dataSnapshot Snapshot of the database (mock snapshot in out case).
     */
    @VisibleForTesting
    public void callOnDataChangeTimer(final DataSnapshot dataSnapshot) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listenerTimer.onDataChange(dataSnapshot);
            }
        });
    }
}