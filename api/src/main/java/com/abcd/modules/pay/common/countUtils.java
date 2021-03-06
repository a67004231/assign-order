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
//        //??????????????????,???????????????????????????
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
//            //?????????????????????????????????
//            log.info("?????????"+factorType.getFactorName()+"????????????");
//            //????????????????????????????????????????????????
//            switch(factorType.getType()){
//                case FactorConstant.TYPE_1:eq(list,factorType);break;
//                case FactorConstant.TYPE_2:add(list,factorType);break;
//                case FactorConstant.TYPE_3:between(list,factorType);break;
//                case FactorConstant.TYPE_4:betweenTime(list,factorType);break;
//                case FactorConstant.TYPE_5:greater(list,factorType);break;
//                case FactorConstant.TYPE_6:less(list,factorType);break;
//                //?????????????????????????????????????????????
//                default:break;
//            }
//        }
//        //????????????????????????0???????????????
//        if(list.size()==0){
//            return null;
//        }
//        //????????????????????????1???????????????
//        if(list.size()==1){
//            return list.get(0);
//        }
//        //?????????????????????????????????
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
//     * ?????????????????????????????????(redis_key?????????redis_key???????????????????????????)
//     * @author 
//     */
//    private  void eq(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //??????????????????,??????????????????????????????????????????
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                queryInfoWrapper.lambda().eq(FactorInfo::getValue,factor.getData());
//            }
//            //?????????????????????redis??????????????????????????????redis??????????????????redis?????????????????????????????????
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                queryInfoWrapper.lambda().eq(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
//            }
//        }
//    }
//    /**
//     * ???????????????????????????
//     * @author 
//     */
//    private  void add(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //??????????????????????????????????????????????????????
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
//            }
//        }
//    }
//    /**
//     * ??????????????????????????????(redis_key?????????redis_key???????????????????????????)
//     * @author 
//     */
//    private  void between(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //??????????????????,??????????????????????????????????????????
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //????????????
//                queryInfoWrapper.lambda().ge(FactorInfo::getMinData,factor.getData());
//                //??????
//                queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,factor.getData());
//            }
//            //?????????????????????redis??????????????????????????????redis??????????????????redis?????????????????????????????????
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //????????????
//                queryInfoWrapper.lambda().ge(FactorInfo::getMinData,redisValue+"_"+factor.getChannelCode());
//                //??????
//                queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
//            }
//        }
//    }
//    /**
//     * ????????????????????????????????????????????????????????????????????????????????????????????????
//     * (redis_key?????????redis_key???????????????????????????)
//     * @author 
//     */
//    private  void betweenTime(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //???????????????????????????
//            String dateTime = DateUtil.format(new Date(),"HHmmss");
//            //????????????
//            queryInfoWrapper.lambda().ge(FactorInfo::getMinData,dateTime);
//            //??????
//            queryInfoWrapper.lambda().lt(FactorInfo::getMaxData,dateTime);
//
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
//            }
//        }
//    }
//    /**
//     * ????????????????????????(redis_key?????????redis_key???????????????????????????)
//     * @author 
//     */
//    private  void greater(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //??????????????????,??????????????????????????????????????????
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //??????
//                queryInfoWrapper.lambda().gt(FactorInfo::getValue,factor.getData());
//            }
//            //?????????????????????redis??????????????????????????????redis??????????????????redis?????????????????????????????????
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //??????
//                queryInfoWrapper.lambda().gt(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
//            }
//        }
//    }
//    /**
//     * ????????????????????????(redis_key?????????redis_key???????????????????????????)
//     * @author 
//     */
//    private  void less(List<Factor> list,FactorType factorType){
//        for(Factor factor : list){
//            log.info("??????:"+factor.getChannelCode()+"??????"+factorType.getFactorName()+"??????");
//
//            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
//            queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factorType.getId());
//            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
//            //??????????????????,??????????????????????????????????????????
//            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
//                //??????
//                queryInfoWrapper.lambda().lt(FactorInfo::getValue,factor.getData());
//            }
//            //?????????????????????redis??????????????????????????????redis??????????????????redis?????????????????????????????????
//            else{
//                String redisValue = redisTemplate.opsForValue().get(factorType.getRedisKey()).toString();
//                //??????
//                queryInfoWrapper.lambda().lt(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
//            }
////                queryInfoWrapper.lambda().orderByAsc()
//            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
//
//            //???????????????????????????,?????????????????????
//            if(factorInfo!=null){
//                factor.setTotal(factor.getTotal()+factorInfo.getWeight());
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"???????????????:"+factor.getTotal());
//            }
//
//            //????????????????????????????????????????????????????????????
//            if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
//                list.remove(factor);
//                log.info("??????:???"+factor.getChannelCode()+"???????????????"+factorType.getFactorName()+"?????????????????????????????????");
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
