package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.List;

import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.FilterInvocationExpression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public final class RegroupParser implements TokenParser {

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
        String field = parser.getExpressionParser().parseNewVariableName(); // String name
        stream.next(); // skip operator
        List<FilterInvocationExpression> filters = new ArrayList<>();
        filters.add(parser.getExpressionParser().parseFilterInvocationExpression()); // FilterInvocationExpression filter
        while (stream.current().test(Token.Type.OPERATOR, "|")) {
            stream.next(); // skip the '|' token
            filters.add(parser.getExpressionParser().parseFilterInvocationExpression());
        }
        stream.expect(Token.Type.NAME, "as");
        String outName = parser.getExpressionParser().parseNewVariableName();
        stream.expect(Token.Type.EXECUTE_END);
        return new RegroupNode(srcList, field, filters, outName, stream.getFilename(), token.getLineNumber());
    }
}
