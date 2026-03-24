package org.example;

import java.util.*;

public class DelphiInterpreter extends DelphiBaseVisitor<Object> {
    private final Map<String, Object> globals = new HashMap<>();
    private final Map<String, DelphiParser.MethodImplementationContext> methodBodies = new HashMap<>();
    private final Map<String, DelphiParser.RoutineImplementationContext> routineBodies = new HashMap<>();
    private final Map<String, DelphiParser.ClassTypeContext> classDefinitions = new HashMap<>();
    private final Map<String, DelphiParser.InterfaceTypeContext> interfaceDefinitions = new HashMap<>();
    private final Map<String, String> classInheritance = new HashMap<>();
    private final Map<String, List<String>> classInterfaces = new HashMap<>();

    private final Scope globalScope = new Scope(null, globals);
    private Scope currentScope = globalScope;

    private ObjectInstance currentSelf = null;
    private final Scanner scanner = new Scanner(System.in);
    private final boolean printOptimizedAst = true;

    private static class BreakSignal extends RuntimeException {}
    private static class ContinueSignal extends RuntimeException {}

    private static class FoldResult {
        final boolean isConstant;
        final Object value;
        final String rendered;

        FoldResult(boolean isConstant, Object value, String rendered) {
            this.isConstant = isConstant;
            this.value = value;
            this.rendered = rendered;
        }
    }

    private static class Scope {
        private final Scope parent;
        private final Map<String, Object> values;

        Scope(Scope parent) {
            this(parent, new HashMap<>());
        }

        Scope(Scope parent, Map<String, Object> values) {
            this.parent = parent;
            this.values = values;
        }

        void define(String name, Object value) {
            values.put(name.toLowerCase(), value);
        }

        boolean containsLocal(String name) {
            return values.containsKey(name.toLowerCase());
        }

        boolean containsInChain(String name) {
            String key = name.toLowerCase();
            Scope s = this;
            while (s != null) {
                if (s.values.containsKey(key)) return true;
                s = s.parent;
            }
            return false;
        }

        Object resolve(String name) {
            String key = name.toLowerCase();
            Scope s = this;
            while (s != null) {
                if (s.values.containsKey(key)) return s.values.get(key);
                s = s.parent;
            }
            return null;
        }

        void assign(String name, Object value) {
            String key = name.toLowerCase();
            Scope s = this;
            while (s != null) {
                if (s.values.containsKey(key)) {
                    s.values.put(key, value);
                    return;
                }
                s = s.parent;
            }
            values.put(key, value);
        }
    }

    public static class ObjectInstance {
        String typeName;
        Map<String, Object> fields = new HashMap<>();
        Map<String, String> fieldVisibility = new HashMap<>();

        public ObjectInstance(String type) {
            this.typeName = type;
        }
    }

    @Override
    public Object visitProgram(DelphiParser.ProgramContext ctx) {
        if (ctx.typeSection() != null) visit(ctx.typeSection());
        if (ctx.varSection() != null) visit(ctx.varSection());
        if (ctx.implementationSection() != null) visit(ctx.implementationSection());
        return visit(ctx.compoundStatement());
    }

    @Override
    public Object visitVarSection(DelphiParser.VarSectionContext ctx) {
        for (DelphiParser.VariableDeclarationContext decl : ctx.variableDeclaration()) {
            visit(decl);
        }
        return null;
    }

    @Override
    public Object visitVariableDeclaration(DelphiParser.VariableDeclarationContext ctx) {
        Object defaultValue = defaultValueForType(ctx.type_().getText());
        for (DelphiParser.IdentifierContext id : ctx.identifierList().identifier()) {
            currentScope.define(id.getText(), defaultValue);
        }
        return null;
    }

    @Override
    public Object visitCompoundStatement(DelphiParser.CompoundStatementContext ctx) {
        Scope old = currentScope;
        currentScope = new Scope(old);
        try {
            if (ctx.statementList() != null) {
                return visit(ctx.statementList());
            }
            return null;
        } finally {
            currentScope = old;
        }
    }

