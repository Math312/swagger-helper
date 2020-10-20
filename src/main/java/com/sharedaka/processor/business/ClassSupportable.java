package com.sharedaka.processor.business;

import com.intellij.psi.PsiClass;

public interface ClassSupportable {

    boolean support(PsiClass psiClass);

    void process(PsiClass psiClass);
}
