package com.immine.wechat.api.response;

import java.util.List;

import com.immine.wechat.api.entity.UpstreamMsgMonth;

/**
 * @author peiyu
 */
public class GetUpstreamMsgMonthResponse extends BaseResponse {

    private List<UpstreamMsgMonth> list;

    public List<UpstreamMsgMonth> getList() {
        return list;
    }

    public void setList(List<UpstreamMsgMonth> list) {
        this.list = list;
    }
}