package ch.epfl.sweng.SDP.matchmaking;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MatchmakerTest {

    @Test
    public void matchMaker() {
        Matchmaker.INSTANCE.leaveRoom("2312");
        assertTrue(true);
    }


    @Test
    public void joinRoom() {
        Matchmaker.INSTANCE.joinRoom("2312");
        assertTrue(true);

    }

    @Test
    public void leaveRoom() {
        Matchmaker.INSTANCE.leaveRoom("2312");
        assertTrue(true);
    }

}