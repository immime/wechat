package com.immine.wmall.handle;

import com.immine.wechat.handle.MessageHandle;
import com.immine.wechat.message.BaseMsg;
import com.immine.wechat.message.req.BaseReqMsg;

public class MyMessageHandle implements MessageHandle<BaseReqMsg> {

	@Override
	public BaseMsg handle(BaseReqMsg message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean beforeHandle(BaseReqMsg message) {
		// TODO Auto-generated method stub
		return false;
	}

}
