package com.theNemos.shadowPack.tabs;

/*import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.Card.CardMenuListener;
*/
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;


public class Tab implements TabBase<Tab>{

    private String title;
    private String content;
    private boolean isHeader;
    private int mPopupMenu;
    private TabMenuListener<Tab> mPopupListener;
    private boolean isClickable = true;
    private Object mTag;
    private Drawable mThumbnail;
    private int mLayout;
    
    protected Tab() {
    	}
    protected Tab(String title, String subtitle, boolean isHeader) {
        this(title, subtitle);
        this.isHeader = isHeader;
    }

    public Tab(String title) {
        this.title = title;
    }

    public Tab(String title, String content) {
        this(title);
        this.content = content;
    }

    public Tab(Context context, int titleRes) {
        this(context.getString(titleRes));
    }

    public Tab(Context context, String title, int contentRes) {
        this(title, context.getString(contentRes));
    }

    public Tab(Context context, int titleRes, String content) {
        this(context.getString(titleRes), content);
    }

    public Tab(Context context, int titleRes, int contentRes) {
        this(context.getString(titleRes), context.getString(contentRes));
    }

    public Object getSilkId() {
        return isHeader() + ":" + getTitle() + ":" + getContent();
    }
    @Override
    public boolean equalTo(Tab other) {
        boolean equal = getTitle().equals(other.getTitle()) &&
                isHeader() == other.isHeader() &&
                getPopupMenu() == other.getPopupMenu() &&
                getLayout() == other.getLayout();
        if (getContent() != null) {
            equal = equal && getContent().equals(other.getContent());
        } else {
            equal = equal && other.getContent() == null;
        }
        if (getThumbnail() != null) {
            Bitmap one = ((BitmapDrawable) getThumbnail()).getBitmap();
            Bitmap two = ((BitmapDrawable) other.getThumbnail()).getBitmap();
            equal = equal && one.sameAs(two);
        } else {
            equal = equal && other.getThumbnail() == null;
        }
        return equal;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }

    @Override
    public boolean isClickable() {
        return isClickable;
    }

    /**
     * Sets whether or not the Tab is clickable, setting it to false will turn the Tab's list selector off
     * and the list's OnItemClickListener will not be called.
     * <p/>
     * This <b>does not</b> override the isClickable value set to a {@link TabAdapter}, however.
     */
    public Tab setClickable(boolean clickable) {
        isClickable = clickable;
        return this;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    /**
     * Sets a tag of any type that can be used to keep track of Tabs.
     */
    public Tab setTag(Object tag) {
        mTag = tag;
        return this;
    }

    @Override
    public int getPopupMenu() {
        return mPopupMenu;
    }

    @Override
    public TabHeader.ActionListener getActionCallback() {
        return null;
    }

    @Override
    public String getActionTitle() {
        return null;
    }

    @Override
    public TabMenuListener getPopupListener() {
        return mPopupListener;
    }

    @Override
    public Drawable getThumbnail() {
        return mThumbnail;
    }

    /**
     * Sets an optional thumbnail drawable that's displayed on the left side of the Tab.
     *
     * @param drawable The drawable to be displayed as a thumbnail.
     */
    public Tab setThumbnail(Drawable drawable) {
        mThumbnail = drawable;
        return this;
    }

    @Override
    public int getLayout() {
        return mLayout;
    }

    /**
     * Sets a custom layout to be used for this Tab. This will override custom layouts set to the {@link TabAdapter}.
     *
     * @param layoutRes The resource ID of a layout containing views with the same IDs as the layout contained in this library.
     */
    public Tab setLayout(int layoutRes) {
        mLayout = layoutRes;
        return this;
    }

    /**
     * Sets an optional thumbnail image resource that's displayed on the left side of the Tab.
     *
     * @param context The context used to resolve the drawable resource ID.
     * @param resId   The resource ID of the drawable to use as a thumbnail.
     */
    public Tab setThumbnail(Context context, int resId) {
        setThumbnail(context.getResources().getDrawable(resId));
        return this;
    }

    /**
     * Sets an optional thumbnail image that's displayed on the left side of the Tab.
     *
     * @param context The context used to convert the Bitmap to a Drawable.
     * @param bitmap  The bitmap thumbnail to be displayed.
     */
    public Tab setThumbnail(Context context, Bitmap bitmap) {
        setThumbnail(new BitmapDrawable(context.getResources(), bitmap));
        return this;
    }

    /**
     * Sets a unique popup menu for the Tab, this will override any popup menu set for the {@link TabAdapter}
     * that the Tab is displayed in.
     * <p/>
     * Setting the menu resource to -1 disables the menu for this Tab, and will override any popup menu set
     * to the {@link TabAdapter}.
     *
     * @param menuRes  The menu resource ID to use for the Tab's popup menu.
     * @param listener A listener invoked when an option in the popup menu is tapped by the user.
     */
    public Tab setPopupMenu(int menuRes, TabMenuListener<Tab> listener) {
        mPopupMenu = menuRes;
        mPopupListener = listener;
        return this;
    }

    public interface TabMenuListener<ItemType> {
        public void onMenuItemClick(ItemType Tab, MenuItem item);
    }
}
