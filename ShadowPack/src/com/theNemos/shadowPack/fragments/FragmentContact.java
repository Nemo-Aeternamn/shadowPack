/*
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.theNemos.shadowPack.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.theNemos.shadowPack.tabs.*;
import com.theNemos.shadowPack.tabs.TabListView.TabClickListener;
/*
import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardListView.CardClickListener;
import com.theNemos.nemosPack.R;
import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardListView.CardClickListener;
import com.theNemos.nemosPack.R;
*/
import com.theNemos.shadowPack.*;


public class FragmentContact extends Fragment implements
	Tab.TabMenuListener<Tab> {
	
	private TabListView list;
	private void actGPlus() {
		}{
		Uri gplusuriString = Uri.parse(getString(R.string.gplus_link));
		Intent gplusIntent = new Intent("android.intent.action.VIEW",
				gplusuriString);{
		try {
			startActivity(gplusIntent);
		} catch (ActivityNotFoundException e2) {
			e2.printStackTrace();
		}}
	}

	private void actTwitter() {
		Uri twitteruriString = Uri.parse(getString(R.string.twitter_link));
		Intent twitterIntent = new Intent("android.intent.action.VIEW",
				twitteruriString);
		try {
			startActivity(twitterIntent);
		} catch (ActivityNotFoundException e2) {
			e2.printStackTrace();
		}
	}


	private void actFacebook() {
		Uri facebookuriString = Uri.parse(getString(R.string.facebook_link));
		Intent facebookIntent = new Intent("android.intent.action.VIEW",
				facebookuriString);
		try {
			startActivity(facebookIntent);
		} catch (ActivityNotFoundException e2) {
			e2.printStackTrace();
		}
	}
	
	private void actEmail() {
		Uri emailuriString = Uri.parse(getString(R.string.email_link));
		Intent emailIntent = new Intent("android.intent.action.VIEW",
				emailuriString);
		try {
			startActivity(emailIntent);
		} catch (ActivityNotFoundException e2) {
			e2.printStackTrace();
		}
	}

	private void actWeb() {
		Uri weburiString = Uri.parse(getString(R.string.web_link));
		Intent webIntent = new Intent("android.intent.action.VIEW",
				weburiString);
		try {
			startActivity(webIntent);
		} catch (ActivityNotFoundException e2) {
			e2.printStackTrace();
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_contact, container,
				false);
 
		list = (TabListView) view.findViewById(R.id.ListView);
		list.setOnTabClickListener(new TabClickListener() {
			public void onCardClick(AdapterView<?> adapter, View view,
					int position, long arg) {
 
			}
			//The below section is what tells the Tab to perform the above action when clicked, make sure to remove the if of any Tab you are not using
			public <TabBase> void onTabClick(int position, TabBase Tab, View view) {
				String str = (String) ((MenuItem) Tab).getTitle();
				if (str.equals(getString(R.string.facebook))) {
					actFacebook();
				}
				if (str.equals(getString(R.string.gplus))) {
					actGPlus();
				}
				if (str.equals(getString(R.string.email))) {
					actEmail();
				}
				if (str.equals(getString(R.string.twitter))) {
					actTwitter();
				}
				if (str.equals(getString(R.string.web))) {
					actWeb();
				}
			}
			@Override
			public void onTabClick(int index,
					com.theNemos.shadowPack.tabs.TabBase Tab, View view) {
				// TODO Auto-generated method stub
				
			}
	 });
		return view;
	}
	//The below are the Tabs to be displayed, if there is a social network you are not going to support, remove the Tab as well as the act and OnTabClick if statement
	@Override
	public void onStart() {
		super.onStart();
		TabAdapter<Tab> TabsAdapter = new TabAdapter<Tab>(getActivity())
				.setAccentColorRes(R.color.text)
				.setPopupMenu(R.menu.contact_popup, this);
		TabsAdapter.add(new TabHeader(getActivity(), R.string.socialheader)
				);
		TabsAdapter.add(new Tab(getString(R.string.gplus),
				getString(R.string.gplus_extra))
				.setThumbnail(getActivity(), R.drawable.apps_googleplus) // sets a thumbnail image from drawable resources
				.setPopupMenu(-1, null) // -1 disables the popup menu for this individual Tab
				); 
		TabsAdapter.add(new Tab(getString(R.string.twitter),
				getString(R.string.twitter_extra))
				.setThumbnail(getActivity(), R.drawable.apps_twitter) // sets a thumbnail image from drawable resources
				.setPopupMenu(-1, null) // -1 disables the popup menu for this individual Tab
				);
		TabsAdapter.add(new Tab(getString(R.string.facebook),
				getString(R.string.facebook_extra))
				.setThumbnail(getActivity(), R.drawable.apps_facebook) // sets a thumbnail image from drawable resources
				.setPopupMenu(-1, null) // -1 disables the popup menu for this individual Tab
				);
		TabsAdapter.add(new TabHeader(getActivity(), R.string.emailheader));
		TabsAdapter.add(new Tab(getString(R.string.email),
				getString(R.string.email_extra))
				.setThumbnail(getActivity(), R.drawable.apps_googlemail) // sets a thumbnail image from drawable resources
				.setPopupMenu(-1, null) // -1 disables the popup menu for this individual Tab
				); 
		TabsAdapter.add(new TabHeader(getActivity(), R.string.webheader)
				);
		TabsAdapter.add(new Tab(getString(R.string.web),
				getString(R.string.web_extra))
				.setThumbnail(getActivity(), R.drawable.system_browser) // sets a thumbnail image from drawable resources
				.setPopupMenu(-1, null) // -1 disables the popup menu for this individual Tab
				); 

		list.setAdapter(TabsAdapter);
	}

	@Override
	public void onMenuItemClick(Tab Tab, MenuItem item) {
		Toast.makeText(getActivity(), Tab.getTitle() + ": " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
	}

}