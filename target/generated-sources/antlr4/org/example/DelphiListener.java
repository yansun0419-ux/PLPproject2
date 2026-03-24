// Generated from org/example/Delphi.g4 by ANTLR 4.13.2
package org.example;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DelphiParser}.
 */
public interface DelphiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void enterTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void exitTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(DelphiParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(DelphiParser.TypeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(DelphiParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(DelphiParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceType}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceType(DelphiParser.InterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceType}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceType(DelphiParser.InterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceList}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceList(DelphiParser.InterfaceListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceList}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceList(DelphiParser.InterfaceListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceMemberList}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberList(DelphiParser.InterfaceMemberListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceMemberList}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberList(DelphiParser.InterfaceMemberListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaration(DelphiParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaration(DelphiParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#memberList}.
	 * @param ctx the parse tree
	 */
	void enterMemberList(DelphiParser.MemberListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#memberList}.
	 * @param ctx the parse tree
	 */
	void exitMemberList(DelphiParser.MemberListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#memberContent}.
	 * @param ctx the parse tree
	 */
	void enterMemberContent(DelphiParser.MemberContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#memberContent}.
	 * @param ctx the parse tree
	 */
	void exitMemberContent(DelphiParser.MemberContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#visibilitySpecifier}.
	 * @param ctx the parse tree
	 */
	void enterVisibilitySpecifier(DelphiParser.VisibilitySpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#visibilitySpecifier}.
	 * @param ctx the parse tree
	 */
	void exitVisibilitySpecifier(DelphiParser.VisibilitySpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(DelphiParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(DelphiParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 */
	void enterVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 */
	void exitVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(DelphiParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(DelphiParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(DelphiParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(DelphiParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#implementationSection}.
	 * @param ctx the parse tree
	 */
	void enterImplementationSection(DelphiParser.ImplementationSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#implementationSection}.
	 * @param ctx the parse tree
	 */
	void exitImplementationSection(DelphiParser.ImplementationSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodImplementation}.
	 * @param ctx the parse tree
	 */
	void enterMethodImplementation(DelphiParser.MethodImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodImplementation}.
	 * @param ctx the parse tree
	 */
	void exitMethodImplementation(DelphiParser.MethodImplementationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#routineImplementation}.
	 * @param ctx the parse tree
	 */
	void enterRoutineImplementation(DelphiParser.RoutineImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#routineImplementation}.
	 * @param ctx the parse tree
	 */
	void exitRoutineImplementation(DelphiParser.RoutineImplementationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#nonEmptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyStatement(DelphiParser.NonEmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#nonEmptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyStatement(DelphiParser.NonEmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(DelphiParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(DelphiParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(DelphiParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(DelphiParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(DelphiParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(DelphiParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(DelphiParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(DelphiParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(DelphiParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(DelphiParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#relExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelExpression(DelphiParser.RelExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#relExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelExpression(DelphiParser.RelExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(DelphiParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(DelphiParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(DelphiParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(DelphiParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(DelphiParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(DelphiParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(DelphiParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(DelphiParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdentifier(DelphiParser.QualifiedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdentifier(DelphiParser.QualifiedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(DelphiParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(DelphiParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#parameterGroup}.
	 * @param ctx the parse tree
	 */
	void enterParameterGroup(DelphiParser.ParameterGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#parameterGroup}.
	 * @param ctx the parse tree
	 */
	void exitParameterGroup(DelphiParser.ParameterGroupContext ctx);
}