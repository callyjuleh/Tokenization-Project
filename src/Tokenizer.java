import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Tokenizer {
    public List<String> keywords;
    public List<String> accessModifiers;
    public List<String> arithmeticOperators;
    public List<String> unaryOperators;
    public List<String> assignmentOperators;
    public List<String> relationalOperators;
    public List<String> logicalOperators;
    public List<String> bitwiseOperators;
    public List<String> shiftOperators;
    public List<String> ternaryOperators;
    public List<String> instanceofOperators;

    public List<String> separators;

    public Tokenizer() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            keywords = mapper.readValue(
                    new File("db/keywords.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            accessModifiers = mapper.readValue(
                    new File("db/access_modifiers.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            arithmeticOperators = mapper.readValue(
                    new File("db/arithmetic_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            unaryOperators = mapper.readValue(
                    new File("db/unary_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            assignmentOperators = mapper.readValue(
                    new File("db/assignment_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            relationalOperators = mapper.readValue(
                    new File("db/relational_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            logicalOperators = mapper.readValue(
                    new File("db/logical_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            bitwiseOperators = mapper.readValue(
                    new File("db/bitwise_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            shiftOperators = mapper.readValue(
                    new File("db/shift_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            ternaryOperators = mapper.readValue(
                    new File("db/ternary_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            instanceofOperators = mapper.readValue(
                    new File("db/instanceof_operators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            separators = mapper.readValue(
                    new File("db/separators.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public List<String> tokenize(String command) {
        List<String> tokens = new ArrayList<>();

        String tokenRegex = "//\\s*.*" + // single-line comment
                "|/\\*[\\s\\S]*?\\*/" + // complete block comment
                "|/\\*[\\s\\S]*$" + // unclosed block comment
                "|\\*/" + // closing
                "|\"(\\\\.|[^\"])*\"" + // string literal
                "|'(\\\\.|[^'])'" + // char literal
                "|[a-zA-Z_$][a-zA-Z0-9_$]*|\\d+[a-zA-Z0-9_$]*|\\d+\\.\\d+" + // identifiers + invalid + numbers
                "|==|!=|>=|<=|&&|\\|\\||\\+\\+|--|\\+=|-=|\\*=|/=|%=|->" + // multi-character operators
                "|[+\\-*/%=<>&|!^~?:]" + // single-character operators
                "|[{}()\\[\\];,.]"; // separators

        Pattern pattern = Pattern.compile(tokenRegex);
        Matcher matcher = pattern.matcher(command);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    public String getTokenType(String token) {
        if (keywords.contains(token)) {
            return "Keyword";
        }
        if (accessModifiers.contains(token)) {
            return "Access Modifier";
        }
        if (arithmeticOperators.contains(token)) {
            return "Arithmetic Operator";
        }
        if (unaryOperators.contains(token)) {
            return "Unary Operator";
        }
        if (assignmentOperators.contains(token)) {
            return "Assignment Operator";
        }
        if (relationalOperators.contains(token)) {
            return "Relational Operator";
        }
        if (logicalOperators.contains(token)) {
            return "Logical Operator";
        }
        if (bitwiseOperators.contains(token)) {
            return "Bitwise Operator";
        }
        if (shiftOperators.contains(token)) {
            return "Shift Operator";
        }
        if (ternaryOperators.contains(token)) {
            return "Ternary Operator";
        }
        if (instanceofOperators.contains(token)) {
            return "Instanceof Operator";
        }

        if (separators.contains(token)) {
            return "Separator";
        }

        if (token.startsWith("\"") && token.endsWith("\"") && token.length() >= 2) {
            return "String Literal";
        }
        if (token.startsWith("'") && token.endsWith("'") && token.length() == 3) {
            return "Char Literal";
        }
        if (token.matches("\\d+(\\.\\d+)?")) {
            return "Numeric Literal";
        }
        if (token.equals("true") || token.equals("false")) {
            return "Boolean Literal";
        }
        if (token.matches("null")) {
            return "Literal";
        }

        if ((Character.isLetter(token.charAt(0)) || token.charAt(0) == '_' || token.charAt(0) == '$')) {
            boolean valid = true;

            for (int i = 1; i < token.length(); i++) {
                char c = token.charAt(i);
                if (!(Character.isLetterOrDigit(c) || c == '_' || c == '$')) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return "Identifier (Valid)";
            }
        }

        return "Identifier (Invalid)";
    }
}
