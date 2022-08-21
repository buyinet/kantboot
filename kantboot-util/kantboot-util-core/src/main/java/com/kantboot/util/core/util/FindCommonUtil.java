package com.kantboot.util.core.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kantboot.util.core.entity.CommonEntity;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FindCommonUtil<T> {

//    /**
//     * 获取泛型T的class
//     * @return
//     */
//    public Class getEntityClass(){
//        ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
//        //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
//        Class clazz = (Class)type.getActualTypeArguments()[0];//<T>
//        return clazz;
//    }
    /**
     * 用来通用查询的方法
     *
     * @param commonEntity
     * @param entityManager
     * @param transaction
     * @return
     */
    public Specification<T> findCommon(CommonEntity<T> commonEntity, EntityManager entityManager, EntityTransaction transaction) {
        transaction.begin();
        List<T> andEq = commonEntity.getAnd().getEq();
        String andEqJSONString = JSON.toJSONString(andEq);

        List<HashMap> andEqMaps = JSON.parseArray(andEqJSONString, HashMap.class);


        List<T> andLike = commonEntity.getAnd().getLike();
        String andLikeJSONString = JSON.toJSONString(andLike);
        List<HashMap> andLikeMaps = JSON.parseArray(andLikeJSONString, HashMap.class);


        List<T> andVague = commonEntity.getAnd().getVague();
        String andVagueJSONString = JSON.toJSONString(andVague);
        List<HashMap> andVagueMaps = JSON.parseArray(andVagueJSONString, HashMap.class);



        List<T> andGt = commonEntity.getAnd().getGt();
        String andGtJSONString = JSON.toJSONString(andGt);
        List<HashMap> andGtMaps = JSON.parseArray(andGtJSONString, HashMap.class);


        List<T> andLt = commonEntity.getAnd().getLt();
        String andLtJSONString = JSON.toJSONString(andLt);
        List<HashMap> andLtMaps = JSON.parseArray(andLtJSONString, HashMap.class);


        List<T> andGe = commonEntity.getAnd().getGe();
        String andGeJSONString = JSON.toJSONString(andGe);
        List<HashMap> andGeMaps = JSON.parseArray(andGeJSONString, HashMap.class);

        List<T> andLe = commonEntity.getAnd().getLe();
        String andLeJSONString = JSON.toJSONString(andLe);
        List<HashMap> andLeMaps = JSON.parseArray(andLeJSONString, HashMap.class);

        ///////
        List<T> orEq = commonEntity.getOr().getEq();
        String orEqJSONString = JSON.toJSONString(orEq);
        List<HashMap> orEqMaps = JSON.parseArray(orEqJSONString, HashMap.class);


        List<T> orLike = commonEntity.getOr().getLike();
        String orLikeJSONString = JSON.toJSONString(orLike);
        List<HashMap> orLikeMaps = JSON.parseArray(orLikeJSONString, HashMap.class);


        List<T> orVague = commonEntity.getOr().getVague();
        String orVagueJSONString = JSON.toJSONString(orVague);
        List<HashMap> orVagueMaps = JSON.parseArray(orVagueJSONString, HashMap.class);


        List<T> orGt = commonEntity.getOr().getGt();
        String orGtJSONString = JSON.toJSONString(orGt);
        List<HashMap> orGtMaps = JSON.parseArray(orGtJSONString, HashMap.class);

        List<T> orLt = commonEntity.getOr().getLt();
        String orLtJSONString = JSON.toJSONString(orLt);
        List<HashMap> orLtMaps = JSON.parseArray(orLtJSONString, HashMap.class);


        List<T> orGe = commonEntity.getOr().getGe();
        String orGeJSONString = JSON.toJSONString(orGe);
        List<HashMap> orGeMaps = JSON.parseArray(orGeJSONString, HashMap.class);

        List<T> orLe = commonEntity.getAnd().getLe();
        String orLeJSONString = JSON.toJSONString(orLe);
        List<HashMap> orLeMaps = JSON.parseArray(orLeJSONString, HashMap.class);
//        for (HashMap mapItem : orLeMaps) {
//            Set<String> set = mapItem.keySet();
//
//            Iterator<String> iterator1 = set.iterator();
//            while (iterator1.hasNext()) {
//                String o = iterator1.next();
//                if(isHasTransient(commonEntity.getEntity(),o)) {
//                    iterator1.remove();
//                }
//            }
//        }
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicatesByAnd = new ArrayList<Predicate>();

                //查询所有非空的数据
                for (String s : commonEntity.getNotNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNotNull(root.get(s)));
                }


                // 查询所有为空的数据
                for (String s : commonEntity.getIsNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNull(root.get(s)));
                }

                //查询所有 and 下的 = 条件
                for (HashMap map : andEqMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {


                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject) ) {

                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .equal(root.get(s), map.get(s)));
                            }
                            //当对一对多或者多对多查询时
                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .equal(root.join(s).get(s2), hashMap.get(s2)));
                                    }
                                }
                            }
                            //当对一对一查询时
                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .equal(root.get(s).get(s2), jsonObject.get(s2)));
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 % 条件
                for (HashMap map : andLikeMaps) {
                    Set<String> set = map.keySet();

                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject) ){
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .like(root.get(s), map.get(s) + ""));
                            }
                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), hashMap.get(s2) + ""));
                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .like(root.get(s).get(s2), jsonObject.get(s2)+""));
                                }
                            }
                        }
                    }
                }

                for (HashMap map : andVagueMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {


                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject) ) {
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .like(root.get(s), "%" + map.get(s) + "%"));
                            }


                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        if(hashMap.get(s2) instanceof String){
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .like(root.join(s).get(s2), "%" + hashMap.get(s2) + "%"));
                                        }
                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .like(root.get(s).get(s2), "%"+jsonObject.get(s2)+"%"));
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 > 条件
                for (HashMap map : andGtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject) ) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .gt(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .gt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .gt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                //#######################
                //查询所有 and 下的 < 条件
                for (HashMap map : andLtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {


                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject) ) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lt(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }

                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 >= 条件
                for (HashMap map : andGeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {


                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .ge(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray)  {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .ge(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .ge(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 <= 条件
                for (HashMap map : andLeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        boolean bool=true;
                        if(isHasTransient(commonEntity.getEntity(),s)) {
                            bool=false;
                        }
                        if(bool){

                        }
                        else


                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .le(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray)  {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .le(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .le(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }

                        }
                    }
                }

                Predicate[] predicateArrByAnd = new Predicate[predicatesByAnd.size()];
                Predicate and = criteriaBuilder.and(predicatesByAnd.toArray(predicateArrByAnd));

                ////////////////////////
                List<Predicate> predicatesByOr = new ArrayList<Predicate>();

                for (HashMap map : orEqMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .equal(root.get(s), map.get(s)));
                            }

                            else if(map.get(s) instanceof JSONArray)  {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .equal(root.join(s).get(s2), hashMap.get(s2)));
                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .equal(root.get(s).get(s2), jsonObject.get(s2)));
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLikeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .like(root.get(s), map.get(s) + ""));
                            }

                            else if(map.get(s) instanceof JSONArray)  {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), hashMap.get(s2) + ""));
                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .equal(root.get(s).get(s2), jsonObject.get(s2)));
                                }
                            }


                        }
                    }
                }

                for (HashMap map : orVagueMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .like(root.get(s), "%" + map.get(s) + "%"));
                            }

                            else if(map.get(s) instanceof JSONArray)  {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), "%" + hashMap.get(s2) + "%"));
                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    System.out.println(s2);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .like(root.get(s).get(s2), "%"+jsonObject.get(s2)+"%"));
                                }
                            }

                        }
                    }
                }

                for (HashMap map : orGtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {

                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .gt(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }

                            else if(map.get(s) instanceof JSONArray)   {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .gt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .gt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {

                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lt(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray)    {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                for (HashMap map : orGeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .ge(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray)    {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .ge(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .ge(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {



                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number=(Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .le(root.get(s), (Number) map.get(s)));
                                }catch (IllegalArgumentException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                }catch (ClassCastException ex){
                                    Date date=new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }



                            }
                            else if(map.get(s) instanceof JSONArray) {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number=(Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .le(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        }catch (IllegalArgumentException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }catch (ClassCastException ex){
                                            Date date=new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }

                            else if(map.get(s) instanceof JSONObject){
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number=(Number) jsonObject.get(s2);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .le(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    }catch (IllegalArgumentException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    }catch (ClassCastException ex){
                                        Date date=new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                                    }

                                }
                            }

                        }
                    }
                }

                Predicate[] predicateArrByOr = new Predicate[predicatesByOr.size()];
                Predicate or = criteriaBuilder.or(predicatesByOr.toArray(predicateArrByOr));

                if (predicateArrByAnd.length == 0 && predicateArrByOr.length == 0) {
                    return criteriaQuery.where(and).getRestriction();
                }
                if (predicateArrByAnd.length == 0) {
                    return criteriaQuery.where(or).getRestriction();
                }
                if (predicateArrByOr.length == 0) {
                    return criteriaQuery.where(and).getRestriction();
                }
                return criteriaQuery.where(and, or).getRestriction();
            }
        };
        return specification;
    }


    /**
     * 这个方法会将一段文本注入到某个类中添加了@Autowired注解的方法中，并将实例对象返回
     */
    public Object getId(Object object) {
        // 从Class对象中获取Demo中声明方法对应的Method对象
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断方法是否被加上了@Autowired这个注解
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Boolean isHasTransient(Object object,String fieldName) {
//        isHasTransientByFiled(object,fieldName) ||
        if(isHasTransientByMethod(object,fieldName)){
            return true;
        }
        return false;
    }

    public Boolean isHasTransientByFiled(Object object,String fieldName){
        // 从Class对象中获取Demo中声明方法对应的Method对象
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return true;
//            throw new RuntimeException(e);
        }

//        System.out.println(field.getName()+":"+field.isAnnotationPresent(Transient.class));
        // 判断方法是否被加上了@Transient这个注解
        if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
            field.setAccessible(true);
            return true;
        }
        return false;
    }
    public Boolean isHasTransientByMethod(Object object,String fieldName){
        Method method=null;
        Boolean ab=false;
        try {
            String methodName="get"+ StrUtil.upperFirst(fieldName);
            method=object.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
            ab=true;
        }


        Method method1=null;
        Boolean bb=false;
        try {
            String methodName="is"+ StrUtil.upperFirst(fieldName);
            method1=object.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException ex) {
//                throw new RuntimeException(ex);
            bb=true;
        }

        if(ab&&bb){
            return true;
        }

        if(!ab){
            System.out.println(method.getName()+":"+method.isAnnotationPresent(Transient.class));
            // 判断方法是否被加上了@Transient这个注解
            if (method.isAnnotationPresent(Transient.class) || method.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
                method.setAccessible(true);
                return true;
            }

        }
        if(!bb){
            System.out.println(method1.getName()+":"+method1.isAnnotationPresent(Transient.class));
            // 判断方法是否被加上了@Transient这个注解
            if (method1.isAnnotationPresent(Transient.class) || method1.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
                method1.setAccessible(true);
                return true;
            }
        }
        return false;

    }
    /**
     * 获得所有不为空的字段
     *
     * @param source
     * @return
     */
    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
