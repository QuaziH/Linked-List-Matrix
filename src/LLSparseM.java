
public class LLSparseM implements SparseM {

//	private static class Node{
//		private int rowIdx;
//		private int colIdx;
//		private int val;
//		private Node next;
//
//		public Node(int r, int c, int v, Node n){
//			rowIdx = r;
//			colIdx = c;
//			val = v;
//			next = n;
//		}
//
//		public int getRowIndex(){
//			return rowIdx;
//		}
//
//		public int getColIndex(){
//			return colIdx;
//		}
//
//		public int getValue(){
//			return val;
//		}
//
//		public Node getNext(){
//			return next;
//		}
//
//		public void setNext(Node n){
//			next = n;
//		}
//	}

	private static class Row{
		private int rIdx;
		private LLSparseVec val;
		private Row next;

		public Row(int rIdx, LLSparseVec val, Row next){
			this.rIdx = rIdx;
			this.val = val;
			this.next = next;
		}

		public int getRowIndex(){
			return rIdx;
		}

		public LLSparseVec getValue(){
			return val;
		}

		public Row getNext(){
			return next;
		}

		public void setNext(Row n){
			next = n;
		}
	}

	private static class Col{
		private int cIdx;
		private int val;
		private Col next;

		public Col(int cIdx, int val, Col next){
			this.cIdx = cIdx;
			this.val = val;
			this.next = next;
		}

		public int getColIndex(){
			return cIdx;
		}
		public int getValue(){
			return val;
		}

		public Col getNext(){
			return next;
		}

		public void setNext(Col n){
			next = n;
		}
	}

	private int nRows, nCols;
	private Row rHead = null;
	private Row rTail = null;
	private Col cHead = null;
	private Col cTail = null;
	private int nElements = 0;

	public LLSparseM(int nr, int nc){
		if(nr <= 0) nRows = 1;
		if(nc <= 0) nCols = 1;
    	this.nRows = nr;
    	this.nCols = nc;
	}

	public void addFirst(int ridx, LLSparseVec val){
		rHead = new Row(ridx, val, rHead);
		if(this.nElements == 0)
			this.rTail = this.rHead;
		this.nElements++;
	}

	public void addLast(int ridx, LLSparseVec val){
		Row newest = new Row(ridx, val, null);
		if(this.nElements == 0)
			this.rHead = newest;
		else
			this.rTail.setNext(newest);
		this.rTail = newest;
		this.nElements++;
	}

	@Override
	public int nrows() {
		// TODO Auto-generated method stub
		return nRows;
	}

	@Override
	public int ncols() {
		// TODO Auto-generated method stub
		return nCols;
	}

	@Override
	public int numElements() {
		// TODO Auto-generated method stub
		return nElements;
	}

	@Override
	public int getElement(int ridx, int cidx) {
		// TODO Auto-generated method stub
		if(ridx > this.nrows() || ridx < 0)
			return -1;

		if(cidx > this.ncols() || cidx < 0)
			return -1;

		Row rCurrent = this.rHead;

		if(rCurrent.getRowIndex() == ridx){
			LLSparseVec temp = rCurrent.getValue();
			LLSparseVec.Node current = temp.head;
			if(current.getIndex() == cidx)
				return current.getValue();
			while(current.getIndex() != cidx){
				current = current.getNext();
				if(current.getIndex() == cidx){
					return current.getValue();
				}
			}
		}

		while(rCurrent.rIdx != ridx){
			rCurrent = rCurrent.next;
			if(rCurrent.rIdx == ridx){
				LLSparseVec temp = rCurrent.getValue();
				LLSparseVec.Node current = temp.head;
				if(current.getIndex() == cidx)
					return current.getValue();
				while(current.getIndex() != cidx){
					current = current.getNext();
					if(current.getIndex() == cidx){
						return current.getValue();
					}
				}
			}
		}
		return -1;
	}

	@Override
	public void clearElement(int ridx, int cidx) {
		// TODO Auto-generated method stub
		if(ridx > this.nrows() || ridx < 0)
			return;

		if(cidx > this.ncols() || cidx < 0)
			return;

		Row current = this.rHead;
		Row currentNext = this.rHead.getNext();

		if(current.getRowIndex() == ridx){
			LLSparseVec temp = current.val;
			temp.clearElement(cidx);
			if(temp.numElements() == 0){
				this.nElements--;
				this.rHead = currentNext;
				return;
			}
			current.val = temp;
			return;
		}

		while(currentNext != null){
			if(currentNext.getRowIndex() == ridx) {
				LLSparseVec temp = current.val;
				temp.clearElement(cidx);
				if(temp.numElements() == 0){
					current.setNext(currentNext.getNext());
					if(currentNext.getNext() == null)
						this.rTail = current;
					this.nElements--;
					return;
				}
				current.val = temp;
				return;
			}
			current = currentNext;
			currentNext = currentNext.getNext();
		}
	}

	@Override
	public void setElement(int ridx, int cidx, int val) { //i think this is right
		// TODO Auto-generated method stub
		if(ridx > this.nrows() || ridx < 0)
			return;

		if(cidx > this.ncols() || cidx < 0)
			return;

		Row current = this.rHead;
		Row currentNext = this.rHead.getNext();

		LLSparseVec newSparseVec = new LLSparseVec(this.nCols);
		newSparseVec.setElement(cidx, val);
		Row newRow = new Row(ridx, newSparseVec, null);

		if(current.getRowIndex() > ridx){
			this.addFirst(ridx, newSparseVec);
			return;
		}

		if(this.rTail.getRowIndex() < ridx){
			this.addLast(ridx, newSparseVec);
			return;
		}

		if(current.getRowIndex() == ridx){
			LLSparseVec temp = current.val;
			temp.setElement(cidx, val);
			current.val = temp;
			return;
		}

		while(currentNext != null){
			if(currentNext.getRowIndex() == ridx){
				LLSparseVec temp = current.val;
				temp.setElement(cidx, val);
				current.val = temp;
				return;
			}
			if(currentNext.getRowIndex() > ridx && current.getRowIndex() < ridx) {
				current.setNext(newRow);
				newRow.setNext(currentNext);
				this.nElements++;
				return;
			}
			current = currentNext;
			currentNext = currentNext.getNext();
			if(currentNext == null){
				this.addLast(ridx, newSparseVec);
				return;
			}
		}
	}

	@Override
	public int[] getRowIndices() { //unsure of this
		// TODO Auto-generated method stub
		int indices[] = new int[this.nElements];
		Row current = this.rHead;

		for(int i = 0; current != null; i++) {
			indices[i] = current.getRowIndex();
			current = current.getNext();
		}

		return indices;
	}

	@Override
	public int[] getOneRowColIndices(int ridx) {
		// TODO Auto-generated method stub
		if(ridx > this.nrows() || ridx < 0)
			return null;

		Row current = this.rHead;

		while(current.getRowIndex() != ridx) {
			current = current.next;
		}

		LLSparseVec temp = current.getValue();
		return temp.getAllIndices();
	}

	@Override
	public int[] getOneRowValues(int ridx) {
		// TODO Auto-generated method stub
		if(ridx > this.nrows() || ridx < 0)
			return null;

		Row current = this.rHead;

		while(current.getRowIndex() != ridx) {
			current = current.next;
		}

		LLSparseVec temp = current.getValue();
		return temp.getAllValues();
	}

	@Override
	public SparseM addition(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM subtraction(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SparseM multiplication(SparseM otherM) {
		// TODO Auto-generated method stub
		return null;
	}

}
