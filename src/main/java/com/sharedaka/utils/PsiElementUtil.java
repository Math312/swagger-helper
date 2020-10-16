package com.sharedaka.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

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
        // 待导入类有多个同名类或没有时 让用户自行处理
        if (psiClasses.length != 1) {
            return;
        }
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
}
