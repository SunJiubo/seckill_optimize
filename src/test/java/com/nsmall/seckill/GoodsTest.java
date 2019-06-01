package com.nsmall.seckill;

import com.nsmall.seckill.service.GoodsService;
import com.nsmall.seckill.vo.GoodsVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @ClassName GoodsTest
 * @Description TODO
 * @Author Sky
 * @Date 2019/5/30 10:03
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class GoodsTest {

    @Autowired
    GoodsService goodsService;

    @Test
    public void test1(){
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        for (GoodsVo goodsVo :
                goodsVos) {
            System.out.println(goodsVo);
        }
        System.out.println(goodsService.getGoodsVoByGoodsId(1));
    }
}
