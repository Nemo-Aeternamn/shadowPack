package com.theNemos.shadowPack.tabs;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.theNemos.shadowPack.*;
import com.theNemos.shadowPack.silk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link SilkAdapter} that displays {@link Tab} and {@link TabHeader} objects in a {@link TabListView}.
 * @author Aidan Follestad (afollestad)
 */
public class TabAdapter<ItemType extends TabBase<ItemType>> extends SilkAdapter<ItemType> {

    private final static int TYPE_REGULAR = 0;
    private final static int TYPE_NO_CONTENT = 1;
    private final static int TYPE_HEADER = 2;
    private final static int DEFAULT_TYPE_COUNT = 3;

    private final static int POPUP_MENU_THEME = android.R.style.Theme_Holo_Light;
    private final Map<Integer, Integer> mViewTypes;
    protected int mAccentColor;
    private int mPopupMenu = -1;
    private Tab.TabMenuListener<ItemType> mPopupListener;
    private boolean mTabsClickable = true;
    private int mLayout = R.layout.list_item_tab;
    private int mLayoutNoContent = R.layout.list_item_tab_nocontent;
    private final int mLayoutHeader = R.layout.list_item_header;

    /**
     * Initializes a new TabAdapter instance.
     *
     * @param context The context used to inflate layouts and retrieve resources.
     */
    public TabAdapter(Context context) {
        super(context);
        mAccentColor = context.getResources().getColor(R.color.dark);
        mViewTypes = new HashMap<Integer, Integer>();
        registerLayout(R.layout.list_item_header_centered);
        registerLayout(R.layout.list_item_tab_compressed);
    }

    /**
     * Initializes a new TabAdapter instance.
     *
     * @param context       The context used to inflate layouts and retrieve resources.
     * @param TabLayoutRes Sets a custom layout to be used for all Tabs (not including headers) in the adapter.
     *                      This <b>does not</b> override layouts set to individual Tabs.
     */
    public TabAdapter(Context context, int TabLayoutRes) {
        this(context);
        mLayout = TabLayoutRes;
    }

    /**
     * Initializes a new TabAdapter instance.
     *
     * @param context                The context used to inflate layouts and retrieve resources.
     * @param TabLayoutRes          Sets a custom layout to be used for all Tabs (not including headers) in the adapter.
     *                               This <b>does not</b> override layouts set to individual Tabs.
     * @param TabLayoutNoContentRes Sets a custom layout to be used for all Tabs (not including headers) in the
     *                               adapter with null content. This <b>does not</b> override layouts set to individual Tabs.
     */
    public TabAdapter(Context context, int TabLayoutRes, int TabLayoutNoContentRes) {
        this(context, TabLayoutRes);
        mLayoutNoContent = TabLayoutNoContentRes;
    }

    public static void setupTouchDelegate(Context context, final View menu) {
        final int offset = context.getResources().getDimensionPixelSize(R.dimen.tab_action_touchdelegate);
        ((View) menu.getParent()).post(new Runnable() {
            public void run() {
                Rect delegateArea = new Rect();
                menu.getHitRect(delegateArea);
                delegateArea.top -= offset;
                delegateArea.bottom += offset;
                delegateArea.left -= offset;
                delegateArea.right += offset;
                TouchDelegate expandedArea = new TouchDelegate(delegateArea, menu);
                ((View) menu.getParent()).setTouchDelegate(expandedArea);
            }
        });
    }

    @Override
    public final boolean isEnabled(int position) {
        ItemType item = getItem(position);
        if (!mTabsClickable && !item.isHeader()) return false;
        if (item.isHeader())
            return item.getActionCallback() != null;
        return item.isClickable();
    }

    /**
     * Sets the accent color used on Tab titles and header action buttons.
     * You <b>should</b> call this method before adding any Tabs to the adapter to avoid issues.
     *
     * @param color The resolved color to use as an accent.
     */
    public final TabAdapter<ItemType> setAccentColor(int color) {
        mAccentColor = color;
        return this;
    }

