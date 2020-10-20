package com.sharedaka.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PsiElementUtil {

    public static void importPackage(PsiElementFactory elementFactory, PsiFile file, Project project, String className) {
        if (!(file instanceof PsiJavaFile)) {
            return;
        }
        final PsiJavaFile javaFile = (PsiJavaFile) file;
        final PsiImportList importList = javaFile.getImportList();
        if (importList == null) {
            return;
        }
        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.allScope(project));
        // todo 导入类类名重复问题
        PsiClass waiteImportClass = psiClasses[0];
        for (PsiImportStatementBase is : importList.getAllImportStatements()) {
            String impQualifiedName = is.getImportReference().getQualifiedName();
            if (waiteImportClass.getQualifiedName().equals(impQualifiedName)) {
                // 已经导入
                return;
            }
        }
        importList.add(elementFactory.createImportStatement(waiteImportClass));
    }

    public static PsiAnnotation getAnnotation(PsiModifierListOwner psiClass, String annotationName) {
        Map<String,PsiAnnotation> psiAnnotationMap = Arrays.stream(psiClass.getAnnotations()).collect(Collectors.toMap(PsiAnnotation::getQualifiedName,(psiAnnotation)-> psiAnnotation));
        return psiAnnotationMap.get(annotationName);
    }


}
