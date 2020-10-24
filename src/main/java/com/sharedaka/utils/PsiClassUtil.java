package com.sharedaka.utils;

import com.intellij.psi.PsiClass;

public class PsiClassUtil {

    public boolean isEnum(PsiClass psiClass) {
        return psiClass.isEnum();
    }

//    public boolean isAbstract(PsiClass psiClass) {
//        return psiClass.getModifierList().
//    }

}
