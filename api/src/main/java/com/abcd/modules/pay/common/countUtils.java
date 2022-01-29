package com.abcd.modules.pay.common;
//package com.ryhy.modules.pay.common;
//
//
//import cn.hutool.core.date.DateUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.ryhy.modules.factor.entity.FactorInfo;
//import com.ryhy.modules.factor.entity.FactorType;
//import com.ryhy.modules.factor.service.IFactorInfoService;
//import com.ryhy.modules.factor.service.IFactorTypeService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.*;
//
//@Slf4j
//public class CountUtils {
//
//    @Autowired
//    private  IFactorTypeService iFactorTypeService;
//    @Autowired
//    private  IFactorInfoService iFactorInfoService;
//    @Autowired
//    private  RedisTemplate redisTemplate;
//    public  Factor count(){
////        TestFactor testFactor1 = new TestFactor();
////        testFactor1.setType(1);
////        testFactor1.setValue("50");
////        testFactor1.setWeight(1);
////        testFactor1.setFactor("100");
////        TestFactor testFactor2 = new TestFactor();
////        testFactor2.setType(1);
////        testFactor2.setValue("50");
////        testFactor2.setWeight(1);
////        testFactor2.setFactor("50");
////        HashMap<String,Object> map = new HashMap<String,Object>();
////        map.put("TEST_A",testFactor1);
////        map.put("TEST_B",testFactor2);
//////        List<Factor> factorList = new List<Factor>();
////        List<Factor> list = new ArrayList<>();
//////        for (Map.Entry<String, Object> entry : map.entrySet()) {
//////            TestFactor factor = map.entrySet();
//////
//////            if(entry.getValue())
//////            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//////
//////        }
////        List<TestFactor> list1 = new ArrayList<>();
////        list1.add(testFactor1);
////        list1.add(testFactor2);
////        Factor factor1 = new Factor.Builder<>().channelCode("TEST_A").total(1).data(list1).build();
////        List<TestFactor> list2 = new ArrayList<>();
////        list1.add(testFactor2);
////        Factor factor2 = new Factor.Builder<>().channelCode("TEST_B").total(1).data(list1).build();
////
////        List<TestFactor> testFactor = (List<TestFactor>) factor1.getData();
//
//        //因子测试数据,组装符合的通道列表
//        Factor factor1 = new Factor.Builder<>().channelCode("TEST_A").total(1).build();
//        Factor factor2 = new Factor.Builder<>().channelCode("TEST_B").total(1).build();
//        List<Factor> list = new ArrayList<>();
//        list.add(factor1);
//        list.add(factor2);
//
//
//        QueryWrapper<FactorType> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(FactorType::getStatus,1);
//        List<FactorType> factorTypeList = iFactorTypeService.list(queryWrapper);
//        for(FactorType factorType:factorTypeList){
//            //进入到计算因子公共方法
//            log.info("进入到"+factorType.getFactorName()+"计算过程");
//            //根据不同的类型进入到不同的方法中
//            switch(factorType.getType()){
//                case FactorConstant.TYPE_1:eq(list,factorType);break;
//                case FactorConstant.TYPE_2:add(list,factorType);break;
//                case FactorConstant.TYPE_3:between(list,factorType);break;
//                case FactorConstant.TYPE_4:betweenTime(list,factorType);break;
//                case FactorConstant.TYPE_5:greater(list,factorType);break;
//                case FactorConstant.TYPE_6:less(list,factorType);break;
//                //类型错误不做任何操作进行下一步
//                default:break;
//            }
//        }
//        //若满足条件通道为0则进行返回
//        if(list.size()==0){
//            return null;
//        }
//        //若满足条件通道为1则进行返回
//        if(list.size()==1){
//            return list.get(0);
//        }
//        //找出得分最高的通道信息
//        Factor factorMax  = list.get(0);
//        for(Factor factor:list){
//            if(factor.getTotal()<factorMax.getTotal()){
//                list.remove(factor);
//            }
//            else {
//                list.remove(factorMax);
//                factorMax=factor;
//            }
//        }
//        return factorMax;
//
//    }
//    /**
//     * 静态路由因子比较型计算(redis_key为空，redis_key不为空则为动态因子)
//     * @author 
//     */
//    private  void eq(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //匹配因子数据,若为静态因子则进行匹配参数值
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                queryInfoWrapper.lambda().eq(FactorInfo::getValue,factor.getData());
//            }
//            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                queryInfoWrapper.lambda().eq(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    /**
//     * 路由因子加分型计算
//     * @author 
//     */
//    private  void add(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //不做匹配因子数据，有则加分，无则通过
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    /**
//     * 路由因子数据区间计算(redis_key为空，redis_key不为空则为动态因子)
//     * @author 
//     */
//    private  void between(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //匹配因子数据,若为静态因子则进行匹配参数值
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //大于等于
//                queryInfoWrapper.lambda().ge(FactorInfo::getMinData,factor.getData());
//                //小于
//                queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,factor.getData());
//            }
//            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //大于等于
//                queryInfoWrapper.lambda().ge(FactorInfo::getMinData,redisValue+"_"+factor.getChannelCode());
//                //小于
//                queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    /**
//     * 路由因子时间数据区间计算（获取当前时间，时分转为字符串作为参数）
//     * (redis_key为空，redis_key不为空则为动态因子)
//     * @author 
//     */
//    private  void betweenTime(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //获取当期时间字符串
//            String dateTime = DateUtil.format(new Date(),"HHmmss");
//            //大于等于
//            queryInfoWrapper.lambda().ge(FactorInfo::getMinData,dateTime);
//            //小于
//            queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,dateTime);
//
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    /**
//     * 路由因子大于计算(redis_key为空，redis_key不为空则为动态因子)
//     * @author 
//     */
//    private  void greater(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //匹配因子数据,若为静态因子则进行匹配参数值
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //大于
//                queryInfoWrapper.lambda().gt(FactorInfo::getValue,factor.getData());
//            }
//            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //大于
//                queryInfoWrapper.lambda().gt(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    /**
//     * 路由因子小于计算(redis_key为空，redis_key不为空则为动态因子)
//     * @author 
//     */
//    private  void less(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("通道:"+factor.getChannelCode()+"计算"+factorType.getFactorName()+"开始");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //匹配因子数据,若为静态因子则进行匹配参数值
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //小于
//                queryInfoWrapper.lambda().lt(FactorInfo::getValue,factor.getData());
//            }
//            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //小于
//                queryInfoWrapper.lambda().lt(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //判断该因子是否存在,则进行加分计算
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
//            }
//
//            //判断该因子是否允许通过，不允许则进行踢出
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
//            }
//        }
//    }
//    public static void main(String[] args) {
//
//        CountUtils countUtils = new CountUtils();
//        countUtils.count();
//    }
//
//}
