package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.SkillTree;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.ItemProvider;
import com.thizthizzydizzy.util.Menu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import simplelibrary.Stack;
public class MenuSkillTree extends Menu{
    private static final int BUFFER = 2;
    private final SkillTree tree;
    private int panX = 0;
    private int panY = 0;
    private final Type type;
    private final Skill skill;
    public MenuSkillTree(Menu parent, Plugin plugin, Player player, SkillTree tree, Type type){
        this(parent, plugin, player, tree, type, null);
    }
    public MenuSkillTree(Menu parent, Plugin plugin, Player player, SkillTree tree, Type type, Skill skill){
        super(parent, plugin, player, type==Type.VIEW?tree.root.name:(type.prefix+tree.namespace+":"+tree.name), 9*5);
        if((type==Type.CONNECT||type==Type.LINK||type==Type.MOVE)&&skill==null){
            plugin.getLogger().log(Level.WARNING, "Attempted to {0} a null skill!", type.name().toLowerCase());
            type = Type.MODIFY;
        }
        this.tree = tree;
        this.type = type;
        this.skill = skill;
        if(skill!=null){//can't use pan(), as that requires the inventory to exist
            panX = skill.x;
            panY = skill.y;
        }
    }
    public void pan(int slot){
        int[] coords = convertIdToCenteredCoords(slot);
        pan(coords[0]+panX,coords[1]+panY);
    }
    public void pan(int x, int y){
        int minx = 0,miny = 0,maxx = 0,maxy = 0;
        for(Skill s : tree.skills){
            minx = Math.min(minx,s.x);
            miny = Math.min(miny,s.y);
            maxx = Math.max(maxx,s.x);
            maxy = Math.max(maxy,s.y);
        }
        minx-=BUFFER;
        miny-=BUFFER;
        maxx+=BUFFER;
        maxy+=BUFFER;
        x = Math.min(maxx, Math.max(minx, x));
        y = Math.min(maxy, Math.max(miny, y));
        panX = x;
        panY = y;
        updateInventory();
    }
    @Override
    protected void updateInventory(){
        inventory.clear();
        connectionNodes.clear();
        for(Skill skill : tree.skills){
            int x = skill.x-panX;
            int y = skill.y-panY;
            int id = convertCenteredCoordsToId(x,y);
            if(id==-1)continue;//offscreen
            inventory.setItem(id, new ItemBuilder(skill.icon).setDisplayName(skill.getName()).addLore(skill.getLore()).build());
        }
        for(Skill skill : tree.skills){
            for(Skill pre : skill.prerequisites){
                ArrayList<Node> nodes = path(pre, skill);
                if(nodes==null){
                    player.sendMessage("Could not find path from "+pre.toString()+" to "+skill.toString()+"!\nConsider restructuring the skills!");
                    continue;
                }
                for(Node n : nodes){
                    if(n.parent==null)continue;//this is the start!
                    getConnectionNode(n.x, n.y).connect(getConnectionNode(n.parent.x, n.parent.y),Color.BLACK);
                }
            }
        }
        for(ConnectionNode node : connectionNodes){
            if(tree.getSkill(node.x, node.y)!=null)continue;
            int id = convertCenteredCoordsToId(node.x-panX, node.y-panY);
            if(id==-1)continue;
            inventory.setItem(id, node.getLine());
        }
        if(type==Type.LINK){
            int id = convertCenteredCoordsToId(skill.x-panX, skill.y-panY);
            if(id!=-1)inventory.getItem(id).addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            for(Skill s : skill.linkedSkills){
                id = convertCenteredCoordsToId(s.x-panX, s.y-panY);
                if(id!=-1)inventory.getItem(id).addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            }
        }
    }
    //Find a path between skills using A*
    ArrayList<Node> allNodes = new ArrayList<>();
    ArrayList<ConnectionNode> connectionNodes = new ArrayList<>();
    private synchronized ArrayList<Node> path(Skill start, Skill finish){
        allNodes.clear();
        ArrayList<Node> open = new ArrayList<>();
        HashSet<Node> closed = new HashSet<>();
        open.add(getNode(start.x, start.y));
        while(!open.isEmpty()){
            Node current = null;
            for(Node n : open){
                if(current==null||n.getFCost()<current.getFCost()||n.getFCost()==current.getFCost()&&n.hCost<current.hCost)current = n;
            }
            open.remove(current);
            closed.add(current);
            if(current.x==finish.x&&current.y==finish.y){
                Stack<Node> path = new Stack<>();
                Node retraceCurrent = getNode(finish.x,finish.y);
                while(retraceCurrent!=getNode(start.x, start.y)){
                    path.push(retraceCurrent);
                    retraceCurrent = retraceCurrent.parent;
                }
                ArrayList<Node> foundPath = new ArrayList<>();
                while(!path.isEmpty()){
                    foundPath.add(path.pop());
                }
                return foundPath;
            }
            for(Node neighbour : current.getNeighbours()){
                if(neighbour==null||closed.contains(neighbour)||!neighbour.isTraversable(finish)){
                    continue;
                }
                int newMovementCostToNeighbor = current.gCost + dist(current, neighbour);
                if(newMovementCostToNeighbor<neighbour.gCost||!open.contains(neighbour)){
                    neighbour.gCost = newMovementCostToNeighbor;
                    neighbour.hCost = dist(neighbour, getNode(finish.x, finish.y));
                    neighbour.parent = current;
                    if(!open.contains(neighbour)){
                        open.add(neighbour);
                    }
                }
            }
        }
        return null;
    }
    private int dist(Node a, Node b){
        int dx = Math.abs(a.x-b.x);
        int dy = Math.abs(a.y-b.y);
        if(dx>dy){
            return 14*dy+10*(dx-dy);
        }else{
            return 14*dx+10*(dy-dx);
        }
    }
    public static enum Type{
        VIEW(""),
        MODIFY("Modify Skill Tree: "),
        LINK("Link Skills | "),
        CONNECT("Connect Skills | "),
        MOVE("Move Skill | ");
        private final String prefix;
        private Type(String prefix){
            this.prefix = prefix;
        }
    }
    /**
     * Nodes used for A*
     */
    private class Node{
        private final int x;
        private final int y;
        private int gCost;
        private int hCost;
        private Node parent;
        private Node(Skill skill){
            this(skill.x,skill.y);
        }
        private Node(int x, int y){
            this.x = x;
            this.y = y;
            allNodes.add(this);
        }
        private Node[] getNeighbours(){
            return new Node[]{
                getNode(x-1,y-1),getNode(x,y-1),getNode(x+1,y-1),
                getNode(x-1,y),getNode(x+1,y),
                getNode(x-1,y+1),getNode(x,y+1),getNode(x+1,y+1)
            };
        }
        private boolean isTraversable(Skill target){
            Skill s = tree.getSkill(x,y);
            return s==null||s==target;
        }
        private int getFCost(){
            return gCost+hCost;
        }
    }
    private Node getNode(int x, int y){
        for(Node n : allNodes){
            if(n.x==x&&n.y==y)return n;
        }
        int minx = 0,miny = 0,maxx = 0,maxy = 0;
        for(Skill s : tree.skills){
            minx = Math.min(minx,s.x);
            miny = Math.min(miny,s.y);
            maxx = Math.max(maxx,s.x);
            maxy = Math.max(maxy,s.y);
        }
        minx--;
        miny--;
        maxx++;
        maxy++;
        if(x<minx||y<miny||x>maxx||y>maxy)return null;
        return new Node(x,y);
    }
    /**
     * A class to store how the lines connect to each other
     */
    private class ConnectionNode{
        private boolean n,s,e,w,ne,se,sw,nw;
        private int x;
        private int y;
        private Color color;
        public ConnectionNode(int x, int y){
            this(x, y, false, false, false, false, false, false, false, false);
        }
        public ConnectionNode(int x, int y, boolean n, boolean ne, boolean e, boolean se, boolean s, boolean sw, boolean w, boolean nw){
            this.x = x;
            this.y = y;
            this.n = n;
            this.ne = ne;
            this.s = s;
            this.se = se;
            this.e = e;
            this.sw = sw;
            this.w = w;
            this.nw = nw;
            connectionNodes.add(this);
        }
        private void connect(ConnectionNode other, Color color){
            this.color = color;
            other.color = color;
            if(other.x==x&&other.y==y-1){//north
                n = true;
                other.s = true;
            }
            if(other.x==x+1&&other.y==y-1){//northeast
                ne = true;
                other.sw = true;
            }
            if(other.x==x+1&&other.y==y){//east
                e = true;
                other.w = true;
            }
            if(other.x==x+1&&other.y==y+1){//southeast
                se = true;
                other.nw = true;
            }
            
            if(other.x==x&&other.y==y+1){//south
                s = true;
                other.n = true;
            }
            if(other.x==x-1&&other.y==y+1){//southwest
                sw = true;
                other.ne = true;
            }
            if(other.x==x-1&&other.y==y){//west
                w= true;
                other.e = true;
            }
            if(other.x==x-1&&other.y==y-1){//northwest
                nw = true;
                other.se = true;
            }
        }
        private ItemStack getLine(){
            return ItemProvider.getLine(n, ne, e, se, s, sw, w, nw, color).build();
        }
    }
    private ConnectionNode getConnectionNode(int x, int y){
        for(ConnectionNode n : connectionNodes){
            if(n.x==x&&n.y==y)return n;
        }
        int minx = 0,miny = 0,maxx = 0,maxy = 0;
        for(Skill s : tree.skills){
            minx = Math.min(minx,s.x);
            miny = Math.min(miny,s.y);
            maxx = Math.max(maxx,s.x);
            maxy = Math.max(maxy,s.y);
        }
        minx--;
        miny--;
        maxx++;
        maxy++;
        if(x<minx||y<miny||x>maxx||y>maxy)return null;
        return new ConnectionNode(x,y);
    }
    @Override
    public void onClick(int slot, ClickType click){
        int[] coords = convertIdToCenteredCoords(slot);
        Skill clickedSkill = tree.getSkill(coords[0]+panX, coords[1]+panY);
        switch(click){
            case MIDDLE:
                pan(0, 0);
                updateInventory();
                break;
            case LEFT:
                if(type==Type.CONNECT&&clickedSkill!=null&&clickedSkill!=skill){
                    skill.connect(clickedSkill);
                }else if(type==Type.LINK&&clickedSkill!=null&&clickedSkill!=skill&&clickedSkill!=tree.root){
                    skill.link(clickedSkill);
                }else{
                    pan(slot);
                }
                updateInventory();
                break;
            case RIGHT:
                switch(type){
                    case VIEW:
                        break;
                    case LINK:
                    case CONNECT:
                    case MOVE:
                        open(parent);
                        break;
                    case MODIFY:
                        if(clickedSkill!=null)open(new MenuModifySkill(this, plugin, player, clickedSkill));
                        break;
                }
                break;
            case SHIFT_LEFT:
                if(type==Type.MOVE){
                    if(clickedSkill==null){
                        skill.move(coords[0]+panX,coords[1]+panY);
                        updateInventory();
                    }
                }
                if(type==Type.MODIFY){
                    open(new MenuModifySkill(this, plugin, player, tree.createSkill(coords[0]+panX, coords[1]+panY)));
                    break;
                }
                break;
        }
    }
    @Override
    public void onClose(){
        tree.refreshInstances();
        super.onClose();
    }
}