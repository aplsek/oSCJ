package list;

public class List {

	public Node head;
	public Node tail;
	
	public List(int nodes) {
		
		head = null;
		tail = null;
		
		for (int i=0 ;i< nodes; i++) {
			Node n = new Node(i);
			if (head == null) {
				head = n;
				n.prev = null;
				n.next = null;
				tail = n;
			} else {
				tail.next = n;
				n.next = null;
				n.prev = tail;
				tail = n;
			}
		}
	}
	
	public void invert() {
		Node n = head;
		tail = head;
		while (n != null) {
			head = n;
			Node next = n.next;
			n.next = n.prev;
			n.prev = next;
			n = next;
		}
		
		
	}
	
	
	public void print() {
		Node n = head;
		while (n != null) {
			System.out.print(n.field + " ");
			n = n.next;
		}
		System.out.println();
	}
		
	
	
}


class Node {
	int field;
	
	Node next;
	Node prev;
	
	public Node(int field) {
		this.field = field;
	}
	
	public Node(int field, Node prev, Node next) {
		this.field = field;
		this.next = next;
		this.prev = prev;
	}
	
	public void set(Node prev, Node next) {
		this.next = next;
		this.prev = prev;
	}
}