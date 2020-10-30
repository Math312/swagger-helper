package com.sharedaka.utils;

import com.intellij.psi.PsiType;

public class PsiTypeUtil {

    public static String getReturnType(PsiType psiType) {
//        psiType.getInternalCanonicalText()
        return StringUtil.removeSpace(psiType.getCanonicalText()).replaceAll("<.*>", "");
    }
}