    /**
     * Sets the accent color resource used on Tab titles and header action buttons.
     * You <b>should</b> call this method before adding any Tabs to the adapter to avoid issues.
     *
     * @param colorRes The color resource ID to use as an accent.
     */
    public final TabAdapter<ItemType> setAccentColorRes(int colorRes) {
        setAccentColor(getContext().getResources().getColor(colorRes));
        return this;
    }

    /**
     * Sets a popup menu used for every Tab in the adapter, this will not override individual Tab popup menus.
     * You <b>should</b> call this method before adding any Tabs to the adapter to avoid issues.
     *
     * @param menuRes  The menu resource ID to use for the Tab's popup menu.
     * @param listener A listener invoked when an option in the popup menu is tapped by the user.
     */
    public final TabAdapter<ItemType> setPopupMenu(int menuRes, Tab.TabMenuListener<ItemType> listener) {
        mPopupMenu = menuRes;
        mPopupListener = listener;
        return this;
    }

    protected Tab.TabMenuListener<ItemType> getMenuListener() {
        return mPopupListener;
    }

    /**
     * Sets whether or not Tabs in the adapter are clickable, setting it to false will turn Tab's list selectors off
     * and the list's OnItemClickListener will not be called. This <b>will</b> override individual isClickable values
     * set to {@link Tab}s.
     */
    public final TabAdapter<ItemType> setTabsClickable(boolean clickable) {
        mTabsClickable = clickable;
        return this;
    }

    @Override
    public final int getLayout(int index, int type) {
        TabBase Tab = getItem(index);
        if (type == TYPE_NO_CONTENT)
            return mLayoutNoContent;
        else if (type == TYPE_HEADER)
            return R.layout.list_item_header;
        int layout = Tab.getLayout();
        if (layout <= 0) {
            // If no layout was specified for the individual Tab, use the adapter's set layout
            layout = mLayout;
        }
        return layout;
    }

    private void setupHeader(ItemType header, View view) {
        TextView title = (TextView) view.findViewById(android.R.id.title);
        if (title == null)
            throw new RuntimeException("Your header layout must contain a TextView with the ID @android:id/title.");
        TextView subtitle = (TextView) view.findViewById(android.R.id.content);
        if (subtitle == null)
            throw new RuntimeException("Your header layout must contain a TextView with the ID @android:id/content.");
        title.setText(header.getTitle());
        if (header.getContent() != null && !header.getContent().trim().isEmpty()) {
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(header.getContent());
        } else subtitle.setVisibility(View.GONE);
        TextView button = (TextView) view.findViewById(android.R.id.button1);
        if (button == null)
            throw new RuntimeException("The header layout must contain a TextView with the ID @android:id/button1.");
        if (header.getActionCallback() != null) {
            button.setVisibility(View.VISIBLE);
            button.setBackgroundColor(mAccentColor);
            String titleTxt = header.getActionTitle();
            if (header.getActionTitle() == null || header.getActionTitle().trim().isEmpty())
                titleTxt = getContext().getString(R.string.see_more);
            button.setText(titleTxt);
        } else button.setVisibility(View.GONE);
    }

    private void invalidatePadding(int index, View view) {
        int top = index == 0 ? R.dimen.tab_outer_padding_firstlast : R.dimen.tab_outer_padding_top;
        int bottom = index == (getCount() - 1) ? R.dimen.tab_outer_padding_firstlast : R.dimen.tab_outer_padding_top;
        view.setPadding(view.getPaddingLeft(),
                getContext().getResources().getDimensionPixelSize(top),
                view.getPaddingRight(),
                getContext().getResources().getDimensionPixelSize(bottom));
    }

