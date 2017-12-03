import java.util.*;

public class ScopeNode{
	private Map<String,Node> sctMap;
	private ScopeNode kid;
	private ScopeNode parent;
	public ScopeNode(){
		sctMap = new HashMap<String,Node>();
	}
	public ScopeNode(ScopeNode parent){
		sctMap = new HashMap<String,Node>();
		this.parent = parent;
	}

	public Map<String, Node> getSCTMap(){
		return sctMap;
	}
	public void linkParentToChild(ScopeNode child){
		this.kid = child;
	}
	public ScopeNode getKid() {
		return kid;
	}
	public ScopeNode getParent(){
		return parent;
	}
}