package org.example;

import java.util.*;

public class DelphiInterpreter extends DelphiBaseVisitor<Object> {
    private Map<String, Object> globals = new HashMap<>();
    private Map<String, DelphiParser.MethodImplementationContext> methodBodies = new HashMap<>();
    private Map<String, DelphiParser.ClassTypeContext> classDefinitions = new HashMap<>();
    private Map<String, DelphiParser.InterfaceTypeContext> interfaceDefinitions = new HashMap<>();
    private Map<String, String> classInheritance = new HashMap<>();
    private Map<String, List<String>> classInterfaces = new HashMap<>();

    private ObjectInstance currentSelf = null;
    private Scanner scanner = new Scanner(System.in);

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
    public Object visitTypeSection(DelphiParser.TypeSectionContext ctx) {
        for (DelphiParser.TypeDefinitionContext typeDef : ctx.typeDefinition()) {
            String typeName = typeDef.identifier().getText().toLowerCase();

            if (typeDef.classType() != null) {
                DelphiParser.ClassTypeContext classCtx = typeDef.classType();
                classDefinitions.put(typeName, classCtx);

                // Distinguish inheritance from interface lists.
                // Grammar: class(TParent) or class(IInterface1, IInterface2).
                // With one identifier, it may be a parent class or an interface.
                // With multiple identifiers, the first may be a parent class and the rest are interfaces.

                if (classCtx.identifier() != null) {
                    // Single inheritance: class(TParent)
                    String parentClass = classCtx.identifier().getText().toLowerCase();
                    classInheritance.put(typeName, parentClass);
                    System.out.println("Class '" + typeName + "' extends/implements '" + parentClass + "'");
                }

                if (classCtx.interfaceList() != null) {
                    // Interface list: class(IInterface1, IInterface2) or class(TParent, IInterface1)
                    List<DelphiParser.IdentifierContext> identifiers = classCtx.interfaceList().identifier();

                    if (!identifiers.isEmpty()) {
                        List<String> interfaces = new ArrayList<>();

                        // The first identifier can be a parent class.
                        // If classCtx.identifier() is set, all identifiers here are interfaces.
                        // Otherwise, the first may be a parent class.

                        int startIndex = 0;

                        // If there is no explicit parent and there are multiple identifiers,
                        // check whether the first is a class (not an interface).
                        if (classCtx.identifier() == null && identifiers.size() > 1) {
                            // The first may be a parent class; check if it is defined as a class.
                            String firstId = identifiers.get(0).getText().toLowerCase();
                            if (classDefinitions.containsKey(firstId) || !interfaceDefinitions.containsKey(firstId)) {
                                // The first is a parent class.
                                classInheritance.put(typeName, firstId);
                                System.out.println("Class '" + typeName + "' extends/implements '" + firstId + "'");
                                startIndex = 1; // Start from the second item; the rest are interfaces.
                            }
                        }

                        // Collect interfaces.
                        for (int i = startIndex; i < identifiers.size(); i++) {
                            String interfaceName = identifiers.get(i).getText().toLowerCase();
                            interfaces.add(interfaceName);
                        }

                        if (!interfaces.isEmpty()) {
                            classInterfaces.put(typeName, interfaces);
                            System.out.println("Class '" + typeName + "' implements interfaces: " + interfaces);
                        }
                    }
                }
            }

            if (typeDef.interfaceType() != null) {
                interfaceDefinitions.put(typeName, typeDef.interfaceType());
                System.out.println("Defined interface '" + typeName + "'");
            }
        }
        return null;
    }

    @Override
    public Object visitImplementationSection(DelphiParser.ImplementationSectionContext ctx) {
        for (DelphiParser.MethodImplementationContext method : ctx.methodImplementation()) {
            visit(method);
        }
        return null;
    }

    @Override
    public Object visitMethodImplementation(DelphiParser.MethodImplementationContext ctx) {
        String fullName = ctx.qualifiedIdentifier().getText().toLowerCase();
        methodBodies.put(fullName, ctx);
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

            if (chain.contains(current)) {
                System.err.println("Warning: circular inheritance detected: " + chain);
                break;
            }
        }

