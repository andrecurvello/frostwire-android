/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011, 2012, FrostWire(TM). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.search.mininova;

import java.util.LinkedList;
import java.util.List;

import com.frostwire.search.PagedWebSearchPerformer;
import com.frostwire.search.SearchResult;
import com.frostwire.util.JsonUtils;
import com.frostwire.websearch.TorrentWebSearchResult;

/**
 * @author gubatron
 * @author aldenml
 *
 */
public class MininovaSearchPerformer extends PagedWebSearchPerformer {

    public MininovaSearchPerformer(String keywords, int timeout) {
        super(keywords, timeout, 1);
    }

    @Override
    protected String getUrl(int page, String encodedKeywords) {
        return "http://www.mininova.org/vuze.php?search=" + encodedKeywords;
    }

    @Override
    protected List<? extends SearchResult> searchPage(String page) {
        List<SearchResult> result = new LinkedList<SearchResult>();

        //fix what seems to be an intentional JSON syntax typo put ther by mininova
        String json = page.replace("\"hash\":", ", \"hash\":");
        MininovaVuzeResponse response = JsonUtils.toObject(json, MininovaVuzeResponse.class);

        for (MininovaVuzeItem item : response.results) {
            if (!isStopped()) {
                TorrentWebSearchResult sr = new MininovaVuzeWebSearchResult(item);
                result.add(sr);
            }
        }

        return result;
    }
}
