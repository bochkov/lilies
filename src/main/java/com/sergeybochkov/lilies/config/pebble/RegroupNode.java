package com.sergeybochkov.lilies.config.pebble;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.sergeybochkov.lilies.model.Music;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegroupNode extends AbstractRenderableNode {

    private Expression<?> srcList;
    private String outName;
    private RegroupComparator comparator = new RegroupComparator();

    public RegroupNode(Expression<?> srcList, String outName) {
        this.srcList = srcList;
        this.outName = outName;
    }

    @Override
    public void render(PebbleTemplateImpl tmpl, Writer writer, EvaluationContext ctx) throws PebbleException, IOException {
        List<RegroupList> list = new ArrayList<>();

        Object evaluated = srcList.evaluate(tmpl, ctx);
        List<Object> objList = new ArrayList<>();
        if (evaluated instanceof List) {
            List<?> evList = (List<?>) evaluated;
            objList.addAll(evList.stream().collect(Collectors.toList()));
        }

        for (Object obj : objList) {
            String grouper = "_";
            if (obj instanceof Music) {
                Music m = (Music) obj;
                grouper = m.getName().substring(0, 1);
            }

            boolean founded = false;
            for (RegroupList rl : list) {
                if (rl.getGrouper().equals(grouper)) {
                    rl.addToList(obj);
                    founded = true;
                    break;
                }
            }
            if (!founded) {
                RegroupList rl = new RegroupList();
                rl.setGrouper(grouper);
                rl.addToList(obj);
                list.add(rl);
            }
        }
        list.sort(comparator);
        ctx.getScopeChain().put(outName, list);
        //ctx.put(outName, list);
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
    }

    private class RegroupComparator implements Comparator<RegroupList> {
        @Override
        public int compare(RegroupList o1, RegroupList o2) {
            if (o1 != null && o2 != null)
                return o1.getGrouper().compareTo(o2.getGrouper());

            return 0;
        }
    }
}
