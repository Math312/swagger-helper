package com.sharedaka.processor.business;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

public interface MethodSupportable {

    boolean support(PsiClass psiClass, PsiMethod psiMethod);
}
