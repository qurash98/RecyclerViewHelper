/*
 * Copyright (C) 2016.  BoBoMEe(wbwjx115@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.bobomee.android.recyclerviewhelperdemo.pager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.bobomee.android.common.widget.BackHandlerHelper;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.bean.PagerBean;

/**
 * Created on 2016/12/16.下午2:33.
 *
 * @author bobomee.
 */

public class PagerActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.root_view);
    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    if (null != pager) {
      pager.setAdapter(new PageAdapter(getSupportFragmentManager(), PagerBean.provide()));
    }
  }

  @Override public void onBackPressed() {

    if (BackHandlerHelper.handleBackPress(this)) {
      return;
    }
    super.onBackPressed();
  }
}