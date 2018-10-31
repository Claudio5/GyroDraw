package ch.epfl.sweng.SDP.matchmaking;

import com.google.android.gms.tasks.Task;


public interface MatchmakingInterface {
  
    void leaveRoom(String roomId);

    Task<String> joinRoom();

    Boolean joinRoomOther();

    Boolean leaveRoomOther(String roomId);
  
}

