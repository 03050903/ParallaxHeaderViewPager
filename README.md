# Android ViewPager With Vertical Scroll

## Description
A code demo to show one single parallax top image with a sticky toolbar, with a viewpager hosting fragments with scrollview and listview. The top image and sticky toolbar will be preserved when the user scrolls horizontally through the viewpager.

The code is based loosely from the snippets in http://nerds.airbnb.com/host-experience-android/ The differences are due to the inability to get the snippets to work as expected. 

## Support Pull To Refresh
if you use [Chris Banes' Android-PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh) to add refresh thing in your project 
something you need to do
1.Add a listener in PullToRefreshBase like:
   ```java 
    
     private OnPullDistanceListener<T> mOnPullDistanceListener;
     ..................
       
     //set listener
       public void setOnPullDistanceListener(OnPullDistanceListener<T> mOnPullDistanceListener) {
               this.mOnPullDistanceListener = mOnPullDistanceListener;
           }
           
           
     //add listener callback in method setHeaderScroll
          
     /**
     	 * Helper method which just calls scrollTo() in the correct scrolling
     	 * direction.
     	 * 
     	 * @param value
     	 *            - New Scroll value
     	 */
     	protected final void setHeaderScroll(int value) {
     		if (DEBUG) {
     			Log.d(LOG_TAG, "setHeaderScroll: " + value);
     		}
     
     		// Clamp value to with pull scroll range
     		final int maximumPullScroll = getMaximumPullScroll();
     		value = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, value));
     
             // add pull callback
             mOnPullDistanceListener.onPullDistance(this,mCurrentMode,-value);
             
             .....
             remainder omitted.
                  
   ```
   
   2.set setOnPullDistanceListener in your pullrefresh view
   
   3.add callback method in interface ScrollTabHolder
   ```java 
   
      void onListOverScroll(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.Mode mode, int distance);
   
   ```
   
   4.handle OnPullDistanceListener and set onListOverScroll
   
   ```java
   
    @Override
    public void onPullDistance(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.Mode mode, int distance) {
        if(mode == PullToRefreshBase.Mode.PULL_FROM_START){//下拉
           if(mScrollTabHolder != null){
               mScrollTabHolder.onListOverScroll(refreshView,mode,distance);
           }
        }
    }
   
   ```
   And the next, handle onListOverScroll in outer activity or outer fragment
   
## Caveat
1. If one of the scrollable fragments does not contain enough elements for a full page scroll, the sticky toolbar will be translated back to the appropriate height, along with the top image.

## Video Demo
Link: https://www.youtube.com/embed/Mam4TFEiWWI 
