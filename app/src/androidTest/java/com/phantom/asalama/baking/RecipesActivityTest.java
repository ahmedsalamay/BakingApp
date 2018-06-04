package com.phantom.asalama.baking;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.phantom.asalama.baking.screens.Recipes.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    private final String INGREDIENTS = "Ingredients";
    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipesActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void regiseterIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void ShowRecipeDetails() {

        onView(ViewMatchers.withId(R.id.recipes_rec_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
        onView(withText(INGREDIENTS)).check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    /*
    @Test
    public void ValidateIntent(){
    /* Bundle bundle=new Bundle();
        Step step=new Step();
        step.setDescription("Recipe Introduction");
        step.setId(0);
        step.setShortDescription("Recipe Introduction");
        step.setThumbnailURL( "");
        step.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        bundle.putParcelable(StepDetailFragment.ARG_ITEM_ID,step);

        onView(ViewMatchers.withId(R.id.recipes_rec_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0,click()));

        intended(allOf(
                hasAction(equalTo(Intent.ACTION_VIEW)),
                //  hasExtras(equalTo(bundle)),
                // hasExtraWithKey(equalTo(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST )),
                toPackage("com.phantom.asalama.baking")));
    }
*/


}
