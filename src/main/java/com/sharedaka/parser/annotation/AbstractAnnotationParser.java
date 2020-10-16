package com.sharedaka.parser.annotation;

import com.intellij.psi.PsiAnnotation;

/**
 * @author math312
 */
public abstract class AbstractAnnotationParser {

    public abstract boolean support(PsiAnnotation psiAnnotation);

    public Object parse(PsiAnnotation psiAnnotation) {
        if (support(psiAnnotation)) {
            return doParse(psiAnnotation);
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected abstract Object doParse(PsiAnnotation psiAnnotation);
}
