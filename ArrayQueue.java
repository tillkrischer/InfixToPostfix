import java.util.NoSuchElementException;

public class ArrayQueue<T> {
	private T[] data;
	private int start = 0;
	private int size = 0;
	
	public ArrayQueue() {
		data = (T[]) new Object[10];
	}
	
	public void enqueue(T element) {
		if(size == data.length) {
			T[] temp = (T[]) new Object[data.length * 2];
			for(int i = 0; i < data.length; ++i)
				temp[i] = data[(i+start)%data.length];
			data = temp;
			start = 0;
		}
		data[(start+size)%data.length] = element;
		size++;
	}
	
	public T dequeue() {
		if(size == 0)
			throw new NoSuchElementException();
		T temp = data[start];
		start = (start+1)%data.length;
		size--;
		return temp;
	}
	
	public T front() {
		if(size == 0)
			throw new NoSuchElementException();
		return data[start];
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		String s = "";
		for(int i = start; i < start+size; ++i)
			s += data[i%data.length] + " ";
		return s;
	}
}
