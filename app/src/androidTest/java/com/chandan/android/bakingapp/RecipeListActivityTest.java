package com.chandan.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import com.chandan.android.bakingapp.activity.RecipeListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testRecipeItemScroll() {
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));
    }

    @Test
    public void testRecyclerviewClick() {
        sleepForSomeTime();
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.scrollToPosition(3));
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void testTextViewExists() {
        sleepForSomeTime();
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recyclerview)).check(matches(hasDescendant(withText("Servings: 8"))));
    }

    @Test
    public void testRecipeNameExists() {
        sleepForSomeTime();
        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_name_text_view), withText("Brownies"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_card_view),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeItemNavigation() {
        sleepForSomeTime();
        ViewInteraction cardView = onView(
                allOf(withId(R.id.recipe_card_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_recyclerview),
                                        0),
                                0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
    }

    @Test
    public void testRecipeStepsSelectionAndNavigation() {
        sleepForSomeTime();
        ViewInteraction cardView = onView(
                allOf(withId(R.id.recipe_card_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_recyclerview),
                                        0),
                                0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_steps_recyclerview),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
    }

    private static org.hamcrest.Matcher<View> childAtPosition(
            final org.hamcrest.Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private void sleepForSomeTime() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
