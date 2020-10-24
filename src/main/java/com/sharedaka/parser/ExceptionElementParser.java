package com.sharedaka.parser;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiThrowStatement;
import com.intellij.psi.PsiType;

public abstract class ExceptionElementParser extends HelperElementParser {

    @Override
    public void visitThrowStatement(PsiThrowStatement statement) {
        PsiExpression thrownException = statement.getException();
        if (thrownException != null) {
            if (interestingException(thrownException.getType())) {
                processException(statement);
            }
        }
    }


    public abstract boolean interestingException(PsiType exceptionType);

    public abstract void processException(PsiThrowStatement statement);

}
