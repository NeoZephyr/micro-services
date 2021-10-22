package com.pain.blue.shopping.service;

import com.pain.blue.mapping.CopyUtils;
import com.pain.blue.shopping.domain.dto.ActivityDTO;
import com.pain.blue.shopping.domain.dto.ActivityDestDTO;
import com.pain.blue.shopping.domain.pojo.ActivityInfo;
import com.pain.blue.shopping.domain.pojo.ActivityInfoExample;
import com.pain.blue.shopping.mapper.ActivityInfoMapper;
import com.pain.blue.shopping.mapper.ProductInfoMapper;
import com.pain.blue.shopping.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityInfoMapper activityInfoMapper;
    private final ProductInfoMapper productInfoMapper;
    private final RedisUtils redisUtils;

    public void save(ActivityDTO activityDTO) {
        ActivityInfo existActivity = activityInfoMapper.selectOpenedByProductId(activityDTO.getProductId());

        if (existActivity != null) {
            throw new RuntimeException("活动已存在");
        }

        ActivityInfo activityInfo = CopyUtils.copy(activityDTO, ActivityInfo.class);
        activityInfo.setStatus((byte) 0);
        activityInfoMapper.insert(activityInfo);
    }

    public ActivityDestDTO description(String productId) {
        ActivityInfo activityInfo = activityInfoMapper.selectOpenedByProductId(productId);
        ActivityDestDTO activityDestDTO = CopyUtils.copy(activityInfo, ActivityDestDTO.class);

        if (activityInfo.getStatus() == 0) {
            activityDestDTO.setStatusText("未开始");
        } else if (activityInfo.getStatus() == 1) {
            activityDestDTO.setStatusText("进行中");
        } else if (activityInfo.getStatus() == 2) {
            activityDestDTO.setStatusText("已结束");
        }

        return activityDestDTO;
    }

    public void start(String productId) {
        ActivityInfoExample example = new ActivityInfoExample();
        ActivityInfoExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        criteria.andStatusEqualTo((byte) 0);
        List<ActivityInfo> activityInfoList = activityInfoMapper.selectByExample(example);
        ActivityInfo activityInfo = activityInfoList.get(0);

        Date now = new Date();

        if (now.before(activityInfo.getDateStart())) {
            throw new RuntimeException("活动尚未开始");
        }

        if (now.after(activityInfo.getDateEnd())) {
            throw new RuntimeException("活动已结束");
        }

        activityInfoMapper.updateStatus(activityInfo.getId(), (byte) 1);
        productInfoMapper.updateTag(productId, (byte) 2);
        redisUtils.set("store_" + productId, String.valueOf(activityInfo.getStockNum()));
    }
}
