package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.account.common.AccountErrorCode;
import cn.itcast.wanxinp2p.common.domain.BusinessException;
import cn.itcast.wanxinp2p.common.domain.CommonErrorCode;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    public RestResponse getSmsCode(String mobile){
        if (smsEnable){
            return OkHttpUtil.post(smsUrl + "generate?effectiveTime=300&name=sms", "{\"mobile\":\"" + mobile + "\"}");
        }
        return RestResponse.success();
    }

    public void verifySmsCode(String key, String code){
        if (smsEnable){
            StringBuilder sb = new StringBuilder();
            sb.append("verify?name=sms").append("&verificationKey=").append(key).append("&verificationCode=").append(code);
            String params = sb.toString();
            RestResponse result = OkHttpUtil.post(smsUrl + params, "");
            if(result.getCode() != CommonErrorCode.SUCCESS.getCode() || result.getResult().toString().equalsIgnoreCase("false")){
                throw new BusinessException(AccountErrorCode.E_140152);
            }
        }
    }
}
