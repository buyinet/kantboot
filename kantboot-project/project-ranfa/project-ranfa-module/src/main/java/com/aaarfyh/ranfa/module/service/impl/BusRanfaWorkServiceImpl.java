package com.aaarfyh.ranfa.module.service.impl;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.aaarfyh.ranfa.module.util.RanfaWorkChangeUtil;
import com.alibaba.fastjson.JSON;
import com.kantboot.pay.util.common.util.GoodsParentParam;
import com.kantboot.pay.util.common.util.PayParam;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.util.common.util.RedisUtil;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import com.kantboot.util.core.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BusRanfaWorkServiceImpl extends BaseServiceImpl<BusRanfaWork, Long> implements IBusRanfaWorkService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    ISysUserService userService;

    @Resource
    BusRanfaWorkRepository repository;

    @Override
    public BusRanfaWork change() {


        SysUser userInfo = userService.getUserInfo();
        Long userId = userInfo.getId();

        //redis中保存的key
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        String redisData = redisUtil.get(redisKey);


        long count = repository.count();
        if (redisData == null) {
            RanfaWorkChangeUtil ranfaWorkChangeUtil = new RanfaWorkChangeUtil();

            CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
            pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
            pageParam.setSortField("gmtCreate");
            pageParam.setSortType("DESC");
            pageParam.setPageNumber(1);
            pageParam.setPageSize(20);
            Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
            List<BusRanfaWork> common = commonByPage.getContent();
            List<Long> blockIds = new ArrayList<Long>();
            for (BusRanfaWork cesRanfaWork : common) {
                blockIds.add(cesRanfaWork.getId());
            }
            Collections.shuffle(blockIds);
            ranfaWorkChangeUtil.setBlockIds(blockIds);
            Integer index = 0;
            ranfaWorkChangeUtil.setPage(1);
            ranfaWorkChangeUtil.setIndex(index);
            ranfaWorkChangeUtil.setId(blockIds.get(index));

            /**
             * 用户切换的区块视频id数组
             */
            redisUtil.setEx("user_id:" + userId + ":ranfa_work_change_util", JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

            return repository.findById(ranfaWorkChangeUtil.getId()).get();

        }

        if (redisData != null) {
            RanfaWorkChangeUtil ranfaWorkChangeUtil = JSON.parseObject(redisData, RanfaWorkChangeUtil.class);
            Integer index = ranfaWorkChangeUtil.getIndex()+1;
            if(index>ranfaWorkChangeUtil.getBlockIds().size()-1){
                CommonEntityPageParam<BusRanfaWork> pageParam1 = new CommonEntityPageParam<BusRanfaWork>();
                pageParam1.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
                pageParam1.setSortField("gmtCreate");
                pageParam1.setSortType("DESC");
                Integer page1=ranfaWorkChangeUtil.getPage()+1;
                pageParam1.setPageNumber(1);
                pageParam1.setPageSize(20);
                Page<BusRanfaWork> commonByPage1 = findCommonByPage(pageParam1);

                CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
                pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
                pageParam.setSortField("gmtCreate");
                pageParam.setSortType("DESC");
                Integer page=ranfaWorkChangeUtil.getPage()+1;
                System.out.println(commonByPage1.getTotalPages()+"====");
                if(page>commonByPage1.getTotalPages()){
                    page=1;
                }
                pageParam.setPageNumber(1*page);
                pageParam.setPageSize(20);
                Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
                List<BusRanfaWork> common = commonByPage.getContent();
                List<Long> blockIds = new ArrayList<Long>();
                for (BusRanfaWork cesRanfaWork : common) {
                    blockIds.add(cesRanfaWork.getId());
                }
                Collections.shuffle(blockIds);
                ranfaWorkChangeUtil.setPage(page);
                ranfaWorkChangeUtil.setBlockIds(blockIds);
                index=0;
            }
            ranfaWorkChangeUtil.setIndex(index);
            ranfaWorkChangeUtil.setId(ranfaWorkChangeUtil.getBlockIds().get(index));
            /**
             * 用户切换的区块视频id数组
             */
            redisUtil.setEx("user_id:" + userId + ":ranfa_work_change_util", JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

            return repository.findById(ranfaWorkChangeUtil.getId()).get();
        }

        return null;
    }

    @Override
    public PayResult payBefore(GoodsParentParam param) {
        //此商品只能一个个单独购买，所以只会有一个下标
        List<PayParam> payParams = param.getPayParams();
        PayParam payPramItem = payParams.get(0);
        payPramItem.setTitle("染发一号-教学视频");
//        param.setPayParams(payParams);
        System.out.println("param.getPayParams() = " + JSON.toJSONString(param.getPayParams()));
        BusRanfaWork cesRanfaWork = repository.findById(Long.valueOf(payPramItem.getGoodsId())).get();
        String body="染发一号-教学视频";

        if(cesRanfaWork.getTitle()!=null){
            body+="-"+cesRanfaWork.getTitle();
        }

        PayResult result = new PayResult();
        result.setState(2000);
        System.out.println("cesRanfaWork.getPrice() = " + cesRanfaWork.getPrice());
        result.setTotalFee(cesRanfaWork.getPrice());
        result.setPayParams(payParams);
        result.setBody(body);
        return result;
    }
}
