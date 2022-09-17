package com.gy.reggie.Utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaModel;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param) throws Exception {
		Config config = new Config()
				// 您的 AccessKey ID
				.setAccessKeyId("LTAI5tC9ybtpHMz92eyxccEu")
				// 您的 AccessKey Secret
				.setAccessKeySecret("MIl28p123iGASdz2DpgylXPrBiIYkv");
		// 访问的域名
		config.endpoint = "dysmsapi.aliyuncs.com";
		Client client = new Client(config);
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
				.setSignName(signName)
				.setTemplateCode(templateCode)
				.setPhoneNumbers(phoneNumbers)
				.setTemplateParam("{\"code\":\""+param+"\"}");
		RuntimeOptions runtime = new RuntimeOptions();
		SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
		com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(resp)));

	}
}
