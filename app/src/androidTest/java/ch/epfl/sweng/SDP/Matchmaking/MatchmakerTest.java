package ch.epfl.sweng.SDP.Matchmaking;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;

//import org.mockito.Matchers;
//import org.mockito.Mockito;

import ch.epfl.sweng.SDP.Account;
import ch.epfl.sweng.SDP.ConstantsWrapper;
import ch.epfl.sweng.SDP.matchmaking.Matchmaker;
import ch.epfl.sweng.SDP.matchmaking.MatchmakingInterface;


import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;

import android.support.test.InstrumentationRegistry;

public class MatchmakerTest {

    ConstantsWrapper mockConstantsWrapper;
    DatabaseReference mockReference;
    Query mockQuery;
    Account account;

    MatchmakingInterface mockedmatchmaker;

    @Before
    public void init() {

        Matchmaker.getInstance().testing = true;

        mockedmatchmaker = new MatchmakingInterface() {

            @Override
            public Boolean joinRoom() {
                return true;
            }

            @Override
            public Boolean leaveRoom(String roomId) {
                return true;
            }
        };

    }

    @Test
    public void joinRoom() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getContext());
        Boolean functionReturned = Matchmaker.getInstance().joinRoom();
        assertTrue(functionReturned);
    }

    @Test
    public void leaveRoom() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getContext());
        Boolean funtionReturned = Matchmaker.getInstance().leaveRoom("Testroom");
        assertTrue(funtionReturned);
    }


}