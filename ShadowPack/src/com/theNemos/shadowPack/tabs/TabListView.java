package com.theNemos.shadowPack.tabs;

import com.theNemos.shadowPack.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * A {@link ListView} that automates many Tab related things, such as disabling the background list selector,
 * removing the list divider, and calling a {@link TabHeader}'s ActionListener when tapped by the user.
 *
 * @author Aidan Follestad (afollestad)
 */
public class TabListView extends ListView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private TabClickListener mTabClickListener;
    private TabLongClickListener mTabLongClickListener;

    public TabListView(Context context) {
        super(context);
        init(null);
    }

    public TabListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setDivider(null);
        setDividerHeight(0);
        setSelector(R.color.dark);
        super.setOnItemClickListener(this);
        super.setOnItemLongClickListener(this);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.background});
            if (a.length() > 0) {
                int color = a.getColor(0, 0);
                if (color == 0) setDefaultBackground();
                else {
                    setBackgroundColor(color);
                    setCacheColorHint(color);
                }
            } else setDefaultBackground();
        } else setDefaultBackground();
    }

    private void setDefaultBackground() {
        int gray = getResources().getColor(R.color.light);
        setBackgroundColor(gray);
        setCacheColorHint(gray);
    }

    /**
     * @deprecated Use {@link #setAdapter(TabAdapter)} instead.
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof TabAdapter) {
            setAdapter((TabAdapter) adapter);
            return;
        } else if (adapter instanceof TabCursorAdapter) {
            setAdapter((TabCursorAdapter) adapter);
            return;
        }
        throw new RuntimeException("The TabListView only accepts TabAdapters.");
    }

    /**
     * Sets the list's adapter, enforces the use of only a TabAdapter, not any other type of adapter
     */
    public void setAdapter(TabAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Sets the list's adapter, enforces the use of only a TabCursorAdapter, not any other type of adapter
     */
    public void setAdapter(TabCursorAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * @deprecated Use {@link #setOnTabClickListener(com.afollestad.Tabsui.TabListView.TabClickListener)} instead.
     */
    @Override
    public final void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * @deprecated Use {@link #setOnTabLongClickListener(com.afollestad.Tabsui.TabListView.TabLongClickListener)} instead.
     */
    @Override
    public final void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    /**
     * Sets a click listener for Tabs (doesn't include Tab headers).
     */
    public void setOnTabClickListener(TabClickListener listener) {
        mTabClickListener = listener;
    }

    /**
     * Sets a long click listener for Tabs (doesn't include Tab headers).
     */
    public void setOnTabLongClickListener(TabLongClickListener listener) {
        mTabLongClickListener = listener;
    }

    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TabBase item = (TabBase) getAdapter().getItem(position);
        if (item.isHeader()) {
            TabHeader header = (TabHeader) item;
            if (header.getActionCallback() != null)
                header.getActionCallback().onClick(header);
            else if (mItemClickListener != null) mItemClickListener.onItemClick(parent, view, position, id);
        } else {
            if (mItemClickListener != null) mItemClickListener.onItemClick(parent, view, position, id);
            if (mTabClickListener != null) mTabClickListener.onTabClick(position, item, view);
            if (mTabLongClickListener != null) mTabLongClickListener.onTabLongClick(position, item, view);
        }
    }

    @Override
    public final boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TabBase item = (TabBase) getAdapter().getItem(position);
        if (mTabLongClickListener != null)
            return mTabLongClickListener.onTabLongClick(position, item, view);
        return mItemLongClickListener != null && mItemLongClickListener.onItemLongClick(parent, view, position, id);
    }

    public interface TabClickListener {
        public void onTabClick(int index, TabBase Tab, View view);
    }

    public interface TabLongClickListener {
        public boolean onTabLongClick(int index, TabBase Tab, View view);
    }
}
