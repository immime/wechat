package com.immine.wmall.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/weixin")
public class WeixinCtrl {

	private static final Logger logger = LoggerFactory
			.getLogger(WeixinCtrl.class);

	private WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
	private WxMpService wxMpService;
	private WxMpMessageRouter wxMpMessageRouter;
	private final static String [] helpMsg = {"help", "帮助"};
	private final static String noticeMsg = "发送%s获取帮助";
	private final static String defaultMsg = "回复以下数字获取服务：\n"
											+ "1.查询天气\n" 
											+ "2.听歌";
	

	@ModelAttribute
	public void before() {
		config.setAppId("wxe6626fc25736c77e"); // 设置微信公众号的appid
		config.setSecret("c5ea13a94c08a1ed07fc4eaeb6ca913b"); // 设置微信公众号的app
																// corpSecret
		config.setToken("myqiqi"); // 设置微信公众号的token
		config.setAesKey("DvILSt6R9ciXQCP9rA7HusoEzEhPTKPUXKtYjCIiZxn"); // 设置微信公众号的EncodingAESKey

		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);

		WxMpMessageHandler handler = new WxMpMessageHandler() {
			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
					Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager sessionManager) throws WxErrorException {
				WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
						.content(defaultMsg).fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName()).build();
				return m;
			}
		};

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter
			.rule()
				.async(false)
				.content(helpMsg[0]) 
				.handler(handler)
				.end()	
			// 拦截内容为“哈哈”的消息
			.rule()
				.async(false)
				.content(helpMsg[1])
				.handler(handler)
				.end();

	}

	@RequestMapping(produces="application/xml;charset=UTF-8")
	public String msg(HttpServletRequest request) throws IOException {
		String replyMsg = "";
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			logger.debug("验证signature失败，timestamp：{0}, nonce:{1}, signature{2}", timestamp, nonce, signature);
			return "非法请求";
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			return echostr;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			outMessage = putDefaultMsgIfNull(inMessage, outMessage);
			replyMsg = outMessage.toXml();
		} else if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), config, timestamp, nonce, msgSignature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			outMessage = putDefaultMsgIfNull(inMessage, outMessage);
			replyMsg = outMessage.toEncryptedXml(config);
		} else {
			replyMsg = "不可识别的加密类型";
		}
		logger.debug("回复的消息：{}", replyMsg);
		return replyMsg;
	}

	/**
	 * 如果没有匹配到自动回复关键词，则发送默认回复内容
	 * @param inMessage
	 * @param outMessage
	 * @return
	 */
	private WxMpXmlOutMessage putDefaultMsgIfNull(WxMpXmlMessage inMessage,
			WxMpXmlOutMessage outMessage) {
		if(outMessage == null) {
			String msg = String.format(noticeMsg, Arrays.toString(helpMsg));
			outMessage = WxMpXmlOutMessage.TEXT()
					.content(msg).fromUser(inMessage.getToUserName())
					.toUser(inMessage.getFromUserName()).build();
		}
		return outMessage;
	}

}
