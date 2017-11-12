
public class LLSparseVec implements SparseVec {

	public static class Node{
		private int idx;
		private int val;
		private Node next;

		public Node(int i, int v, Node n){
			idx = i;
			val = v;
			next = n;
		}

		public int getIndex(){
			return idx;
		}
		public void setIndex(int idx){ this.idx = idx; }

		public int getValue(){
			return val;
		}
		public void setValue(int val){ this.val = val; }

		public Node getNext(){
			return next;
		}
		public void setNext(Node n){
			next = n;
		}
	}

	private int length;
	public Node head = null;
	public Node tail = null;
	public int nElements = 0;


	public LLSparseVec(int len){
		if(this.length <= 0)
			this.length = 1;
		this.length = len;
	}

	public void addFirst(int idx, int val){
		head = new Node(idx, val, head);
		if(this.nElements == 0)
			this.tail = this.head;
		this.nElements++;
	}

	public void addLast(int idx, int val){
		Node newest = new Node(idx, val, null);
		if(this.nElements == 0)
			this.head = newest;
		else
			this.tail.setNext(newest);
		this.tail = newest;
		this.nElements++;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}

	@Override
	public int numElements() {
		// TODO Auto-generated method stub
		return this.nElements;
	}

	@Override
	public int getElement(int idx) {
		// TODO Auto-generated method stub
		if(idx > this.getLength() || idx < 0)
			return -1;

		Node current = this.head;

		while(current.idx != idx) {
			current = current.next;
		}

		return current.val;
	}

	@Override
	public void clearElement(int idx) {
		// TODO Auto-generated method stub
		if(idx > this.getLength() || idx < 0)
			return;

		Node current = this.head;
		Node currentNext = current.getNext();

		if(current.idx == idx){
			this.head = currentNext;
			this.nElements--;
			return;
		}

		while(currentNext != null) {
			if(currentNext.idx == idx){
				current.setNext(currentNext.getNext());
				if(currentNext.getNext() == null)
					this.tail = current;
				currentNext.val = 0;
				this.nElements--;
				return;
			}
			current = currentNext;
			currentNext = currentNext.getNext();
		}
	}

	@Override
	public void setElement(int idx, int val) {
		// TODO Auto-generated method stub
		if(idx > this.getLength() || idx < 0)
			return;

		if(val == 0) {
			this.clearElement(idx);
			return;
		}

		if(this.numElements() == 0){
			this.addFirst(idx, val);
			return;
		}

		Node current = this.head;
		Node currentNext = current.getNext();
		Node newElement = new Node(idx, val, null);

		if(current.getIndex() > idx){
			this.addFirst(idx, val);
			return;
		}

		if(this.tail.getIndex() < idx){
			this.addLast(idx, val);
			return;
		}

		if(current.getIndex() == idx){
			current.val = val;
			return;
		}

		while(currentNext != null){
			if(currentNext.getIndex() == idx){
				currentNext.val = val;
				return;
			}
			if(currentNext.getIndex() > idx && current.getIndex() < idx) {
				current.setNext(newElement);
				newElement.setNext(currentNext);
				this.nElements++;
				return;
			}
			current = currentNext;
			currentNext = currentNext.getNext();
			if(currentNext == null){
				this.addLast(idx, val);
				return;
			}
		}
	}

	@Override
	public int[] getAllIndices() {
		// TODO Auto-generated method stub
		int indices[] = new int[this.nElements];
		Node current = this.head;

		for(int i = 0; current != null; i++) {
			indices[i] = current.getIndex();
			current = current.getNext();
		}

		return indices;
	}

	@Override
	public int[] getAllValues() {
		// TODO Auto-generated method stub
		int values[] = new int[this.nElements];
		Node current = this.head;

		for(int i = 0; current != null; i++) {
			values[i] = current.getValue();
			current = current.getNext();
		}

		return values;
	}

	@Override
	public SparseVec addition(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.getLength() != otherV.getLength())
			return null;

		if(otherV.numElements() == 0){
			return null;
		}

		if(this.numElements() == 0) {
			return otherV;
		}

		LLSparseVec newVector = new LLSparseVec(this.length);

		int[] thisIdx = this.getAllIndices();
		int[] thisVal = this.getAllValues();

		int[] otherIdx = otherV.getAllIndices();
		int[] otherVal = otherV.getAllValues();

		int i = 0;
		int j = 0;

		while(i < thisIdx.length && j < otherIdx.length){
			if (thisIdx[i] < otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			} else if (thisIdx[i] > otherIdx[j]){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			} else if (thisIdx[i] == otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i] + otherVal[j]);
				i++;
				j++;
			}
		}

		if(i >= thisIdx.length){
			while (j < otherIdx.length){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			}
		} else {
			while (i < thisIdx.length){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			}
		}

		return newVector;
	}

	@Override
	public SparseVec subtraction(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.getLength() != otherV.getLength())
			return null;

		if(otherV.numElements() == 0){
			return null;
		}

		if(this.numElements() == 0) {
			return otherV;
		}

		LLSparseVec newVector = new LLSparseVec(this.length);

		int[] thisIdx = this.getAllIndices();
		int[] thisVal = this.getAllValues();

		int[] otherIdx = otherV.getAllIndices();
		int[] otherVal = otherV.getAllValues();

		int i = 0;
		int j = 0;

		while(i < thisIdx.length && j < otherIdx.length){
			if (thisIdx[i] < otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			} else if (thisIdx[i] > otherIdx[j]){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			} else if (thisIdx[i] == otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i] - otherVal[j]);
				i++;
				j++;
			}
		}

		if(i >= thisIdx.length){
			while (j < otherIdx.length){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			}
		} else {
			while (i < thisIdx.length){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			}
		}

		return newVector;
	}

	@Override
	public SparseVec multiplication(SparseVec otherV) {
		// TODO Auto-generated method stub
		if(this.getLength() != otherV.getLength())
			return null;

		if(otherV.numElements() == 0){
			return null;
		}

		if(this.numElements() == 0) {
			return otherV;
		}

		LLSparseVec newVector = new LLSparseVec(this.length);

		int[] thisIdx = this.getAllIndices();
		int[] thisVal = this.getAllValues();

		int[] otherIdx = otherV.getAllIndices();
		int[] otherVal = otherV.getAllValues();

		int i = 0;
		int j = 0;

		while(i < thisIdx.length && j < otherIdx.length){
			if (thisIdx[i] < otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			} else if (thisIdx[i] > otherIdx[j]){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			} else if (thisIdx[i] == otherIdx[j]){
				newVector.setElement(thisIdx[i], thisVal[i] * otherVal[j]);
				i++;
				j++;
			}
		}

		if(i >= thisIdx.length){
			while (j < otherIdx.length){
				newVector.setElement(otherIdx[j], otherVal[j]);
				j++;
			}
		} else {
			while (i < thisIdx.length){
				newVector.setElement(thisIdx[i], thisVal[i]);
				i++;
			}
		}

		return newVector;
	}

}