        return chain;
    }

    private boolean implementsInterface(String className, String interfaceName) {
        List<String> chain = getInheritanceChain(className);

        for (String cls : chain) {
            List<String> interfaces = classInterfaces.get(cls);
            if (interfaces != null && interfaces.contains(interfaceName.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private void validateInterfaceImplementation(String className) {
        List<String> interfaces = classInterfaces.get(className.toLowerCase());
        if (interfaces == null || interfaces.isEmpty()) return;

        for (String interfaceName : interfaces) {
            DelphiParser.InterfaceTypeContext interfaceDef = interfaceDefinitions.get(interfaceName);
            if (interfaceDef == null) {
                System.err.println("Warning: interface '" + interfaceName + "' is not defined");
                continue;
            }

            if (interfaceDef.interfaceMemberList() == null) continue;

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

    private Object getValue(String name) {
        name = name.toLowerCase();
        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            Object obj = globals.get(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                checkFieldAccess(instance, parts[1]);
                return instance.fields.getOrDefault(parts[1], 0);
            }
        }
        if (currentSelf != null && currentSelf.fields.containsKey(name)) {
            return currentSelf.fields.get(name);
        }
        return globals.getOrDefault(name, 0);
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
                if (currentSelf != null) {
                    if (currentSelf == instance || isSameClassOrSubclass(currentSelf, instance)) {
                        break;
                    }
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
        Object value = visit(ctx.expression());

        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            Object obj = globals.get(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                checkFieldAccess(instance, parts[1]);
                instance.fields.put(parts[1], value);
            }
        } else if (currentSelf != null) {
            currentSelf.fields.put(name, value);
        } else {
            globals.put(name, value);
        }
        return value;
    }

    @Override
    public Object visitExpression(DelphiParser.ExpressionContext ctx) {
        return visit(ctx.relExpression(0));
    }

    @Override
    public Object visitRelExpression(DelphiParser.RelExpressionContext ctx) {
        Object val = visit(ctx.additiveExpression(0));
        if (ctx.additiveExpression().size() > 1) {
            int v1 = (int) val;
            int v2 = (int) visit(ctx.additiveExpression(1));
            if (ctx.GT() != null) return v1 > v2 ? 1 : 0;
            if (ctx.LT() != null) return v1 < v2 ? 1 : 0;
            if (ctx.EQUAL() != null) return v1 == v2 ? 1 : 0;
        }
        return val;
    }

    @Override
    public Object visitAdditiveExpression(DelphiParser.AdditiveExpressionContext ctx) {
        Object result = visit(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            int nextVal = (int) visit(ctx.term(i));
            String op = ctx.getChild(2 * i - 1).getText();
            if (op.equals("+")) result = (int) result + nextVal;
            else result = (int) result - nextVal;
        }
        return result;
    }

    @Override
    public Object visitTerm(DelphiParser.TermContext ctx) {
        Object result = visit(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            int nextVal = (int) visit(ctx.factor(i));
            String op = ctx.getChild(2 * i - 1).getText();
            if (op.equals("*")) result = (int) result * nextVal;
            else result = (int) result / nextVal;
        }
        return result;
    }

    @Override
    public Object visitFactor(DelphiParser.FactorContext ctx) {
        if (ctx.INT_LITERAL() != null) return Integer.parseInt(ctx.INT_LITERAL().getText());
        if (ctx.methodCall() != null) return visit(ctx.methodCall());
        if (ctx.qualifiedIdentifier() != null) return getValue(ctx.qualifiedIdentifier().getText());
        if (ctx.LPAREN() != null) return visit(ctx.expression());
        return 0;
    }

    @Override
    public Object visitIfStatement(DelphiParser.IfStatementContext ctx) {
        Object condition = visit(ctx.expression());
        int condValue = (condition instanceof Integer) ? (int) condition : 0;

        if (condValue != 0) {
            return visit(ctx.nonEmptyStatement(0));
        } else if (ctx.ELSE() != null) {
            return visit(ctx.nonEmptyStatement(1));
        }
        return null;
    }

    @Override
    public Object visitMethodCall(DelphiParser.MethodCallContext ctx) {
        String name = ctx.qualifiedIdentifier().getText().toLowerCase();

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
            Object val = (ctx.expressionList() != null) ? visit(ctx.expressionList().expression(0)) : "";
            System.out.println(val);
            return null;
        }

        boolean hasParentheses = (ctx.LPAREN() != null);

        if (!hasParentheses && name.contains(".")) {
            String[] parts = name.split("\\.");
            Object obj = globals.get(parts[0]);
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
            Object obj = globals.get(parts[0]);
            if (obj instanceof ObjectInstance) {
                ObjectInstance instance = (ObjectInstance) obj;
                String methodKey = findMethodInHierarchy(instance.typeName, parts[1]);

                if (methodKey != null) {
                    return executeMethod(instance, methodKey, ctx.expressionList(), parts[0]);
                } else {
                    System.err.println("Warning: method not found '" + parts[1] + "'");
                    return null;
                }
            }
        }

        return getValue(name);
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

    private Object executeMethod(ObjectInstance instance, String methodKey,
                                 DelphiParser.ExpressionListContext args, String objectVarName) {
        DelphiParser.MethodImplementationContext methodCtx = methodBodies.get(methodKey.toLowerCase());
        if (methodCtx == null) {
            System.err.println("Warning: method implementation not found '" + methodKey + "'");
            return null;
        }

        ObjectInstance oldSelf = this.currentSelf;
        this.currentSelf = instance;

        if (args != null && methodCtx.formalParameters() != null) {
            List<DelphiParser.ParameterGroupContext> paramGroups = methodCtx.formalParameters().parameterGroup();
            if (!paramGroups.isEmpty()) {
                List<DelphiParser.IdentifierContext> paramIds = paramGroups.get(0).identifierList().identifier();
                if (!paramIds.isEmpty()) {
                    String paramName = paramIds.get(0).getText().toLowerCase();
                    Object argValue = visit(args.expression(0));
                    instance.fields.put(paramName, argValue);
                }
            }
        }

        visit(methodCtx.compoundStatement());

        Object result = null;
        String methodType = getMethodType(methodCtx);

        switch (methodType) {
            case "constructor":
                result = instance;
                break;

            case "destructor":
                if (objectVarName != null) {
                    globals.remove(objectVarName.toLowerCase());
                    System.out.println("Object '" + objectVarName + "' destroyed");
                } else {
                    System.out.println("Destructor '" + methodKey + "' executed");
                }
                instance.fields.clear();
                instance.fieldVisibility.clear();
                result = null;
                break;

            case "function":
                String funcName = methodCtx.qualifiedIdentifier().getText()
                        .split("\\.")[1].toLowerCase();
                result = instance.fields.get(funcName);

                if (result == null) {
                    System.err.println("Warning: function '" + funcName + "' has no return value set");
                    result = 0;
                }
                break;

            case "procedure":
                result = null;
                break;
        }

        this.currentSelf = oldSelf;
        return result;
    }
}