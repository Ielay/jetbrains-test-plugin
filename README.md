**Design description**

Firstly, we need to select some text to work with and then click
the button in a main toolbar.

We can access selected text from an object of Editor class using AnActionEvent.
Now we need to build AST. For that purpose we can use PsiBuilder class passing into it a
special parser and lexer objects.

When we get an instance of PsiBuilder, we need to split all tokens into a number of groups of tokens using markers. Each group is
representing some language expression. (I determine just a number of all possible language expressions).

After we split all the tokens into groups we create an AST and visualize it using Swing components (this is not
the best solution to not use Intellij provided UI components, but Swing objects is easier to implement).


