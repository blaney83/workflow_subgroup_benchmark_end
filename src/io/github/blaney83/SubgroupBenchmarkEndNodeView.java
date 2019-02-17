package io.github.blaney83;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SubgroupBenchmarkEnd" Node.
 * The downstream companion to the Subgroup Benchmark Start Node, this node passes through the input table unaltered and additionally outputs a table with execution times for comparisons between repeated executions. 		
 *
 * @author Benjamin Laney
 */
public class SubgroupBenchmarkEndNodeView extends NodeView<SubgroupBenchmarkEndNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link SubgroupBenchmarkEndNodeModel})
     */
    protected SubgroupBenchmarkEndNodeView(final SubgroupBenchmarkEndNodeModel nodeModel) {
        super(nodeModel);

    }

    @Override
    protected void modelChanged() {

        SubgroupBenchmarkEndNodeModel nodeModel = 
            (SubgroupBenchmarkEndNodeModel)getNodeModel();
        assert nodeModel != null;

    }

    @Override
    protected void onClose() {

    }

    @Override
    protected void onOpen() {

    }

}

