package com.theNemos.shadowPack.tabs;

import android.graphics.drawable.Drawable;
import com.theNemos.shadowPack.silk.*;

/**
 * @author Aidan Follestad (afollestad)
 */
public interface TabBase<ItemType> extends SilkComparable<ItemType> {

    public abstract String getTitle();

    public abstract String getContent();

    public abstract boolean isHeader();

    public abstract boolean isClickable();

    public abstract int getPopupMenu();

    public TabHeader.ActionListener getActionCallback();

    public String getActionTitle();

    public abstract Tab.TabMenuListener<ItemType> getPopupListener();

    public abstract Drawable getThumbnail();

    public abstract int getLayout();

    public abstract Object getTag();
}
