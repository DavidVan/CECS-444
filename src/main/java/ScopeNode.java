import java.util.*;

public class SNode{
	private Map<String,Node> sctMap;
	private SNode kid;
	private SNode parent;
	public SNode(){
		sctMap = new HashMap<String,Node>();
	}
	public SNode(SNode parent){
		sctMap = new HashMap<String,Node>();
		this.parent = parent;
	}

	public Map<String, Node> getSCTMap(){
		return sctMap;
	}
	public void linkParentToChild(SNode child){
		this.kid = child;
	}
	public SNode getKid() {
		return kid;
	}
	public SNode getParent(){
		return parent;
	}
}