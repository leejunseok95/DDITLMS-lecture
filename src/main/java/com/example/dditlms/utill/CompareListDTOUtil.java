package com.example.dditlms.utill;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareListDTOUtil {

    /**
     * List<VO> 정렬
     *
     * @param list             비교할 리스트(List<VO> 만 가능)
     * @param getterMethodText 비교할 getter 함수
     * @param sortInfo         정렬 문자열 (ASC, DESC)
     */
    public static void sortListVO(List<?> list, String getterMethodText, String sortInfo) {

        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object firstObject, Object secondObject) {
                int rtn = 0;
                int compareIndex = 0; // 비교 인덱스 (작은 문자열 수)
                String firstData = "";
                String secondData = "";
                int firstValue = 0;
                int secondValue = 0;

                // 비교할 대상
                try {
                    Method firstDeclaredMethod = firstObject.getClass().getDeclaredMethod(getterMethodText);
                    firstData = (String) firstDeclaredMethod.invoke(firstObject, new Object[]{});
                    Method secondDeclaredMethod = secondObject.getClass().getDeclaredMethod(getterMethodText);
                    secondData = (String) secondDeclaredMethod.invoke(secondObject, new Object[]{});
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if (firstData == null || firstData == "" || secondData == null || secondData == "") {
                    return rtn;
                }

                // 기본 정렬값 설정
                if ("DESC".equals(sortInfo)) {
                    if (firstData.length() > secondData.length()) {
                        rtn = -1;
                        compareIndex = secondData.length();
                    } else if (firstData.length() < secondData.length()) {
                        rtn = 1;
                        compareIndex = firstData.length();
                    } else {
                        compareIndex = firstData.length();
                    }
                } else {
                    if (firstData.length() < secondData.length()) {
                        rtn = -1;
                        compareIndex = firstData.length();
                    } else if (firstData.length() > secondData.length()) {
                        rtn = 1;
                        compareIndex = secondData.length();
                    } else {
                        compareIndex = firstData.length();
                    }
                }

                for (int i = 0; i < compareIndex; i++) {
                    firstValue = Integer.valueOf(firstData.charAt(i));
                    secondValue = Integer.valueOf(secondData.charAt(i));

                    if ("DESC".equals(sortInfo)) {
                        if (firstValue > secondValue) {
                            rtn = -1;
                            break;
                        } else if (firstValue < secondValue) {
                            rtn = 1;
                            break;
                        }
                    } else {
                        if (firstValue < secondValue) {
                            rtn = -1;
                            break;
                        } else if (firstValue > secondValue) {
                            rtn = 1;
                            break;
                        }
                    }
                }

                return rtn;
            }
        });
    }
}
