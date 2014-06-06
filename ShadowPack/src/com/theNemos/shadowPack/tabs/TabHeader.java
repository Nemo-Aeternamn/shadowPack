package com.theNemos.shadowPack.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Represents a header displayed at the top of a set of Tabs.
 *
 * @author Aidan Follestad (afollestad)
 */
public class TabHeader extends Tab {

    private String mActionTitle;
    private ActionListener mCallback;

    public TabHeader(String title) {
        super(title, null, true);
    }

    public TabHeader(String title, String subtitle) {
        super(title, subtitle, true);
    }

    public TabHeader(Context context, int titleRes) {
        this(context.getString(titleRes));
    }

    public TabHeader(Context context, String title, int subtitleRes) {
        this(title, context.getString(subtitleRes));
    }

    public TabHeader(Context context, int titleRes, String subtitle) {
        this(context.getString(titleRes), subtitle);
    }

    public TabHeader(Context context, int titleRes, int subtitleRes) {
        this(context.getString(titleRes), context.getString(subtitleRes));
    }

    @Override
    public ActionListener getActionCallback() {
        return mCallback;
    }

    /**
     * Sets the Tabs action and uses the default action text ("See More").
     *
     * @param callback The action listener invoked when the header is tapped.
     */
    public TabHeader setAction(ActionListener callback) {
        mActionTitle = null;
        mCallback = callback;
        return this;
    }

    /**
     * Sets the Tabs action and action title.
     *
     * @param title    The text to be displayed on the action button (defaults to "See More").
     * @param callback The action listener invoked when the header is tapped.
     */
    public TabHeader setAction(String title, ActionListener callback) {
        mActionTitle = title;
        mCallback = callback;
        return this;
    }

    /**
     * Sets the Tabs action and action title.
     *
     * @param context  The context used to resolve the string resource ID.
     * @param titleRes The string resource to be displayed on the action button (defaults to "See More").
     * @param callback The action listener invoked when the header is tapped.
     */
    public TabHeader setAction(Context context, int titleRes, ActionListener callback) {
        setAction(context.getString(titleRes), callback);
        return this;
    }

    @Override
    public String getActionTitle() {
        return mActionTitle;
    }

    /**
     * @deprecated This method cannot be called for headers, it only works with regular {@link Tab} objects.
     */
    @Override
    public Tab setThumbnail(Drawable drawable) {
        throw new RuntimeException("Tab headers do not support setThumbnail().");
    }

    /**
     * @deprecated This method cannot be called for headers, it only works with regular {@link Tab} objects.
     */
    @Override
    public Tab setLayout(int layoutRes) {
        throw new RuntimeException("Tab headers do not support setLayout().");
    }

    public interface ActionListener {
        public void onClick(TabHeader header);
    }
}