import java.util.*;
import java.text.*;

public class Node{
    int count;
    int id;
    Node[] neighbors;
    
    Node(int d, int nn){
	id = d;
	neighbors = new Node[nn];
        count = nn;
    }
public static void main(String[] args) {
    long startTime=System.nanoTime();    
    Node node1 = new Node(1, 2);
    Node node2 = new Node(2, 2);
    Node node3 = new Node(3, 1);
    Node node4 = new Node(4, 2);
    Node node5 = new Node(5, 2);
    Node node6 = new Node(6, 2);
    Node node7 = new Node(7, 2);
    Node node8 = new Node(8, 2);
    Node node9 = new Node(9, 2);
    Node node10 = new Node(10, 2);
    Node node11 = new Node(11, 2);
    Node node12 = new Node(12, 2);
    Node node13 = new Node(13, 2);
    Node node14 = new Node(14, 2);
    Node node15 = new Node(15, 2);
    Node node16 = new Node(16, 2);
    Node node17 = new Node(17, 2);
    Node node18 = new Node(18, 2);
    Node node19 = new Node(19, 2);
    Node node20 = new Node(20, 2);


    node1.neighbors[0] = node2;
    node1.neighbors[1] = node3;
    node2.neighbors[0] = node1;
    node2.neighbors[1] = node3;
    node3.neighbors[0] = node2;
    node4.neighbors[0] = node2;
    node4.neighbors[1] = node3;
    node5.neighbors[0] = node3;
    node5.neighbors[1] = node4;
    node6.neighbors[0] = node4;
    node6.neighbors[1] = node5;
    node7.neighbors[0] = node5;
    node7.neighbors[1] = node6;
    node8.neighbors[0] = node6;
    node8.neighbors[1] = node7;
    node9.neighbors[0] = node7;
    node9.neighbors[1] = node8;
    node10.neighbors[0] = node8;
    node10.neighbors[1] = node9;
    node11.neighbors[0] = node9;
    node11.neighbors[1] = node10;
    node12.neighbors[0] = node10;
    node12.neighbors[1] = node11;
    node13.neighbors[0] = node11;
    node13.neighbors[1] = node12;
    node14.neighbors[0] = node12;
    node14.neighbors[1] = node13;
    node15.neighbors[0] = node13;
    node15.neighbors[1] = node14;
    node16.neighbors[0] = node14;
    node16.neighbors[1] = node15;
    node17.neighbors[0] = node15;
    node17.neighbors[1] = node16;
    node18.neighbors[0] = node16;
    node18.neighbors[1] = node17;
    node19.neighbors[0] = node17;
    node19.neighbors[1] = node18;
    node20.neighbors[0] = node18;
    node20.neighbors[1] = node19;   
        
    for(int i=0; i< node1.count; i++)
	{
	    System.out.println("node"+node1.id+"<--"+"node"+node1.neighbors[i].id);	
        }
    for(int i=0; i< node2.count; i++)
        {
            System.out.println("node"+node2.id+"<--"+"node"+node2.neighbors[i].id);
	}
     for(int i=0; i< node3.count; i++)
	{
	    System.out.println("node"+node3.id+"<--"+"node"+node3.neighbors[i].id);
	}    
      for(int i=0; i< node4.count; i++)
     	 {
     	     System.out.println("node"+node4.id+"<--"+"node"+node4.neighbors[i].id);
     	 }
     for(int i=0; i< node5.count; i++)
    	 {
     	     System.out.println("node"+node5.id+"<--"+"node"+node5.neighbors[i].id);
     	 }
     for(int i=0; i< node6.count; i++)
    	 {
     	     System.out.println("node"+node6.id+"<--"+"node"+node6.neighbors[i].id);
     	 }
     for(int i=0; i< node7.count; i++)
     	 {
     	     System.out.println("node"+node7.id+"<--"+"node"+node7.neighbors[i].id);
     	 }
     for(int i=0; i< node8.count; i++)
     	 {
     	     System.out.println("node"+node8.id+"<--"+"node"+node8.neighbors[i].id);
     	 }
     for(int i=0; i< node9.count; i++)
     	 {
     	     System.out.println("node"+node9.id+"<--"+"node"+node9.neighbors[i].id);
     	 }
     for(int i=0; i< node10.count; i++)
     	 {
     	     System.out.println("node"+node10.id+"<--"+"node"+node10.neighbors[i].id);
     	 }
     for(int i=0; i< node11.count; i++)
     	 {
     	     System.out.println("node"+node11.id+"<--"+"node"+node11.neighbors[i].id);
     	 }
     for(int i=0; i< node12.count; i++)
     	 {
     	     System.out.println("node"+node12.id+"<--"+"node"+node12.neighbors[i].id);
     	 }
     for(int i=0; i< node13.count; i++)
      {
     	     System.out.println("node"+node13.id+"<--"+"node"+node13.neighbors[i].id);
     	 }
     for(int i=0; i< node14.count; i++)
     	 {
     	     System.out.println("node"+node14.id+"<--"+"node"+node14.neighbors[i].id);
     	 }
     for(int i=0; i< node15.count; i++)
     	 {
     	     System.out.println("node"+node15.id+"<--"+"node"+node15.neighbors[i].id);
     	 }
     for(int i=0; i< node16.count; i++)
     	 {
     	     System.out.println("node"+node16.id+"<--"+"node"+node16.neighbors[i].id);
     	 }
    for(int i=0; i< node17.count; i++)
     	 {
     	     System.out.println("node"+node17.id+"<--"+"node"+node17.neighbors[i].id);
     	 }
     for(int i=0; i< node18.count; i++)
     	 {
     	     System.out.println("node"+node18.id+"<--"+"node"+node18.neighbors[i].id);
     	 }
     for(int i=0; i< node19.count; i++)
     	 {
     	     System.out.println("node"+node19.id+"<--"+"node"+node19.neighbors[i].id);
     	 }
     for(int i=0; i< node20.count; i++)
     	 {
     	     System.out.println("node"+node20.id+"<--"+"node"+node20.neighbors[i].id);
     	 }



    long estimatedTime=System.nanoTime()-startTime;
    System.out.println("Running Time:"+estimatedTime+"us");

	}
}