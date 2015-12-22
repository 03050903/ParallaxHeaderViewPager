package com.desmond.parallaxviewpager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by desmond on 1/6/15.
 */
public abstract class ParallaxViewPagerBaseActivity extends AppCompatActivity implements ScrollTabHolder {

    public static final String TAG = ParallaxViewPagerBaseActivity.class.getSimpleName();

    protected static final String IMAGE_TRANSLATION_Y = "image_translation_y";
    protected static final String HEADER_TRANSLATION_Y = "header_translation_y";

    protected View mHeader;
    protected ViewPager mViewPager;
    protected ParallaxFragmentPagerAdapter mAdapter;

    protected int mMinHeaderHeight;
    protected int mHeaderHeight;
    protected int mMinHeaderTranslation;
    protected int mNumFragments;

    protected abstract void initValues();
    protected abstract void scrollHeader(int scrollY);
    protected abstract void setupAdapter();

    /**this method only works on listView with the same item height and without header
     * look at this:
     * http://stackoverflow.com/questions/10808387/android-getting-exact-scroll-position-in-listview?answertab=oldest#tab-top
     * @deprecated use getScrollY replaced
     * */
    protected int getScrollYOfListView(AbsListView view) {
        View child = view.getChildAt(0);
        if (child == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = child.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * child.getHeight() + headerHeight;
    }

    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();

    /**
     * Use this method to replace getScrollYofListView will work fine in anytime
     * Get listview top scroll height
     * @param view
     * @return
     */
    private int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        int scrollY = -c.getTop();
        listViewItemHeights.put(view.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < view.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null)
                scrollY += listViewItemHeights.get(i); //顶部消失的所有Item的高度
        }
        return scrollY;
    }

    protected ParallaxViewPagerChangeListener getViewPagerChangeListener() {
        return new ParallaxViewPagerChangeListener(mViewPager, mAdapter, mHeader);
    }

    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {}

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
//            scrollHeader(getScrollYOfListView(view));
            scrollHeader(getScrollY(view));
        }
    }

    @Override
    public void onScrollViewScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            scrollHeader(view.getScrollY());
        }
    }

    @Override
    public void onRecyclerViewScroll(RecyclerView view, int dx, int dy, int scrollY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            scrollHeader(scrollY);
        }
    }
}
