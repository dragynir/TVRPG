class NodeInitialData{
    private String parentNodeHost;
    private int parentNodePort;
    private int nodePort;
    private String nodeName;
    private int lossPercentage;
    private boolean isRoot;

    public NodeInitialData(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public String getParentNodeHost() {
        return parentNodeHost;
    }

    public void setParentNodeHost(String parentNodeHost) {
        this.parentNodeHost = parentNodeHost;
    }

    public int getParentNodePort() {
        return parentNodePort;
    }

    public void setParentNodePort(int parentNodePort) {
        this.parentNodePort = parentNodePort;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getLossPercentage() {
        return lossPercentage;
    }

    public void setLossPercentage(int lossPercentage) {
        this.lossPercentage = lossPercentage;
    }
}