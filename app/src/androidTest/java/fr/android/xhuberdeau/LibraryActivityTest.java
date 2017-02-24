package fr.android.xhuberdeau;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LibraryActivityTest {
    @Rule
    public ActivityTestRule<LibraryActivity> activityRule = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);

    @Before
    public void setUp(){
        // wait for books to be loaded
        onView(isRoot()).perform(waitId(R.id.bookListView, TimeUnit.SECONDS.toMillis(15)));
    }
    @Test
    public void init_with_portrait_shows_only_book_list(){
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.bookListView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bookDetailView))
                .check(doesNotExist());
    }

    @Test
    public void init_with_landscape_shows_both_book_list_and_book_detail(){
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.bookListView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bookDetailView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void click_on_book_item_in_portrait_leads_to_detail(){
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.bookDetailView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bookListView)) // list view not displayed anymore
                .check(doesNotExist());
    }

    @Test
    public void landscape_choose_book_turn_portrait_should_display_book_detail(){
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookDetailView))));
        onView(withId(R.id.bookListView))
                .check(doesNotExist());
    }

    @Test
    public void portrait_book_detail_go_back_should_lead_to_list_view(){
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookDetailView))));
        pressBack();
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookListView))));
    }

    @Test
    public void full_behavior_from_portrait_state(){
        // portrait mode
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // list view should be visible
        onView(withId(R.id.bookListView))
                .check(matches(isDisplayed()));
        // click on the first book
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // detail view should have replaced list view
        onView(withId(R.id.bookListView))
                .check(doesNotExist());
        onView(withId(R.id.bookDetailView))
                .check(matches(isDisplayed()));
        // go back
        pressBack();
        // list view should have replaced detail view
        onView(withId(R.id.bookListView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bookDetailView))
                .check(doesNotExist());
        // click again on the first book
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // go into landscape mode
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // left side frame should be the book list
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookListView))));
        // right side frame should be the book detail
        onView(withId(R.id.secondaryFrame))
                .check(matches(withChild(withId(R.id.bookDetailView))));
    }

    @Test
    public void full_behavior_from_landscape_state(){
        // set orientation to landscape
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // left side should be book list
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookListView))));
        // right side should be book detail
        onView(withId(R.id.secondaryFrame))
                .check(matches(withChild(withId(R.id.bookDetailView))));
        // select first book
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // go into portrait mode
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // book detail view should be the only one visible
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookDetailView))));
        onView(withId(R.id.bookListView))
                .check(doesNotExist());
        // go back
        pressBack();
        // list book view should have replaced detail book view
        onView(withId(R.id.mainFrame))
                .check(matches(withChild(withId(R.id.bookListView))));
    }

    /**
     * To change screen orientation
     * @param orientation
     */
    private void setOrientation(int orientation){
        activityRule.getActivity().setRequestedOrientation(orientation);
    }

    /**
     * // Used to wait asynchronous action to be performed
     * @param viewId
     * @param millis
     * @return ViewAction
     */
    private static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
