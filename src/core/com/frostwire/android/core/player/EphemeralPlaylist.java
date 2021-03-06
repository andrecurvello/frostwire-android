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

package com.frostwire.android.core.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.frostwire.android.core.FileDescriptor;

/**
 * Playlist based on a given list of file descriptors.
 * 
 * @author gubatron
 * @author aldenml
 *
 */
public final class EphemeralPlaylist implements Playlist {

    private final List<PlaylistItem> items;

    private int currentIndex;

    public EphemeralPlaylist(List<FileDescriptor> fds) {
        this.items = new ArrayList<PlaylistItem>();

        for (FileDescriptor fd : fds) {
            this.items.add(new PlaylistItem(fd));
        }
        
        Collections.sort(this.items, new Comparator<PlaylistItem>() {

            @Override
            public int compare(PlaylistItem a, PlaylistItem b) {
                if (a.getFD().dateAdded == b.getFD().dateAdded) {
                    return 0;
                }
                return (a.getFD().dateAdded > b.getFD().dateAdded) ? -1 : 1;
            }
        });

        this.currentIndex = -1;
    }

    @Override
    public PlaylistItem getPreviousItem() {
        if (items.size() == 0) {
            return null;
        }

        if (currentIndex == 0 || currentIndex == -1) {
            currentIndex = items.size() - 1;
        } else {
            currentIndex--;
        }

        PlaylistItem item = items.get(currentIndex);

        return item;
    }

    @Override
    public PlaylistItem getNextItem() {
        if (items.size() == 0) {
            return null;
        }

        currentIndex = ((currentIndex + 1) % items.size());

        PlaylistItem item = items.get(currentIndex);

        return item;
    }

    public void setNextItem(PlaylistItem playlistItem) {
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).equals(playlistItem)) {
                currentIndex = index - 1;
                break;
            }
        }
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String s) {   
    }

    @Override
    public void removeItem(PlaylistItem item) {
        if (items.size() > 0) {
            items.remove(item);
            currentIndex--;
            
            if (currentIndex < 0) {
                currentIndex = 0;
            }
        }
    }
}
