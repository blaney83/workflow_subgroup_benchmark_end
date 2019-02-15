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

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        SubgroupBenchmarkEndNodeModel nodeModel = 
            (SubgroupBenchmarkEndNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

