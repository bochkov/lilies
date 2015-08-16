package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.FilterInvocationExpression;
import com.mitchellbosecke.pebble.tokenParser.AbstractTokenParser;

public class RegroupToken extends AbstractTokenParser {

    @Override
    public String getTag() {
        return "regroup";
    }

    @Override
    public RenderableNode parse(Token token) throws ParserException {
        TokenStream stream = this.parser.getStream();

        stream.next(); // skip the "regroup" token

        Expression<?> srcList = this.parser.getExpressionParser().parseExpression();
        stream.expect(Token.Type.NAME, "by");
        String name = this.parser.getExpressionParser().parseNewVariableName();

        stream.next(); // skip operator
        FilterInvocationExpression filter = this.parser.getExpressionParser().parseFilterInvocationExpression();

        stream.expect(Token.Type.NAME, "as");
        String outName = this.parser.getExpressionParser().parseNewVariableName();
        stream.expect(Token.Type.EXECUTE_END);

        return new RegroupNode(srcList, name, filter, outName);
    }
}
