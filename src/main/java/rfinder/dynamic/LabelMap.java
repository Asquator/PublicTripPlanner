package rfinder.dynamic;

import rfinder.structures.nodes.PathNode;

public interface LabelMap {
    MultilabelBag getLabelBag(PathNode node);
}
