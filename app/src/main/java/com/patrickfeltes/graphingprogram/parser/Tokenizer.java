package com.patrickfeltes.graphingprogram.parser;

import com.patrickfeltes.graphingprogram.parser.exceptions.InvalidExpressionException;
import com.patrickfeltes.graphingprogram.parser.tokens.*;

import static com.patrickfeltes.graphingprogram.parser.tokens.Reserved.*;

/**
 * The Tokenizer takes a String expression and splits it up into individual tokens for the parser
 * to use.
 */
public class Tokenizer {

    private static final char EMPTY_CHAR = '\u0000';
    private static final char SPACE = ' ';
    private String string;
    private int currentPosition;
    private char currentChar;

    public Tokenizer(String string) {
        this.string = string;
        this.currentPosition = 0;
        this.currentChar = string.charAt(currentPosition);
    }

    /**
     * Grabs the next token from the input String
     * @return The Tokens.Token object of the next token
     */
    public Token getNextToken() throws InvalidExpressionException {
        while (currentChar != EMPTY_CHAR) {
            if (currentChar == SPACE) {
                skipWhitespace();
            }

            if (Character.isAlphabetic(currentChar)) {
                StringBuilder tempBuffer = new StringBuilder();
                while (Character.isAlphabetic(currentChar)) {
                    tempBuffer.append(currentChar);
                    advance();
                }
                String variableName = tempBuffer.toString();
                String lowerCaseName = variableName.toLowerCase();
                if (lowerCaseName.equals(SIN) || lowerCaseName.equals(COS) ||
                        lowerCaseName.equals(TAN) || lowerCaseName.equals(CSC) ||
                        lowerCaseName.equals(SEC) || lowerCaseName.equals(COT) ||
                        lowerCaseName.equals(LN) ||
                        lowerCaseName.equals(SQRT) || lowerCaseName.equals(COSH)
                        || lowerCaseName.equals(SINH) || lowerCaseName.equals(TANH)
                        || lowerCaseName.equals(SECH) || lowerCaseName.equals(CSCH)
                        || lowerCaseName.equals(COTH) || lowerCaseName.equals(ARCCOS)
                        || lowerCaseName.equals(ARCSIN) || lowerCaseName.equals(ARCTAN)
                        || lowerCaseName.equals(ARCSEC) || lowerCaseName.equals(ARCCSC)
                        || lowerCaseName.equals(ARCCOT)) {
                    return new Token(TokenType.FUNC, lowerCaseName);
                } else if (lowerCaseName.equals(PI)) {
                    return new Token(TokenType.DOUBLE, Math.PI);
                } else if (lowerCaseName.equals(E)) {
                    return new Token(TokenType.DOUBLE, Math.E);
                }

                return new Token(TokenType.VAR, variableName);
            }

            if (currentChar == PLUS) {
                advance();
                return new Token(TokenType.PLUS, PLUS);
            }

            if (currentChar == MINUS) {
                advance();
                return new Token(TokenType.MINUS, MINUS);
            }

            if (currentChar == MUL) {
                advance();
                return new Token(TokenType.MUL, MUL);
            }

            if (currentChar == DIV) {
                advance();
                return new Token(TokenType.DIV, DIV);
            }

            if (currentChar == LPAREN) {
                advance();
                return new Token(TokenType.LPAREN, LPAREN);
            }

            if (currentChar == RPAREN) {
                advance();
                return new Token(TokenType.RPAREN, RPAREN);
            }

            if (currentChar == POW) {
                advance();
                return new Token(TokenType.POW, POW);
            }

            if (Character.isDigit(currentChar)) {
                return getNumber();
            }

            throw new InvalidExpressionException();
        }

        return new Token(TokenType.EOL, null);
    }

    private Token getNumber() throws InvalidExpressionException {
        StringBuilder value = new StringBuilder();
        boolean hasDecimal = false;
        while (Character.isDigit(currentChar)) {
            value.append(currentChar);
            advance();
            if (currentChar == '.') {
                if (hasDecimal) throw new InvalidExpressionException(); // two decimal points in the number
                hasDecimal = true;
                value.append(currentChar);
                advance();
            }
        }

        return new Token(TokenType.DOUBLE, Double.parseDouble(value.toString()));
    }

    /**
     * Method to advance to the next character in the String
     */
    private void advance() {
        currentPosition++;
        if (currentPosition >= string.length()) {
            currentChar = EMPTY_CHAR;
        } else {
            currentChar = string.charAt(currentPosition);
        }
    }

    /**
     * A method to continue advancing until whitespace is gone
     */
    private void skipWhitespace() {
        while (currentChar != EMPTY_CHAR && currentChar == SPACE) {
            advance();
        }
    }
}
