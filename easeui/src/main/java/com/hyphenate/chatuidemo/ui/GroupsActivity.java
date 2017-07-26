/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatuidemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.adapter.GroupAdapter;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupsActivity extends BaseActivity {
    public static final String TAG = "GroupsActivity";
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupsActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int pagesize = 20;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
//                    Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_fragment_groups);
        instance = this;
        goToJoinPublicGroup();//加入公共群
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupListView = (ListView) findViewById(R.id.list);
        //show group list
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        //pull down to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", groupAdapter.getItem(position).getGroupId());
                startActivityForResult(intent, 0);
            }

        });
        groupListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

    }

    /**
     * 获取公共群集合 判断是否已经加入了该群
     */
    private void goToJoinPublicGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pagesize, null);
                    final List<EMGroupInfo> returnGroups = result.getData();
                    String gradeName = SharedPrefUtils.getString(GroupsActivity.this, SharedPrefUtils.APP_USER_GRADENAME);
                    for (int i = 0; i < returnGroups.size(); i++) {
                        final EMGroupInfo emGroupInfo = returnGroups.get(i);
                        EMGroupInfo em = emGroupInfo;
                        String groupId = emGroupInfo.getGroupId();
                        String groupName = emGroupInfo.getGroupName();

                        final EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                        if (group != null&& gradeName.equals(groupName)) {
                            EMClient.getInstance().groupManager().applyJoinToGroup(groupId, "请求加入");
                        } else if(gradeName.equals(groupName)){
                            EMClient.getInstance().groupManager().joinGroup(groupId);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        try {
                                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                                            handler.sendEmptyMessage(0);
                                        } catch (HyphenateException e) {
                                            e.printStackTrace();
                                            handler.sendEmptyMessage(1);
                                        }
                                        if (group.isMembersOnly()) {
                                            Toast.makeText(GroupsActivity.this, getResources().getString(R.string.send_the_request_is), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },1000);
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void back(View view) {
        finish();
    }
}
