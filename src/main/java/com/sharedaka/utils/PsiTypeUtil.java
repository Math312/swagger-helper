package com.sharedaka.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

public class PsiTypeUtil {

    public static String getReturnType(PsiType psiType) {
//        psiType.getInternalCanonicalText()
        return StringUtil.removeSpace(psiType.getCanonicalText()).replaceAll("<.*>", "");
    }

    public static PsiClass getPsiClass(Project project, PsiType psiType) {
        return JavaPsiFacade.getInstance(project).findClass(psiType.getCanonicalText(), GlobalSearchScope.projectScope(project));
    }
}
