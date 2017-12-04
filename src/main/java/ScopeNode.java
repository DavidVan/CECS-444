import java.util.*;

public class ScopeNode{
	private Map<String,Node> sctMap; // store our node
   private Map<String,Object> valMap; // store our value

	private ScopeNode kid;
	private ScopeNode parent;
	public ScopeNode(){
		sctMap = new HashMap<>();
      valMap = new HashMap<>();
	}
	public ScopeNode(ScopeNode parent){
		sctMap = new HashMap<>();
      valMap = new HashMap<>();
		this.parent = parent;
	}

	public Map<String, Node> getSCTMap(){
		return sctMap;
	}
   
   public Map<String, Object> getValMap() {
      return valMap;
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