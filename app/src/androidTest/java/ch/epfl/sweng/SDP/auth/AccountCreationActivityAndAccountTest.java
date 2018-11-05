package ch.epfl.sweng.SDP.auth;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.SDP.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


@RunWith(AndroidJUnit4.class)
public class AccountCreationActivityAndAccountTest {

    @Rule
    public final ActivityTestRule<AccountCreationActivity> activityRule =
            new ActivityTestRule<>(AccountCreationActivity.class);

    private ConstantsWrapper mockConstantsWrapper;
    private Account mockAccount;

    @Before
    public void init() {
        mockConstantsWrapper = mock(ConstantsWrapper.class);
        mockAccount = mock(Account.class);
        DatabaseReference mockReference = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);

        when(mockConstantsWrapper.getReference(isA(String.class))).thenReturn(mockReference);
        when(mockConstantsWrapper.getFirebaseUserId()).thenReturn("123456789");

        when(mockReference.child(isA(String.class))).thenReturn(mockReference);
        when(mockReference.orderByChild(isA(String.class))).thenReturn(mockQuery);

        when(mockQuery.equalTo(isA(String.class))).thenReturn(mockQuery);

        doNothing().when(mockReference)
                .setValue(isA(Integer.class), isA(DatabaseReference.CompletionListener.class));
        doNothing().when(mockReference)
                .setValue(isA(Boolean.class), isA(DatabaseReference.CompletionListener.class));
        doNothing().when(mockReference)
                .removeValue(isA(DatabaseReference.CompletionListener.class));

        doNothing().when(mockQuery).addListenerForSingleValueEvent(isA(ValueEventListener.class));

        doNothing().when(mockAccount).registerAccount();
        doNothing().when(mockAccount).updateUsername(isA(String.class));

        Account.createAccount(activityRule.getActivity(), mockConstantsWrapper, "123456789");
        Account.getInstance(activityRule.getActivity()).setUserId("123456789");
        Account.enableTesting();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountWithNullUsername() {
        Account.createAccount(activityRule.getActivity(), mockConstantsWrapper, null);
    }

    @Test
    public void testSetTrophies() {
        Account.getInstance(activityRule.getActivity()).setTrophies(1);
    }

    @Test
    public void testSetStars() {
        Account.getInstance(activityRule.getActivity()).setStars(1);
    }


    @Test
    public void testSetCurrentLeague() {
        Account.getInstance(activityRule.getActivity()).setCurrentLeague("test");
    }

    @Test
    public void testSetMatchesWon() {
        Account.getInstance(activityRule.getActivity()).setMatchesWon(1);
    }

    @Test
    public void testSetMatchesLost() {
        Account.getInstance(activityRule.getActivity()).setMatchesLost(1);
    }

    @Test
    public void testSetMaxTrophies() {
        Account.getInstance(activityRule.getActivity()).setMaxTrophies(1);
    }

    @Test
    public void testSetAverageRating() {
        Account.getInstance(activityRule.getActivity()).setAverageRating(1.0);
    }

    @Test
    public void testSetUsersRef() {
        DatabaseReference databaseReference = Mockito.mock(DatabaseReference.class);
        Account.getInstance(activityRule.getActivity()).setUsersRef(databaseReference);
    }

    @Test
    public void testAccountValuesCorrect() {
        assertThat(Account.getInstance(activityRule.getActivity()).getTrophies(), is(0));
        assertThat(Account.getInstance(activityRule.getActivity()).getStars(), is(0));
        assertThat(Account.getInstance(activityRule.getActivity()).getUsername(), is("123456789"));
        assertThat(mockConstantsWrapper.getFirebaseUserId(), is("123456789"));
    }

    @Test
    public void testCurrentLeague() {
        assertThat(Account.getInstance(activityRule.getActivity()).getCurrentLeague(), is("league1"));
    }

    @Test
    public void testGetStars() {
        assertThat(Account.getInstance(activityRule.getActivity()).getStars(),
                is(0));
    }

    @Test
    public void testGetUserId() {
        assertThat(Account.getInstance(activityRule.getActivity()).getUserId(), is("123456789"));
    }

    @Test
    public void testGetUsername() {
        assertThat(Account.getInstance(activityRule.getActivity()).getUsername(), is("123456789"));
    }

    @Test
    public void testGetTrophies() {
        assertThat(Account.getInstance(activityRule.getActivity()).getTrophies(), is(0));
    }

    @Test
    public void testGetMatchesWon() {
        assertThat(Account.getInstance(activityRule.getActivity()).getMatchesWon(), is(0));
    }

    @Test
    public void testGetMatchesLost() {
        assertThat(Account.getInstance(activityRule.getActivity()).getMatchesLost(), is(0));
    }

    @Test
    public void testGetAverageRating() {
        assertThat(Account.getInstance(activityRule.getActivity()).getAverageRating(), is(0.0));
    }

    @Test
    public void testGetMaxTrophies() {
        assertThat(Account.getInstance(activityRule.getActivity()).getMaxTrophies(), is(0));
    }

    @Test
    public void testIncreaseMatchesWon() {
        Account.getInstance(activityRule.getActivity()).increaseMatchesWon();
    }

    @Test
    public void testIncreaseMatchesLost() {
        Account.getInstance(activityRule.getActivity()).increaseMatchesLost();
    }

    @Test
    public void testChangeAverageRating() {
        Account.getInstance(activityRule.getActivity()).changeAverageRating(3.5);
    }

    @Test
    public void testChangeTrophies() {
        Account.getInstance(activityRule.getActivity()).changeTrophies(20);
        //assertEquals(account.getTrophies(), 20);
    }

    @Test
    public void testAddStars() {
        Account.getInstance(activityRule.getActivity()).changeStars(20);
        //assertEquals(account.getStars(), 20);
    }

    @Test
    public void testAddFriend() {
        Account.getInstance(activityRule.getActivity()).addFriend("EPFLien");
    }

    @Test
    public void testRemoveFriend() {
        Account.getInstance(activityRule.getActivity()).removeFriend("EPFLien");
    }

    @Test
    public void testUpdateUsername() {
        mockAccount.updateUsername("987654321");
        Account.getInstance(activityRule.getActivity()).setUsername("987654321");
        assertThat(Account.getInstance(activityRule.getActivity()).getUsername(), is("987654321"));
    }

    @Test
    public void testRegisterAccount() {
        mockAccount.registerAccount();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUsername() {
        Account.getInstance(activityRule.getActivity()).updateUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeBalanceStars() {
        Account.getInstance(activityRule.getActivity()).changeStars(-1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullFriend() {
        Account.getInstance(activityRule.getActivity()).addFriend(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullFriend() {
        Account.getInstance(activityRule.getActivity()).removeFriend(null);
    }

    @Test
    public void testCreateAccIsClickable() {
        onView(ViewMatchers.withId(R.id.createAcc)).check(matches(isClickable()));
    }

    @Test
    public void testCreateAccountWithNullName() {
        onView(ViewMatchers.withId(R.id.createAcc)).perform(click());
        onView(ViewMatchers.withId(R.id.usernameTaken))
                .check(matches(withText("Username must not be empty.")));
    }

    @Test
    public void testUsernameInputInputsCorrectly() {
        onView(withId(R.id.usernameInput))
                .perform(typeText("Max Muster"), closeSoftKeyboard())
                .check(matches(withText(R.string.test_name)));
    }

    @Test
    public void testCreateAccountWithValidInput() {
        onView(withId(R.id.usernameInput))
                .perform(typeText("Max Muster"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.createAcc)).perform(click());
        assertNotEquals(null, Account.getInstance(activityRule.getActivity()));
    }


}