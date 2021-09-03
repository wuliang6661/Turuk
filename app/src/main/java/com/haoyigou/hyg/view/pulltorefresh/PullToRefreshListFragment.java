/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.haoyigou.hyg.view.pulltorefresh;

import android.os.Bundle;
import android.view.LayoutInflater;



public class PullToRefreshListFragment extends PullToRefreshBaseListFragment<com.haoyigou.hyg.view.pulltorefresh.PullToRefreshListView> {

    protected com.haoyigou.hyg.view.pulltorefresh.PullToRefreshListView onCreatePullToRefreshListView(LayoutInflater inflater, Bundle savedInstanceState) {
        return new com.haoyigou.hyg.view.pulltorefresh.PullToRefreshListView(getActivity());
    }

}