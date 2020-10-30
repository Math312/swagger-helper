package com.sharedaka.parser;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiThrowStatement;
import com.intellij.psi.PsiType;

public abstract class ExceptionElementParser extends HelperElementParser {

    @Override
    public void visitThrowStatement(PsiThrowStatement statement) {
        PsiExpression thrownException = statement.getException();
        if (thrownException != null) {
            if (interestingException(thrownException.getProject(), thrownException.getType())) {
                processException(statement);
            }
        }
    }


    public abstract boolean interestingException(Project project, PsiType exceptionType);

    public abstract void processException(PsiThrowStatement statement);

}
