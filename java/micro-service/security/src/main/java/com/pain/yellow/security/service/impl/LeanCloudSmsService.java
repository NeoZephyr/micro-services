package com.pain.yellow.security.service.impl;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import com.google.common.collect.Maps;
import com.pain.yellow.security.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "auth.sms-provider", name = "name", havingValue = "lean-cloud")
@Service
public class LeanCloudSmsService implements SmsService {

    @Override
    public void send(String mobile, String msg) {
        AVSMSOption option = new AVSMSOption();
        option.setTtl(10);
        option.setApplicationName("Spring Security");
        option.setOperation("两步验证");
        option.setTemplateName("登录验证");
        option.setSignatureName("Security");
        option.setType(AVSMS.TYPE.TEXT_SMS);

        Map<String, String> envMap = new HashMap<>();
        envMap.put("smsCode", msg);
        option.setEnvMap(Collections.unmodifiableMap(envMap));
        AVSMS.requestSMSCodeInBackground(mobile, option)
                .take(1)
                .subscribe(
                        (res) -> log.info("短信发送成功 {}", res),
                        (err) -> log.error("发送短信时产生服务端异常 {}", err.getLocalizedMessage())
                );
    }
}
