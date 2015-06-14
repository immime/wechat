package com.immine.wmall;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.immine.wechat.handle.EventHandle;
import com.immine.wechat.handle.MessageHandle;
import com.immine.wechat.message.BaseMsg;
import com.immine.wechat.message.TextMsg;
import com.immine.wechat.message.req.TextReqMsg;
import com.immine.wechat.servlet.WeixinControllerSupport;
import com.immine.wmall.handle.MyEventHandle;
import com.immine.wmall.handle.MyMessageHandle;

@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinControllerSupport {
	
	private static final Logger LOG = LoggerFactory.getLogger(WeixinController.class);
	private static final String TOKEN = "myqiqi";
	private static final String AES_KEY = "DvILSt6R9ciXQCP9rA7HusoEzEhPTKPUXKtYjCIiZxn";
	private static final String APP_ID = "wxe6626fc25736c77e";

	// 使用安全模式时设置：密钥
	@Override
	protected String getAESKey() {
		return AES_KEY;
	}

	// 使用安全模式时设置：APPID
	@Override
	protected String getAppId() {
		return APP_ID;
	}

	// 设置TOKEN，用于绑定微信服务器
	@Override
	protected String getToken() {
		return TOKEN;
	}

	// 重写父类方法，处理对应的微信消息
	@Override
	protected BaseMsg handleTextMsg(TextReqMsg msg) {
		String content = msg.getContent();
		LOG.debug("用户发送到服务器的内容:{}", content);

		return new TextMsg(content);
	}

	/*
	 * 1.1版本新增，重写父类方法，加入自定义微信消息处理器不是必须的，<br/>
	 * 上面的方法是统一处理所有的文本消息，如果业务觉复杂，上面的会显得比较乱
	 * 这个机制就是为了应对这种情况，每个MessageHandle就是一个业务，只处理指定的那部分消息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected List<MessageHandle> initMessageHandles() {
		List<MessageHandle> handles = new ArrayList<MessageHandle>();
		handles.add(new MyMessageHandle());
		return handles;
	}

	// 1.1版本新增，重写父类方法，加入自定义微信事件处理器，同上
	@SuppressWarnings("rawtypes")
	@Override
	protected List<EventHandle> initEventHandles() {
		List<EventHandle> handles = new ArrayList<EventHandle>();
		handles.add(new MyEventHandle());
		return handles;
	}

}
