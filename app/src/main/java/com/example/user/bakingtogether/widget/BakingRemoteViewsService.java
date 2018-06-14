package com.example.user.bakingtogether.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this, intent);
    }
}
