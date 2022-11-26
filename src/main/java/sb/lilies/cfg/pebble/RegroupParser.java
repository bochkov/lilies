package sb.lilies.cfg.pebble;

import java.util.ArrayList;
import java.util.List;

import io.pebbletemplates.pebble.error.ParserException;
import io.pebbletemplates.pebble.lexer.Token;
import io.pebbletemplates.pebble.lexer.TokenStream;
import io.pebbletemplates.pebble.node.RenderableNode;
import io.pebbletemplates.pebble.node.expression.Expression;
import io.pebbletemplates.pebble.node.expression.FilterInvocationExpression;
import io.pebbletemplates.pebble.parser.Parser;
import io.pebbletemplates.pebble.tokenParser.TokenParser;

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
