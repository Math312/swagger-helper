package com.sharedaka.utils;

import com.intellij.psi.PsiType;
import org.apache.commons.lang.StringUtils;

public class CommentUtils {


    /**
     * 获取注解说明  不写/@desc/@describe/@description
     * @param comment 所有注释
     * @return String
     */
    public static String getCommentDesc(String comment) {
        String[] strings = comment.split("\n");
        if (strings.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            String row = StringUtils.deleteWhitespace(string);
            if (StringUtils.isEmpty(row) || StringUtils.startsWith(row,"/**")) {
                continue;
            }
            if (StringUtils.startsWithIgnoreCase(row,"*@desc")
                    && !StringUtils.startsWithIgnoreCase(row,"*@describe")
                    && !StringUtils.startsWithIgnoreCase(row,"*@description")) {
                appendComment(string, stringBuilder, 5);
            }
            if (StringUtils.startsWithIgnoreCase(row,"*@description")) {
                appendComment(string, stringBuilder, 12);
            }
            if (StringUtils.startsWithIgnoreCase(row,"*@describe")) {
                appendComment(string, stringBuilder, 9);
            }
            if (StringUtils.startsWith(row,"*@") || StringUtils.startsWith(row,"*/")) {
                continue;
            }
            int descIndex = StringUtils.ordinalIndexOf(string,"*",1);
            if (descIndex == -1) {
                descIndex = StringUtils.ordinalIndexOf(string,"//",1);
                descIndex += 1;
            }
            String desc = string.substring(descIndex + 1);
            stringBuilder.append(desc);
        }
        return StringUtils.trim(stringBuilder.toString());
    }


    private static void appendComment(String string, StringBuilder stringBuilder, int index) {
        String lowerCaseStr = string.toLowerCase();
        int descIndex = StringUtils.ordinalIndexOf(lowerCaseStr,"@",1);
        descIndex += index;
        String desc = string.substring(descIndex);
        stringBuilder.append(desc);
    }

    /**
     * dfdfdf
     * @param args
     */
    public static void main(String[] args) {

//        System.out.println(getCommentDesc("/**\n" +
//                " * @describe pwhxbdk\n" +
//                " * @author pwhxbdk\n" +
//                " * @date 2020/4/6\n" +
//                " */"));
//        System.out.println(StringUtils.ordinalIndexOf("*@desc fdfdfdfdf","c",1));
    }


}