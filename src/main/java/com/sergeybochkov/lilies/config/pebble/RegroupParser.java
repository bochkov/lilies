package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.AbstractTokenParser;

public final class RegroupParser extends AbstractTokenParser {

    @Override
    public String getTag() {
        return "regroup";
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        stream.next(); // skip the "regroup" token
        Expression<?> srcList = parser.getExpressionParser().parseExpression();
        stream.expect(Token.Type.NAME, "by");
        parser.getExpressionParser().parseNewVariableName(); // String name
        stream.next(); // skip operator
        parser.getExpressionParser().parseFilterInvocationExpression(); // FilterInvocationExpression filter
        stream.expect(Token.Type.NAME, "as");
        String outName = parser.getExpressionParser().parseNewVariableName();
        stream.expect(Token.Type.EXECUTE_END);
        return new RegroupNode(srcList, outName);
    }
}
