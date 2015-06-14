package com.immine.wmall.handle;

import com.immine.wechat.handle.EventHandle;
import com.immine.wechat.message.BaseMsg;
import com.immine.wechat.message.req.BaseEvent;

public class MyEventHandle implements EventHandle<BaseEvent> {

	@Override
	public BaseMsg handle(BaseEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean beforeHandle(BaseEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
