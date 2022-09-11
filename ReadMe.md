# Esoteric

Transpiles esoteric languages to more verbose languages as described in the following table:

|**Transpiles to**| Brainfuck  | Ook! | C   | Java | Python | JS  |
|:---------------:|:---:|:---:|:---:|:----:|:------:|:---:|
|**Brainfuck** [^1]| 😂  | ✅  | ✅  | ✅   | ✅    | ❌ |
|**Ook!** [^2]          | ✅  | 😂  | ✅  | ✅   | ✅    |❌  |
|**JSFuck** [^3]       | ❌  | ❌  | ❌  | ❌   | ❌    | ✅  |

*Note 1*: `JSFuck` can only be deobfuscated since there's no point converting JS to different languages.

*Note 2*: `Brainfuck` and `Ook!` can't be transpiled into `JS` because of IO commands.

---

## Automatic Transpilation

### Loading data

Before the data can be given to the lexer/parser/transpilers, they need to be loaded into a `CharStream` instance, as such:

```java
// From a String
CharStream stream = CharStream.of(stringVariable);
// From a File
CharStream stream = CharStream.of(new File(filePath));
```

*Note*: In the subsequent codeblocks, every reference to `stream` refers to an instance of `CharStream`.

### Transpiling

The `Esoteric` interface defines two enum classes called `Input` and `Output` which relate to the input languages that can be interpreted and the output languages that can be transpiled into. The following is an example of transpiling `Brainfuck` into `Python`:

```java
StringFormatBuilder result = Esoteric.transpile(stream, Input.Brainfuck, Output.Python);
```

*Note*: A transpilation result is always stored inside a `StringFormatBuilder` which is an extended version of `StringBuilder`.

---

## Manual Transpilation Example

### Brainfuck Transpilation

After creating the `CharStream`, you can then transpile into any target (in this example `Java`).
Let's suppose we have the following "hello world" `Brainfuck` code:

```bf
>++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<+
+.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-
]<+.
```

You can then proceed to transpile it as such:

```java
Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
BFParser parser = new BFParser(lexer);
BFOptimiser optimiser = new BFOptimiser();
AST optimised = optimiser.visit(parser.parse());
JavaTranspiler transpiler = new JavaTranspiler();
System.out.println(transpiler.transpile(optimised));
```

Which prints the following [Java code](https://pastebin.com/fq2dmfyn)

### JSFuck Deobfuscation

Let's suppose we want to deobfuscate the following "Hello World" `JSFuck` code [here](https://pastebin.com/55t5TeQn).
We can deobfuscate it as such:

```java
Lexer<JSFuck> lexer = new Lexer<>(stream, JSFuck.class);
JSFuckParser parser = new JSFuckParser(lexer);
JSFuckOptimiser optimiser = new JSFuckOptimiser();
AST ast = optimiser.visit(parser.parse());
JSFuckDeobfuscator transpiler = new JSFuckDeobfuscator();
System.out.println(transpiler.transpile(ast));
```
Which prints:
```js
[].flat.constructor("return eval")()("alert(\"Hello, world!\")")
```

---

## Transpiler Classes Location

- `Brainfuck` transpilers can be found in `esoteric.brainfuck.transpiler`
- `Ook!` transpilers can be found in `esoteric.ook.transpiler` (since `Ook!` uses the same Abstract Syntax Tree as `Brainfuck` the same transpilers can be used)
- `JSFuck` deobfuscator can be found in `esoteric.jsfuck.transpiler`

---

# Reference
[^1]: https://esolangs.org/wiki/Brainfuck
[^2]: https://esolangs.org/wiki/Ook
[^3]: https://esolangs.org/wiki/JSFuck
