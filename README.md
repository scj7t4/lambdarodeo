# Lambda.Rodeo
Lambda.Rodeo is a compiled, strongly-typed functional programming language for the JVM. It intends to have a clear, simple syntax with an advanced type checker.

## Language Overview

Lambda Rodeo uses `.rdo` as its file extension. Files should be organized in the usual Java fashion. The Lambda.Rodeo compiler automatically detects what the package
is from the project organization, so files can be quite simple:

```
def helloWorld() => String {
  "hello world"
}
```

The language still needs a standard library and a REPL, so you can't do basic things like print to the screen yet. However, it does support the following things so far:

### Atoms

You can use atoms, which are both a value and a type by doing `@someCoolNameForYourAtom`

### Interfaces

Lambda.Rodeo supports javascript style objects. Right now, they need to have a fixed set of members declared ahead of time. You can give interfaces a name, or use them
anonymously:

```
type MyCoolInterface => {
  member1: Int;
  member2: @null;
};

def test2() => {hello: String} {
  {
    hello: "world!";
  };
}
```

And of course, you can access members in interfaces:

```
type MyCoolInterface => {
  member1: Int;
  member2: @null;
};

def test(var: MyCoolInterface) => Int {
  var.member1;
}
```


### Imports

You can reference functions in other Lambda.Rodeo modules:

```
import testcase.BasicFunctionCall;

def callAndAdd() => Int {
  3 + BasicFunctionCall.twoptwo();
}
```

and those imports can be aliased:

```
import testcase.BasicFunctionCall as BFC;

def callAndAdd() => Int {
  3 + BFC.twoptwo();
}
```

### Pattern Matching

It supports pattern matching. Currently matches on equality (both literal and variable) and type:

```
def fibonacci(n: Int) => Int {
  case (1) {
    1;
  }
  case (0) {
    1;
  }
  case (*) {
    fibonacci(n, 2, 1, 1);
  }
}

def fibonacci(n: Int, current: Int, fn1: Int, fn2: Int) => Int {
  case(n, n, *, *) {
    fn1 + fn2;
  }
  case(*, *, *, *) {
    fibonacci(n, current + 1, fn1 + fn2, fn1);
  }
}
```

```
def somefunc(arg0: Int) => Int {
  case (1) {
    1;
  }
  case (0) {
    0;
  }
}
```

```
def stringOrInt(arg: Combo) => @zero | @notZero {
  case(0) {
    @zero;
  }
  case(*) {
    @notZero;
  }
}
```

### Types

Lambda.Rodeo currently supports arbitrary width integers, Strings, interfaces, and union types.