    @Override
    public View onViewCreated(int index, View recycled, ItemType item) {
        if (item.isHeader()) {
            setupHeader(item, recycled);
            return recycled;
        }

        TextView title = (TextView) recycled.findViewById(android.R.id.title);
        if (title != null) onProcessTitle(title, item, mAccentColor);
        TextView content = (TextView) recycled.findViewById(android.R.id.content);
        if (content != null) onProcessContent(content, item);
        ImageView icon = (ImageView) recycled.findViewById(android.R.id.icon);
        if (icon != null) {
            if (onProcessThumbnail(icon, item)) {
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }
        }
        View menu = recycled.findViewById(android.R.id.button1);
        if (menu != null) {
            if (onProcessMenu(menu, item)) {
                menu.setVisibility(View.VISIBLE);
            } else {
                menu.setOnClickListener(null);
                menu.setVisibility(View.INVISIBLE);
            }
        }
        invalidatePadding(index, recycled);
        return recycled;
    }

    @Override
    public Object getItemId(ItemType item) {
        return item.getSilkId();
    }

    @Override
    public final int getViewTypeCount() {
        return mViewTypes.size() + DEFAULT_TYPE_COUNT;
    }

    /**
     * Registers a custom layout in the adapter, that isn't one of the default layouts, and that was passed in the adapter's constructor.
     * <p/>
     * This must be used if you override getLayout() and specify custom layouts for certain list items.
     */
    public final TabAdapter<ItemType> registerLayout(int layoutRes) {
        if (layoutRes == mLayout || layoutRes == mLayoutNoContent || layoutRes == mLayoutHeader) return this;
        mViewTypes.put(layoutRes, mViewTypes.size() + DEFAULT_TYPE_COUNT);
        return this;
    }

    @Override
    public final int getItemViewType(int position) {
        TabBase item = getItem(position);
        if (item.getLayout() > 0) {
            if (mViewTypes.containsKey(item.getLayout()))
                return mViewTypes.get(item.getLayout());
            else if (mLayout == item.getLayout())
                return TYPE_REGULAR;
            else if (mLayoutNoContent == item.getLayout())
                return TYPE_NO_CONTENT;
            else if (mLayoutHeader == item.getLayout())
                return TYPE_HEADER;
            String name = getContext().getResources().getResourceName(item.getLayout());
            throw new RuntimeException("The layout " + name + " is not registered.");
        } else {
            if (item.isHeader())
                return TYPE_HEADER;
            else if ((item.getContent() == null || item.getContent().trim().isEmpty()))
                return TYPE_NO_CONTENT;
            else return TYPE_REGULAR;
        }
    }

    protected boolean onProcessTitle(TextView title, ItemType Tab, int accentColor) {
        if (title == null) return false;
        title.setText(Tab.getTitle());
        title.setTextColor(accentColor);
        return true;
    }

    protected boolean onProcessThumbnail(ImageView icon, ItemType Tab) {
        if (icon == null) return false;
        if (Tab.getThumbnail() == null) return false;
        icon.setImageDrawable(Tab.getThumbnail());
        return true;
    }

    protected boolean onProcessContent(TextView content, ItemType Tab) {
        content.setText(Tab.getContent());
        return false;
    }

    protected boolean onProcessMenu(final View view, final ItemType Tab) {
        if (Tab.getPopupMenu() < 0) {
            // Menu for this Tab is disabled
            return false;
        }
        int menuRes = mPopupMenu;
        if (Tab.getPopupMenu() != 0) menuRes = Tab.getPopupMenu();
        if (menuRes < 0) {
            // No menu for the adapter or the Tab
            return false;
        }
        setupTouchDelegate(getContext(), view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int menuRes = mPopupMenu;
                if (Tab.getPopupMenu() != 0) menuRes = Tab.getPopupMenu();
                // Force the holo light theme on every Tab's popup menu
                Context themedContext = getContext();
                themedContext.setTheme(POPUP_MENU_THEME);
                PopupMenu popup = new PopupMenu(themedContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(menuRes, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (Tab.getPopupMenu() > 0 && Tab.getPopupListener() != null) {
                            // This individual Tab has it unique menu
                            Tab.getPopupListener().onMenuItemClick(Tab, item);
                        } else if (mPopupListener != null) {
                            // The Tab does not have a unique menu, use the adapter's default
                            mPopupListener.onMenuItemClick(Tab, item);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        return true;
    }
}