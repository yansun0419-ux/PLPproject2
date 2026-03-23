# Delphi Interpreter (Project 1)

## Overview
This project extends a Pascal grammar to support Delphi-style object-oriented features (classes, constructors, destructors, encapsulation, inheritance, and interfaces) and provides a Java interpreter that walks the AST produced by ANTLR4.

---

## Features Implemented
We have successfully implemented **100% of the core requirements and all BONUS features**, validated by a comprehensive suite of automated tests.

### Core Requirements (✓)
- **Classes and Objects:** Full support for class declarations, instantiation (`Create`), and method dispatch.
- **Constructors and Destructors:** Memory management simulation, including field initialization and object destruction.
- **Encapsulation:** Strict access control validation for `PUBLIC`, `PRIVATE`, `PROTECTED`, and `PUBLISHED` modifiers.
- **Input/Output:** Built-in `ReadInt()` and `WriteLn()` for console interaction.

### BONUS Features (✓)
- **Inheritance:** Single inheritance support with method overriding and hierarchical field initialization.
- **Interfaces:** Multi-interface implementation with strict validation of method signatures during object creation.

**Showcase: Combined Inheritance & Interfaces**
*(See `test_inheritance_interface.pas` for the full executable test)*
```pascal
type
  TAnimal = class
  protected
    speed: Integer;
  end;

  // Bonus: Inherits from TAnimal AND implements IMovable
  TBird = class(TAnimal, IMovable)  
  public
    procedure Move;
  end;
```

## Project Structure

### Core Files
- **Grammar:** `src/main/antlr4/org/example/Delphi.g4`
  - Extends Pascal grammar with class, interface, constructor, destructor, and visibility keywords
  - Defines syntax for inheritance and interface implementation
  
- **Interpreter:** `src/main/java/org/example/DelphiInterpreter.java`
  - Visitor pattern walks the ANTLR-generated AST
  - Manages object lifecycle, field access control, method dispatch, and inheritance
  
- **Test Runner:** `src/main/java/org/example/Main.java`
  - Batch-runs all test files
  - Reports parse success/failure and runtime errors

### Test Files
| File | Tests |
|------|-------|
| `test_1.pas` | Basic class with constructor, method calls, and function |
| `test_class_object.pas` | Class instantiation and field manipulation |
| `test_constructor_destructor.pas` | Constructor parameters and destructor execution |
| `test_encapsulation.pas` | Private field access through public methods |
| `test_access_control.pas` | Private, protected, and public field visibility |
| `test_io.pas` | ReadInt() and WriteLn() functionality |
| `test_inheritance.pas` | Parent-child class relationship and method overriding |
| `test_interface.pas` | Interface definition and implementation |
| `test_inheritance_interface.pas` | Combined inheritance and interface implementation |
| `test_full_program.pas` | Full integration: class, constructor, method, field access |

---

## How To Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
Make sure you have Java (JDK) and Maven installed. From the project root directory (where `pom.xml` is located), run the following commands:

```bash
# Compile the grammar and Java sources
mvn clean compile

# Run the batch tests
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Expected Output Format
For each test file, the runner prints:
```
----------------------------------------------
Test file: test_name.pas
----------------------------------------------
Parse succeeded
>>> Program output:
-----------------------
[program output from WriteLn calls]
-----------------------
Status: PASSED
```

---

## Implementation Details

### Key Data Structures
- `classDefinitions`: Maps class names to their parsed AST nodes
- `interfaceDefinitions`: Maps interface names to their method signatures
- `classInheritance`: Maps child classes to their parent classes
- `classInterfaces`: Maps classes to the interfaces they implement
- `methodBodies`: Maps fully-qualified method names (e.g., "tbox.init") to their implementation AST nodes
- `globals`: Maps variable names to their runtime values
- `currentSelf`: Tracks the current object context during method execution

### Method Resolution Algorithm
1. When calling `object.method()`, first check if the method exists in the object's class
2. If not found, search up the inheritance chain using `findMethodInHierarchy()`
3. Execute the method with `currentSelf` set to the object instance
4. For constructors, create and return a new `ObjectInstance`
5. For destructors, clean up object state and remove from globals

### Field Initialization
1. Constructor creates a new `ObjectInstance`
2. `initializeFieldsWithInheritance()` walks the inheritance chain from root to child
3. For each class in the chain, iterate through its fields and initialize them with default values (0 for integers)
4. Track each field's visibility for later access control checks

### Interface Validation
1. When a class declares interface implementation, store the interface list in `classInterfaces`
2. After class definition, `validateInterfaceImplementation()` checks:
   - Interface is defined in `interfaceDefinitions`
   - Each interface method has a corresponding implementation in the class or its parents
3. Throw runtime error if any interface method is missing

---

## Known Limitations
- Only integer types are fully supported
- String literals are recognized but not fully operational
- No support for arrays, records, or advanced Pascal features
- Single inheritance only (Delphi standard)

---

## Conclusion
This interpreter successfully implements all core object-oriented features (classes, constructors, destructors, encapsulation) and both bonus features (inheritance, interfaces). All test cases pass and demonstrate the functionality through terminal I/O operations.
