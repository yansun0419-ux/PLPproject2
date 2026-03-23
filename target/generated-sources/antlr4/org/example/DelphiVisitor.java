// Generated from org/example/Delphi.g4 by ANTLR 4.13.2
package org.example;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DelphiParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DelphiVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDefinition(DelphiParser.TypeDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(DelphiParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceType(DelphiParser.InterfaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceList(DelphiParser.InterfaceListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceMemberList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMemberList(DelphiParser.InterfaceMemberListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodDeclaration(DelphiParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#memberList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberList(DelphiParser.MemberListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#memberContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberContent(DelphiParser.MemberContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#visibilitySpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibilitySpecifier(DelphiParser.VisibilitySpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(DelphiParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(DelphiParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(DelphiParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#implementationSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementationSection(DelphiParser.ImplementationSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodImplementation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodImplementation(DelphiParser.MethodImplementationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#nonEmptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonEmptyStatement(DelphiParser.NonEmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(DelphiParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(DelphiParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#relExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpression(DelphiParser.RelExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(DelphiParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(DelphiParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(DelphiParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(DelphiParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(DelphiParser.QualifiedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(DelphiParser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#parameterGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterGroup(DelphiParser.ParameterGroupContext ctx);
}