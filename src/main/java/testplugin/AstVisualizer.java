package testplugin;

import com.intellij.lang.*;
import com.intellij.lang.java.JavaParserDefinition;
import com.intellij.lang.java.lexer.JavaLexer;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.impl.source.tree.JavaElementType;

import javax.swing.tree.DefaultMutableTreeNode;

import static com.intellij.psi.JavaTokenType.*;

/**
 * @author petrov
 * @since 11.09.19
 */
public class AstVisualizer extends AnAction {

    public AstVisualizer() {
        super("Build AST");
    }

    private int variablesInitCount;
    private int variablesAccessCount;
    private int exceptionsThrownCount;

    private void resetCounters() {
        variablesInitCount = 0;
        variablesAccessCount = 0;
        exceptionsThrownCount = 0;
    }

    public void actionPerformed(AnActionEvent event) {
        resetCounters();

        Project project = event.getProject();

        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        if (editor == null) {
            showWarning(project);
            return;
        }

        String selectedText = editor.getCaretModel().getCurrentCaret().getSelectedText();

        if (selectedText == null) {
            showWarning(project);
            return;
        }


        PsiBuilder psiBuilder = PsiBuilderFactory.getInstance().createBuilder(new JavaParserDefinition(), new JavaLexer(LanguageLevel.JDK_1_8), selectedText);
        ASTNode rootASTNode = parse(psiBuilder);

        DefaultMutableTreeNode root = getTree(rootASTNode, new DefaultMutableTreeNode(rootASTNode));

        new Runnable() {
            @Override
            public void run() {
                new Ast(root, exceptionsThrownCount, variablesInitCount, variablesAccessCount);
            }
        }.run();
    }

    private ASTNode parse(PsiBuilder builder){
        PsiBuilder.Marker rootMarker = builder.mark();

        while(!builder.eof()) {
            if (builder.getTokenType() == IDENTIFIER && builder.lookAhead(1) == DOT && builder.lookAhead(2) == IDENTIFIER && builder.lookAhead(3) == LPARENTH) {
                parseMethodCall(builder);
                continue;
            }

            if (builder.getTokenType() == THROW_KEYWORD && (builder.lookAhead(2) == NEW_KEYWORD || builder.lookAhead(2) == IDENTIFIER)) {
                ++exceptionsThrownCount;
                parseExceptionThrowing(builder);
                continue;
            }

//            if (builder.getTokenType() == IF_KEYWORD) {
//                parseIfCondition(builder);
//                continue;
//            }

            if (builder.getTokenType() == FINAL_KEYWORD || builder.getTokenType() == STATIC_KEYWORD || builder.getTokenType() == IDENTIFIER) {
                parseStatement(builder);
                continue;
            }

            builder.advanceLexer();
        }

        rootMarker.done(JavaElementType.CODE_BLOCK);
        return builder.getTreeBuilt();
    }

    private void parseExceptionThrowing(PsiBuilder builder) {
        PsiBuilder.Marker exceptionThrowingMarker = builder.mark();

        while (!builder.eof()) {
            builder.advanceLexer();
            if (builder.getTokenType() == SEMICOLON) {
                builder.advanceLexer();
                exceptionThrowingMarker.done(JavaElementType.THROW_STATEMENT);
                break;
            }
        }
    }

    private void parseMethodCall(PsiBuilder builder) {
        PsiBuilder.Marker functionCallMarker = builder.mark();

        while (!builder.eof()) {
            builder.advanceLexer();
            if (builder.getTokenType() == SEMICOLON) {
                builder.advanceLexer();
                functionCallMarker.done(JavaElementType.METHOD_CALL_EXPRESSION);
                break;
            }
        }
    }

    private void parseStatement(PsiBuilder builder) {
        PsiBuilder.Marker statementMarket = builder.mark();

        while (!builder.eof()) {
            builder.advanceLexer();
            if (builder.getTokenType() == SEMICOLON) {
                builder.advanceLexer();
                statementMarket.done(JavaElementType.EXPRESSION_STATEMENT);
                break;
            }
        }
    }

    private DefaultMutableTreeNode getTree(ASTNode ast, DefaultMutableTreeNode parentTreeNode) {
        ASTNode[] children = ast.getChildren(null);

        if (children.length == 0) {
            return parentTreeNode;
        }

        for (ASTNode astNode : children) {
            if (astNode.getElementType() == WHITE_SPACE) {
                continue;
            }

            DefaultMutableTreeNode childTreeNode;
//            if (astNode.getChildren(null).length == 0) {
//                childTreeNode = getTree(astNode, new DefaultMutableTreeNode(astNode.getText()));
//            } else {
                childTreeNode = getTree(astNode, new DefaultMutableTreeNode(astNode));
//            }

            parentTreeNode.add(childTreeNode);
        }

        return parentTreeNode;
    }

    private void showWarning(Project project) {
        Messages.showMessageDialog(project, "You need to select some text before trying to build AST", "Warning", Messages.getWarningIcon());
    }
}
