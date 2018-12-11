package ch.epfl.sweng.SDP.utils;

import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.SDP.firebase.Database;

import static ch.epfl.sweng.SDP.utils.Preconditions.checkPrecondition;
import static java.lang.String.format;

/**
 * Enum representing whether the user is online or offline.
 */
public enum OnlineStatus {
    OFFLINE, ONLINE;

    /**
     * Builds a {@link OnlineStatus} value from the given integer.
     *
     * @param integer the integer corresponding to the desired enum value
     * @return an enum value
     * @throws IllegalArgumentException if the given integer does not correspond to a status
     */
    public static OnlineStatus fromInteger(int integer) {
        switch (integer) {
            case 0:
                return OFFLINE;
            case 1:
                return ONLINE;
            default:
                throw new IllegalArgumentException(integer + " does not correspond to a status");
        }
    }

    /**
     * Changes the user online status to the given {@link OnlineStatus} value.
     *
     * @param userId the userId of the user
     * @param status the desired status for the user
     * @return a {@link Task} wrapping the operation
     * @throws IllegalArgumentException if the userId string is null or the given status is
     *                                  wrong/unknown
     */
    public static Task<Void> changeOnlineStatus(String userId, OnlineStatus status) {
        checkPrecondition(userId != null, "userId is null");
        checkPrecondition(status == OFFLINE || status == ONLINE,
                "Wrong status given");

        return Database
                .getReference(format("users.%s.online", userId))
                .setValue(status.ordinal());
    }

    /**
     * Changes the user status to offline upon disconnection (app closed).
     *
     * @param userId the userId of the user
     * @throws IllegalArgumentException if the userId string is null
     */
    public static void changeToOfflineOnDisconnect(String userId) {
        checkPrecondition(userId != null, "userId is null");
        Database.getReference(format("users.%s.online", userId))
                .onDisconnect()
                .setValue(OFFLINE.ordinal());
    }
}