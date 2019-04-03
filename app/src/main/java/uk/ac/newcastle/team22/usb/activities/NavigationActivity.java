package uk.ac.newcastle.team22.usb.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.fragments.NavigationAdapter;
import uk.ac.newcastle.team22.usb.fragments.NavigationDirection;

/**
 * A class which prepares navigation between two locations in the Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @author Daniel Vincent
 * @version 1.0
 */
public class NavigationActivity extends USBActivity {
    private RecyclerView recyclerView;
    private NavigationAdapter adapter;
    private List<Object> directionCardViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        directionCardViewList = new ArrayList<>();
        adapter = new NavigationAdapter(this, directionCardViewList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareDirections();
    }


    /**
     * Adding few albums for testing
     */
    private void prepareDirections() {
        int[] covers = new int[] {
                R.drawable.navigation_forward,
                R.drawable.navigation_keycard,
                R.drawable.navigation_left,
                R.drawable.navigation_lift_down
        };

        NavigationDirection a = new NavigationDirection("Continue Forwards", 13, covers[0]);
        directionCardViewList.add(a);

        a = new NavigationDirection("keycard", 8, covers[1]);
        directionCardViewList.add(a);

        a = new NavigationDirection("Turn Left", 11, covers[2]);
        directionCardViewList.add(a);

        a = new NavigationDirection("Take the Lift Down", 3, covers[3]);
        directionCardViewList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * @return Boolean value whether the user requires a lift when changing floors.
     */
    private boolean navigationRequiresLifts() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        return manager.getBoolean("navigationRequiresLiftSettingsKey", false);
    }
}