package com.sharedaka.parser.enums;

import com.intellij.psi.PsiClass;

public abstract class AbstractEnumParser {

    public boolean support(PsiClass psiClass) {
        return psiClass.isEnum();
    }

    public Object parse(PsiClass psiClass) {
        if (support(psiClass)) {
            return doParse(psiClass);
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected abstract Object doParse(PsiClass psiClass);
}