    @Override
    public Object visitTypeSection(DelphiParser.TypeSectionContext ctx) {
        for (DelphiParser.TypeDefinitionContext typeDef : ctx.typeDefinition()) {
            String typeName = typeDef.identifier().getText().toLowerCase();

            if (typeDef.classType() != null) {
                DelphiParser.ClassTypeContext classCtx = typeDef.classType();
                classDefinitions.put(typeName, classCtx);

                if (classCtx.identifier() != null) {
                    String parentClass = classCtx.identifier().getText().toLowerCase();
                    classInheritance.put(typeName, parentClass);
                }

                if (classCtx.interfaceList() != null) {
                    List<DelphiParser.IdentifierContext> identifiers = classCtx.interfaceList().identifier();
                    if (!identifiers.isEmpty()) {
                        List<String> interfaces = new ArrayList<>();
                        for (DelphiParser.IdentifierContext id : identifiers) {
                            interfaces.add(id.getText().toLowerCase());
                        }
                        classInterfaces.put(typeName, interfaces);
                    }
                }
            }

            if (typeDef.interfaceType() != null) {
                interfaceDefinitions.put(typeName, typeDef.interfaceType());
            }
        }
        return null;
    }

    @Override
    public Object visitImplementationSection(DelphiParser.ImplementationSectionContext ctx) {
        for (DelphiParser.MethodImplementationContext method : ctx.methodImplementation()) {
            visit(method);
        }
        for (DelphiParser.RoutineImplementationContext routine : ctx.routineImplementation()) {
            visit(routine);
        }
        return null;
    }

    @Override
    public Object visitMethodImplementation(DelphiParser.MethodImplementationContext ctx) {
        String fullName = ctx.qualifiedIdentifier().getText().toLowerCase();
        methodBodies.put(fullName, ctx);
        return null;
    }

    @Override
    public Object visitRoutineImplementation(DelphiParser.RoutineImplementationContext ctx) {
        String name = ctx.identifier().getText().toLowerCase();
        routineBodies.put(name, ctx);
        return null;
    }

    private String getMethodType(DelphiParser.MethodImplementationContext ctx) {
        if (ctx.CONSTRUCTOR() != null) return "constructor";
        if (ctx.DESTRUCTOR() != null) return "destructor";
        if (ctx.FUNCTION() != null) return "function";
        if (ctx.PROCEDURE() != null) return "procedure";
        return "procedure";
    }

    private List<String> getInheritanceChain(String className) {
        List<String> chain = new ArrayList<>();
        String current = className.toLowerCase();

        while (current != null) {
            chain.add(current);
            current = classInheritance.get(current);

            if (current != null && chain.contains(current)) {
                throw new RuntimeException("Error: circular inheritance detected: " + chain);
            }
        }

        return chain;
    }

    private void validateInterfaceImplementation(String className) {
        List<String> interfaces = classInterfaces.get(className.toLowerCase());
        if (interfaces == null || interfaces.isEmpty()) return;

        for (String interfaceName : interfaces) {
            DelphiParser.InterfaceTypeContext interfaceDef = interfaceDefinitions.get(interfaceName);
            if (interfaceDef == null || interfaceDef.interfaceMemberList() == null) {
                continue;
            }

            for (DelphiParser.InterfaceMethodDeclarationContext method :
                    interfaceDef.interfaceMemberList().interfaceMethodDeclaration()) {

                String methodName = method.identifier().getText().toLowerCase();
                String fullMethodName = className.toLowerCase() + "." + methodName;

                if (!methodBodies.containsKey(fullMethodName)) {
                    boolean foundInParent = false;
                    String parent = classInheritance.get(className.toLowerCase());

                    while (parent != null) {
                        String parentMethodName = parent + "." + methodName;
                        if (methodBodies.containsKey(parentMethodName)) {
                            foundInParent = true;
                            break;
                        }
                        parent = classInheritance.get(parent);
                    }

                    if (!foundInParent) {
                        throw new RuntimeException(
                                "Error: class '" + className + "' does not implement interface '" +
                                        interfaceName + "' method '" + methodName + "'"
                        );
                    }
                }
            }
        }
    }

    private Object defaultValueForType(String typeName) {
        String t = typeName.toLowerCase();
        if (t.equals("string")) return "";
        return 0;
    }

