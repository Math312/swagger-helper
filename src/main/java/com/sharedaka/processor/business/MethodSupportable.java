package com.sharedaka.processor.business;

import com.intellij.psi.PsiMethod;

public interface MethodSupportable {

    boolean support(PsiMethod psiMethod);
}