    private Object getValue(String name) {
        String key = name.toLowerCase();

        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Object obj = currentScope.resolve(parts[0]);
            if (!(obj instanceof ObjectInstance)) {
                return 0;
            }
            ObjectInstance instance = (ObjectInstance) obj;
            checkFieldAccess(instance, parts[1]);
            return instance.fields.getOrDefault(parts[1], 0);
        }

        if (currentSelf != null && currentSelf.fields.containsKey(key)) {
            return currentSelf.fields.get(key);
        }

        if (currentScope.containsInChain(key)) {
            return currentScope.resolve(key);
        }
        return 0;
    }

    private void assignValue(String name, Object value) {
        String key = name.toLowerCase();

        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Object obj = currentScope.resolve(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                checkFieldAccess(instance, parts[1]);
                instance.fields.put(parts[1], value);
                return;
            }
            throw new RuntimeException("Error: cannot assign through non-object reference '" + parts[0] + "'");
        }

        if (currentScope.containsInChain(key)) {
            currentScope.assign(key, value);
            return;
        }

        if (currentSelf != null && currentSelf.fields.containsKey(key)) {
            currentSelf.fields.put(key, value);
            return;
        }

        currentScope.define(key, value);
    }

    private void checkFieldAccess(ObjectInstance instance, String fieldName) {
        String visibility = instance.fieldVisibility.get(fieldName);
        if (visibility == null) {
            visibility = "public";
        }

        switch (visibility.toLowerCase()) {
            case "private":
                if (currentSelf != instance) {
                    throw new RuntimeException("Error: cannot access private field '" + fieldName + "'");
                }
                break;
            case "protected":
                if (currentSelf != null && (currentSelf == instance || isSameClassOrSubclass(currentSelf, instance))) {
                    break;
                }
                break;
            case "published":
            case "public":
                break;
            default:
                break;
        }
    }

    private boolean isSameClassOrSubclass(ObjectInstance current, ObjectInstance target) {
        if (current.typeName.equalsIgnoreCase(target.typeName)) {
            return true;
        }

        List<String> currentChain = getInheritanceChain(current.typeName);
        return currentChain.contains(target.typeName.toLowerCase());
    }

    @Override
    public Object visitAssignment(DelphiParser.AssignmentContext ctx) {
        String name = ctx.qualifiedIdentifier().getText().toLowerCase();
        FoldResult folded = foldExpression(ctx.expression());
        if (printOptimizedAst && folded != null) {
            String original = ctx.expression().getText();
            if (!original.equals(folded.rendered)) {
                System.out.println("[AST-OPT] " + name + " := " + folded.rendered);
            }
        }

        Object value;
        if (folded != null && folded.isConstant) {
            value = folded.value;
        } else {
            value = visit(ctx.expression());
        }
        assignValue(name, value);
        return value;
    }

    @Override
    public Object visitExpression(DelphiParser.ExpressionContext ctx) {
        Object result = visit(ctx.relExpression(0));
        for (int i = 1; i < ctx.relExpression().size(); i++) {
            Object rhs = visit(ctx.relExpression(i));
            String op = ctx.getChild(2 * i - 1).getText().toLowerCase();
            boolean leftBool = toBoolean(result);
            boolean rightBool = toBoolean(rhs);
            if (op.equals("and")) {
                result = leftBool && rightBool ? 1 : 0;
            } else if (op.equals("or")) {
                result = leftBool || rightBool ? 1 : 0;
            }
        }
        return result;
    }

    @Override
    public Object visitRelExpression(DelphiParser.RelExpressionContext ctx) {
        Object val = visit(ctx.additiveExpression(0));
        if (ctx.additiveExpression().size() == 1) {
            return val;
        }

        int v1 = toInt(val);
        int v2 = toInt(visit(ctx.additiveExpression(1)));

        if (ctx.GT() != null) return v1 > v2 ? 1 : 0;
        if (ctx.LT() != null) return v1 < v2 ? 1 : 0;
        if (ctx.EQUAL() != null) return v1 == v2 ? 1 : 0;
        if (ctx.LE() != null) return v1 <= v2 ? 1 : 0;
        if (ctx.GE() != null) return v1 >= v2 ? 1 : 0;
        if (ctx.NOT_EQUAL() != null) return v1 != v2 ? 1 : 0;
        return val;
    }

    @Override
    public Object visitAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx) {
        int result = toInt(visit(ctx.term(0)));
        for (int i = 1; i < ctx.term().size(); i++) {
            int nextVal = toInt(visit(ctx.term(i)));
            String op = ctx.getChild(2 * i - 1).getText();
            if (op.equals("+")) result += nextVal;
            else result -= nextVal;
        }
        return result;
    }

    @Override
    public Object visitTerm(DelphiParser.TermContext ctx) {
        int result = toInt(visit(ctx.factor(0)));
        for (int i = 1; i < ctx.factor().size(); i++) {
            int nextVal = toInt(visit(ctx.factor(i)));
            String op = ctx.getChild(2 * i - 1).getText();
            if (op.equals("*")) result *= nextVal;
            else result /= nextVal;
        }
        return result;
    }

    @Override
    public Object visitFactor(DelphiParser.FactorContext ctx) {
        if (ctx.INT_LITERAL() != null) return Integer.parseInt(ctx.INT_LITERAL().getText());
        if (ctx.STRING_LITERAL() != null) return parseStringLiteral(ctx.STRING_LITERAL().getText());
        if (ctx.methodCall() != null) return visit(ctx.methodCall());
        if (ctx.qualifiedIdentifier() != null) return getValue(ctx.qualifiedIdentifier().getText());
        if (ctx.LPAREN() != null) return visit(ctx.expression());
        return 0;
    }

    @Override
    public Object visitIfStatement(DelphiParser.IfStatementContext ctx) {
        if (toBoolean(visit(ctx.expression()))) {
            return visit(ctx.nonEmptyStatement(0));
        }
        if (ctx.ELSE() != null) {
            return visit(ctx.nonEmptyStatement(1));
        }
        return null;
    }

    @Override
    public Object visitWhileStatement(DelphiParser.WhileStatementContext ctx) {
        while (toBoolean(visit(ctx.expression()))) {
            Scope old = currentScope;
            currentScope = new Scope(old);
            try {
                visit(ctx.nonEmptyStatement());
            } catch (ContinueSignal c) {
                // Continue is handled by the loop.
            } catch (BreakSignal b) {
                break;
            } finally {
                currentScope = old;
            }
        }
        return null;
    }

    @Override
    public Object visitForStatement(DelphiParser.ForStatementContext ctx) {
        String loopVar = ctx.identifier().getText().toLowerCase();
        int start = toInt(visit(ctx.expression(0)));
        int end = toInt(visit(ctx.expression(1)));
        boolean isDownTo = ctx.DOWNTO() != null;

        if (isDownTo) {
            for (int i = start; i >= end; i--) {
                assignValue(loopVar, i);
                Scope old = currentScope;
                currentScope = new Scope(old);
                try {
                    visit(ctx.nonEmptyStatement());
                } catch (ContinueSignal c) {
                    // Continue is handled by the loop.
                } catch (BreakSignal b) {
                    break;
                } finally {
                    currentScope = old;
                }
            }
        } else {
            for (int i = start; i <= end; i++) {
                assignValue(loopVar, i);
                Scope old = currentScope;
                currentScope = new Scope(old);
                try {
                    visit(ctx.nonEmptyStatement());
                } catch (ContinueSignal c) {
                    // Continue is handled by the loop.
                } catch (BreakSignal b) {
                    break;
                } finally {
                    currentScope = old;
                }
            }
        }
        return null;
    }

    @Override
    public Object visitBreakStatement(DelphiParser.BreakStatementContext ctx) {
        throw new BreakSignal();
    }

    @Override
    public Object visitContinueStatement(DelphiParser.ContinueStatementContext ctx) {
        throw new ContinueSignal();
    }

    @Override
    public Object visitMethodCall(DelphiParser.MethodCallContext ctx) {
        String name = ctx.qualifiedIdentifier().getText().toLowerCase();
        boolean hasParentheses = ctx.LPAREN() != null;

        if (name.equals("readint")) {
            System.out.print("Enter an integer: ");
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.err.println("Input error, returning default value 0");
                scanner.nextLine();
                return 0;
            }
        }

        if (name.equals("writeln")) {
            if (ctx.expressionList() == null || ctx.expressionList().expression().isEmpty()) {
                System.out.println();
                return null;
            }
            List<String> parts = new ArrayList<>();
            for (DelphiParser.ExpressionContext expr : ctx.expressionList().expression()) {
                Object val = visit(expr);
                parts.add(String.valueOf(val));
            }
            System.out.println(String.join(" ", parts));
            return null;
        }

        if (!hasParentheses && name.contains(".")) {
            String[] parts = name.split("\\.");
            Object obj = currentScope.resolve(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                if (instance.fields.containsKey(parts[1])) {
                    checkFieldAccess(instance, parts[1]);
                    return instance.fields.get(parts[1]);
                }
            }
        }

        if (methodBodies.containsKey(name)) {
            DelphiParser.MethodImplementationContext mCtx = methodBodies.get(name);
            String methodType = getMethodType(mCtx);

            if (methodType.equals("constructor")) {
                String typeName = name.split("\\.")[0];
                ObjectInstance newInstance = new ObjectInstance(typeName);
                initializeFieldsWithInheritance(newInstance);
                validateInterfaceImplementation(typeName);
                return executeMethod(newInstance, name, ctx.expressionList());
            }
        }

        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            Object obj = currentScope.resolve(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                String methodKey = findMethodInHierarchy(instance.typeName, parts[1]);
                if (methodKey != null) {
                    return executeMethod(instance, methodKey, ctx.expressionList(), parts[0]);
                }
                throw new RuntimeException("Error: method not found '" + parts[1] + "'");
            }
        }

        if (routineBodies.containsKey(name)) {
            return executeRoutine(name, ctx.expressionList());
        }

        return getValue(name);
    }

    private int toInt(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private boolean toBoolean(Object value) {
        return toInt(value) != 0;
    }

    private String parseStringLiteral(String raw) {
        if (raw == null || raw.length() < 2) return "";
        return raw.substring(1, raw.length() - 1).replace("''", "'");
    }

    private FoldResult foldExpression(DelphiParser.ExpressionContext ctx) {
        if (ctx == null || ctx.relExpression().isEmpty()) {
            return new FoldResult(false, null, "");
        }

        FoldResult result = foldRelExpression(ctx.relExpression(0));
        for (int i = 1; i < ctx.relExpression().size(); i++) {
            FoldResult rhs = foldRelExpression(ctx.relExpression(i));
            String op = ctx.getChild(2 * i - 1).getText().toLowerCase();

            if (result.isConstant && rhs.isConstant) {
                boolean lv = toBoolean(result.value);
                boolean rv = toBoolean(rhs.value);
                int val = op.equals("and") ? ((lv && rv) ? 1 : 0) : ((lv || rv) ? 1 : 0);
                result = new FoldResult(true, val, String.valueOf(val));
            } else {
                result = new FoldResult(false, null, result.rendered + op + rhs.rendered);
            }
        }

        return result;
    }

    private FoldResult foldRelExpression(DelphiParser.RelExpressionContext ctx) {
        FoldResult left = foldAdditiveExpression(ctx.additiveExpression(0));
        if (ctx.additiveExpression().size() == 1) {
            return left;
        }

        FoldResult right = foldAdditiveExpression(ctx.additiveExpression(1));
        String op = detectRelOp(ctx);

        if (left.isConstant && right.isConstant) {
            int l = toInt(left.value);
            int r = toInt(right.value);
            int val;

            switch (op) {
                case ">":
                    val = (l > r) ? 1 : 0;
                    break;
                case "<":
                    val = (l < r) ? 1 : 0;
                    break;
                case "=":
                    val = (l == r) ? 1 : 0;
                    break;
                case "<=":
                    val = (l <= r) ? 1 : 0;
                    break;
                case ">=":
                    val = (l >= r) ? 1 : 0;
                    break;
                case "<>":
                    val = (l != r) ? 1 : 0;
                    break;
                default:
                    return new FoldResult(false, null, left.rendered + op + right.rendered);
            }

            return new FoldResult(true, val, String.valueOf(val));
        }

        return new FoldResult(false, null, left.rendered + op + right.rendered);
    }

    private FoldResult foldAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx) {
        FoldResult result = foldTerm(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            FoldResult rhs = foldTerm(ctx.term(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (result.isConstant && rhs.isConstant) {
                int l = toInt(result.value);
                int r = toInt(rhs.value);
                int val = op.equals("+") ? (l + r) : (l - r);
                result = new FoldResult(true, val, String.valueOf(val));
            } else {
                result = new FoldResult(false, null, result.rendered + op + rhs.rendered);
            }
        }
        return result;
    }

    private FoldResult foldTerm(DelphiParser.TermContext ctx) {
        FoldResult result = foldFactor(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            FoldResult rhs = foldFactor(ctx.factor(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (result.isConstant && rhs.isConstant) {
                int l = toInt(result.value);
                int r = toInt(rhs.value);

                if (op.equals("*") ) {
                    result = new FoldResult(true, l * r, String.valueOf(l * r));
                } else {
                    if (r == 0) {
                        result = new FoldResult(false, null, result.rendered + "/" + rhs.rendered);
                    } else {
                        int val = l / r;
                        result = new FoldResult(true, val, String.valueOf(val));
                    }
                }
            } else {
                result = new FoldResult(false, null, result.rendered + op + rhs.rendered);
            }
        }
        return result;
    }

    private FoldResult foldFactor(DelphiParser.FactorContext ctx) {
        if (ctx.INT_LITERAL() != null) {
            int value = Integer.parseInt(ctx.INT_LITERAL().getText());
            return new FoldResult(true, value, String.valueOf(value));
        }

        if (ctx.STRING_LITERAL() != null) {
            String value = parseStringLiteral(ctx.STRING_LITERAL().getText());
            return new FoldResult(true, value, "'" + value.replace("'", "''") + "'");
        }

        if (ctx.methodCall() != null) {
            return new FoldResult(false, null, ctx.methodCall().getText());
        }

        if (ctx.qualifiedIdentifier() != null) {
            return new FoldResult(false, null, ctx.qualifiedIdentifier().getText().toLowerCase());
        }

        if (ctx.LPAREN() != null) {
            FoldResult inner = foldExpression(ctx.expression());
            if (inner.isConstant) {
                return inner;
            }
            return new FoldResult(false, null, "(" + inner.rendered + ")");
        }

        return new FoldResult(false, null, "0");
    }

    private String detectRelOp(DelphiParser.RelExpressionContext ctx) {
        if (ctx.GT() != null) return ">";
        if (ctx.LT() != null) return "<";
        if (ctx.EQUAL() != null) return "=";
        if (ctx.LE() != null) return "<=";
        if (ctx.GE() != null) return ">=";
        if (ctx.NOT_EQUAL() != null) return "<>";
        return "";
    }

    private List<Object> evaluateArguments(DelphiParser.ExpressionListContext argsCtx) {
        List<Object> values = new ArrayList<>();
        if (argsCtx == null) return values;
        for (DelphiParser.ExpressionContext expr : argsCtx.expression()) {
            values.add(visit(expr));
        }
        return values;
    }

    private void bindFormalParameters(DelphiParser.FormalParametersContext formalParameters,
                                      List<Object> args,
                                      Scope targetScope) {
        if (formalParameters == null) return;

        int argIndex = 0;
        for (DelphiParser.ParameterGroupContext group : formalParameters.parameterGroup()) {
            for (DelphiParser.IdentifierContext id : group.identifierList().identifier()) {
                Object value = (argIndex < args.size()) ? args.get(argIndex) : 0;
                targetScope.define(id.getText(), value);
                argIndex++;
            }
        }
    }

    private Object executeRoutine(String routineName, DelphiParser.ExpressionListContext argsCtx) {
        DelphiParser.RoutineImplementationContext routineCtx = routineBodies.get(routineName.toLowerCase());
        if (routineCtx == null) {
            throw new RuntimeException("Error: routine not found '" + routineName + "'");
        }

        List<Object> argValues = evaluateArguments(argsCtx);
        Scope oldScope = currentScope;
        ObjectInstance oldSelf = currentSelf;

        currentSelf = null;
        currentScope = new Scope(globalScope);

        try {
            if (routineCtx.varSection() != null) {
                visit(routineCtx.varSection());
            }

            bindFormalParameters(routineCtx.formalParameters(), argValues, currentScope);

            boolean isFunction = routineCtx.FUNCTION() != null;
            String functionName = routineCtx.identifier().getText().toLowerCase();
            if (isFunction && !currentScope.containsLocal(functionName)) {
                currentScope.define(functionName, 0);
            }

            try {
                visit(routineCtx.compoundStatement());
            } catch (BreakSignal | ContinueSignal signal) {
                throw new RuntimeException("Error: break/continue used outside loop in routine '" + routineName + "'");
            }

            if (isFunction) {
                return currentScope.resolve(functionName);
            }
            return null;
        } finally {
            currentScope = oldScope;
            currentSelf = oldSelf;
        }
    }

    private String findMethodInHierarchy(String className, String methodName) {
        List<String> chain = getInheritanceChain(className);
        for (String cls : chain) {
            String methodKey = cls + "." + methodName.toLowerCase();
            if (methodBodies.containsKey(methodKey)) {
                return methodKey;
            }
        }
        return null;
    }

    private void initializeFieldsWithInheritance(ObjectInstance instance) {
        List<String> chain = getInheritanceChain(instance.typeName);
        Collections.reverse(chain);

        for (String className : chain) {
            DelphiParser.ClassTypeContext classDef = classDefinitions.get(className);
            if (classDef == null || classDef.memberList() == null) continue;

            String currentVisibility = "public";
            for (int i = 0; i < classDef.memberList().getChildCount(); i++) {
                var child = classDef.memberList().getChild(i);

                if (child instanceof DelphiParser.VisibilitySpecifierContext) {
                    currentVisibility = child.getText().toLowerCase();
                } else if (child instanceof DelphiParser.MemberContentContext) {
                    DelphiParser.MemberContentContext member = (DelphiParser.MemberContentContext) child;

                    if (member.identifierList() != null) {
                        for (DelphiParser.IdentifierContext id : member.identifierList().identifier()) {
                            String fieldName = id.getText().toLowerCase();

                            if (!instance.fields.containsKey(fieldName)) {
                                instance.fieldVisibility.put(fieldName, currentVisibility);
                                instance.fields.put(fieldName, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private Object executeMethod(ObjectInstance instance, String methodKey, DelphiParser.ExpressionListContext args) {
        return executeMethod(instance, methodKey, args, null);
    }

    private Object executeMethod(ObjectInstance instance,
                                 String methodKey,
                                 DelphiParser.ExpressionListContext args,
                                 String objectVarName) {
        DelphiParser.MethodImplementationContext methodCtx = methodBodies.get(methodKey.toLowerCase());
        if (methodCtx == null) {
            throw new RuntimeException("Error: method implementation not found '" + methodKey + "'");
        }

        List<Object> argValues = evaluateArguments(args);

        ObjectInstance oldSelf = currentSelf;
        Scope oldScope = currentScope;

        currentSelf = instance;
        currentScope = new Scope(globalScope);

        try {
            if (methodCtx.varSection() != null) {
                visit(methodCtx.varSection());
            }

            bindFormalParameters(methodCtx.formalParameters(), argValues, currentScope);

            String methodType = getMethodType(methodCtx);
            String methodName = methodCtx.qualifiedIdentifier().identifier(1).getText().toLowerCase();
            if (methodType.equals("function") && !currentScope.containsLocal(methodName)) {
                currentScope.define(methodName, 0);
            }

            try {
                visit(methodCtx.compoundStatement());
            } catch (BreakSignal | ContinueSignal signal) {
                throw new RuntimeException("Error: break/continue used outside loop in method '" + methodKey + "'");
            }

            switch (methodType) {
                case "constructor":
                    return instance;
                case "destructor":
                    if (objectVarName != null && globalScope.containsInChain(objectVarName)) {
                        globalScope.assign(objectVarName, null);
                    }
                    instance.fields.clear();
                    instance.fieldVisibility.clear();
                    return null;
                case "function":
                    if (currentScope.containsInChain(methodName)) {
                        return currentScope.resolve(methodName);
                    }
                    return 0;
                case "procedure":
                default:
                    return null;
            }
        } finally {
            currentSelf = oldSelf;
            currentScope = oldScope;
        }
    }
}